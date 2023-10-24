package org.example;

import io.restassured.response.ValidatableResponse;
import org.example.services.Playlist;
import org.example.services.PlaylistService;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;

public class MusicServiceApiTest{
    private PlaylistService playlistService = new PlaylistService();

    @Test
    public void createPlaylistAndVerify() {
        Playlist playlist = new Playlist("My favorite playlist2", true, "My Playlist", 1);

        ValidatableResponse createResponse = playlistService.createPlaylist(playlist);
        createResponse.assertThat().statusCode(201);

        int createdPlaylistId = createResponse.extract().path("id");

        ValidatableResponse retrieveResponse = playlistService.getPlaylist(createdPlaylistId);
        retrieveResponse.assertThat().statusCode(200);
        retrieveResponse.assertThat().body("name", equalTo(playlist.getName()));
        retrieveResponse.assertThat().body("description", equalTo(playlist.getDescription()));
        retrieveResponse.assertThat().body("isPublic", equalTo(playlist.isPublic()));
    }

    @Test
    public void modifyPlaylistAttributesAndVerify() {
        Playlist playlist = new Playlist("My favorite playlist", true, "My Playlist", 1);

        ValidatableResponse createResponse = playlistService.createPlaylist(playlist);
        createResponse.assertThat().statusCode(201);

        int createdPlaylistId = createResponse.extract().path("id");

        Playlist updatedPlaylist = new Playlist("Updated description", false, "Updated Playlist", 1);
        ValidatableResponse updateResponse = playlistService.updatePlaylist(createdPlaylistId, updatedPlaylist);
        updateResponse.assertThat().statusCode(200);

        ValidatableResponse retrieveResponse = playlistService.getPlaylist(createdPlaylistId);
        retrieveResponse.assertThat().statusCode(200);
        retrieveResponse.assertThat().body("name", equalTo(updatedPlaylist.getName()));
        retrieveResponse.assertThat().body("description", equalTo(updatedPlaylist.getDescription()));
        retrieveResponse.assertThat().body("isPublic", equalTo(updatedPlaylist.isPublic()));
    }

    @Test
    public void addTracksToPlaylistAndVerify() {
        Playlist playlist = new Playlist("My favorite playlist", true, "My Playlist", 1);

        ValidatableResponse createResponse = playlistService.createPlaylist(playlist);
        createResponse.assertThat().statusCode(201);

        int createdPlaylistId = createResponse.extract().path("id");

        // Список добавляемых треков
        List<Integer> trackIdsToAdd = Arrays.asList(1, 2, 3);

        // Добавление треков в плейлист
        for (int trackId : trackIdsToAdd) {
            ValidatableResponse addTrackResponse = playlistService.addTrackToPlaylist(createdPlaylistId, trackId);
            addTrackResponse.assertThat().statusCode(200);
        }

        // Проверка, что треки добавлены в плейлист
        ValidatableResponse retrieveResponse = playlistService.getPlaylist(createdPlaylistId);
        retrieveResponse.assertThat().statusCode(200);

        // Получение списка треков из ответа
        List<Integer> trackIdsInPlaylist = retrieveResponse.extract().path("tracks.id");

        for (int trackId : trackIdsToAdd) {
            // Проверка, что треки добавлены в плейлист
            Assert.assertTrue(trackIdsInPlaylist.contains(trackId), "Track " + trackId + " should be in the playlist.");
        }
    }

    @Test
    public void removeTrackFromPlaylistAndVerify() {
        Playlist playlist = new Playlist("My favorite playlist", true, "My Playlist", 1);

        ValidatableResponse createResponse = playlistService.createPlaylist(playlist);
        createResponse.assertThat().statusCode(201);

        int createdPlaylistId = createResponse.extract().path("id");

        // Список треков, которые будут добавлены и затем удалены
        List<Integer> trackIdsToAddAndRemove = Arrays.asList(1, 2, 3);

        // Добавление треков в плейлист
        for (int trackId : trackIdsToAddAndRemove) {
            ValidatableResponse addTrackResponse = playlistService.addTrackToPlaylist(createdPlaylistId, trackId);
            addTrackResponse.assertThat().statusCode(200);
        }

        // Удаление треков из плейлиста
        for (int trackId : trackIdsToAddAndRemove) {
            ValidatableResponse removeTrackResponse = playlistService.removeTrackFromPlaylist(createdPlaylistId, trackId);
            removeTrackResponse.assertThat().statusCode(200);
        }

        // Проверка, что треки успешно удалены из плейлиста
        ValidatableResponse retrieveResponse = playlistService.getPlaylist(createdPlaylistId);
        retrieveResponse.assertThat().statusCode(200);

        // Получение списка треков из ответа
        List<Integer> trackIdsInPlaylist = retrieveResponse.extract().path("tracks.id");

        for (int trackId : trackIdsToAddAndRemove) {
            // Проверка, что треки удалены из плейлиста
            Assert.assertFalse(trackIdsInPlaylist.contains(trackId), "Track " + trackId + " should not be in the playlist.");
        }
    }

    @Test
    public void deletePlaylistAndVerify() {
        Playlist playlist = new Playlist("My favorite playlist", true, "My Playlist", 1);

        ValidatableResponse createResponse = playlistService.createPlaylist(playlist);
        createResponse.assertThat().statusCode(201);

        int createdPlaylistId = createResponse.extract().path("id");

        ValidatableResponse deleteResponse = playlistService.deletePlaylist(createdPlaylistId);
        deleteResponse.assertThat().statusCode(200);

        ValidatableResponse retrieveResponse = playlistService.getPlaylist(createdPlaylistId);
        retrieveResponse.assertThat().statusCode(404);
    }
}