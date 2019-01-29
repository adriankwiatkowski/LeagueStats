package com.example.android.leaguestats.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.data.glide.GlideApp;
import com.example.android.leaguestats.data.glide.GlideUtils;
import com.example.android.leaguestats.interfaces.IdClickListener;
import com.example.android.leaguestats.models.Mastery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MasteryAdapter extends RecyclerView.Adapter<MasteryAdapter.MasteryViewHolder> {

    private IdClickListener mListener;
    private List<Mastery> mMasteryList;
    private final Context mContext;
    private final String PATCH_VERSION;

    public MasteryAdapter(Context context, IdClickListener listener, String patchVersion) {
        mContext = context;
        mMasteryList = new ArrayList<>();
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
        Mastery mastery = mMasteryList.get(position);
        String name = mastery.getChampionName();
        int level = mastery.getChampionLevel();
        int points = mastery.getChampionPoints();
        long timestamp = mastery.getLastPlayTime();

        GlideApp.with(mContext)
                .load(GlideUtils.getChampionUrl(PATCH_VERSION, mastery.getChampionImageId()))
                .roundedImage()
                .into(holder.image);

        holder.nameTv.setText(name);
        holder.levelTv.setText(String.valueOf(level));
        holder.pointsTv.setText(String.valueOf(points));

        Date dateObject = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        String date = dateFormat.format(dateObject);
        holder.lastPlayTimeTv.setText(date);

        boolean chestGranted = mastery.isChestGranted();
        String isGrantedString;
        if (chestGranted) {
            isGrantedString = mContext.getString(R.string.yes);
        } else {
            isGrantedString = mContext.getString(R.string.no);
        }
        holder.isChestGrantedTv.setText(isGrantedString);
    }

    @Override
    public int getItemCount() {
        return mMasteryList.size();
    }

    public void setData(final List<Mastery> newList) {
        if (mMasteryList.isEmpty()) {
            mMasteryList = newList;
            notifyDataSetChanged();
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mMasteryList.size();
                }

                @Override
                public int getNewListSize() {
                    return newList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldPosition, int newPosition) {
                    return mMasteryList.get(oldPosition).getPlayerId() == newList.get(newPosition).getPlayerId();
                }

                @Override
                public boolean areContentsTheSame(int oldPosition, int newPosition) {
                    String oldChampionName = mMasteryList.get(oldPosition).getChampionName();
                    String newChampionName = newList.get(newPosition).getChampionName();
                    if (oldChampionName != null && newChampionName != null) {
                        return oldChampionName.equals(newChampionName);
                    } else {
                        return false;
                    }
                }

                @Nullable
                @Override
                public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                    // You can return particular field for changed item.
                    return super.getChangePayload(oldItemPosition, newItemPosition);
                }
            });
            mMasteryList = newList;
            diffResult.dispatchUpdatesTo(this);
        }
    }

    class MasteryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView nameTv;
        TextView levelTv;
        TextView pointsTv;
        TextView lastPlayTimeTv;
        TextView isChestGrantedTv;

        MasteryViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = itemView.findViewById(R.id.champion_thumbnail);
            nameTv = itemView.findViewById(R.id.champion_name_tv);
            levelTv = itemView.findViewById(R.id.champion_level_tv);
            pointsTv = itemView.findViewById(R.id.champion_points_tv);
            lastPlayTimeTv = itemView.findViewById(R.id.last_played_tv);
            isChestGrantedTv = itemView.findViewById(R.id.chest_granted_tv);
        }

        @Override
        public void onClick(View v) {
            int championId = mMasteryList.get(getAdapterPosition()).getChampionId();
            mListener.onClick(championId);
        }
    }
}