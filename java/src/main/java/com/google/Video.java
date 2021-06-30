package com.google;

import java.util.Collections;
import java.util.List;

import org.apache.commons.io.filefilter.FalseFileFilter;

/** A class used to represent a video. */
class Video {

  private final String title;
  private final String videoId;
  private final List<String> tags;
  private String state;
  private String flagReason;

  Video(String title, String videoId, List<String> tags) {
    this.title = title;
    this.videoId = videoId;
    this.tags = Collections.unmodifiableList(tags);
    this.state = "stopped";
    this.flagReason = null;    
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getState() {
    return state;
  }

  /** Returns the title of the video. */
  String getTitle() {
    return title;
  }

  /** Returns the video id of the video. */
  String getVideoId() {
    return videoId;
  }

  /** Returns a readonly collection of the tags of the video. */
  List<String> getTags() {
    return tags;
  }

  public String getFlagReason() {
    return flagReason;
  }

  public void setFlagReason(String flagReason) {
    this.flagReason = flagReason;
  }

  @Override
  public String toString() {
    String str = String.format("%s (%s) ", this.title, this.videoId) + this.tags.toString().replaceAll(",", "");
    if (flagReason != null) {
      str += " - FLAGGED (reason: " + flagReason + ")";
    }
    if (state.equals("paused")) {
      str += " - PAUSED";
    }

    return str;
  }
}
