package com.ldy.ex4.POJO;

import com.ldy.ex4.MainActivity;

public class Music {
    String musicName;
    String url;
    String path;
    int state;

    public Music(String musicName, String url, String path, int state) {
        this.musicName = musicName;
        this.url = url;
        this.path = path;
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Music{" +
                "musicName='" + musicName + '\'' +
                ", url='" + url + '\'' +
                ", path='" + path + '\'' +
                ", state=" + state +
                '}';
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
