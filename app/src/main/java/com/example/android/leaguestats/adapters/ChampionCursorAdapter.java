package com.example.android.leaguestats.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.database.Contract;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ChampionCursorAdapter extends CursorAdapter {

    private static final String HTTP_ENTRY_URL_THUMBNAIL = "https://ddragon.leagueoflegends.com/cdn/8.11.1/img/champion";

    public ChampionCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.champion_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = view.findViewById(R.id.champion_name_tv);
        TextView titleTextView = view.findViewById(R.id.champion_title_tv);
        ImageView championImage = view.findViewById(R.id.champion_image);

        String championName = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_CHAMPION_NAME));
        String championTitle = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_CHAMPION_TITLE));
        int championThumbnailIndex = cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_THUMBNAIL);
        String championThumbnail = cursor.getString(championThumbnailIndex);

        nameTextView.setText(championName);
        titleTextView.setText(championTitle);
        Picasso.get()
                .load(HTTP_ENTRY_URL_THUMBNAIL + "/" + championThumbnail)
                .error(R.color.colorAccent)
                .placeholder(R.color.colorPrimary)
                .into(championImage);
    }
}

