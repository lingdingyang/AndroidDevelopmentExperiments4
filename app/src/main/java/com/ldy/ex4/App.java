package com.ldy.ex4;

import android.app.Application;
import android.os.Handler;

import com.ldy.ex4.POJO.Music;

import java.util.List;

public class App extends Application {

    private Handler handler;

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    private List<Music> musics;

    public List<Music> getMusics() {
        return musics;
    }

    public void setMusics(List<Music> musics) {
        this.musics = musics;
    }
}
