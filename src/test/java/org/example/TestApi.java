package org.example;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TestApi {
    private static final String BASE_URL = "http://localhost:8888/api/playlists";

    @Test
    public void createPlaylistAndVerify() {
        // Create a new playlist for existing users
        String createPlaylistEndpoint = BASE_URL;

        // Define the playlist details using the provided example value
        String playlistDetails = "{\n" +
                "   \"description\": \"My favorite playlist2\",\n" +
                "   \"isPublic\": true,\n" +
                "   \"name\": \"My Playlist\",\n" +
                "   \"userId\": 1\n" +
                "}";

        // Send a POST request to create the playlist
        ValidatableResponse createResponse = given()
                .contentType("application/json")
                .body(playlistDetails)
                .when()
                .post(createPlaylistEndpoint)
                .then();

        // Assert that the response status code is 200
        createResponse.assertThat().statusCode(201);

        // Extract the ID of the created playlist
        int createdPlaylistId = createResponse.extract().path("id");

        // Retrieve the created playlist and verify its details
        String retrievePlaylistEndpoint = BASE_URL + "/" + createdPlaylistId;

        ValidatableResponse retrieveResponse = given()
                .when()
                .get(retrievePlaylistEndpoint)
                .then();

        // Assert that the retrieved playlist details match the expected values
        retrieveResponse.assertThat().statusCode(200);
        retrieveResponse.assertThat().body("name", equalTo("My Playlist"));
        retrieveResponse.assertThat().body("description", equalTo("My favorite playlist2"));
        retrieveResponse.assertThat().body("isPublic", equalTo(true));
    }


    @Test
    public void modifyPlaylistAttributesAndVerify() {
        // Create a new playlist for existing users
        String createPlaylistEndpoint = BASE_URL;
        String playlistDetails = "{\n" +
                "   \"description\": \"My favorite playlist\",\n" +
                "   \"isPublic\": true,\n" +
                "   \"name\": \"My Playlist\",\n" +
                "   \"userId\": 1\n" +
                "}";
        ValidatableResponse createResponse = given()
                .contentType("application/json")
                .body(playlistDetails)
                .when()
                .post(createPlaylistEndpoint)
                .then();
        createResponse.assertThat().statusCode(201);

        // Extract the ID of the created playlist
        int createdPlaylistId = createResponse.extract().path("id");

        // Update the playlist's attributes
        String updatePlaylistEndpoint = BASE_URL + "/" + createdPlaylistId;
        String updatedDetails = "{\n" +
                "   \"description\": \"Updated description\",\n" +
                "   \"isPublic\": false,\n" +
                "   \"name\": \"Updated Playlist\",\n" +
                "   \"userId\": 1\n" +
                "}";
        ValidatableResponse updateResponse = given()
                .contentType("application/json")
                .body(updatedDetails)
                .when()
                .put(updatePlaylistEndpoint)
                .then();
        updateResponse.assertThat().statusCode(200);

        // Retrieve the updated playlist and verify its details
        ValidatableResponse retrieveResponse = given()
                .when()
                .get(updatePlaylistEndpoint)
                .then();
        retrieveResponse.assertThat().statusCode(200);
        retrieveResponse.assertThat().body("name", equalTo("Updated Playlist"));
        retrieveResponse.assertThat().body("description", equalTo("Updated description"));
        retrieveResponse.assertThat().body("isPublic", equalTo(false));
    }

    @Test
    public void addTracksToPlaylistAndVerify() {
        // Create a new playlist for existing users
        String createPlaylistEndpoint = BASE_URL;
        String playlistDetails = "{\n" +
                "   \"description\": \"My favorite playlist\",\n" +
                "   \"isPublic\": true,\n" +
                "   \"name\": \"My Playlist\",\n" +
                "   \"userId\": 1\n" +
                "}";
        ValidatableResponse createResponse = given()
                .contentType("application/json")
                .body(playlistDetails)
                .when()
                .post(createPlaylistEndpoint)
                .then();
        createResponse.assertThat().statusCode(201);

        // Extract the ID of the created playlist
        int createdPlaylistId = createResponse.extract().path("id");

        // Add tracks to the playlist
        String addTracksEndpoint = BASE_URL + "/" + createdPlaylistId + "/tracks/add";
        String tracksToAdd = "{\n" +
                "   \"trackId\": 1\n" +
                "}";
        ValidatableResponse addTracksResponse = given()
                .contentType("application/json")
                .body(tracksToAdd)
                .when()
                .post(addTracksEndpoint)
                .then();
        addTracksResponse.assertThat().statusCode(200);

        // Retrieve the playlist and verify that the tracks were added
        String retrievePlaylistEndpoint = BASE_URL + "/" + createdPlaylistId;
        ValidatableResponse retrieveResponse = given()
                .when()
                .get(retrievePlaylistEndpoint)
                .then();
        retrieveResponse.assertThat().statusCode(200);
        retrieveResponse.assertThat().body("tracks[0].id", equalTo(1));
    }

    @Test
    public void removeTrackFromPlaylistAndVerify() {
        // Create a new playlist for existing users
        String createPlaylistEndpoint = BASE_URL;
        String playlistDetails = "{\n" +
                "   \"description\": \"My favorite playlist\",\n " +
                "   \"isPublic\": true,\n" +
        "   \"name\": \"My Playlist\",\n" +
                "   \"userId\": 1\n" +
        "}";
        ValidatableResponse createResponse = given()
                .contentType("application/json")
                .body(playlistDetails)
                .when()
                .post(createPlaylistEndpoint)
                .then();
        createResponse.assertThat().statusCode(201);

        // Extract the ID of the created playlist
        int createdPlaylistId = createResponse.extract().path("id");

        // Add a track to the playlist
        String addTracksEndpoint = BASE_URL + "/" + createdPlaylistId + "/tracks/add";
        String tracksToAdd = "{\n" +
                "   \"trackId\": 1\n" +
                "}";
        ValidatableResponse addTracksResponse = given()
                .contentType("application/json")
                .body(tracksToAdd)
                .when()
                .post(addTracksEndpoint)
                .then();
        addTracksResponse.assertThat().statusCode(200);

        // Remove the added track from the playlist
        String removeTrackEndpoint = BASE_URL + "/" + createdPlaylistId + "/tracks/remove";
        String trackToRemove = "{\n" +
                "   \"trackId\": 1\n" +
                "}";
        ValidatableResponse removeTrackResponse = given()
                .contentType("application/json")
                .body(trackToRemove)
                .when()
                .delete(removeTrackEndpoint)
                .then();
        removeTrackResponse.assertThat().statusCode(200);

        // Retrieve the playlist and verify that the track was removed
        String retrievePlaylistEndpoint = BASE_URL + "/" + createdPlaylistId;
        ValidatableResponse retrieveResponse = given()
                .when()
                .get(retrievePlaylistEndpoint)
                .then();
        retrieveResponse.assertThat().statusCode(200);
        retrieveResponse.assertThat().body("tracks", is(empty()));
    }

    @Test
    public void deletePlaylistAndVerify() {
        // Create a new playlist for existing users
        String createPlaylistEndpoint = BASE_URL;
        String playlistDetails = "{\n" +
                "   \"description\": \"My favorite playlist\",\n " +
                "   \"isPublic\": true,\n" +
                "   \"name\": \"My Playlist\",\n" +
                "   \"userId\": 1\n" +
                "}";
        ValidatableResponse createResponse = given()
                .contentType("application/json")
                .body(playlistDetails)
                .when()
                .post(createPlaylistEndpoint)
                .then();
        createResponse.assertThat().statusCode(201);

        // Extract the ID of the created playlist
        int createdPlaylistId = createResponse.extract().path("id");

        // Delete the created playlist
        String deletePlaylistEndpoint = BASE_URL + "/" + createdPlaylistId;
        ValidatableResponse deleteResponse = given()
                .when()
                .delete(deletePlaylistEndpoint)
                .then();
        deleteResponse.assertThat().statusCode(200);

        // Verify that the deleted playlist no longer exists
        String retrievePlaylistEndpoint = BASE_URL + "/" + createdPlaylistId;
        ValidatableResponse retrieveResponse = given()
                .when()
                .get(retrievePlaylistEndpoint)
                .then();
        retrieveResponse.assertThat().statusCode(404);
    }
}