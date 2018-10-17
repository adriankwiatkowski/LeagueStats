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
import com.example.android.leaguestats.data.database.models.ListSummonerSpellEntry;
import com.example.android.leaguestats.interfaces.IdClickListener;
import com.example.android.leaguestats.utilities.PicassoUtils;

import java.util.List;

public class SummonerSpellAdapter extends RecyclerView.Adapter<SummonerSpellAdapter.SummonerSpellViewHolder> {

    private Context mContext;
    private static List<ListSummonerSpellEntry> mList;
    private static IdClickListener mListener;
    private final String PATCH_VERSION;

    public SummonerSpellAdapter(Context context, List<ListSummonerSpellEntry> list,
                                IdClickListener listener, String patchVersion) {
        mContext = context;
        mList = list;
        mListener = listener;
        PATCH_VERSION = patchVersion;
    }

    @NonNull
    @Override
    public SummonerSpellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.summoner_spell_item, parent, false);
        return new SummonerSpellViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SummonerSpellViewHolder holder, int position) {
        holder.mNameTv.setText(mList.get(position).getName());
        holder.mCooldownTv.setText(String.valueOf(mList.get(position).getCooldown()));

        String image = mList.get(position).getImage();

        PicassoUtils.setSpellImage(holder.mImage, image, PATCH_VERSION,
                R.dimen.summoner_spell_width, R.dimen.summoner_spell_height);
    }

    public void add(ListSummonerSpellEntry summonerSpellEntry) {
        mList.add(summonerSpellEntry);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void setData(List<ListSummonerSpellEntry> list) {
        clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class SummonerSpellViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mNameTv;
        TextView mCooldownTv;
        ImageView mImage;

        public SummonerSpellViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mNameTv = itemView.findViewById(R.id.summoner_spell_name_item);
            mCooldownTv = itemView.findViewById(R.id.summoner_spell_cooldown_item);
            mImage = itemView.findViewById(R.id.summoner_spell_image_item);
        }

        @Override
        public void onClick(View v) {
            mListener.onClickListener(mList.get(getAdapterPosition()).getId());
        }
    }
}
