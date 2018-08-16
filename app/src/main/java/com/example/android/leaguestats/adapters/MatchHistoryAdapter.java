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
import com.example.android.leaguestats.models.Match;
import com.example.android.leaguestats.database.ChampionEntry;
import com.example.android.leaguestats.database.SummonerSpellEntry;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MatchHistoryAdapter extends RecyclerView.Adapter<MatchHistoryAdapter.MatchHistoryViewHolder> {

    private Context mContext;
    private ArrayList<Match> mMatches;
    private ArrayList<ChampionEntry> mChampions;
    private ArrayList<SummonerSpellEntry> mSummonerSpell1;
    private ArrayList<SummonerSpellEntry> mSummonerSpell2;
    private final String PATCH_VERSION;

    public MatchHistoryAdapter(Context context, ArrayList<Match> matches,
                               ArrayList<ChampionEntry> championEntries,
                               ArrayList<SummonerSpellEntry> summonerSpell1Entries,
                               ArrayList<SummonerSpellEntry> summonerSpell2Entries,
                               String patchVersion) {
        mContext = context;
        mMatches = matches;
        mChampions = championEntries;
        mSummonerSpell1 = summonerSpell1Entries;
        mSummonerSpell2 = summonerSpell2Entries;
        PATCH_VERSION = patchVersion;
    }

    @NonNull
    @Override
    public MatchHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.match_item, parent, false);


        final String HTTP_ENTRY_SPELL_URL = "http://ddragon.leagueoflegends.com/cdn/" + PATCH_VERSION + "/img/spell";
        final String HTTP_ENTRY_THUMBNAIL_URL = "https://ddragon.leagueoflegends.com/cdn/" + PATCH_VERSION + "/img/champion";

        view.setTag(R.id.match_summoner_spell_tag, HTTP_ENTRY_SPELL_URL);
        view.setTag(R.id.match_champion_thumbnail_tag, HTTP_ENTRY_THUMBNAIL_URL);

        return new MatchHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchHistoryViewHolder holder, int position) {
        if (mMatches != null) {
            if (!mMatches.isEmpty()) {
                String summonerName = mMatches.get(0).getSummonerName().get(position);
                holder.summonerName.setText(summonerName);
            }
        }

        String httpEntryChampionUrl = (String) holder.itemView.getTag(R.id.match_champion_thumbnail_tag);
        String httpEntrySpellUrl = (String) holder.itemView.getTag(R.id.match_summoner_spell_tag);

        if (mSummonerSpell1 != null) {
            if (!mSummonerSpell1.isEmpty()) {
                Picasso.get()
                        .load(httpEntrySpellUrl + "/" + mSummonerSpell1.get(position).getImage())
                        .error(R.drawable.ic_launcher_background)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(holder.spell1);
            }
        }

        if (mSummonerSpell2 != null) {
            if (!mSummonerSpell2.isEmpty()) {
                Picasso.get()
                        .load(httpEntrySpellUrl + "/" + mSummonerSpell2.get(position).getImage())
                        .error(R.drawable.ic_launcher_background)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(holder.spell2);
            }
        }

        if (mChampions != null) {
            if (!mChampions.isEmpty()) {
                Picasso.get()
                        .load(httpEntryChampionUrl + "/" + mChampions.get(position).getThumbnail())
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

    public void setMatches(List<Match> matches) {
        if (matches != null) {
            mMatches.clear();
            mMatches.addAll(matches);
            notifyDataSetChanged();
        }
    }

    public void setChampions(List<ChampionEntry> champions) {
        mChampions.clear();
        mChampions.addAll(champions);
        notifyDataSetChanged();
    }

    public void setSummonerSpell1(List<SummonerSpellEntry> spellEntries) {
        mSummonerSpell1.clear();
        mSummonerSpell1.addAll(spellEntries);
        notifyDataSetChanged();
    }

    public void setSummonerSpell2(List<SummonerSpellEntry> spellEntries) {
        mSummonerSpell2.clear();
        mSummonerSpell2.addAll(spellEntries);
        notifyDataSetChanged();
    }

    public void setData(List<Match> matches, List<ChampionEntry> championEntries,
                        List<SummonerSpellEntry> spell1, List<SummonerSpellEntry> spell2) {
        mMatches.clear();
        mChampions.clear();
        mSummonerSpell1.clear();
        mSummonerSpell2.clear();
        mMatches.addAll(matches);
        mChampions.addAll(championEntries);
        mSummonerSpell1.addAll(spell1);
        mSummonerSpell2.addAll(spell2);
        notifyDataSetChanged();
    }

    public class MatchHistoryViewHolder extends RecyclerView.ViewHolder {

        ImageView championImage;
        ImageView spell1;
        ImageView spell2;
        TextView summonerName;

        public MatchHistoryViewHolder(View itemView) {
            super(itemView);

            championImage = itemView.findViewById(R.id.match_champion_image);
            spell1 = itemView.findViewById(R.id.match_summoner_spell_1_image);
            spell2 = itemView.findViewById(R.id.match_summoner_spell_2_image);
            summonerName = itemView.findViewById(R.id.match_summoner_tv);
        }
    }
}
