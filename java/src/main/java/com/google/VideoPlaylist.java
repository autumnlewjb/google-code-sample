package com.google;

import java.util.ArrayList;

/** A class used to represent a Playlist */
class VideoPlaylist {
    private String name;
    private ArrayList<Video> videos;
    
    public VideoPlaylist(String name) {
        this.name = name;
        this.videos = new ArrayList<>();
    }

    public boolean addVideo(Video video) {
        if (videos.contains(video)) return false;
        videos.add(video);
        return true;
    }

    public ArrayList<Video> getVideos() {
        return videos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean removeVideo(Video video) {
        if (video == null) {
            return false;
        }
        return videos.remove(video);
    }

    public void clearAll() {
        videos.clear();
    }
}
