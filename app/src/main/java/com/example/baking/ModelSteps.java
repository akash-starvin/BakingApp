package com.example.baking;

public class ModelSteps {
    String id, shortDes, des, videoUrl;

    public ModelSteps(String id, String shortDes, String des, String videoUrl) {
        this.id = id;
        this.shortDes = shortDes;
        this.des = des;
        this.videoUrl = videoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortDes() {
        return shortDes;
    }

    public void setShortDes(String shortDes) {
        this.shortDes = shortDes;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

}
