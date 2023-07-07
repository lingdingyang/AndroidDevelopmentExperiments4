package com.ldy.ex4.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ldy.ex4.MainActivity;
import com.ldy.ex4.POJO.Music;
import com.ldy.ex4.R;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Music> {
    private int resourceID;
    private MainActivity clickActivity;

    public ItemAdapter(@NonNull Context context, int resource, @NonNull List objects, MainActivity clickActivity) {
        super(context, resource, objects);
        this.resourceID = resource;
        this.clickActivity = clickActivity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Music music = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceID, null);
        } else {
            view = convertView;
        }
        TextView text_musicID = view.findViewById(R.id.text_musicID);
        TextView text_musicName = view.findViewById(R.id.text_musicName);
        TextView text_percentage = view.findViewById(R.id.text_percentage);
        Button btn_delete = view.findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(clickActivity);
        btn_delete.setTag(music);
        if (music.getState() == 0) {
            text_percentage.setText("未下载");
        } else if (music.getState() == 1) {
            text_percentage.setText("已下载");
        } else {
            text_percentage.setText("下载中");
        }
        text_musicID.setText(String.valueOf(position));
        view.setTag(music);
        view.setOnClickListener(clickActivity);
        text_musicName.setText(music.getMusicName());
        return view;
    }
}
