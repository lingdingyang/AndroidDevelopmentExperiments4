package com.ldy.ex4.utils;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadUtil {

    private static DownloadUtil downloadUtil;
    private final OkHttpClient okHttpClient;

    public static DownloadUtil get() {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtil();
        }
        return downloadUtil;
    }

    private DownloadUtil() {
        okHttpClient = new OkHttpClient();
    }

    /**
     * @param url      下载连接
     * @param saveDir  储存下载文件的SDCard目录
     * @param listener 下载监听
     */
    public void download(final String url, final String saveDir, final OnDownloadListener listener) throws IOException {
// 创建请求对象
        Request request = new Request.Builder().url(url).addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows 7)").build();
//        使用okHttpClient发送请求，获取Response对象
        Response response = okHttpClient.newCall(request).execute();
        Log.d("download", String.valueOf(response.code()));
//        创建输入流
        InputStream is = null;
//        设置每次输入的大小
        byte[] buf = new byte[2048];
        int len = 0;
//        创建文件输出流
        FileOutputStream fos = null;
//        设置输入流来自响应体
        is = response.body().byteStream();
//        获取输入流长度
        long total = response.body().contentLength();
        Log.d("download", "total: " + total);
//        创建文件
        File file = new File(saveDir);
//        获取文件的输出流
        fos = new FileOutputStream(file);
//        记录写入文件的总大小
        long sum = 0;
//        如果还能从输入流读取
        while ((len = is.read(buf)) != -1) {
//            将读到的写入到文件
            fos.write(buf, 0, len);
//            更新写入的总大小
            sum += len;
//            计算写入进度
            int progress = (int) (sum * 1.0f / total * 100);
//            显示进度
            listener.onDownloading(progress);
        }
//        关闭文件输出流
        fos.flush();
        // 下载完成
        listener.onDownloadSuccess();
        if (is != null)
            is.close();
        if (fos != null)
            fos.close();
    }


    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * @param progress 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }

}
