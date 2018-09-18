package com.example.android.leaguestats.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.models.Match;
import com.example.android.leaguestats.utilities.PicassoUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExpandableMatchAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<Match> mMatches;
    private final String PATCH_VERSION;
    private long mCurrentSummonerId;

    public ExpandableMatchAdapter(Context context, ArrayList<Match> matches, String patchVersion) {
        mContext = context;
        mMatches = matches;
        PATCH_VERSION = patchVersion;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.match_item_header, null);
        }

        Match match = (Match) getGroup(groupPosition);

        TextView summonerStatsTv = convertView.findViewById(R.id.match_summoner_stats_header_tv);
        TextView matchTimestampTv = convertView.findViewById(R.id.match_timestamp_tv);

        ImageView championImage = convertView.findViewById(R.id.match_champion_header_image);
        ImageView summonerSpell1Image = convertView.findViewById(R.id.match_summoner_spell_1_header_image);
        ImageView summonerSpell2Image = convertView.findViewById(R.id.match_summoner_spell_2_header_image);

        int currentSummonerPosition = -1;
        for (int i = 0; i < match.getSummonerId().size(); i++) {
            long summonerId = match.getSummonerId().get(i);
            if (mCurrentSummonerId == summonerId) {
                currentSummonerPosition = i;
                break;
            }
        }

        int kills = match.getKills().get(currentSummonerPosition);
        int deaths = match.getDeaths().get(currentSummonerPosition);
        int assists = match.getAssists().get(currentSummonerPosition);

        summonerStatsTv.setText(String.valueOf(kills) + "/" + String.valueOf(deaths) + "/" + String.valueOf(assists));
        Date dateObject = new Date(match.getGameCreation());
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        String formattedDate = dateFormat.format(dateObject);
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String formattedTime = timeFormat.format(dateObject);
        String date = formattedDate + "     " + formattedTime;
        matchTimestampTv.setText(date);

        String championImagePath = match.getChampionEntries().get(currentSummonerPosition).getImage();
        PicassoUtils
                .getChampionThumbnailCreator(championImagePath, PATCH_VERSION,
                        R.dimen.champion_match_group_width, R.dimen.champion_match_group_height)
                .into(championImage);

        String summonerSpell1Path = match.getSpellEntries1().get(currentSummonerPosition).getImage();
        PicassoUtils
                .getSpellCreator(summonerSpell1Path, PATCH_VERSION,
                        R.dimen.summoner_spell_match_group_width, R.dimen.summoner_spell_match_group_height)
                .into(summonerSpell1Image);

        String summonerSpell2Path = match.getSpellEntries2().get(currentSummonerPosition).getImage();
        PicassoUtils
                .getSpellCreator(summonerSpell2Path, PATCH_VERSION,
                        R.dimen.summoner_spell_match_group_width, R.dimen.summoner_spell_match_group_height)
                .into(summonerSpell2Image);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.match_item_child, null);
        }

        Match match = (Match) getGroup(groupPosition);

        ImageView championImage = convertView.findViewById(R.id.match_champion_child_image);
        ImageView summonerSpell1Image = convertView.findViewById(R.id.match_summoner_spell_1_child_image);
        ImageView summonerSpell2Image = convertView.findViewById(R.id.match_summoner_spell_2_child_image);

        String championImagePath = match.getChampionEntries().get(childPosition).getImage();
        PicassoUtils
                .getChampionThumbnailCreator(championImagePath, PATCH_VERSION,
                        R.dimen.champion_match_child_width, R.dimen.champion_match_child_height)
                .into(championImage);

        String summonerSpell1Path = match.getSpellEntries1().get(childPosition).getImage();
        PicassoUtils
                .getSpellCreator(summonerSpell1Path, PATCH_VERSION,
                        R.dimen.summoner_spell_match_child_width, R.dimen.summoner_spell_match_child_height)
                .into(summonerSpell1Image);

        String summonerSpell2Path = match.getSpellEntries2().get(childPosition).getImage();
        PicassoUtils
                .getSpellCreator(summonerSpell2Path, PATCH_VERSION,
                        R.dimen.summoner_spell_match_child_width, R.dimen.summoner_spell_match_child_height)
                .into(summonerSpell2Image);

        return convertView;
    }

    private void addMatch(Match match) {
        mMatches.add(match);
        notifyDataSetChanged();
    }

    private void clear() {
        mMatches.clear();
        notifyDataSetChanged();
    }

    public void setData(List<Match> matches, long summonerId) {
        clear();
        mCurrentSummonerId = summonerId;
        for (Match match : matches) {
            addMatch(match);
            notifyDataSetChanged();
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mMatches.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getGroupCount() {
        return mMatches.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mMatches.get(groupPosition).getSummonerId().size();
    }
}
