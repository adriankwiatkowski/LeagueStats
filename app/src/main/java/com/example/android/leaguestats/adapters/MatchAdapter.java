package com.example.android.leaguestats.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.database.Contract;
import com.example.android.leaguestats.models.Match;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {

    private Context mContext;
    private ArrayList<Match> mMatches;
    private Cursor mSummonerSpellCursor1;
    private Cursor mSummonerSpellCursor2;
    private Cursor mChampionCursor;
    private String mVersion;

    public MatchAdapter(Context context, ArrayList<Match> matches, Cursor summonerSpellCursor1,
                        Cursor summonerSpellCursor2, Cursor championCursor, String patchVersion) {
        mContext = context;
        mMatches = matches;
        mSummonerSpellCursor1 = summonerSpellCursor1;
        mSummonerSpellCursor2 = summonerSpellCursor2;
        mChampionCursor = championCursor;
        mVersion = patchVersion;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.match_item, parent, false);


        final String HTTP_ENTRY_SPELL_URL = "http://ddragon.leagueoflegends.com/cdn/" + mVersion + "/img/spell";
        final String HTTP_ENTRY_THUMBNAIL_URL = "https://ddragon.leagueoflegends.com/cdn/" + mVersion + "/img/champion";

        view.setTag(R.id.match_summoner_spell_tag, HTTP_ENTRY_SPELL_URL);
        view.setTag(R.id.match_champion_thumbnail_tag, HTTP_ENTRY_THUMBNAIL_URL);

        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        String summonerName = mMatches.get(position).getSummonerName();
        holder.summonerName.setText(summonerName);

        if (mSummonerSpellCursor1 != null)  {
            if (mSummonerSpellCursor1.moveToPosition(position)) {
                String httpEntrySpellUrl = (String) holder.itemView.getTag(R.id.match_summoner_spell_tag);
                String spellImage = mSummonerSpellCursor1.getString(mSummonerSpellCursor1.getColumnIndex(Contract.SummonerSpellEntry.COLUMN_IMAGE));
                Picasso.get()
                        .load(httpEntrySpellUrl + "/" + spellImage)
                        .resize(200, 200)
                        .centerCrop()
                        .error(R.drawable.ic_launcher_background)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(holder.spell1);
            }
        }



        Log.i("MatchAdapter", String.valueOf(mMatches.get(position).getSpell1Id()) + "  " + String.valueOf(mMatches.get(position).getSpell2Id()));

        if (mSummonerSpellCursor2 != null) {
            if (mSummonerSpellCursor2.moveToPosition(position)) {
                String httpEntrySpellUrl = (String) holder.itemView.getTag(R.id.match_summoner_spell_tag);
                String spellImage = mSummonerSpellCursor2.getString(mSummonerSpellCursor2.getColumnIndex(Contract.SummonerSpellEntry.COLUMN_IMAGE));
                Picasso.get()
                        .load(httpEntrySpellUrl + "/" + spellImage)
                        .resize(200, 200)
                        .centerCrop()
                        .error(R.drawable.ic_launcher_background)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(holder.spell2);
            }
        }

        if (mChampionCursor != null) {
            if (mChampionCursor.moveToPosition(position)) {
                String httpEntryChampionUrl = (String) holder.itemView.getTag(R.id.match_champion_thumbnail_tag);
                String championImage = mChampionCursor.getString(mChampionCursor.getColumnIndex(Contract.ChampionEntry.COLUMN_CHAMPION_THUMBNAIL));
                Picasso.get()
                        .load(httpEntryChampionUrl + "/" + championImage)
                        .resize(300, 300)
                        .centerCrop()
                        .error(R.drawable.ic_launcher_background)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(holder.championImage);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mMatches.size();
    }

    public void swapMatchesData(List<Match> matches) {
        if (matches != null) {
            mMatches.clear();
            mMatches.addAll(matches);
            notifyDataSetChanged();
        }
    }

    public void swapSummonerSpell1Data(Cursor summonerSpellCursor1) {
        if (summonerSpellCursor1 != null) {
            mSummonerSpellCursor1 = summonerSpellCursor1;
            notifyDataSetChanged();
        }
    }

    public void swapSummonerSpell2Data(Cursor summonerSpellCursor2) {
        if (summonerSpellCursor2 != null) {
            mSummonerSpellCursor2 = summonerSpellCursor2;
            notifyDataSetChanged();
        }
    }

    public void swapChampionCursor(Cursor championCursor) {
        if (championCursor != null) {
            mChampionCursor = championCursor;
            notifyDataSetChanged();
        }
    }

    public void swapData(List<Match> matches, Cursor summonerSpellCursor1, Cursor summonerSpellCursor2, Cursor championCursor) {
        swapMatchesData(matches);
        swapSummonerSpell1Data(summonerSpellCursor1);
        swapSummonerSpell2Data(summonerSpellCursor2);
        swapChampionCursor(championCursor);
    }

    public class MatchViewHolder extends RecyclerView.ViewHolder {

        ImageView championImage;
        ImageView spell1;
        ImageView spell2;
        TextView summonerName;

        public MatchViewHolder(View itemView) {
            super(itemView);

            championImage = itemView.findViewById(R.id.match_champion_image);
            spell1 = itemView.findViewById(R.id.match_summoner_spell_1_image);
            spell2 = itemView.findViewById(R.id.match_summoner_spell_2_image);
            summonerName = itemView.findViewById(R.id.match_summoner_tv);
        }
    }
}
