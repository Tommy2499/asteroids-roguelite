package com.pluto.app;

// Testing utilities
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

// Spring utilties
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

// For parsing responses as JSON object
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Verifies correctness of HTTP responses from local backend for user logins
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) // May customize port here
class LocalControllerTest {

	/**
	 * Synchronous client for performing HTTP requests to mock the frontend
	 * 
	 * Autowired annotation allows Spring to automatically inject dependencies
	 * (beans) at runtime 
	 */
	@Autowired
	private TestRestTemplate restTemplate; // TODO: rename to something more useful

	/**
	 * Injects the HTTP server port allocated at runtime
	 */
	@LocalServerPort
	private int port;

	/**
	 * Included by Spring Boot
	 * 
	 * Verifies the application is able to load the Spring context
	 */
	@Test
	void contextLoads() {
	}

	/**
	 * Verifies response for login call with default arguments
	 */
	@Test
	void testDefaultLoginResponse() {
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/login", String.class);
		String expected = "{\"success\": \"false\"},{\"error\": \"Invalid login credentials\"}";
		assertEquals(expected, response.getBody());
	}

	/**
	 * Verifies response for login call with specified arguments
	 */
	@Test
	void testLoginResponse() {
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/login?name=steve boss&pass=456", String.class);
		String expected = "{\"success\": \"false\"},{\"error\": \"Invalid login credentials\"}";
		assertEquals(expected, response.getBody());
	}

	/**
	 * Verifies response for register call with default arguments
	 */
	@Test
	void testDefaultRegisterResponse() {
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/register", String.class);
		String expected = "{\"success\": \"false\"},{\"error\": \"User already exists\"}";
		assertEquals(expected, response.getBody());
	}

	/**
	 * Verifies response for register call with specified arguments
	 */
	@Test
	void testRegisterResponse() {
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/register?name=steve boss&pass=456", String.class);
		String expected = "{\"success\": \"false\"},{\"error\": \"User already exists\"}";
		assertEquals(expected, response.getBody());
	}

	/**
 	* Verifies response for leaderboard call when leaderboard is empty
 	*/
	@Test
	void testLeaderboardEmptyResponse() {
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/leaderboard", String.class);
		String expected = "{\"leaderboard\":[]}";
		assertEquals(expected, response.getBody());
	}

	/**
 	* Verifies response for uploading a score with invalid profile_id
 	*/
	@Test
	void testUploadScoreFailureResponse() {
		String url = String.format("http://localhost:%d/api/uploadScore?profile_id=9999&score=1000&level=5&duration=300", port);
		ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
		String expected = "{\"success\":false, \"error\":\"Failed to upload score\"}";
		assertEquals(expected, response.getBody());
	}
}
