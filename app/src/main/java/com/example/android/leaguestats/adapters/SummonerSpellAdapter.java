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
import com.example.android.leaguestats.database.Contract;
import com.squareup.picasso.Picasso;

public class SummonerSpellAdapter extends RecyclerView.Adapter<SummonerSpellAdapter.SummonerSpellViewHolder> {

    private static final String LOG_TAG = SummonerSpellAdapter.class.getSimpleName();
    private Context mContext;
    private static Cursor mCursor;
    private final String PATCH_VERSION;

    public SummonerSpellAdapter(Context context, Cursor cursor, String patchVersion) {
        mContext = context;
        mCursor = cursor;
        PATCH_VERSION = patchVersion;
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
        if (!mCursor.moveToPosition(position))
            return;

        String name = mCursor.getString(mCursor.getColumnIndex(Contract.SummonerSpellEntry.COLUMN_NAME));
        int cooldown = mCursor.getInt(mCursor.getColumnIndex(Contract.SummonerSpellEntry.COLUMN_COOLDOWN));
        String image = mCursor.getString(mCursor.getColumnIndex(Contract.SummonerSpellEntry.COLUMN_IMAGE));

        holder.mNameTv.setText(name);
        holder.mCooldownTv.setText(String.valueOf(cooldown));

        String httpEntryUrl = (String) holder.itemView.getTag();

        Picasso.get()
                .load(httpEntryUrl + "/" + image)
                .resize(200, 200)
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) return;
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public static class SummonerSpellViewHolder extends RecyclerView.ViewHolder {
        TextView mNameTv;
        TextView mCooldownTv;
        ImageView mImage;

        public SummonerSpellViewHolder(View itemView) {
            super(itemView);

            mNameTv = itemView.findViewById(R.id.summoner_spell_name_item);
            mCooldownTv = itemView.findViewById(R.id.summoner_spell_cooldown_item);
            mImage = itemView.findViewById(R.id.summoner_spell_image_item);
        }
    }
}
