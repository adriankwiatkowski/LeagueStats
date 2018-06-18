package com.example.android.leaguestats.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.leaguestats.models.Mastery;
import com.example.android.leaguestats.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MasteryAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<Mastery> mMastery;

    public MasteryAdapter(Context context, ArrayList<Mastery> mastery) {
        mContext = context;
        mMastery = mastery;
    }

    public void add(Mastery mastery) {
        mMastery.add(mastery);
        notifyDataSetChanged();
    }

    public void clear() {
        mMastery.clear();
        notifyDataSetChanged();
    }

    public void setData(ArrayList<Mastery> data) {
        clear();
        for (Mastery mastery : data) {
            add(mastery);
        }
    }

    @Override
    public int getCount() {
        return mMastery.size();
    }

    @Override
    public Mastery getItem(int position) {
        return mMastery.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView playerId;
        TextView championId;
        TextView championLevel;
        TextView championPoints;
        TextView lastPlayTime;
        TextView isChestGranted;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.mastery_item, parent, false);
        }

        playerId = convertView.findViewById(R.id.text_view_player_id);
        playerId.setText(String.valueOf(getItem(position).getPlayerId()));

        championId = convertView.findViewById(R.id.text_view_champion_id);
        championId.setText(String.valueOf(getItem(position).getChampionId()));

        championLevel = convertView.findViewById(R.id.text_view_champion_level);
        championLevel.setText(String.valueOf(getItem(position).getChampionLevel()));

        championPoints = convertView.findViewById(R.id.text_view_champion_points);
        championPoints.setText(String.valueOf(getItem(position).getChampionPoints()));

        lastPlayTime = convertView.findViewById(R.id.text_view_last_played);
        Date dateObject = new Date(getItem(position).getLastPlayTime());
        //"LLL dd, yyyy"
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        String formattedDate = dateFormat.format(dateObject);
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String formattedTime = timeFormat.format(dateObject);
        String date = formattedDate + "\n" + formattedTime;
        lastPlayTime.setText(date);

        isChestGranted = convertView.findViewById(R.id.text_view_chest_granted);
        boolean isGranted = getItem(position).isChestGranted();
        String isGrantedString;
        if (isGranted) {
            isGrantedString = mContext.getString(R.string.yes);
        } else {
            isGrantedString = mContext.getString(R.string.no);
        }
        isChestGranted.setText(isGrantedString);

        return convertView;
    }
}
