# Research Report
## pixiJS
### Summary of Work
<!--One paragraph summary of the research being performed-->

I researched how to use PixiJS, a 2D rendering engine for WebGL and canvas, to create an interactive graphics application. I initialized a PixiJS application, load assets, created and manipulated sprites, and implemented an animation loop using the application's ticker. Through this research, I created a simple demo that loads a bunny sprite, positions it in the center of the screen, and rotates it continuously. The code is been recoded from the PixiJS website with extra documentation I added.

### Motivation
<!--Explain why you felt the need to perform this research-->

I wanted to see if there was an easier alternative to our stack, which was using the frontend as a rendering screen for the backend. I thought PixiJS would be easy to use and implement which would make coding our game easier. PixiJS utilizes WebGL which would make the possibility for lag or frame drops much lower. 

I briefly looked at a YouTube tutorial on Asteroids in purely js. 

### Time Spent
<!--Explain how your time was spent-->

- 20 minutes reading PixiJS documentation

- 5 minutes exploring PixiJS asset loading

- 5 minutes setting up a project and writing the initial code

- 5 minutes asking ChatGPT for keyboard controller implementation

- 10 minutes looking at Youtube tutorial code

### Results
<!--Explain what you learned/produced/etc. This section should explain the
important things you learned so that it can serve as an easy reference for yourself
and others who could benefit from reviewing this topic. Include your sources as
footnotes. Make sure you include the footnotes where appropriate e.g [^1]-->

I successfully created a demo using PixiJS that initializes an application, loads a sprite, and applies a transformation. I followed the tutorial here [^1].

#### Initialization:
This code initializes a new PixiJS application with a specified background color and appends the canvas to the DOM. The DOM itself does not use WebGL, but PixiJS utilizes WebGL for rendering when possible. If not, it uses canvas elements. I followed and read through this documentation [^3]. 

```javascript
// Create a PixiJS application
// DOM element will be created automatically
const app = new Application();

// Intialize the application.
await app.init({ background: '#1099bb', resizeTo: window });

// Then adding the application's canvas to the DOM body
document.body.appendChild(app.canvas);
```

#### Loading and Displaying a Sprite
This snippet loads an image as a texture and creates a sprite from it. The sprite is then positioned at the center of the screen. PixiJS has a set of assets that are public and easy to load in. This could be useful for assets that we don't want to make or don't choose to make. Also, it seems useful that objects can have a set anchor instead of the usual top left corner that canvas uses. I followed and read through this documentation, which was very useful for sprites, which extend from containers [^4]. 

```javascript
// Load the bunny texture.
// The Assets class is a singleton that manages the loading of assets
const texture = await Assets.load('https://pixijs.com/assets/bunny.png');

// Create a new Sprite from an image path
// Sprite extends the Container class, which is the base class for all display objects in PixiJS
const bunny = new Sprite(texture);

// Add to stage
app.stage.addChild(bunny);

// Center the sprite's anchor point.
// Anchor point is relative to the sprite's width and height
bunny.anchor.set(0.5);

// Move the sprite to the center of the screen.
bunny.x = app.screen.width / 2;
bunny.y = app.screen.height / 2;
```

#### Animating the Sprite
This animation loop rotates the sprite continuously, adjusting the rotation speed based on the frame delta time for smooth animations. I found out that instead of using frames, it uses the time delta instead. This ensures that for any issues for frames or visual effects, the "backend" calculation for the rotation keeps consistent across those. 

```javascript
// Add an animation loop callback to the application's ticker.
app.ticker.add((time) =>
{
    // Time delta is the time elapsed since the last frame
    // Using delta will ensure that the animation is smooth and consistent across different frame rates/frame drops
    bunny.rotation += 0.1 * time.deltaTime;
});
```

#### Other info

The code in full is in the T_07 repository found here [^2]

#### JS implementation
I spent some time looking at the YouTube tutorial's code, and after a couple minutes I decided it was too long to follow for the purposes of this research doc. At that point of deciding, Alex had an implementation that was up and running so I decided to end this research. Also, this game logic sat entirely within javascript which sort of defeats the purpose of our backend's singleton and game logic. The code is here [^5]

#### Conclusion
PixiJS was actually harder to use than I initially anticipated and decided against it. The JS implementation is simplier to understand, but too much logic is stored purely in JS. Both are possible and fast solutions to a web game, but not exactly what we need. We prefer to use the frontend as a renderer rather than the entire code.

### Sources
<!--list your sources and link them to a footnote with the source url-->
- PixiJS[^1]
- Research Code[^2]
- Getting Started[^3]
- Container[^4]
- Tutorial [^5]
[^1]: https://pixijs.com/8.x/tutorials/getting-started#1
[^2]: https://git.doit.wisc.edu/cdis/cs/courses/cs506/sp2025/team/T_07/research-pixijs
[^3]: https://pixijs.com/8.x/guides/basics/getting-started
[^4]: https://pixijs.com/8.x/guides/components/containers
[^5]: https://github.com/chriscourses/net-ninja-asteroids