package com.google;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private final VideoPlaylistLibrary playlistLibrary;
  private Video currentVideo;

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
    this.playlistLibrary = new VideoPlaylistLibrary();
    this.currentVideo = null;
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    System.out.println("Here's a list of all available videos:");
    List<Video> videos = videoLibrary.getVideos();
    videos.sort((Video a, Video b) -> a.getTitle().compareTo(b.getTitle()));
    for (Video val: videos) {
      System.out.println("\t" + val);
    }
  }

  public void playVideo(String videoId) {
    Video video = videoLibrary.getVideo(videoId);
    if (video == null) {
      System.out.println("Cannot play video: Video does not exist");
      return;
    }
    if (video.getFlagReason() != null) {
      System.out.println("Cannot play video: Video is currently flagged (reason: " + video.getFlagReason() + ")");
      return;
    }
    if (currentVideo != null) {
      System.out.println("Stopping video: " + currentVideo.getTitle());
      currentVideo.setState("stopped");
    }
    video.setState("playing");
    currentVideo = video;
    System.out.println("Playing video: " + video.getTitle());
  }

  public void stopVideo() {
    if (currentVideo == null) {
      System.out.println("Cannot stop video: No video is currently playing");
      return;
    }
    System.out.println("Stopping video: " + currentVideo.getTitle());
    currentVideo.setState("stopped");
    currentVideo = null;
  }

  public void playRandomVideo() {
    List<Video> allVideos = videoLibrary.getVideos();
    if (allVideos.isEmpty()) {
      System.out.println("No videos available");
      return;
    }

    int randomIdx = -1;

    while (allVideos.size() > 0) {
      randomIdx = new Random().nextInt(allVideos.size());
      if (allVideos.get(randomIdx).getFlagReason() == null) {
        break;
      } else {
        allVideos.remove(randomIdx);
      }
    }

    if (allVideos.size() == 0) {
      System.out.println("No videos available");
      return;
    }

    playVideo(allVideos.get(randomIdx).getVideoId());
  }

  public void pauseVideo() {
    if (currentVideo == null) {
      System.out.println("Cannot pause video: No video is currently playing");
      return;
    }
    if (currentVideo.getState().equals("paused")) {
      System.out.println("Video already paused: " + currentVideo.getTitle());
      return;
    }

    currentVideo.setState("paused");
    System.out.println("Pausing video: " + currentVideo.getTitle());
  }

  public void continueVideo() {
    if (currentVideo == null) {
      System.out.println("Cannot continue video: No video is currently playing");
      return;
    }

    if (!currentVideo.getState().equals("paused")) {
      System.out.println("Cannot continue video: Video is not paused");
      return;
    }

    System.out.println("Continuing video: " + currentVideo.getTitle());
    currentVideo.setState("playing");
  }

  public void showPlaying() {
    if (currentVideo == null) {
      System.out.println("No video is currently playing");
      return;
    }
    System.out.println("Currently playing: " + currentVideo + (currentVideo.getState().equals("paused") ? " - PAUSED" : ""));
  }

  public void createPlaylist(String playlistName) {
    playlistLibrary.createPlaylist(playlistName);
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    if (!playlistLibrary.isExist(playlistName)) {
      System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
      return;
    }

    Video video = videoLibrary.getVideo(videoId);

    if (video == null) {
      System.out.println("Cannot add video to " + playlistName + ": Video does not exist");
      return;
    }

    if (video.getFlagReason() != null) {
      System.out.println("Cannot add video to " + playlistName + ": Video is currently flagged (reason: " + video.getFlagReason() + ")");
      return;
    }

    VideoPlaylist playlist = playlistLibrary.getVideoPlaylist(playlistName);

    if (!playlist.addVideo(video)) {
      System.out.println("Cannot add video to " + playlistName + ": Video already added");
      return;
    }

    System.out.println("Added video to " + playlistName + ": " + video.getTitle());
  }

  public void showAllPlaylists() {
    List<VideoPlaylist> playlists = playlistLibrary.getAllPlaylists();
    if (playlists.isEmpty()) {
      System.out.println("No playlists exist yet");
      return;
    }
    playlists.sort((VideoPlaylist a, VideoPlaylist b) -> a.getName().compareTo(b.getName()));
    System.out.println("Showing all playlists:");
    for (VideoPlaylist val: playlists) {
      System.out.println("\t" + val.getName());
    }
  }

  public void showPlaylist(String playlistName) {
    if (!playlistLibrary.isExist(playlistName)) {
      System.out.println("Cannot show playlist " + playlistName + ": Playlist does not exist");
      return;
    }

    System.out.println("Showing playlist: " + playlistName);
    List<Video> videos = playlistLibrary.getVideoPlaylist(playlistName).getVideos();

    if (videos.size() == 0) {
      System.out.println("\tNo videos here yet");
      return;
    }

    for (Video video: videos) {
      System.out.println("\t" + video);
    }
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    VideoPlaylist playlist = playlistLibrary.getVideoPlaylist(playlistName);
    if (playlist == null) {
      System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");
      return;
    }
    Video video = videoLibrary.getVideo(videoId);
    if (video == null) {
      System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");
      return;
    }
    if (!playlist.removeVideo(video)) {
      System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");
      return;
    }
    System.out.println("Removed video from " + playlistName + ": " + video.getTitle());
  }

  public void clearPlaylist(String playlistName) {
    VideoPlaylist playlist = playlistLibrary.getVideoPlaylist(playlistName);
    if (playlist == null) {
      System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist");
      return;
    }
    System.out.println("Successfully removed all videos from " + playlistName);
    playlist.clearAll();
  }

  public void deletePlaylist(String playlistName) {
    VideoPlaylist playlist = playlistLibrary.getVideoPlaylist(playlistName);
    if (playlist == null) {
      System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
      return;
    }
    System.out.println("Deleted playlist: " + playlistName);
    playlistLibrary.removePlaylist(playlist);
  }

  public void searchVideos(String searchTerm) {
    List<Video> videos = videoLibrary.getVideos();
    List<Video> result = new ArrayList<>();
    for (Video video: videos) {
      if (video.getFlagReason() == null) {
        String title = video.getTitle().toLowerCase();
        for (int i=searchTerm.length(); i<title.length(); i++) {
          if (title.substring(i-searchTerm.length(), i).equals(searchTerm.toLowerCase())) {
            result.add(video);
            break;
          }
        }
      }
    }
    if (result.size() == 0) {
      System.out.println("No search results for " + searchTerm);
      return;
    }
    result.sort((Video a, Video b) -> a.getTitle().compareTo(b.getTitle()));
    System.out.println("Here are the results for " + searchTerm + ": ");
    for (int i=0; i<result.size(); i++) {
      System.out.println("\t" + (i+1) + ") " + result.get(i));
    }
    askPlay(result);
  }

  public void askPlay(List<Video> result) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
    System.out.println("If your answer is not a valid number, we will assume it's a no.");
    try {
      int option = scanner.nextInt();
      if (option >= 1 && option <= result.size()) {
        playVideo(result.get(option-1).getVideoId());
      }
    } catch (InputMismatchException ex) {}
  }

  public void searchVideosWithTag(String videoTag) {
    List<Video> videos = videoLibrary.getVideos();
    List<Video> result = new ArrayList<>();
    for (Video video: videos) {
      if (video.getFlagReason() == null && video.getTags().contains(videoTag)) {
        result.add(video);
      }
    }
    if (result.size() == 0) {
      System.out.println("No search results for " + videoTag);
      return;
    }
    result.sort((Video a, Video b) -> a.getTitle().compareTo(b.getTitle()));
    System.out.println("Here are the results for " + videoTag + ": ");
    for (int i=0; i<result.size(); i++) {
      System.out.println("\t" + (i+1) + ") " + result.get(i));
    }
    askPlay(result);
  }

  public void flagVideo(String videoId) {
    flagVideo(videoId, "Not supplied");
  }

  public void flagVideo(String videoId, String reason) {
    Video video = videoLibrary.getVideo(videoId);
    if (video == null) {
      System.out.println("Cannot flag video: Video does not exist");
      return;
    }

    if (video.getFlagReason() != null) {
      System.out.println("Cannot flag video: Video is already flagged");
      return;
    }

    if (video.getState() != "stopped") {
      stopVideo();
    }

    video.setFlagReason(reason);
    System.out.println("Successfully flagged video: " + video.getTitle() + " (reason: " + reason +")");
  }

  public void allowVideo(String videoId) {
    Video video = videoLibrary.getVideo(videoId);
    if (video == null) {
      System.out.println("Cannot remove flag from video: Video does not exist");
      return;
    }
    if (video.getFlagReason() == null) {
      System.out.println("Cannot remove flag from video: Video is not flagged");
      return;
    }

    System.out.println("Successfully removed flag from video: " + video.getTitle());
    video.setFlagReason(null);
  }
}