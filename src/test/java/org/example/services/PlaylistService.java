package org.example.services;

import io.restassured.response.ValidatableResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class PlaylistService {
    private static final String BASE_URL = "http://localhost:8888/api/playlists";

    public ValidatableResponse createPlaylist(Playlist playlist) {
        return given()
                .contentType("application/json")
                .body(playlist)
                .when()
                .post(BASE_URL)
                .then();
    }

    public ValidatableResponse updatePlaylist(int playlistId, Playlist updatedPlaylist) {
        String updatePlaylistEndpoint = BASE_URL + "/" + playlistId;

        return given()
                .contentType("application/json")
                .body(updatedPlaylist)
                .when()
                .put(updatePlaylistEndpoint)
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
        String addTrackEndpoint = BASE_URL + "/" + playlistId + "/tracks/add";
        String trackToAdd = "{\"trackId\": " + trackId + "}";

        return given()
                .contentType("application/json")
                .body(trackToAdd)
                .when()
                .post(addTrackEndpoint)
                .then();
    }

    public ValidatableResponse removeTrackFromPlaylist(int playlistId, int trackId) {
        String removeTrackEndpoint = BASE_URL + "/" + playlistId + "/tracks/remove";
        String trackToRemove = "{\"trackId\": " + trackId + "}";

        return given()
                .contentType("application/json")
                .body(trackToRemove)
                .when()
                .delete(removeTrackEndpoint)
                .then();
    }

    public ValidatableResponse deletePlaylist(int playlistId) {
        String deletePlaylistEndpoint = BASE_URL + "/" + playlistId;

        return given()
                .when()
                .delete(deletePlaylistEndpoint)
                .then();
    }
}