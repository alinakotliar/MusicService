package org.example;

import io.restassured.response.ValidatableResponse;
import org.example.services.PlaylistService;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class MusicServiceApiTest{
    private PlaylistService playlistService = new PlaylistService();

    @Test
    public void createPlaylistAndVerify() {
        String playlistDetails = "{\n" +
                "   \"description\": \"My favorite playlist2\",\n" +
                "   \"isPublic\": true,\n" +
                "   \"name\": \"My Playlist\",\n" +
                "   \"userId\": 1\n" +
                "}";

        ValidatableResponse createResponse = playlistService.createPlaylist(playlistDetails);
        createResponse.assertThat().statusCode(201);

        int createdPlaylistId = createResponse.extract().path("id");

        ValidatableResponse retrieveResponse = playlistService.getPlaylist(createdPlaylistId);
        retrieveResponse.assertThat().statusCode(200);
        retrieveResponse.assertThat().body("name", equalTo("My Playlist"));
        retrieveResponse.assertThat().body("description", equalTo("My favorite playlist2"));
        retrieveResponse.assertThat().body("isPublic", equalTo(true));
    }

    @Test
    public void modifyPlaylistAttributesAndVerify() {
        String playlistDetails = "{\n" +
                "   \"description\": \"My favorite playlist\",\n" +
                "   \"isPublic\": true,\n" +
                "   \"name\": \"My Playlist\",\n" +
                "   \"userId\": 1\n" +
                "}";

        ValidatableResponse createResponse = playlistService.createPlaylist(playlistDetails);
        createResponse.assertThat().statusCode(201);

        int createdPlaylistId = createResponse.extract().path("id");

        String updatedDetails = "{\n" +
                "   \"description\": \"Updated description\",\n" +
                "   \"isPublic\": false,\n" +
                "   \"name\": \"Updated Playlist\",\n" +
                "   \"userId\": 1\n" +
        "}";

        ValidatableResponse updateResponse = playlistService.updatePlaylist(createdPlaylistId, updatedDetails);
        updateResponse.assertThat().statusCode(200);

        ValidatableResponse retrieveResponse = playlistService.getPlaylist(createdPlaylistId);
        retrieveResponse.assertThat().statusCode(200);
        retrieveResponse.assertThat().body("name", equalTo("Updated Playlist"));
        retrieveResponse.assertThat().body("description", equalTo("Updated description"));
        retrieveResponse.assertThat().body("isPublic", equalTo(false));
    }

    @Test
    public void addTracksToPlaylistAndVerify() {
        String playlistDetails = "{\n" +
                "   \"description\": \"My favorite playlist\",\n" +
                "   \"isPublic\": true,\n" +
                "   \"name\": \"My Playlist\",\n" +
                "   \"userId\": 1\n" +
                "}";

        ValidatableResponse createResponse = playlistService.createPlaylist(playlistDetails);
        createResponse.assertThat().statusCode(201);

        int createdPlaylistId = createResponse.extract().path("id");

        String tracksToAdd = "{\n" +
                "   \"trackId\": 1\n" +
                "}";

        ValidatableResponse addTracksResponse = playlistService.addTrackToPlaylist(createdPlaylistId, 1);
        addTracksResponse.assertThat().statusCode(200);

        ValidatableResponse retrieveResponse = playlistService.getPlaylist(createdPlaylistId);
        retrieveResponse.assertThat().statusCode(200);
        retrieveResponse.assertThat().body("tracks[0].id", equalTo(1));
    }

    @Test
    public void removeTrackFromPlaylistAndVerify() {
        String playlistDetails = "{\n" +
                "   \"description\": \"My favorite playlist\",\n" +
                "   \"isPublic\": true,\n" +
                "   \"name\": \"My Playlist\",\n" +
                "   \"userId\": 1\n" +
                "}";

        ValidatableResponse createResponse = playlistService.createPlaylist(playlistDetails);
        createResponse.assertThat().statusCode(201);

        int createdPlaylistId = createResponse.extract().path("id");

        String tracksToAdd = "{\n" +
                "   \"trackId\": 1\n" +
                "}";

        ValidatableResponse addTracksResponse = playlistService.addTrackToPlaylist(createdPlaylistId, 1);
        addTracksResponse.assertThat().statusCode(200);

        ValidatableResponse removeTrackResponse = playlistService.removeTrackFromPlaylist(createdPlaylistId, 1);
        removeTrackResponse.assertThat().statusCode(200);

        ValidatableResponse retrieveResponse = playlistService.getPlaylist(createdPlaylistId);
        retrieveResponse.assertThat().statusCode(200);
        retrieveResponse.assertThat().body("tracks", is(empty()));
    }

    @Test
    public void deletePlaylistAndVerify() {
        String playlistDetails = "{\n" +
                "   \"description\": \"My favorite playlist\",\n" +
                "   \"isPublic\": true,\n" +
                "   \"name\": \"My Playlist\",\n" +
                "   \"userId\": 1\n" +
                "}";

        ValidatableResponse createResponse = playlistService.createPlaylist(playlistDetails);
        createResponse.assertThat().statusCode(201);

        int createdPlaylistId = createResponse.extract().path("id");

        ValidatableResponse deleteResponse = playlistService.deletePlaylist(createdPlaylistId);
        deleteResponse.assertThat().statusCode(200);

        ValidatableResponse retrieveResponse = playlistService.getPlaylist(createdPlaylistId);
        retrieveResponse.assertThat().statusCode(404);
    }
}