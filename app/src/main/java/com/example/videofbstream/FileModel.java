package com.example.videofbstream;

public class FileModel {
    String title, vurl;

    public FileModel() {
    }

    public FileModel(String title, String vurl) {
        this.title = title;
        this.vurl = vurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVurl() {
        return vurl;
    }

    public void setVurl(String vurl) {
        this.vurl = vurl;
    }
}
