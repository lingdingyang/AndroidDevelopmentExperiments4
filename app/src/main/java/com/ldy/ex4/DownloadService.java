package com.ldy.ex4;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.ldy.ex4.utils.DownloadUtil;

import java.io.IOException;


public class DownloadService extends IntentService {

    public DownloadService() {
        super("DownloadService");
    }

    // 在后台线程执行的任务
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
//通过intent获取下载链接
        String downloadUrl = intent.getStringExtra("downloadUrl");
//        获取保存路径
        String path = intent.getStringExtra("path");
//        获取音乐ID
        int musicID = intent.getIntExtra("musicID", -1);
        try {
//            开启下载任务，第三个参数是DownloadUtil类中声明的接口，需要提供三个方法，分别是下载成功的回调，下载进度更新的回调，下载失败的回调
            DownloadUtil.get().download(downloadUrl, path, new DownloadUtil.OnDownloadListener() {
                @Override
                public void onDownloadSuccess() {
                    //下载成功通过全局的App对象获取MainActivity中声明的Handler对象，向其发送消息
                    Log.i("download", "下载成功");
                    Bundle bundle = new Bundle();
                    bundle.putInt("musicID", musicID);
                    Message message = new Message();
                    message.setData(bundle);
//                    在DownloadService中获取handler对象并发送信息
                    ((App) getApplication()).getHandler().sendMessage(message);
                }

                @Override
                public void onDownloading(int progress) {
//                    显示下载进度
                    Log.i("download", progress + "%");
                }

                @Override
                public void onDownloadFailed() {
                    //显示下载失败
                    Log.i("download", "下载失败");
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void onDestroy() {
        Log.d("download", "onDestroy");
        super.onDestroy();
    }
}