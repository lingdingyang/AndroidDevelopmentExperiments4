package com.ldy.ex4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ldy.ex4.POJO.Music;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer player = new MediaPlayer();
    private ProgressBar bar;
    private Button btn_playPre;
    private Button btn_playOrPause;
    private Button btn_playNext;
    private TextView text_playMusicName;
    private String path;
    private PlayTask playTask;
    private List<Music> musicList;
    private boolean close = false;
    private String musicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        musicList = ((App) getApplication()).getMusics();
        path = getIntent().getStringExtra("musicPath");
        musicName = getIntent().getStringExtra("musicName");
        bar = findViewById(R.id.bar_music);
        text_playMusicName = findViewById(R.id.text_playMusicName);
        btn_playPre = findViewById(R.id.btn_playPre);
        btn_playOrPause = findViewById(R.id.btn_playOrPause);
        btn_playNext = findViewById(R.id.btn_playNext);
        btn_playPre.setOnClickListener(this);
        btn_playOrPause.setOnClickListener(this);
        btn_playNext.setOnClickListener(this);
        text_playMusicName.setText(musicName);


        initMediaPlayer();
        bar.setProgress(0);
        bar.setMax(player.getDuration());
        playTask = new PlayTask();
        playTask.execute(player);

    }

    @Override
    protected void onPause() {
        super.onPause();
        player.pause();
        btn_playOrPause.setText("继续");
        playTask.cancel(true);
    }

    private void initMediaPlayer() {
        try {
            player.reset();
            player.setDataSource(this, Uri.parse(path));
            player.prepare();
            Log.d("player", "start");
        } catch (Exception e) {
            Log.d("player", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        Log.d("music", musicList.toString());
        int id = view.getId();
        int position = 0;
        for (int i = 0; i < musicList.size(); i++) {
            if (musicList.get(i).getPath().equals(path)) {
                position = i;
            }
        }
        Log.d("music", String.valueOf(position));
        boolean flag = false;
        if (id == R.id.btn_playNext) {
            for (int i = position + 1; i < musicList.size(); i++) {
                if (musicList.get(i).getState() == 1) {
                    Log.d("music", musicList.get(i).toString());
                    path = musicList.get(i).getPath();
                    text_playMusicName.setText(musicList.get(i).getMusicName());
                    player.stop();
                    initMediaPlayer();
                    player.start();
                    bar.setProgress(0);
                    bar.setMax(player.getDuration());
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                Toast.makeText(this, "没有下一首了", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.btn_playPre) {
            for (int i = position - 1; i >= 0; i--) {
                if (musicList.get(i).getState() == 1) {
                    flag = true;
                    Log.d("music", musicList.get(i).toString());
                    path = musicList.get(i).getPath();
                    text_playMusicName.setText(musicList.get(i).getMusicName());
                    player.stop();
                    initMediaPlayer();
                    player.start();
                    bar.setProgress(0);
                    bar.setMax(player.getDuration());
                    break;
                }
            }
            if (!flag) {
                Toast.makeText(this, "没有下一首了", Toast.LENGTH_SHORT).show();
            }
//            Toast.makeText(this, "没有上一首了", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btn_playOrPause) {
            Button button = (Button) view;
            if (button.getText().toString().equals("暂停")) {
                player.pause();
                button.setText("继续");
            } else {
                player.start();
                button.setText("暂停");
            }
        }
    }

    private class PlayTask extends AsyncTask<MediaPlayer, Integer, String> {
        @Override
        protected void onPreExecute() {
            btn_playOrPause.setText("暂停");
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d("music", "onCancelled");
        }

        @Override
        protected String doInBackground(MediaPlayer... mediaPlayers) {
            mediaPlayers[0].start();
            while (!close) {
//                Log.d("music", String.valueOf(mediaPlayers[0].getCurrentPosition()));
                publishProgress(mediaPlayers[0].getCurrentPosition());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return "";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            bar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

}