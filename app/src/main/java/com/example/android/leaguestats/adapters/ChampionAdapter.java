package com.example.android.leaguestats.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.data.database.models.ListChampionEntry;
import com.example.android.leaguestats.interfaces.IdClickListener;
import com.example.android.leaguestats.utilities.PicassoUtils;

import java.util.List;

public class ChampionAdapter extends RecyclerView.Adapter<ChampionAdapter.ChampionViewHolder> {

    private static IdClickListener mListener;

    private Context mContext;
    private static List<ListChampionEntry> mList;
    private final String PATCH_VERSION;

    public ChampionAdapter(Context context, List<ListChampionEntry> championEntries,
                           IdClickListener listener, String patchVersion) {
        mContext = context;
        mList = championEntries;
        mListener = listener;
        PATCH_VERSION = patchVersion;
    }

    @Override
    public ChampionAdapter.ChampionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.champion_item, parent, false);
        return new ChampionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChampionAdapter.ChampionViewHolder holder, int position) {
        holder.mNameTv.setText(mList.get(position).getName());
        holder.mTitleTv.setText(mList.get(position).getTitle());

        String imagePath = mList.get(position).getImage();

        PicassoUtils.setChampionThumbnail(holder.mImage, imagePath, PATCH_VERSION);
    }

    public void add(ListChampionEntry championEntry) {
        mList.add(championEntry);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void setData(List<ListChampionEntry> list) {
        clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ChampionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mNameTv;
        TextView mTitleTv;
        ImageView mImage;

        public ChampionViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mNameTv = itemView.findViewById(R.id.champion_name_tv);
            mTitleTv = itemView.findViewById(R.id.champion_title_tv);
            mImage = itemView.findViewById(R.id.champion_image);
        }

        @Override
        public void onClick(View v) {
            mListener.onClickListener(mList.get(getAdapterPosition()).getId());
        }
    }
}
