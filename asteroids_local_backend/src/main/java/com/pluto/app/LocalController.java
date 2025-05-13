package com.pluto.app;

import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.*;
import com.pluto.database.DatabaseClient;
import com.pluto.game.Difficulty;
import com.pluto.game.GameManager;
import com.pluto.game.Spaceship;

/**
 * A controller class for the local backend. It handles HTTP requests from the
 * front end using RestAPI and the Spring Boot framework. By default, it
 * listens on port 8080. All requests are located on localhost:8080/api/
 */
@RestController
@RequestMapping("/api")
public class LocalController {

    /**
     * Regex pattern for matching usernames that must have 3-16 characters
     * (inclusive) and contain only letters (lowercase and uppercase), numbers,
     * and underscores (_).
     */
    private static final String USERNAME_FORMAT = "^[a-zA-Z0-9_]{3,16}$";

    /**
     * Regex pattern for matching passwords that must have 4-32 characters
     * (inclusive) and contain only letters (lowercase and uppercase), numbers,
     * and the following symbols: -=[]\;',./!@#$%^&*()_+{}|:"<>?`~
     * 
     * Note: the frontend must encode all symbols in form %XX with hex digit XX
     * in order for it to not be interpreted as a special character in the URL
     */
    private static final String PASSWORD_FORMAT = 
            "^[a-zA-Z0-9-=\\[\\]\\\\;',.\\/!@#$%^&*()_+{}|:\"<>?`~]{4,32}$";

    /**
     * Stores all the game managers for each user. The key is "username
     * profile_name"
     * and the value is the GameManager object.
     */
    private HashMap<String, GameManager> gameManagers = new HashMap<String, GameManager>();

    /*
     * Stores uploadScore history for a given GameManager (where GameManager)
     * is represented by a string of the username and profile name.
     */
    private HashMap<String, Boolean> uploadedGameScore = new HashMap<String, Boolean>();

    /**
     * This method handles user login requests on localhost:8080/api/login.
     * Response messages are sent in a json format.
     * 
     * @see generateResponse - for json response format
     *
     * @param name - the login name of the user
     * @param pass - the password of the user
     * @return - a json formatted confirmation or error of the login request
     * The json has the following attributes:
     * success - boolean
     * error - string
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/login")
    public String login(
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "pass", defaultValue = "") String pass) {

        // Check that name and pass are of valid format
        if (!name.matches(USERNAME_FORMAT))
            return generateResponse(false, "Username is invalid");
        if (!pass.matches(PASSWORD_FORMAT))
            return generateResponse(false, "Password is invalid");

        // TODO: rewrite condition block as try-catch block for checking dbClient
        // creation errors
        DatabaseClient dbClient = new DatabaseClient();
        String error = dbClient.loginUser(name, pass);
        if (error.equals("")) {
            return generateResponse(true);
        } else {
            return generateResponse(false, error);
        }
    }

    /**
     * This method handles user registration requests on
     * localhost:8080/api/register.
     * Response messages are sent in a json format.
     * 
     * @see generateResponse - for json response format
     * 
     * @param name - the login name of the user
     * @param pass - the password of the user
     * @return - a json formatted confirmation or error of the registration request
     * The json has the following attributes:
     * success - boolean
     * error - string
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/register")
    public String register(
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "pass", defaultValue = "") String pass) {

        // Check that name and pass are of valid format
        if (!name.matches(USERNAME_FORMAT))
            return generateResponse(false, "Username is invalid");
        if (!pass.matches(PASSWORD_FORMAT))
            return generateResponse(false, "Password is invalid");

        // TODO: rewrite condition block as try-catch block for checking dbClient
        // creation errors
        DatabaseClient dbClient = new DatabaseClient();
        String error = dbClient.createUser(name, pass);
        if (error.equals("")) {
            return generateResponse(true);
        } else {
            return generateResponse(false, error);
        }
    }

    /**
     * This method handles user profile creation requests on
     * localhost:8080/api/createProfile.
     * Response messages are sent in a json format.
     * 
     * @see generateResponse - for json response format
     *
     * @param username     - the login name of the user
     * @param profile_name - the name of the profile
     * @return - a json formatted confirmation or error of the profile creation request
     * The json has the following attributes:
     * success - boolean
     * error - string
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/createProfile")
    public String createProfile(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "profile_name", defaultValue = "") String profile_name) {

        // Check that profile_name is of valid format
        if (!profile_name.matches(USERNAME_FORMAT))
            return generateResponse(false, "Profile name is invalid");

        // TODO: rewrite condition block as try-catch block for checking dbClient
        // creation errors
        DatabaseClient dbClient = new DatabaseClient();
        String error = dbClient.createProfile(username, profile_name);
        if (error.equals("")) {
            return generateResponse(true);
        } else {
            return generateResponse(false, error);
        }
    }

    /**
     * This method handles user profile editing requests on
     * localhost:8080/api/editProfile.
     * Response messages are sent in a json format.
     * 
     * @see generateResponse - for json response format
     *
     * @param username         - the login name of the user
     * @param profile_name     - the name of the profile
     * @param new_profile_name - the new name of the profile
     * @return - a json formatted confirmation or error of the profile editing request
     * The json has the following attributes:
     * success - boolean
     * error - string
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/editProfile")
    public String editProfile(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "profile_name", defaultValue = "") String profile_name,
            @RequestParam(value = "new_profile_name", defaultValue = "") String new_profile_name) {

        // Check that new_profile_name is of valid format
        if (!new_profile_name.matches(USERNAME_FORMAT))
            return generateResponse(false, "New profile name is invalid");

        // TODO: rewrite condition block as try-catch block for checking dbClient
        // creation errors
        DatabaseClient dbClient = new DatabaseClient();
        String error = dbClient.renameProfile(username, profile_name, new_profile_name);
        if (error.equals("")) {
            return generateResponse(true);
        } else {
            return generateResponse(false, error);
        }
    }

    /**
     * This method handles user profile deletion requests on
     * localhost:8080/api/deleteProfile.
     * Response messages are sent in a json format.
     * 
     * @see generateResponse - for json response format
     *
     * @param username     - the login name of the user
     * @param profile_name - the name of the profile
     * @return - a json formatted confirmation or error of the profile deletion request
     * The json has the following attributes:
     * success - boolean
     * error - string
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/deleteProfile")
    public String deleteProfile(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "profile_name", defaultValue = "") String profile_name) {

        // TODO: rewrite condition block as try-catch block for checking dbClient
        // creation errors
        DatabaseClient dbClient = new DatabaseClient();
        String error = dbClient.deleteProfile(username, profile_name);
        if (error.equals("")) {
            return generateResponse(true);
        } else {
            return generateResponse(false, error);
        }
    }

    /**
     * This method handles user profile retrieval requests on
     * localhost:8080/api/getProfiles.
     * Response messages are sent in a json format.
     * 
     * @see generateResponse - for json response format
     *
     * @param username - the login name of the user
     * @return - a json formatted list of profiles or an error message
     * The json has the following attributes:
     * success - boolean
     * error - string
     * profiles - a list of strings
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/getProfiles")
    public String getProfiles(
            @RequestParam(value = "username", defaultValue = "") String username) {

        // TODO: rewrite condition block as try-catch block for checking dbClient
        // creation errors
        DatabaseClient dbClient = new DatabaseClient();
        String[] profiles = dbClient.getProfiles(username);
        if (profiles != null) {
            // Converts the array of profiles into a JSON formatted string
            String profilesJson = "[";
            for (int i = 0; i < profiles.length; i++) {
                profilesJson += "\"" + profiles[i] + "\"";
                if (i != profiles.length - 1) {
                    profilesJson += ",";
                }
            }
            profilesJson += "]";
            return "{\"success\":\"" + true + "\","
                    + "\"error\":\"" + "\","
                    + "\"profiles\":" + profilesJson + "}";
        } else {
            return generateResponse(false, "Failed to fetch profiles");
        }
    }

    /**
    * Handles requests related to the leaderboard.
    * This method fetches the top scores from the database view `Leaderboard`
    * and returns them in JSON format. The scores can be sorted by 'score', 'level', 
    * or 'duration_secodns'. The scores can be filtered by difficulty. The difficulty
    * can be "EASY", "MEDIUM", "HARD" or "ALL". The default is "ALL".
    *
    * @param limit The number of top scores to fetch (default is 10).
    * @return A JSON-formatted string containing the top scores or an error message.
    * The JSON has the following attributes:
    * success - boolean
    * error - string
    * leaderboard - a list of scores
    *   user - string
    *   profile - string
    *   score - int
    *   level - int
    *   duration - int
    *   time - string - formatted as yyyy-mm-dd hh:mm:ss.fffffffff, 
    *   @see java.sql.Timestamp.toString()
    */
    @CrossOrigin(origins = "*")
    @GetMapping("/leaderboard")
    public String getLeaderboard(
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "score", defaultValue = "score") String score,
            @RequestParam(value = "difficulty", defaultValue = "ALL") String difficulty) {
        score = score.toLowerCase();
        DatabaseClient dbClient = new DatabaseClient();
        // Fetch top scores ordered by highest Score
        ResultSet rs = dbClient.fetchTopScores(limit, score, difficulty); 
        if (rs == null) {
            return generateResponse(false, "Failed to fetch leaderboard");
        }
        StringBuilder jsonResult = new StringBuilder("{\"leaderboard\":[");

        try {
            boolean first = true;
            while (rs.next()) {
                if (!first) {
                    jsonResult.append(",");
                }
                jsonResult.append("{")
                        .append("\"user\":\"").append(rs.getString("user_name")).append("\",")
                        .append("\"profile\":\"").append(rs.getString("profile_name")).append("\",")
                        .append("\"score\":").append(rs.getInt("score")).append(",")
                        .append("\"level\":").append(rs.getInt("level")).append(",")
                        .append("\"duration\":").append(rs.getInt("duration_seconds")).append(",")
                        .append("\"time\":\"").append(rs.getTimestamp("time_played")).append("\"")
                        .append("}");
                first = false;
            }
            jsonResult.append("], \"success\":true, \"error\":\"\"}");
            return jsonResult.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return generateResponse(false, "Failed to fetch leaderboard");
        }
    }

    /**
    * Handles requests for uploading a new game score.
    * This method inserts a new score record into the `Scores` table.
    *
    * @param username The username of the player.
    * @param profile_name The profile name of the player.
    * @param difficulty The difficulty of the game. Cen be "EASY", "MEDIUM", or "HARD".
    * @param score The score achieved in the game.
    * @param level The level reached in the game.
    * @param duration The duration of the game session in seconds.
    * @return A JSON response indicating success or failure.
    * The JSON has the following attributes:
    * success - boolean
    * error - string
    */
    @CrossOrigin(origins = "*")
    @GetMapping("/uploadScore")
    public String uploadScore(
            @RequestParam("username") String username,
            @RequestParam("profile_name") String profile_name,
            @RequestParam(value = "difficulty", defaultValue = "MEDIUM") String difficulty,
            @RequestParam("score") int score,
            @RequestParam("level") int level,
            @RequestParam("duration") int duration) {
        if (!(difficulty.equals("EASY") || difficulty.equals("MEDIUM") || difficulty.equals("HARD"))) {
            return generateResponse(false, "Invalid difficulty");
        }

        String key = username + " " + profile_name;
        if (!(uploadedGameScore.containsKey(key))) {
           return generateResponse(false, "No game associated with score");
        }
        if (uploadedGameScore.get(key).booleanValue()) {
            return generateResponse(false, "Score has been uploaded already");
        } else {
            uploadedGameScore.put(key, Boolean.valueOf(true));
        }
        
        DatabaseClient dbClient = new DatabaseClient();
        String error = dbClient.uploadScore(username, profile_name, difficulty, score, level, duration);

        if (error.equals("")) {
            return generateResponse(true);
        } else {
            return generateResponse(false, error);
        }
    }

    /**
     * Handles requests for getting profile statistics.
     * This method retrieves the statistics for a given username and profile
     * from the database. Can be filtered by difficulty.
     * 
     * @param username The username of the player.
     * @param profile_name The profile name of the player.
     * @return A JSON response containing the profile statistics or an error message.
     * The json has the following attributes:
     * success - boolean
     * error - string
     * stats - a json object with the following attributes:
     *   highest_score - int
     *   highest_level - int
     *   longest_duration - int
     *   total_games - int
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/getStats")
    public String getStats(
            @RequestParam("username") String username,
            @RequestParam("profile_name") String profile_name,
            @RequestParam(value = "difficulty", defaultValue = "ALL") String difficulty) {
        DatabaseClient dbClient = new DatabaseClient();
        int[] stats = dbClient.getStats(username, profile_name, difficulty);
        if (stats == null) {
            return generateResponse(false, "Failed to fetch profile statistics");
        }
        StringBuilder jsonResult = new StringBuilder("{\"success\":true, \"error\":\"\", \"stats\":{");
        jsonResult.append("\"highest_score\":").append(stats[0]).append(",")
                .append("\"highest_level\":").append(stats[1]).append(",")
                .append("\"longest_duration\":").append(stats[2]).append(",")
                .append("\"total_games\":").append(stats[3])
                .append("}}");
        return jsonResult.toString();
    }

    /**
     * This method creates a new game on localhost:8080/api/newGame.
     * Response messages are sent in a json format.
     * 
     * @param username     - the login name of the user
     * @param profile_name - the name of the profile
     * @param difficulty  - the difficulty of the game, can be "EASY", "MEDIUM" or "HARD"
     * @return - a json formatted confirmation or error of the new game request
     * The json has the following attributes:
     * success - boolean
     * error - string
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/newGame")
    public String newGame(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "profile_name", defaultValue = "") String profile_name,
            @RequestParam(value = "difficulty", defaultValue = "MEDIUM") String difficulty) {
        String key = username + " " + profile_name;
        if (gameManagers.containsKey(key)) {
            gameManagers.remove(key);
        }
        if (uploadedGameScore.containsKey(key)) {
            uploadedGameScore.remove(key);
        }
        gameManagers.put(key, new GameManager(Difficulty.valueOf(difficulty)));
        uploadedGameScore.put(key, Boolean.valueOf(false));
        return generateResponse(true);
    }

    /**
     * This method handles game update requests on localhost:8080/api/updateGame.
     * This method should be called every frame. The time returned is the amount of
     * time the game has been running for in seconds using dt.
     * Response messages are sent in a json format.
     * 
     * @param dt           - the time in seconds since the last update
     * @param username     - the login name of the user
     * @param profile_name - the name of the profile
     * @param inputs       - the player inputs
     * @param difficulty   - the difficulty of the game, can be "EASY", "MEDIUM" or "HARD"
     * @return - a json formatted game state
     * 
     * The json has the following attributes:
     * player - player object
     *    position - Vector2D
     *       x - float
     *       y - float
     *    orientation - float
     *    hitbox - list of hitboxes
     *       position - Vector2D
     *          x - float
     *          y - float
     *       radius - float
     *    lives - int
     *    is_invincible - boolean
     * enemies - array of enemies
     *    type - string, type of enemy ("ASTEROID", "COMET", "ALIEN", "BULLET")
     *    position - Vector2D
     *       x - float
     *       y - float
     *    orientation - float
     *    hitbox - list of hitboxes
     *       position - Vector2D
     *          x - float
     *          y - float
     *       radius - float
     *    size - string (Only for Asteroid enemies. Can be "SMALL", "MEDIUM" or "LARGE")
     * bullets - list of player bullets
     *    position - Vector2D
     *       x - float
     *       y - float
     *    orientation - float
     *    hitbox - list of hitboxes
     *       position - Vector2D
     *          x - float
     *          y - float
     *       radius - float
     * score - int
     * level - int
     * time - float
     * is_running - boolean
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/updateGame")
    public String updateGame(
            @RequestParam(value = "dt", defaultValue = "0") float dt,
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "profile_name", defaultValue = "") String profile_name,
            @RequestParam(value = "inputs", defaultValue = "") String inputs,
            @RequestParam(value = "difficulty", defaultValue = "MEDIUM") String difficulty) {
        // Check that the game manager exists, if not create a new one
        String key = username + " " + profile_name;
        if (!gameManagers.containsKey(key)) {
            gameManagers.put(key, new GameManager(Difficulty.valueOf(difficulty)));
            return gameManagers.get(key).toJson();
        }

        // Parse the inputs
        String[] inputStrings = inputs.trim().split(",");
        if (inputs.equals("")) {
            inputStrings = new String[0];
        } else {
            inputStrings = inputs.split(",");
        }
        // Check if the game is running
        GameManager gameManager = gameManagers.get(key);
        if (!gameManager.is_running) {
            return gameManager.toJson();
        }
        // Call the update method and return the game state
        Spaceship.Input[] input = new Spaceship.Input[inputStrings.length];
        for (int i = 0; i < inputStrings.length; i++) {
            input[i] = Spaceship.Input.valueOf(inputStrings[i]);
        }
        gameManager.update(dt, input);

        return gameManager.toJson();
    }

    /**
     * Generates a JSON formatted string representing a response message.
     * 
     * Overloaded method allowing error message to be specified
     * 
     * @param status   - True if the response was sucessful, false otherwise
     * @param errorMsg - A detailed description of any errors, or blank if none
     */
    private String generateResponse(boolean status, String errorMsg) {
        return "{\"success\":\"" + status + "\","
                + "\"error\":\"" + errorMsg + "\"}";
    }

    /**
     * Generates a JSON formatted string representing a response message.
     * 
     * Overloaded method for no error message
     * 
     * @param status - True if the response was sucessful, false otherwise
     */
    private String generateResponse(boolean status) {
        return generateResponse(status, "");
    }
}
