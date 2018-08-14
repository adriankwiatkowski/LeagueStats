package com.example.android.leaguestats.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.room.SummonerSpellEntry;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SummonerSpellAdapter extends RecyclerView.Adapter<SummonerSpellAdapter.SummonerSpellViewHolder> {

    private static SummonerSpellClickListener mListener;

    public interface SummonerSpellClickListener {
        void onSummonerSpellClick(SummonerSpellEntry summonerSpellEntry);
    }

    private static final String LOG_TAG = SummonerSpellAdapter.class.getSimpleName();
    private Context mContext;
    private static List<SummonerSpellEntry> mList;
    private final String PATCH_VERSION;

    public SummonerSpellAdapter(Context context, List<SummonerSpellEntry> list,
                                SummonerSpellClickListener listener, String patchVersion) {
        mContext = context;
        mList = list;
        PATCH_VERSION = patchVersion;
        mListener = listener;
    }

    @NonNull
    @Override
    public SummonerSpellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.summoner_spell_item, parent, false);

        final String HTTP_ENTRY_URL = "http://ddragon.leagueoflegends.com/cdn/" + PATCH_VERSION + "/img/spell";

        view.setTag(HTTP_ENTRY_URL);

        return new SummonerSpellViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SummonerSpellViewHolder holder, int position) {
        holder.mNameTv.setText(mList.get(position).getName());
        holder.mCooldownTv.setText(String.valueOf(mList.get(position).getCooldown()));

        String httpEntryUrl = (String) holder.itemView.getTag();

        Picasso.get()
                .load(httpEntryUrl + "/" + mList.get(position).getImage())
                .resize(200, 200)
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.mImage);
    }

    public void add(SummonerSpellEntry summonerSpellEntry) {
        mList.add(summonerSpellEntry);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void setData(List<SummonerSpellEntry> list) {
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
            mListener.onSummonerSpellClick(mList.get(getAdapterPosition()));
        }
    }
}
