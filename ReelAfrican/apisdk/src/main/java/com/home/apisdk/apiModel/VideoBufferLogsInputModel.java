package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 1/20/2017.
 */

public class VideoBufferLogsInputModel {

    String authToken;
    String userId;
    String ipAddress = "";
    String muviUniqueId;
    String episodeStreamUniqueId;
    String deviceType;
    String bufferLogId;

    public String getBufferLogUniqueId() {
        return bufferLogUniqueId;
    }

    public void setBufferLogUniqueId(String bufferLogUniqueId) {
        this.bufferLogUniqueId = bufferLogUniqueId;
    }

    String bufferLogUniqueId;
    String location;
    String bufferStartTime;
    String bufferEndTime;
    String videoResolution;

    public String getBufferLogId() {
        return bufferLogId;
    }

    public void setBufferLogId(String bufferLogId) {
        this.bufferLogId = bufferLogId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBufferStartTime() {
        return bufferStartTime;
    }

    public void setBufferStartTime(String bufferStartTime) {
        this.bufferStartTime = bufferStartTime;
    }

    public String getBufferEndTime() {
        return bufferEndTime;
    }

    public void setBufferEndTime(String bufferEndTime) {
        this.bufferEndTime = bufferEndTime;
    }

    public String getVideoResolution() {
        return videoResolution;
    }

    public void setVideoResolution(String videoResolution) {
        this.videoResolution = videoResolution;
    }

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



    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }


}
