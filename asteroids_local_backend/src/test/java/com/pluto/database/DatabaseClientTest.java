package com.pluto.database;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Verifies correctness of HTTP responses for database queries
 * 
 * TODO: TO BE COMPLETED
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) // May customize port here
class DatabaseClientTest {

	/**
	 * IDK what autowired annotation does. supposed to do dependency injection or something?
	 */
	@Autowired
	private TestRestTemplate restTemplate;

	/**
	 * Injects the HTTP server port allocated at runtime
	 */
	@LocalServerPort
	private int port;

	@Test
	void contextLoads() {
	}

	// TODO: Create tests

}
