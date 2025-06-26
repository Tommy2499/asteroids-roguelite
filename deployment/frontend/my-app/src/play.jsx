import React, { useEffect, useRef, useCallback } from "react";
import { useAuth } from "./auth_context";
import { Link } from "react-router-dom"
import "./play.css";

// Load images
import asteroid_32 from "./images/asteroid_32x32.png";
import asteroid_48 from "./images/asteroid_48x48.png";
import asteroid_64 from "./images/asteroid_64x64.png";
import comet from "./images/comet_48x48.png";
import alien from "./images/alien_32x32.png"; 
import alient_bullet from "./images/alien_bullet_4x4.png"; 
import player_bullet from "./images/player_bullet_4x4.png";
// Background is in css

import ship from "./images/player_24x24.png";
import invShip from "./images/invincible_player_24x24.png";

const Game = () => {
  const canvasRef = useRef(null);
  const hitboxCheckboxRef = useRef(null);
  const pauseButtonRef = useRef(null);
  const requestRef = useRef(null);

  // Images
  const asteroidImg32 = new Image();
  const asteroidImg48 = new Image();
  const asteroidImg64 = new Image();
  const cometImg = new Image();
  const alienImg = new Image();
  const alienBulletImg = new Image();
  const playerBulletImg = new Image();

  const shipImg = new Image();
  const invincibleShip = new Image();

  asteroidImg32.src = asteroid_32;
  asteroidImg48.src = asteroid_48; 
  asteroidImg64.src = asteroid_64;
  cometImg.src = comet; 
  alienImg.src = alien;
  alienBulletImg.src = alient_bullet;
  playerBulletImg.src = player_bullet;

  shipImg.src = ship; 
  invincibleShip.src = invShip; 

  const { user } = useAuth();
  const scoreUploadedRef = useRef(null);
  const suppressUploadRef = useRef(false);

  if (scoreUploadedRef.current === null) {
    const stored = sessionStorage.getItem("scoreUploaded");
    scoreUploadedRef.current = stored === "true";
    console.log("Initialized scoreUploadedRef:", scoreUploadedRef.current);
  }

  // Variables for handling shoot inputs per keydown
  const isHoldingShoot = useRef(false);
  const inputRef = useRef(new Set()); // Handles all inputs
  const currFrameBuffer = useRef(false); // Handles shoot inputs pressed on current frame
  const nextFrameBuffer = useRef(false); // Handles shoot inputs pressed for next frame
  const lock = useRef(false); // Acts as a lock to avoid race conditions
  const timeLastShot = useRef(null); // Used to limit holding down shoot inputs

  /**
   * Method for handling user input on a keydown event. All keys except shoot
   * are per frame. Shoot key is handled per keydown and keyup event. A locking
   * mechanism is used to handle race conditions.
   */
  const handleKeyDown = useCallback((event) => {
    let currTime = Date.now();
    let dt = currTime - timeLastShot.current;
    // Process shoot input
    // If we are not holding shoot, then shoot. If we are holding shoot, then
    // check timer to see if we can shoot again.
    if ((event.key === "s" || event.code === "Space") && (!isHoldingShoot.current || dt > 200)) {
      timeLastShot.current = currTime;
      if (!lock.current) {
        // Handle shoot for current frame
        currFrameBuffer.current = true;
      } else {
        // Handle for next frame
        nextFrameBuffer.current = true;
      }
      isHoldingShoot.current = true;
    }

    // Process movement input
    if (event.key === "w") inputRef.current.add("UP");
    if (event.key === "a") inputRef.current.add("LEFT");
    if (event.key === "d") inputRef.current.add("RIGHT");
  }, []);

  /**
   * Method for handling user input on a keyup event.
   */
  const handleKeyUp = useCallback((event) => {
    if (event.key === "w") inputRef.current.delete("UP");
    if (event.key === "a") inputRef.current.delete("LEFT");
    if (event.key === "d") inputRef.current.delete("RIGHT");
    if (event.key === "s" || event.code === "Space") {
      isHoldingShoot.current = false;
    }
  }, []);

  useEffect(() => {
    const canvas = canvasRef.current;
    const context = canvas.getContext("2d");

    const username = user?.username ?? "guest";
    const profile_name = user?.profile_name ?? "guest";
    let last_time = 0;
    let hitboxes = false;
    let paused = false;

    const handleHitboxChange = () => {
      hitboxes = hitboxCheckboxRef.current.checked;
    };

    const handleBeforeUnload = () => {
      // Prevent any upload if game is already over and score has been uploaded
      if (!scoreUploadedRef.current && !document.hidden) {
        sessionStorage.setItem("scoreUploaded", "false");
      }
    };

    window.addEventListener("beforeunload", handleBeforeUnload);

    const animate = async (timestamp) => {
      // If holding shoot key down, trigger a KeyboardEvent
      // Fixes the problem that if another key is pressed while holding shoot
      // Then the event listener no longer triggers shoot events
      if (isHoldingShoot.current) {
        handleKeyDown(
          new KeyboardEvent("keydown", {
            key: "s",
            code: "s",
          }),
        );
      }
      // Transfer previous frame inputs (which is held in next frame buffer) to current frame
      currFrameBuffer.current =
        currFrameBuffer.current || nextFrameBuffer.current;
      nextFrameBuffer.current = false;
      // Reset shoot input for this frame
      inputRef.current.delete("SHOOT");

      // Any shot input before here is sent to currFrameBuffer
      lock.current = true;
      // Any shoot inputs inside lock will be processed into nextFrameBuffer
      // If we have shot this frame, add it to the inputRef
      if (currFrameBuffer.current) {
        inputRef.current.add("SHOOT");
        // Without lock, race condition happens here
        currFrameBuffer.current = false;
      }
      lock.current = false;
      // Any shoot input past here is sent to currFrameBuffer

      let dt = (timestamp - last_time) / 1000;
      last_time = timestamp;
      if (isNaN(dt)) dt = 0;

      if (paused) {
        requestRef.current = requestAnimationFrame(animate);
        context.save();
        context.fillStyle = "white";
        await document.fonts.ready;
        context.font = "50px FuturyLight";
        context.fillText("Paused", 350, 350);
        context.restore();
        return;
      }

      const response = await fetch(
        `http://137.184.232.147:5000/api/updateGame?dt=${encodeURIComponent(dt)}&` +
        `username=${encodeURIComponent(username)}&` +
        `profile_name=${encodeURIComponent(profile_name)}&` +
        `inputs=${encodeURIComponent(Array.from(inputRef.current).join(","))}`,
      );

      const data = await response.json();
      if (timestamp % 1000 < 16) console.log(data);

      // console.log(timestamp);
      // console.log("Enemies:", data.enemies);

      const player = data.player;
      const lives = player?.lives ?? 0;
      const score = data.score ?? 0;
      const level = data.level ?? 0;
      const time = data.time ?? 0;
      const is_running = data.is_running;
      const bullets = data.bullets ?? [];
      const enemies = data.enemies ?? [];

      // Update DOM HUD with current values
      document.getElementById("livesDisplayValue").textContent = lives;
      document.getElementById("scoreDisplayValue").textContent = score;
      document.getElementById("levelDisplayValue").textContent = level;
      document.getElementById("timeDisplayValue").textContent = Math.floor(time);


      context.clearRect(0, 0, canvas.width, canvas.height);

      if (player) {
        context.save();
        context.translate(player.position.x, player.position.y);
        context.rotate(player.orientation + Math.PI / 2); //corrects for image rotation

        const shipSize = 50.0;
        if (player.is_invincible) {
          context.drawImage(
            invincibleShip,
            -shipSize / 2,
            -shipSize / 2,
            shipSize,
            shipSize,
          );
        } else {
          context.drawImage(
            shipImg,
            -shipSize / 2,
            -shipSize / 2,
            shipSize,
            shipSize,
          );
        }

        context.restore();
      }

      // Bullets
      bullets.forEach((bullet) => {
        context?.save();
        context.translate(bullet.position.x, bullet.position.y);
        context.rotate(bullet.orientation);
        const bulletSize = bullet.hitbox[0].radius * 2; // use hitbox radius for alien bullet
        context.drawImage(
          playerBulletImg,
          0 - bulletSize / 2, // center the image
          0 - bulletSize / 2,
          bulletSize,
          bulletSize,
        );
        context.restore();
      });

      // Enemies
      for (let enemy of enemies) {
        // console.log("Enemy type:", enemy.type);

        // Asteroid sprite depends on the size
        if (enemy.type === "ASTEROID") {
          const { x, y } = enemy.position;
          const orientation = enemy.orientation;
          let image;
          let imageSize;

          if (enemy.size === "SMALL") {
            image = asteroidImg32;
            imageSize = enemy.hitbox[0].radius * 2; // use hitbox radius for small/medium asteroids
          } else if (enemy.size === "MEDIUM") {
            image = asteroidImg48;
            imageSize = enemy.hitbox[0].radius * 2; // use hitbox radius for small/medium asteroids
          } else if (enemy.size === "LARGE") {
            image = asteroidImg64;
            imageSize = enemy.hitbox[0].radius * 2; // use hitbox radius for large asteroids
          }

          if (image) {
            context?.save();
            context.translate(x, y);
            context.rotate(orientation);
            context.drawImage(
              image,
              0 - imageSize / 2, // center the image
              0 - imageSize / 2,
              imageSize,
              imageSize,
            );
            context.restore();
          }

        } else if (enemy.type === "COMET") {
          // Draw comet
          context?.save();
          context.translate(enemy.position.x, enemy.position.y);
          context.rotate(enemy.orientation);
          const cometSize = enemy.hitbox[0].radius * 2; // use hitbox radius for comet
          context.drawImage(
            cometImg,
            0 - cometSize / 2, // center the image
            0 - cometSize / 2,
            cometSize,
            cometSize,
          );
          context.restore();

        } else if (enemy.type === "ALIEN") {
          // Draw alien
          context?.save();
          context.translate(enemy.position.x, enemy.position.y);
          context.rotate(enemy.orientation);
          const alienSize = enemy.hitbox[0].radius * 2; // use hitbox radius for alien
          context.drawImage(
            alienImg,
            0 - alienSize / 2, // center the image
            0 - alienSize / 2,
            alienSize,
            alienSize,
          );
          context.restore();

        } else if (enemy.type === "BULLET") {
          // Draw alien bullet
          context?.save();
          context.translate(enemy.position.x, enemy.position.y);
          context.rotate(enemy.orientation);
          const bulletSize = enemy.hitbox[0].radius * 2; // use hitbox radius for alien bullet
          context.drawImage(
            alienBulletImg,
            0 - bulletSize / 2, // center the image
            0 - bulletSize / 2,
            bulletSize,
            bulletSize,
          );
          context.restore();
        } else {
          // fallback: draw green circle for non-asteroid enemies
          context?.save();
          context.fillStyle = "green";
          context.beginPath();
          let size = enemy.hitbox[0].radius;
          context.arc(enemy.position.x, enemy.position.y, size, 0, 2 * Math.PI);
          context.fill();
          context.restore();
        }
      }

      // Hitboxes
      if (hitboxes) {
        enemies.forEach((enemy) => {
          enemy.hitbox?.forEach((hb) => {
            context.save();
            context.strokeStyle = "rgba(255, 0, 0, 0.5)";
            context.beginPath();
            context.arc(
              hb.position.x,
              hb.position.y,
              hb.radius,
              0,
              2 * Math.PI,
            );
            // console.log(typeof hb.position.x, typeof hb.position.y);
            context.stroke();
            context.restore();
          });
          // console.log( "enemy pos",
          //   enemy.position,
          //   "hitbox pos",
          //   enemy.hitbox[0].position,
          // );
        });

        bullets.forEach((bullet) => {
          bullet.hitbox?.forEach((hb) => {
            context.save();
            context.strokeStyle = "rgba(255, 0, 0, 0.5)";
            context.beginPath();
            context.arc(
              hb.position.x,
              hb.position.y,
              hb.radius,
              0,
              2 * Math.PI,
            );
            context.stroke();
            context.restore();
          });
        });

        player?.hitbox?.forEach((hb) => {
          context.save();
          context.strokeStyle = "rgba(255, 0, 0, 0.5)";
          context.beginPath();
          context.arc(hb.position.x, hb.position.y, hb.radius, 0, 2 * Math.PI);
          context.stroke();
          context.restore();
        });
      }

      // Info Text
      // context.save();
      // context.fillStyle = "white";
      // context.font = "20px Arial";
      // context.fillText(`Lives: ${lives}`, 10, 20);
      // context.fillText(`Score: ${score}`, 10, 40);
      // context.fillText(`Level: ${level}`, 10, 60);
      // context.fillText(`Time: ${time}`, 10, 80);
      // context.restore();

      if (!is_running) {
        context.save();
        context.fillStyle = "white";
        await document.fonts.ready;
        context.font = "50px FuturyLight";
        context.fillText("Game Over", 350, 450);

        // upload score if not already uploaded (ensures score is uploaded only once)
        if (!scoreUploadedRef.current) {
          const difficulty = "MEDIUM";
          const uploadUrl =
            `http://137.184.232.147:5000/api/uploadScore?` +
            `username=${encodeURIComponent(username)}&` +
            `profile_name=${encodeURIComponent(username)}&` +
            `difficulty=${encodeURIComponent(difficulty)}&` +
            `score=${encodeURIComponent(score)}&` +
            `level=${encodeURIComponent(level)}&` +
            `duration=${encodeURIComponent(Math.floor(time))}`;

          console.log("Uploading score to:", uploadUrl);

          try {
            const res = await fetch(uploadUrl);
            if (res.ok) {
              console.log("Score uploaded successfully.");
              sessionStorage.setItem("scoreUploaded", "true");
              scoreUploadedRef.current = true;
            } else {
              console.warn("Upload failed with status:", res.status);
            }
          } catch (err) {
            console.error("Failed to upload score:", err);
          }
        }

        context.restore();
      }

      requestRef.current = requestAnimationFrame(animate);
    };

    document.addEventListener("keydown", handleKeyDown);
    document.addEventListener("keyup", handleKeyUp);
    hitboxCheckboxRef.current.addEventListener("change", handleHitboxChange);

    // Start Button (outside of canvas)
    const startBtn = document.getElementById("startGameButton");
    startBtn?.addEventListener("click", async () => {
      scoreUploadedRef.current = false;
      suppressUploadRef.current = true; // prevent upload for a few frames
      sessionStorage.removeItem("scoreUploaded");
    
      await fetch(
        `http://137.184.232.147:5000/api/newGame?` +
        `username=${encodeURIComponent(username)}&` +
        `profile_name=${encodeURIComponent(profile_name)}`
      );
    
      // Allow upload again after a delay (e.g., 500ms or 2 animation frames)
      setTimeout(() => {
        suppressUploadRef.current = false;
      }, 500);
    });

    pauseButtonRef.current.addEventListener("click", () => {
      paused = !paused;
      pauseButtonRef.current.innerText = paused ? "Resume" : "Pause";
    });

    requestRef.current = requestAnimationFrame(animate);

    return () => {
      cancelAnimationFrame(requestRef.current);
      // Clear inputs after rendering
      document.removeEventListener("keydown", handleKeyDown);
      document.removeEventListener("keyup", handleKeyUp);
      window.removeEventListener("beforeunload", handleBeforeUnload);
    };

  }, []);

  return (
    <div className="gameContainer">
      <div className="topBar">
        <div className="topBarLeft">
        <Link to='/main_menu'>
          <button>{'< '}Back</button>
        </Link>
        </div>
        <div className="topBarCenter">
          <label>
            Show Hitboxes{" "}
            <input ref={hitboxCheckboxRef} type="checkbox" id="hitboxes" />
          </label>
          <button id="startGameButton">Start Game</button>
          <button ref={pauseButtonRef} id="pauseGameButton">Pause</button>
        </div>
        <div className="topBarRight"></div>
      </div>

      <div className="centerContentWrapper">
        <div className="canvasWithInfo">
          <div className="canvasWrapper">
            <canvas
              ref={canvasRef}
              id="box1canvas"
              width={1000}
              height={1000}
            ></canvas>
          </div>

          <div className="infoColumn">
            <span><strong>Lives:</strong> <span id="livesDisplayValue">0</span></span>
            <span><strong>Score:</strong> <span id="scoreDisplayValue">0</span></span>
            <span><strong>Level:</strong> <span id="levelDisplayValue">0</span></span>
            <span><strong>Time:</strong> <span id="timeDisplayValue">0</span></span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Game;
