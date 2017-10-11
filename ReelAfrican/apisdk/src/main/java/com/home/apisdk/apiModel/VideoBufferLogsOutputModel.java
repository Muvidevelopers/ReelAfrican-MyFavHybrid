package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 1/20/2017.
 */

public class VideoBufferLogsOutputModel {

    String bufferLogId;
    String bufferLogUniqueId;
    String bufferLocation;

    public String getBufferLocation() {
        return bufferLocation;
    }

    public void setBufferLocation(String bufferLocation) {
        this.bufferLocation = bufferLocation;
    }

    public String getBufferLogId() {
        return bufferLogId;
    }

    public void setBufferLogId(String bufferLogId) {
        this.bufferLogId = bufferLogId;
    }

    public String getBufferLogUniqueId() {
        return bufferLogUniqueId;
    }

    public void setBufferLogUniqueId(String bufferLogUniqueId) {
        this.bufferLogUniqueId = bufferLogUniqueId;
    }
}
