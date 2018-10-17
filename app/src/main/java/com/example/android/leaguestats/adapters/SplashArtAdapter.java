package com.example.android.leaguestats.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.utilities.PicassoUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.util.List;

public class SplashArtAdapter extends RecyclerView.Adapter<SplashArtAdapter.SplashArtViewHolder> {

    private final Context mContext;
    private final List<String> mSplashArt;
    private final List<String> mSplashArtName;

    public SplashArtAdapter(Context context, List<String> splashArt, List<String> splashArtName) {
        mContext = context;
        mSplashArt = splashArt;
        mSplashArtName = splashArtName;
    }

    @Override
    public SplashArtViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.splash_art_item, parent, false);

        return new SplashArtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SplashArtViewHolder holder, int position) {

        PicassoUtils.setSplashArt(holder.mTarget, mContext, mSplashArt.get(position));

        // Set Splash Art Name on Text;
        holder.mSplashArtNameTv.setText(mSplashArtName.get(position));
    }

    public void add(String splashArt, String splashArtName) {
        mSplashArt.add(splashArt);
        mSplashArtName.add(splashArtName);
        notifyDataSetChanged();
    }

    public void clear() {
        mSplashArt.clear();
        mSplashArtName.clear();
        notifyDataSetChanged();
    }

    public void setData(List<String> image, List<String> name) {
        clear();
        for (int i = 0; i < image.size(); i++) {
            add(image.get(i), name.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return mSplashArt.size();
    }

    static class SplashArtViewHolder extends RecyclerView.ViewHolder {
        TextView mSplashArtNameTv;
        Target mTarget;

        public SplashArtViewHolder(final View itemView) {
            super(itemView);
            mSplashArtNameTv = itemView.findViewById(R.id.splash_art_name);

            mTarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Drawable imageDrawable = new BitmapDrawable(Resources.getSystem(), bitmap);
                    mSplashArtNameTv.setCompoundDrawablesWithIntrinsicBounds(null, imageDrawable, null, null);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    mSplashArtNameTv.setCompoundDrawablesWithIntrinsicBounds(null, errorDrawable, null, null);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    mSplashArtNameTv.setCompoundDrawablesWithIntrinsicBounds(null, placeHolderDrawable, null, null);
                }
            };
        }
    }
}
