package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 1/20/2017.
 */

public class VideoLogsInputModel {

    String authToken;
    String userId;
    String ipAddress;
    String muviUniqueId;
    String episodeStreamUniqueId;
    String playedLength;
    String watchStatus;
    String deviceType;
    String videoLogId;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMuviUniqueId() {
        return muviUniqueId;
    }

    public void setMuviUniqueId(String muviUniqueId) {
        this.muviUniqueId = muviUniqueId;
    }

    public String getEpisodeStreamUniqueId() {
        return episodeStreamUniqueId;
    }

    public void setEpisodeStreamUniqueId(String episodeStreamUniqueId) {
        this.episodeStreamUniqueId = episodeStreamUniqueId;
    }

    public String getPlayedLength() {
        return playedLength;
    }

    public void setPlayedLength(String playedLength) {
        this.playedLength = playedLength;
    }

    public String getWatchStatus() {
        return watchStatus;
    }

    public void setWatchStatus(String watchStatus) {
        this.watchStatus = watchStatus;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getVideoLogId() {
        return videoLogId;
    }

    public void setVideoLogId(String videoLogId) {
        this.videoLogId = videoLogId;
    }
}
