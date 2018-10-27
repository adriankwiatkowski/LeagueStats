package com.example.android.leaguestats.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.data.database.entity.ItemEntry;
import com.example.android.leaguestats.models.Match;
import com.example.android.leaguestats.utilities.PicassoUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MatchAdapter extends BaseExpandableListAdapter {

    private static final String LOG_TAG = MatchAdapter.class.getSimpleName();
    private Context mContext;
    private List<Match> mMatches;
    private final String PATCH_VERSION;

    public MatchAdapter(Context context, ArrayList<Match> matches, String patchVersion) {
        mContext = context;
        mMatches = matches;
        PATCH_VERSION = patchVersion;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.match_item_group, null);
        }

        Match match = getGroup(groupPosition);

        TextView matchResultTv = convertView.findViewById(R.id.match_result_group_tv);
        TextView summonerStatsTv = convertView.findViewById(R.id.match_summoner_stats_group_tv);
        TextView matchTimestampTv = convertView.findViewById(R.id.match_timestamp_group_tv);
        TextView minionsKilledTv = convertView.findViewById(R.id.match_minions_group_tv);
        TextView goldEarnedTv = convertView.findViewById(R.id.match_gold_group_tv);

        ImageView championImage = convertView.findViewById(R.id.match_champion_group_image);
        ImageView summonerSpell1Image = convertView.findViewById(R.id.match_summoner_spell_1_group_image);
        ImageView summonerSpell2Image = convertView.findViewById(R.id.match_summoner_spell_2_group_image);

        int currentSummonerPosition = -1;
        for (int i = 0; i < match.getSummonerId().size(); i++) {
            long summonerId = match.getSummonerId().get(i);
            if (match.getCurrentSummonerId() == summonerId) {
                currentSummonerPosition = i;
                break;
            }
        }

        if (currentSummonerPosition == -1) {
            Log.w(LOG_TAG, "Couldn't match summoners. Current summoner id is: " + match.getCurrentSummonerId());
            return null;
        }

        boolean isWin = match.isWin().get(currentSummonerPosition);
        if (isWin) {
            matchResultTv.setText(R.string.victory);
            matchResultTv.setTextColor(mContext.getResources().getColor(R.color.victoryColor));
        } else {
            matchResultTv.setText(R.string.lose);
            matchResultTv.setTextColor(mContext.getResources().getColor(R.color.loseColor));
        }

        int kills = match.getKills().get(currentSummonerPosition);
        int deaths = match.getDeaths().get(currentSummonerPosition);
        int assists = match.getAssists().get(currentSummonerPosition);
        int totalMinionsKilled = match.getTotalMinionsKilled().get(currentSummonerPosition);
        int goldEarned = match.getGoldEarned().get(currentSummonerPosition);

        summonerStatsTv.setText(String.format(Locale.getDefault(), "%d/%d/%d", kills, deaths, assists));
        minionsKilledTv.setText(String.format(Locale.getDefault(), "minions %d", totalMinionsKilled));
        goldEarnedTv.setText(String.format(Locale.getDefault(), "gold %d", goldEarned));

        String gameCreation = getGameCreation(match);
        String gameDuration = getGameDuration(match);

        matchTimestampTv.setText(gameDuration + '\t' + gameCreation);

        String championImagePath = match.getChampionEntries().get(currentSummonerPosition).getImage();
        PicassoUtils.setChampionThumbnail(championImage, championImagePath, PATCH_VERSION,
                R.dimen.champion_match_group_width, R.dimen.champion_match_group_height);

        String summonerSpell1Path = match.getSpellEntries1().get(currentSummonerPosition).getImage();
        PicassoUtils.setSpellImage(summonerSpell1Image, summonerSpell1Path, PATCH_VERSION,
                R.dimen.summoner_spell_match_group_width, R.dimen.summoner_spell_match_group_height);

        String summonerSpell2Path = match.getSpellEntries2().get(currentSummonerPosition).getImage();
        PicassoUtils.setSpellImage(summonerSpell2Image, summonerSpell2Path, PATCH_VERSION,
                R.dimen.summoner_spell_match_group_width, R.dimen.summoner_spell_match_group_height);

        ImageView item1Image = convertView.findViewById(R.id.match_item_1_group_image);
        ImageView item2Image = convertView.findViewById(R.id.match_item_2_group_image);
        ImageView item3Image = convertView.findViewById(R.id.match_item_3_group_image);
        ImageView item4Image = convertView.findViewById(R.id.match_item_4_group_image);
        ImageView item5Image = convertView.findViewById(R.id.match_item_5_group_image);
        ImageView item6Image = convertView.findViewById(R.id.match_item_6_group_image);
        ImageView item7Image = convertView.findViewById(R.id.match_item_7_group_image);

        ImageView[] imageViews = new ImageView[]{item1Image, item2Image, item3Image,
                item4Image, item5Image, item6Image, item7Image};

        setItemData(imageViews, match, currentSummonerPosition, R.dimen.match_item_width, R.dimen.match_item_height);

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.match_item_child, null);
        }

        final Match match = getGroup(groupPosition);

        TextView summonerNameTv = convertView.findViewById(R.id.match_summoner_name_child_tv);

        ImageView championImage = convertView.findViewById(R.id.match_champion_child_image);
        ImageView summonerSpell1Image = convertView.findViewById(R.id.match_summoner_spell_1_child_image);
        ImageView summonerSpell2Image = convertView.findViewById(R.id.match_summoner_spell_2_child_image);

        boolean isWin = match.isWin().get(childPosition);
        if (isWin) {
            convertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.victoryColor));
        } else {
            convertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.loseColor));
        }

        summonerNameTv.setText(match.getSummonerName().get(childPosition));

        String championImagePath = match.getChampionEntries().get(childPosition).getImage();
        PicassoUtils.setChampionThumbnail(championImage, championImagePath, PATCH_VERSION,
                R.dimen.champion_match_child_width, R.dimen.champion_match_child_height);

        String summonerSpell1Path = match.getSpellEntries1().get(childPosition).getImage();
        PicassoUtils.setSpellImage(summonerSpell1Image, summonerSpell1Path, PATCH_VERSION,
                R.dimen.summoner_spell_match_child_width, R.dimen.summoner_spell_match_child_height);

        String summonerSpell2Path = match.getSpellEntries2().get(childPosition).getImage();
        PicassoUtils.setSpellImage(summonerSpell2Image, summonerSpell2Path, PATCH_VERSION,
                R.dimen.summoner_spell_match_child_width, R.dimen.summoner_spell_match_child_height);

        ImageView item1Image = convertView.findViewById(R.id.match_item_1_child_image);
        ImageView item2Image = convertView.findViewById(R.id.match_item_2_child_image);
        ImageView item3Image = convertView.findViewById(R.id.match_item_3_child_image);
        ImageView item4Image = convertView.findViewById(R.id.match_item_4_child_image);
        ImageView item5Image = convertView.findViewById(R.id.match_item_5_child_image);
        ImageView item6Image = convertView.findViewById(R.id.match_item_6_child_image);
        ImageView item7Image = convertView.findViewById(R.id.match_item_7_child_image);

        ImageView[] imageViews = new ImageView[]{item1Image, item2Image, item3Image,
                item4Image, item5Image, item6Image, item7Image};

        setItemData(imageViews, match, childPosition, R.dimen.match_item_child_width, R.dimen.match_item_child_height);

        return convertView;
    }

    private void setItemData(ImageView[] imageViews, Match match, int childPosition,
                             int widthResId, int heightResId) {

        List<ItemEntry> itemEntries = match.getItemEntries();

        String[] itemPaths = new String[7];
        for (int i = 0; i < itemPaths.length; i++) {
            int itemPosition = childPosition * 7 + i;
            itemPaths[i] = itemEntries.get(itemPosition).getImage();
        }

        for (int i = 0; i < imageViews.length; i++) {
            PicassoUtils.setItemImage(imageViews[i], itemPaths[i],
                    PATCH_VERSION, widthResId, heightResId);
        }
    }

    private String getGameCreation(Match match) {
        Date creationDate = new Date(match.getGameCreation());
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(creationDate);
    }

    @NonNull
    private String getGameDuration(Match match) {
        long gameDuration = match.getGameDuration();
        long minutes = TimeUnit.SECONDS.toMinutes(gameDuration);
        long seconds = TimeUnit.SECONDS.toSeconds(gameDuration);
        seconds -= minutes * 60;
        StringBuilder builder = new StringBuilder();
        builder.append(minutes);
        builder.append(':');
        if (seconds < 10) {
            builder.append('0');
        }
        builder.append(seconds);
        builder.append('\t');
        return builder.toString();
    }

    private void addMatch(Match match) {
        mMatches.add(match);
        notifyDataSetChanged();
    }

    private void clear() {
        mMatches.clear();
        notifyDataSetChanged();
    }

    public void setData(List<Match> matches) {
        clear();
        for (Match match : matches) {
            addMatch(match);
        }
    }

    @Override
    public Match getGroup(int groupPosition) {
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
