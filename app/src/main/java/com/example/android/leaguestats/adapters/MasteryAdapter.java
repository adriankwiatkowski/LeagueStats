package com.example.android.leaguestats.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.interfaces.StringIdClickListener;
import com.example.android.leaguestats.models.Mastery;
import com.example.android.leaguestats.utilities.PicassoUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MasteryAdapter extends RecyclerView.Adapter<MasteryAdapter.MasteryViewHolder> {

    static private StringIdClickListener mListener;

    private static List<Mastery> mMastery;
    private final Context mContext;
    private final String PATCH_VERSION;

    public interface ListItemClickListener {
        void onListItemClick(long championId);
    }

    public MasteryAdapter(Context context, List<Mastery> mastery, StringIdClickListener listener,
                          String patchVersion) {
        mContext = context;
        mMastery = mastery;
        mListener = listener;
        PATCH_VERSION = patchVersion;
    }

    @NonNull
    @Override
    public MasteryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.mastery_item, parent, false);

        return new MasteryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MasteryViewHolder holder, int position) {
        String name = mMastery.get(position).getChampionName();
        int level = mMastery.get(position).getChampionLevel();
        int points = mMastery.get(position).getChampionPoints();
        long timestamp = mMastery.get(position).getLastPlayTime();

        String imagePath = mMastery.get(position).getChampionImage();

        PicassoUtils.getChampionThumbnailCreator(imagePath, PATCH_VERSION,
                R.dimen.mastery_champion_width, R.dimen.mastery_champion_height)
                .into(holder.mTarget);

        holder.mChampionNameTv.setText(name);
        holder.mChampionLevelTv.setText(String.valueOf(level));
        holder.mChampionPointsTv.setText(String.valueOf(points));

        Date dateObject = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        String formattedDate = dateFormat.format(dateObject);
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String formattedTime = timeFormat.format(dateObject);
        String date = formattedDate + "    " + formattedTime;
        holder.mLastPlayTimeTv.setText(date);

        boolean chestGranted = mMastery.get(position).isChestGranted();
        String isGrantedString;
        if (chestGranted) {
            isGrantedString = mContext.getString(R.string.yes);
        } else {
            isGrantedString = mContext.getString(R.string.no);
        }
        holder.mIsChestGrantedTv.setText(isGrantedString);
    }

    @Override
    public int getItemCount() {
        return mMastery.size();
    }

    public void swapData(List<Mastery> masteries) {
        mMastery.clear();
        mMastery.addAll(masteries);
        notifyDataSetChanged();
    }

    static class MasteryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mChampionImage;
        TextView mChampionNameTv;
        TextView mChampionLevelTv;
        TextView mChampionPointsTv;
        TextView mLastPlayTimeTv;
        TextView mIsChestGrantedTv;
        Target mTarget;

        public MasteryViewHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mChampionImage = itemView.findViewById(R.id.champion_thumbnail);
            mChampionNameTv = itemView.findViewById(R.id.champion_name_tv);
            mChampionLevelTv = itemView.findViewById(R.id.champion_level_tv);
            mChampionPointsTv = itemView.findViewById(R.id.champion_points_tv);
            mLastPlayTimeTv = itemView.findViewById(R.id.last_played_tv);
            mIsChestGrantedTv = itemView.findViewById(R.id.chest_granted_tv);

            mTarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    mChampionImage.setImageBitmap(bitmap);

                    Palette.from(bitmap)
                            .generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(@NonNull Palette palette) {
                                    Palette.Swatch swatch = palette.getVibrantSwatch();
                                    if (swatch == null) {
                                        return;
                                    }
                                    itemView.setBackgroundColor(swatch.getRgb());
                                    mChampionNameTv.setTextColor(swatch.getBodyTextColor());
                                    mChampionLevelTv.setTextColor(swatch.getBodyTextColor());
                                    mChampionPointsTv.setTextColor(swatch.getBodyTextColor());
                                    mLastPlayTimeTv.setTextColor(swatch.getBodyTextColor());
                                    mIsChestGrantedTv.setTextColor(swatch.getBodyTextColor());
                                }
                            });
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    mChampionImage.setImageDrawable(errorDrawable);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    mChampionImage.setImageDrawable(placeHolderDrawable);
                }
            };
        }

        @Override
        public void onClick(View v) {
            String championId = mMastery.get(getAdapterPosition()).getChampionId();
            mListener.onClickListener(championId);
        }
    }
}