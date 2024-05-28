package com.example.cair.Entity;

import java.util.Arrays;

public class FileUpload {

    private String id;

    private String fileName;

    private String fileType;

    private byte[] data;

    private String typeOfUpload;

    public FileUpload() {

    }

    public FileUpload(String id, String fileName, String fileType, byte[] data, String typeOfUpload) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        this.typeOfUpload = typeOfUpload;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getData() {
        return data;
    }

    public String getTypeOfUpload() {
        return typeOfUpload;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setTypeOfUpload(String typeOfUpload) {
        this.typeOfUpload = typeOfUpload;
    }

    @Override
    public String toString() {
        return "FileUpload{" +
                "id='" + id + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", data=" + Arrays.toString(data) + '\'' + ", typeOfUpload='" + typeOfUpload + '\'' + '}';
    }

}
