package com.ldy.ex4;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ldy.ex4.POJO.Music;
import com.ldy.ex4.adapter.ItemAdapter;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Serializable {
    private MediaPlayer player = new MediaPlayer();
    private MainActivity that = this;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.d("download", "handleMessage");
            Bundle data = msg.getData();
            int musicID = data.getInt("musicID");
            Music music = musicList.get(musicID);
            music.setState(1);
            musicList.set(musicID, music);
            list_music.setAdapter(itemAdapter);
            Toast.makeText(that, "下载成功", Toast.LENGTH_SHORT).show();
        }
    };
    private List<Music> musicList;
    private ItemAdapter itemAdapter;
    private ListView list_music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list_music = findViewById(R.id.list_music);
        musicList = new ArrayList<>();
        musicList.add(new Music("演员 - 陈奕迅", "http://music.163.com/song/media/outer/url?id=1808920015.mp3", getFilesDir() + "/" + "演员 - 陈奕迅.mp3", checkExist(getFilesDir() + "/" + "演员 - 陈奕迅.mp3")));
        musicList.add(new Music("花のうた - 米白", "http://music.163.com/song/media/outer/url?id=573309657.mp3", getFilesDir() + "/" + "花のうた - 米白.mp3", checkExist(getFilesDir() + "/" + "花のうた - 米白.mp3")));
        musicList.add(new Music("老人と海 - ヨルシカ", "http://music.163.com/song/media/outer/url?id=1870469768.mp3", getFilesDir() + "/" + "老人と海 - ヨルシカ.mp3", checkExist(getFilesDir() + "/" + "老人と海 - ヨルシカ.mp3")));
        musicList.add(new Music("Alice - 米白", "http://music.163.com/song/media/outer/url?id=435948605.mp3", getFilesDir() + "/" + "Alice - 米白.mp3", checkExist(getFilesDir() + "/" + "Alice - 米白.mp3")));
        musicList.add(new Music("情熱と残響 - 米白", "http://music.163.com/song/media/outer/url?id=1378036886.mp3", getFilesDir() + "/" + "情熱と残響 - 米白.mp3", checkExist(getFilesDir() + "/" + "情熱と残響 - 米白.mp3")));
        itemAdapter = new ItemAdapter(this, R.layout.list_item, musicList, this);
        list_music.setAdapter(itemAdapter);
//        在MainActivity中设置App中的Handler
        ((App) getApplication()).setHandler(handler);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private int checkExist(String s) {
        File file = new File(s);
        if (file.exists()) {
            Log.d("download", file.getPath() + " 1");
            return 1;
        }
        Log.d("download", file.getPath() + " 0");
        return 0;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_delete) {
            Log.d("download", "点击了删除");
            Music music = (Music) view.getTag();
            File file = new File(music.getPath());
            file.delete();
            for (Music music1 : musicList) {
                if (music1.equals(music)) {
                    music1.setState(0);
                    break;
                }
            }
            list_music.setAdapter(itemAdapter);
        } else {
            Log.d("download", "点击了下载或播放");
            Music music = (Music) view.getTag();
            TextView text_musicID = view.findViewById(R.id.text_musicID);
            int id = Integer.parseInt(text_musicID.getText().toString());
            if (musicList.get(id).getState() == 1) {
                Intent intent = new Intent(this, MusicActivity.class);
                intent.putExtra("musicName", music.getMusicName());
                intent.putExtra("musicPath", music.getPath());
                ((App) getApplication()).setMusics(musicList);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, DownloadService.class);
                intent.putExtra("downloadUrl", music.getUrl());
                intent.putExtra("path", music.getPath());
                intent.putExtra("musicID", id);
                Music music1 = musicList.get(id);
                music1.setState(2);
                musicList.set(id, music1);
                list_music.setAdapter(itemAdapter);
                startService(intent);
                Log.d("download", "开始任务");
            }


        }

    }


}