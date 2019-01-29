package com.example.android.leaguestats.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.data.glide.GlideApp;
import com.example.android.leaguestats.data.glide.GlideUtils;
import com.example.android.leaguestats.models.Champion;

public class SplashArtAdapter extends RecyclerView.Adapter<SplashArtAdapter.SplashArtViewHolder> {

    private final Context mContext;
    private Champion mChampion;

    public SplashArtAdapter(Context context, Champion champion) {
        mContext = context;
        mChampion = champion;
    }

    @NonNull
    @Override
    public SplashArtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.splash_art_item, parent, false);
        return new SplashArtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SplashArtViewHolder holder, int position) {
        holder.splashArtNameTv.setText(mChampion.getSplashArtName().get(position));
        GlideApp.with(mContext)
                .load(GlideUtils.getSplashArtUrl(mChampion.getKey(), mChampion.getSplashArtId().get(position)))
                .splashArt()
                .into(holder.splashArtImage);
    }

    public void setChampion(Champion champion) {
        mChampion = champion;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mChampion == null ? 0 : mChampion.getSplashArtId().size();
    }

    class SplashArtViewHolder extends RecyclerView.ViewHolder {

        ImageView splashArtImage;
        TextView splashArtNameTv;

        SplashArtViewHolder(View itemView) {
            super(itemView);
            splashArtImage = itemView.findViewById(R.id.splash_art_image);
            splashArtNameTv = itemView.findViewById(R.id.splash_art_tv);
        }
    }
}
