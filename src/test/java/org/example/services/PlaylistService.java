package org.example.services;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class PlaylistService {
    private static final String BASE_URL = "http://localhost:8888/api/playlists";

    public ValidatableResponse createPlaylist(String playlistDetails) {
        return given()
                .contentType("application/json")
                .body(playlistDetails)
                .when()
                .post(BASE_URL)
                .then();
    }

    public ValidatableResponse updatePlaylist(int playlistId, String updatedDetails) {
        String updatePlaylistEndpoint = BASE_URL + "/" + playlistId;
        return given()
                .contentType("application/json")
                .body(updatedDetails)
                .when()
                .put(updatePlaylistEndpoint)
                .then();
    }

    public ValidatableResponse deletePlaylist(int playlistId) {
        String deletePlaylistEndpoint = BASE_URL + "/" + playlistId;
        return given()
                .when()
                .delete(deletePlaylistEndpoint)
                .then();
    }

    public ValidatableResponse getPlaylist(int playlistId) {
        String retrievePlaylistEndpoint = BASE_URL + "/" + playlistId;
        return given()
                .when()
                .get(retrievePlaylistEndpoint)
                .then();
    }

    public ValidatableResponse addTrackToPlaylist(int playlistId, int trackId) {
        String addTracksEndpoint = BASE_URL + "/" + playlistId + "/tracks/add";
        String tracksToAdd = "{\n" +
                "   \"trackId\": " + trackId + "\n" +
                "}";
        return given()
                .contentType("application/json")
                .body(tracksToAdd)
                .when()
                .post(addTracksEndpoint)
                .then();
    }

    public ValidatableResponse removeTrackFromPlaylist(int playlistId, int trackId) {
        String removeTrackEndpoint = BASE_URL + "/" + playlistId + "/tracks/remove";
        String trackToRemove = "{\n" +
                "   \"trackId\": " + trackId + "\n" +
                "}";
        return given()
                .contentType("application/json")
                .body(trackToRemove)
                .when()
                .delete(removeTrackEndpoint)
                .then();
    }
}