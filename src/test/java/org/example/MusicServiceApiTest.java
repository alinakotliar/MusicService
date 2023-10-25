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

        // List of tracks to add
        List<Integer> trackIdsToAdd = Arrays.asList(1, 2, 3, 4);

        // Adding tracks to playlist
        for (int trackId : trackIdsToAdd) {
            ValidatableResponse addTrackResponse = playlistService.addTrackToPlaylist(createdPlaylistId, trackId);
            addTrackResponse.assertThat().statusCode(200);
        }

        // Check that tracks are added to playlist
        ValidatableResponse retrieveResponse = playlistService.getPlaylist(createdPlaylistId);
        retrieveResponse.assertThat().statusCode(200);

        // Getting the Track List from the response
        List<Integer> trackIdsInPlaylist = retrieveResponse.extract().path("tracks.id");

        for (int trackId : trackIdsToAdd) {
            // Check that tracks are added to playlist
            Assert.assertTrue(trackIdsInPlaylist.contains(trackId), "Track " + trackId + " should be in the playlist.");
        }
    }

    @Test
    public void removeTrackFromPlaylistAndVerify() {
        // Create a new playlist for existing users.
        List<Integer> trackIds = new ArrayList<>(Arrays.asList(1, 2, 3, 4)); // List of tracks

        int createdPlaylistId = createPlaylistAndAddTracks(trackIds);

        // Remove one of the tracks from the list
        int trackToRemove = 2;
        ValidatableResponse removeTrackResponse = playlistService.removeTrackFromPlaylist(createdPlaylistId, trackToRemove);
        removeTrackResponse.assertThat().statusCode(200);

        // Make sure the track is removed
        ValidatableResponse retrieveResponse = playlistService.getPlaylist(createdPlaylistId);
        retrieveResponse.assertThat().statusCode(200);

        // retrieve list of tracks from response
        List<Integer> trackIdsInPlaylist = retrieveResponse.extract().path("tracks.id");

        //response track is NOT present, the playlist could be retrieved from the endpoint and DOESN’T contain the removed track.
        Assert.assertFalse(trackIdsInPlaylist.contains(trackToRemove), "Track " + trackToRemove + " should not be in the playlist.");
    }

    // Helper method for creating and adding tracks to playlist
    private int createPlaylistAndAddTracks(List<Integer> trackIds) {
        Playlist playlist = new Playlist("My favorite playlist", true, "My Playlist", 1);
        ValidatableResponse createResponse = playlistService.createPlaylist(playlist);
        createResponse.assertThat().statusCode(201);
        int createdPlaylistId = createResponse.extract().path("id");

        // Add tracks to playlist
        addTracksToPlaylist(createdPlaylistId, trackIds);

        return createdPlaylistId;
    }


    private void addTracksToPlaylist(int playlistId, List<Integer> trackIds) {
        for (int trackId : trackIds) {
            ValidatableResponse addTrackResponse = playlistService.addTrackToPlaylist(playlistId, trackId);
            addTrackResponse.assertThat().statusCode(200);
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