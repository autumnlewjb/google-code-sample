package com.google;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class VideoPlaylistLibrary {
    private HashSet<String> history;
    private HashMap<String, VideoPlaylist> videoPlaylists;

    public VideoPlaylistLibrary() {
        history = new HashSet<>();
        videoPlaylists = new HashMap<>();
    }

    public VideoPlaylist createPlaylist(String name) {
        if (name.split("\\s+").length > 1) {
            return null;
        }
        if (history.contains(name.toLowerCase())) {
            System.out.println("Cannot create playlist: A playlist with the same name already exists");
            return null;
        }
        System.out.println("Successfully created new playlist: " + name);
        history.add(name.toLowerCase());

        videoPlaylists.put(name.toLowerCase(), new VideoPlaylist(name));

        return videoPlaylists.get(name);
    }

    public boolean isExist(String name) {
        return videoPlaylists.get(name.toLowerCase()) != null;
    }

    public VideoPlaylist getVideoPlaylist(String name) {
        return videoPlaylists.get(name.toLowerCase());
    }

    public List<VideoPlaylist> getAllPlaylists() {
        return new ArrayList<VideoPlaylist>(videoPlaylists.values());
    }

    public void removePlaylist(VideoPlaylist playlist) {
        videoPlaylists.remove(playlist.getName());
    }
}
