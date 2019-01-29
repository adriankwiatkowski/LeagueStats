package com.example.android.leaguestats.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.data.glide.GlideApp;
import com.example.android.leaguestats.data.glide.GlideUtils;
import com.example.android.leaguestats.data.network.api.models.match.Stats;
import com.example.android.leaguestats.models.Champion;
import com.example.android.leaguestats.models.Item;
import com.example.android.leaguestats.models.Match;
import com.example.android.leaguestats.models.SummonerSpell;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        for (int i = 0; i < match.getParticipantIdentities().size(); i++) {
            long summonerId = match.getParticipantIdentities().get(i).getPlayer().getSummonerId();
            if (match.getCurrentSummonerId() == summonerId) {
                currentSummonerPosition = i;
                break;
            }
        }

        if (currentSummonerPosition == -1) {
            Log.w(LOG_TAG, "Couldn't match summoners. Current summoner id is: " + match.getCurrentSummonerId());
            return null;
        }

        boolean isWin = match.getParticipants().get(currentSummonerPosition).getStats().isWin();
        if (isWin) {
            matchResultTv.setText(R.string.victory);
            matchResultTv.setTextColor(mContext.getResources().getColor(R.color.victoryColor));
        } else {
            matchResultTv.setText(R.string.lose);
            matchResultTv.setTextColor(mContext.getResources().getColor(R.color.loseColor));
        }

        int kills = match.getParticipants().get(currentSummonerPosition).getStats().getKills();
        int deaths = match.getParticipants().get(currentSummonerPosition).getStats().getDeaths();
        int assists = match.getParticipants().get(currentSummonerPosition).getStats().getAssists();
        int totalMinionsKilled = match.getParticipants().get(currentSummonerPosition).getStats().getTotalMinionsKilled();
        int goldEarned = match.getParticipants().get(currentSummonerPosition).getStats().getGoldEarned();

        summonerStatsTv.setText(mContext.getString(R.string.kda, kills, deaths, assists));
        minionsKilledTv.setText(mContext.getString(R.string.minions_args, totalMinionsKilled));
        goldEarnedTv.setText(mContext.getString(R.string.gold_args, goldEarned));

        String gameCreation = getGameCreation(match);
        String gameDuration = getGameDuration(match);

        matchTimestampTv.setText(mContext.getString(R.string.match_timestamp, gameDuration, gameCreation));


        Champion champion = match.getParticipants().get(currentSummonerPosition).getChampion();
        if (champion != null) {
            GlideApp.with(mContext)
                    .load(GlideUtils.getChampionUrl(PATCH_VERSION, champion.getChampionImageId()))
                    .roundedImage()
                    .into(championImage);
        }
        SummonerSpell summonerSpell1 = match.getParticipants().get(currentSummonerPosition).getSummonerSpell1();
        if (summonerSpell1 != null) {
            GlideApp.with(mContext)
                    .load(GlideUtils.getSpellUrl(PATCH_VERSION, summonerSpell1.getSummonerSpellImageId()))
                    .roundedImage()
                    .into(summonerSpell1Image);
        }
        SummonerSpell summonerSpell2 = match.getParticipants().get(currentSummonerPosition).getSummonerSpell2();
        if (summonerSpell2 != null) {
            GlideApp.with(mContext)
                    .load(GlideUtils.getSpellUrl(PATCH_VERSION, summonerSpell2.getSummonerSpellImageId()))
                    .roundedImage()
                    .into(summonerSpell2Image);
        }

        ImageView item1Image = convertView.findViewById(R.id.match_item_1_group_image);
        ImageView item2Image = convertView.findViewById(R.id.match_item_2_group_image);
        ImageView item3Image = convertView.findViewById(R.id.match_item_3_group_image);
        ImageView item4Image = convertView.findViewById(R.id.match_item_4_group_image);
        ImageView item5Image = convertView.findViewById(R.id.match_item_5_group_image);
        ImageView item6Image = convertView.findViewById(R.id.match_item_6_group_image);
        ImageView item7Image = convertView.findViewById(R.id.match_item_7_group_image);

        ImageView[] imageViews = new ImageView[]{item1Image, item2Image, item3Image,
                item4Image, item5Image, item6Image, item7Image};

        setItemData(imageViews, match, currentSummonerPosition);

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

        boolean isWin = match.getParticipants().get(childPosition).getStats().isWin();
        if (isWin) {
            if (convertView instanceof CardView) {
                ((CardView) convertView).setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.victoryColor));
            } else
                convertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.victoryColor));
        } else {
            if (convertView instanceof CardView) {
                ((CardView) convertView).setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.loseColor));
            } else
                convertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.loseColor));
        }

        summonerNameTv.setText(match.getParticipantIdentities().get(childPosition).getPlayer().getSummonerName());

        Champion champion = match.getParticipants().get(childPosition).getChampion();
        if (champion != null) {
            GlideApp.with(mContext)
                    .load(GlideUtils.getChampionUrl(PATCH_VERSION, champion.getChampionImageId()))
                    .roundedImage()
                    .into(championImage);
        }
        SummonerSpell summonerSpell1 = match.getParticipants().get(childPosition).getSummonerSpell1();
        if (summonerSpell1 != null) {
            GlideApp.with(mContext)
                    .load(GlideUtils.getSpellUrl(PATCH_VERSION, summonerSpell1.getSummonerSpellImageId()))
                    .roundedImage()
                    .into(summonerSpell1Image);
        }
        SummonerSpell summonerSpell2 = match.getParticipants().get(childPosition).getSummonerSpell2();
        if (summonerSpell2 != null) {
            GlideApp.with(mContext)
                    .load(GlideUtils.getSpellUrl(PATCH_VERSION, summonerSpell2.getSummonerSpellImageId()))
                    .roundedImage()
                    .into(summonerSpell2Image);
        }

        ImageView item1Image = convertView.findViewById(R.id.match_item_1_child_image);
        ImageView item2Image = convertView.findViewById(R.id.match_item_2_child_image);
        ImageView item3Image = convertView.findViewById(R.id.match_item_3_child_image);
        ImageView item4Image = convertView.findViewById(R.id.match_item_4_child_image);
        ImageView item5Image = convertView.findViewById(R.id.match_item_5_child_image);
        ImageView item6Image = convertView.findViewById(R.id.match_item_6_child_image);
        ImageView item7Image = convertView.findViewById(R.id.match_item_7_child_image);

        ImageView[] imageViews = new ImageView[]{item1Image, item2Image, item3Image,
                item4Image, item5Image, item6Image, item7Image};

        setItemData(imageViews, match, childPosition);

        return convertView;
    }

    private void setItemData(ImageView[] imageViews, Match match, int childPosition) {
        List<Item> itemList = new ArrayList<>();
        Stats stats = match.getParticipants().get(childPosition).getStats();
        itemList.add(stats.getItem0());
        itemList.add(stats.getItem1());
        itemList.add(stats.getItem2());
        itemList.add(stats.getItem3());
        itemList.add(stats.getItem4());
        itemList.add(stats.getItem5());
        itemList.add(stats.getItem6());
        for (int i = 0; i < imageViews.length; i++) {
            String url = "";
            if (itemList.get(i) != null) {
                url = GlideUtils.getItemUrl(PATCH_VERSION, itemList.get(i).getItemImageId());
            }
            GlideApp.with(mContext)
                    .load(url)
                    .roundedImage()
                    .into(imageViews[i]);
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

    public void clear() {
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
    public boolean areAllItemsEnabled() {
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
        return mMatches.get(groupPosition).getParticipantIdentities().size();
    }
}
