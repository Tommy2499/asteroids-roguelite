package com.pluto.database;

import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

/**
 * This client class contains static methods to interact with the MySQL
 * database to fetch and create user data.
 */
public class DatabaseClient {
    private String url;
    private String dbPass;
    private String dbUser;

    /**
     * Constructor for the DatabaseClient. Note that url is meant to be a
     * jdbc connection to a MySql database. An example url would be
     * "jdbc:mysql://localhost:53346" which is the default.
     * 
     * @param url    - url to use for the JDBC connection
     * @param dbUser - username to the database
     * @param dbPass - password to the database
     */
    public DatabaseClient(String url, String dbUser, String dbPass) {
        this.url = url;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
    }

    /**
     * Default constructor for the DatabaseClient.
     */
    public DatabaseClient() {
        this("jdbc:mysql://user_database:3306", "root", "password");
    }

    /**
    * Hashes a plaintext password using the BCrypt hashing algorithm.
    *
    * This method generates a salted hash of the input password using a
    * randomly generated salt with the default log rounds.
    * The result can be safely stored in a database and used for later verification.
    *
    * @param password the plaintext password to hash
    * @return the hashed password as a string
    */
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Method to create a user into the database.
     *
     * @param username - username of the new User
     * @param password - password of the new User
     * @return - String error message, is empty if method is successful
     * 
     */
    public String createUser(String username, String password) {
        // Try with resources making a connection to the MySql database
        // If not, close the database connection
        try (
                // Append Database /Users to the end of the url
                Connection dbConn = DriverManager.getConnection(
                        url + "/Users", dbUser, dbPass);) {
            // Here we create the user
            // Check if the user already exists
            PreparedStatement stmt = dbConn.prepareStatement(
                    "SELECT * FROM Users WHERE user_name = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return "User already exists";
            }
            
            // Hash the password before storing it
            String hashedPassword = hashPassword(password);

            // If the user does not exist, create the user
            stmt = dbConn.prepareStatement(
                    "INSERT INTO Users (user_name, user_password) VALUES (?, ?)");
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.executeUpdate();
            return "";

        } catch (SQLException e) {
            System.out.println(
                    "Could not establish connection to MySQL database.");
            e.printStackTrace();
            return "Error creating user";
        }
    }

    /**
     * Method to log in a user into the database.
     * 
     * @param username - username of the User
     * @param password - password of the User
     * @return - String error message, is empty if method is successful
     */
    public String loginUser(String username, String password) {
        // Try with resources making a connection to the MySql database
        // If not, close the database connection
        try (
            // Append Database /Users to the end of the url
            Connection dbConn = DriverManager.getConnection(url + "/Users", dbUser, dbPass);
        ) {
            // Here we log in the user
            // Check if the user exists and the password is correct
            PreparedStatement stmt = dbConn.prepareStatement(
                    "SELECT user_password FROM Users WHERE user_name = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                String storedHash = rs.getString("user_password");
                // Check the plaintext password against the stored hash
                if (BCrypt.checkpw(password, storedHash)) {
                    return ""; // Success
                } else {
                    return "Username or password is incorrect";
                }
            }
    
            return "Username or password is incorrect"; // User not found
    
        } catch (SQLException e) {
            System.out.println("Could not establish connection to MySQL database.");
            e.printStackTrace();
            return "Error logging in";
        }
    }

    /**
     * Creates a user profile in the database. User cannot have more than four
     * profiles.
     * 
     * @param username     - username of the User
     * @param profile_name - name of the profile
     * @return - Empty string if profile created, error message otherwise
     */
    public String createProfile(String username, String profile_name) {
        try (
                Connection dbConn = DriverManager.getConnection(
                        url + "/Users", dbUser, dbPass);) {
            // Get the user_id from the Users table
            int userId = getUserId(dbConn, username);
            if (userId == -1) {
                return "User does not exist";
            }

            // Check if user already has 4 profiles
            PreparedStatement stmt = dbConn.prepareStatement(
                    "SELECT COUNT(*) FROM Profiles " + 
                            "WHERE user_id = ?");
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) >= 4) {
                return "Max profiles reached";
            }

            // Check if profile name already exists for that user
            stmt = dbConn.prepareStatement(
                    "SELECT * FROM Profiles " + 
                        "WHERE user_id = ? AND Profiles.profile_name = ?");
            stmt.setInt(1, userId);
            stmt.setString(2, profile_name);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return "Profile already exists";
            }

            // Create profile
            stmt = dbConn.prepareStatement(
                    "INSERT INTO Profiles (user_id, profile_name) VALUES (?, ?)");
            stmt.setInt(1, userId);
            stmt.setString(2, profile_name);
            stmt.executeUpdate();
            return "";

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error creating profile";
        }
    }

    /**
     * Renames a user profile in the database.
     * 
     * @param username         - username of the User
     * @param profile_name     - name of the profile
     * @param new_profile_name - new name of the profile
     * @return - Empty string if profile edited, error message otherwise
     */
    public String renameProfile(String username, String profile_name, String new_profile_name) {
        try (
                Connection dbConn = DriverManager.getConnection(
                        url + "/Users", dbUser, dbPass);) {
            // Get the user_id from the Users table
            int userId = getUserId(dbConn, username);
            if (userId == -1) {
                return "User does not exist";
            }

            // Check if profile name exists for that user
            PreparedStatement stmt = dbConn.prepareStatement(
                    "SELECT * FROM Profiles " + 
                            "WHERE user_id = ? AND Profiles.profile_name = ?");
            stmt.setInt(1, userId);
            stmt.setString(2, profile_name);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return "Profile does not exist";
            }

            // Check if new profile name already exists for that user
            stmt = dbConn.prepareStatement(
                    "SELECT * FROM Profiles " + 
                            "WHERE user_id = ? AND Profiles.profile_name = ?");
            stmt.setInt(1, userId);
            stmt.setString(2, new_profile_name);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return "Profile already exists";
            }

            // Edit profile
            stmt = dbConn.prepareStatement(
                    "UPDATE Profiles SET profile_name = ? " + 
                            "WHERE user_id = ? AND Profiles.profile_name = ?");
            stmt.setString(1, new_profile_name);
            stmt.setInt(2, userId);
            stmt.setString(3, profile_name);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                return "";
            } else {
                return "Error editing profile";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error editing profile";
        }
    }

    /**
     * Deletes a user profile in the database. Cascades to delete all data
     * associated with the profile.
     * 
     * @param username     - username of the User
     * @param profile_name - name of the profile
     * @return - Empty string if profile deleted, error message otherwise
     */
    public String deleteProfile(String username, String profile_name) {
        try (
                Connection dbConn = DriverManager.getConnection(
                        url + "/Users", dbUser, dbPass);) {
            // Get the user_id from the Users table
            int userId = getUserId(dbConn, username);
            if (userId == -1) {
                return "User does not exist";
            }

            // Check if profile name exists for that user
            PreparedStatement stmt = dbConn.prepareStatement(
                    "SELECT * FROM Profiles " + 
                        "WHERE user_id = ? AND Profiles.profile_name = ?");
            stmt.setInt(1, userId);
            stmt.setString(2, profile_name);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return "Profile does not exist";
            }

            // Delete profile
            stmt = dbConn.prepareStatement(
                    "DELETE FROM Profiles " +
                        "WHERE user_id = ? AND Profiles.profile_name = ?");
            stmt.setInt(1, userId);
            stmt.setString(2, profile_name);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                return "";
            } else {
                return "Error deleting profile";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error deleting profile";
        }
    }

    /**
     * Fetches all profiles for a user from the database.
     * 
     * @param username - username of the User
     * @return - A string array of all profiles for the user, returns null if an
     *         error occurs
     */
    public String[] getProfiles(String username) {
        try (
                // Append Database /Users to the end of the url
                Connection dbConn = DriverManager.getConnection(
                        url + "/Users", dbUser, dbPass);) {
            // Get the user_id from the Users table
            int userId = getUserId(dbConn, username);
            if (userId == -1) {
                return null;
            }

            // Get all profiles for that user
            PreparedStatement stmt = dbConn.prepareStatement(
                "SELECT profile_name FROM Profiles " + 
                    "WHERE user_id = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            rs.last();
            int numRows = rs.getRow();
            rs.beforeFirst();
            String[] profiles = new String[numRows];
            int i = 0;
            while (rs.next()) {
                profiles[i] = rs.getString("profile_name");
                i++;
            }

            return profiles;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Uploads a score to the database.
     * 
     * @param username     - username of the user
     * @param profile_name - profile name of the user
     * @param difficulty   - difficulty of the game
     *                     Must be one of the following: "EASY", "MEDIUM", "HARD"
     * @param score        - score to upload
     * @param level        - level reached
     * @param duration     - duration of the game
     * @return - Empty string if successful, error message otherwise
     */
    public String uploadScore(String username, String profile_name, String difficulty, int score, int level, int duration) {
        try (
                Connection dbConn = DriverManager.getConnection(
                        url + "/Users", dbUser, dbPass);) {
            // Get the user_id from the Users table
            int userId = getUserId(dbConn, username);
            if (userId == -1) {
                return "User does not exist";
            }

            // Get the profile_id from the Profiles table
            int profile_id = getProfileId(dbConn, userId, profile_name);
            if (profile_id == -1) {
                return "Profile does not exist";
            }

            // Upload the score
            PreparedStatement stmt = dbConn.prepareStatement(
                    "INSERT INTO Scores (profile_id, difficulty, score, level, duration_seconds) VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, profile_id);
            stmt.setString(2, difficulty);
            stmt.setInt(3, score);
            stmt.setInt(4, level);
            stmt.setInt(5, duration);
            stmt.executeUpdate();

            return "";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error uploading score";
        }
    }

    /**
     * Fetches the user id from the database.
     * 
     * @param dbConn - Connection to the database
     * @param username - username of the User
     * @return - The user id, returns -1 if user does not exist or an error occurs
     */
    private int getUserId(Connection dbConn, String username) {
        // Use try with resources to close the statement and result set
        try (PreparedStatement stmt = dbConn.prepareStatement("SELECT User_id FROM Users WHERE User_name = ?")) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("User_id");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user ID for " + username + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }

    /**
     * Fetches the top n scores from the database. Filters by difficulty
     * Score must be one of the following: "score", "level", "duration_seconds".
     * 
     * Caller must close the ResultSet and Statement.
     * 
     * @param n     - number of scores to fetch, must be a positive integer
     * @param score - which score to fetch
     * @param difficulty - difficulty to filter by
     *                  Must be one of the following: "EASY", "MEDIUM", "HARD", "ALL"
     * @return - a ResultSet of the top n scores or null if score is invalid or an
     *         error occurred
     */
    public ResultSet fetchTopScores(int n, String score, String difficulty) {
        if (!score.equals("score") && !score.equals("level") && !score.equals("duration_seconds")) {
            return null;
        }
        if (n <= 0) {
            return null;
        }
        if (!difficulty.equals("EASY") && !difficulty.equals("MEDIUM") && !difficulty.equals("HARD") && !difficulty.equals("ALL")) {
            return null;
        }

        try {
            Connection dbConn = DriverManager.getConnection(url + "/Users", dbUser, dbPass);
            // If difficulty is ALL, do not filter by difficulty
            String difficultyFilter = "";
            if (!difficulty.equals("ALL")) {
                difficultyFilter = "WHERE s.difficulty = ?";
            }
            // Gets the top n scores for distict users
            PreparedStatement stmt = dbConn.prepareStatement(
                    "WITH RankedScores AS (" +
                        "SELECT " + 
                            "u.user_name, " +
                            "p.profile_name, " +
                            "s.score_id, " +
                            "s.score, " +
                            "s.level, " +
                            "s.duration_seconds, " +
                            "s.time_played, " +
                            "ROW_NUMBER() OVER (PARTITION BY u.user_id ORDER BY s." + score + " DESC, s.score_id) AS rn " +
                        "FROM Scores s " +
                        "JOIN Profiles p ON s.profile_id = p.profile_id " +
                        "JOIN Users u ON p.user_id = u.user_id " +
                        difficultyFilter +
                    ") " +
                    "SELECT * " +
                    "FROM RankedScores " +
                    "WHERE rn = 1 " +
                    "ORDER BY " + score +  " DESC " +
                    "LIMIT ?;");
            if (!difficulty.equals("ALL")) {
                stmt.setString(1, difficulty);
                stmt.setInt(2, n);
            } else {
                stmt.setInt(1, n);
            }

            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Fetches statistics for a profile from the database. Has poor performance
     * if there are a lot of scores in the database and there are many reads.
     * Should be fine for this project. Can be filtered by difficulty.
     * 
     * @param username - username of the user
     * @param profile_name - profile name of the user
     * @return - an array of integers containing the statistics for the profile
     * is null if an error occurs
     *           [0] - highest score
     *           [1] - highest level
     *           [2] - highest duration
     *           [3] - number of games played
     */
    public int[] getStats(String username, String profile_name, String difficulty) {
        // Check if difficulty is valid
        if (!difficulty.equals("EASY") && !difficulty.equals("MEDIUM") && !difficulty.equals("HARD") && !difficulty.equals("ALL")) {
            return null;
        }
        try (
            Connection dbConn = DriverManager.getConnection(
                url + "/Users", dbUser, dbPass);) {
            // Get the user_id from the Users table
            int userId = getUserId(dbConn, username);
            if (userId == -1) {
                return null;
            }

            // Get the profile_id from the Profiles table
            int profile_id = getProfileId(dbConn, userId, profile_name);
            if (profile_id == -1) {
                return null;
            }
            // If difficulty is ALL, do not filter by difficulty
            String difficultyFilter = "";
            if (!difficulty.equals("ALL")) {
                difficultyFilter = " AND difficulty = ?";
            }

            // Get the statistics for the profile
            PreparedStatement stmt = dbConn.prepareStatement(
                "SELECT MAX(score) AS highest_score, " +
                       "MAX(level) AS highest_level, " +
                       "MAX(duration_seconds) AS highest_duration, " +
                       "COUNT(*) AS games_played " +
                    "FROM Scores " +
                    "WHERE profile_id = ?"
                    + difficultyFilter
            );
            if (!difficulty.equals("ALL")) {
                stmt.setInt(1, profile_id);
                stmt.setString(2, difficulty);
            } else {
                stmt.setInt(1, profile_id);
            }

            stmt.setInt(1, profile_id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int[] stats = new int[4];
                stats[0] = rs.getInt("highest_score");
                stats[1] = rs.getInt("highest_level");
                stats[2] = rs.getInt("highest_duration");
                stats[3] = rs.getInt("games_played");
                return stats;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
        
    /**
     * Fetches the profile id from the database.
     * 
     * @param dbConn       - Connection to the database
     * @param userId       - user id of the User
     * @param profile_name - profile name of the User
     * @return - The profile id, returns -1 if profile does not exist or an error
     *         occurs
     */
    private int getProfileId(Connection dbConn, int userId, String profile_name) {
        // Use try with resources to close the statement and result set
        try (PreparedStatement stmt = dbConn
                .prepareStatement("SELECT profile_id FROM Profiles WHERE user_id = ? AND profile_name = ?")) {
            stmt.setInt(1, userId);
            stmt.setString(2, profile_name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("profile_id");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching profile ID for " + profile_name + ": " + e.getMessage());
            e.printStackTrace();
        }

        return -1;
    }
}
