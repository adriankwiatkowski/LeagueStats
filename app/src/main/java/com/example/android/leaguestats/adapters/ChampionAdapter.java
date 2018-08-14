package com.example.android.leaguestats.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.room.ChampionEntry;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChampionAdapter extends RecyclerView.Adapter<ChampionAdapter.ChampionViewHolder> {

    private static ChampionClickListener mListener;

    public interface ChampionClickListener {
        void onChampionClick(long id);
    }

    private static final String LOG_TAG = ChampionAdapter.class.getSimpleName();
    private Context mContext;
    private static List<ChampionEntry> mList;
    private final String PATCH_VERSION;

    public ChampionAdapter(Context context, List<ChampionEntry> championEntries, ChampionClickListener listener, String patchVersion) {
        mContext = context;
        mList = championEntries;
        mListener = listener;
        PATCH_VERSION = patchVersion;
    }

    @Override
    public ChampionAdapter.ChampionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.champion_item, parent, false);

        final String HTTP_ENTRY_THUMBNAIL_URL = "https://ddragon.leagueoflegends.com/cdn/" + PATCH_VERSION + "/img/champion";

        view.setTag(HTTP_ENTRY_THUMBNAIL_URL);

        return new ChampionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChampionAdapter.ChampionViewHolder holder, int position) {
        holder.mNameTv.setText(mList.get(position).getName());
        holder.mTitleTv.setText(mList.get(position).getTitle());

        String httpEntryUrl = (String) holder.itemView.getTag();
        String image = mList.get(position).getThumbnail();

        Picasso.get()
                .load(httpEntryUrl + "/" + image)
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.mImage);
    }

    public void add(ChampionEntry championEntry) {
        mList.add(championEntry);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void setData(List<ChampionEntry> list) {
        clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ChampionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            mListener.onChampionClick(mList.get(getAdapterPosition()).getId());
        }
    }
}
