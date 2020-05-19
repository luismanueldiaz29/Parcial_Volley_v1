package com.example.parcial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterMusic extends RecyclerView.Adapter<AdapterMusic.ViewHolder> implements View.OnClickListener {

private View.OnClickListener listener;

public static class ViewHolder extends RecyclerView.ViewHolder {
    private TextView name, duration;

    public ViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.name);
        duration = (TextView) itemView.findViewById(R.id.duration);
    }
}

    public ArrayList<Music> Musics;

    public AdapterMusic(ArrayList<Music> Music) {
        this.Musics = Music;
    }

    @Override
    public AdapterMusic.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        view.setOnClickListener(this);

        AdapterMusic.ViewHolder viewHolder = new AdapterMusic.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterMusic.ViewHolder holder, int position) {
        holder.name.setText(Musics.get(position).name);
        holder.duration.setText(Musics.get(position).duration);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }


    @Override
    public int getItemCount() {
        return Musics.size();
    }
}