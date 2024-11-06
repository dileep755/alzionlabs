package com.alzionlabsjavatest.bean;
public class FileDownloadResponse {
    private byte[] fileData;
    private String fileName;

    public FileDownloadResponse(byte[] fileData, String fileName) {
        this.fileData = fileData;
        this.fileName = fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public String getFileName() {
        return fileName;
    }
}
