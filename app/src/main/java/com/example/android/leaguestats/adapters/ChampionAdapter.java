package com.example.android.leaguestats.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.database.Contract;
import com.squareup.picasso.Picasso;

public class ChampionAdapter extends RecyclerView.Adapter<ChampionAdapter.ChampionViewHolder> {

    private static ChampionClickListener mListener;

    public interface ChampionClickListener {
        void onChampionClick(int championId);
    }

    private static final String LOG_TAG = ChampionAdapter.class.getSimpleName();
    private Context mContext;
    private static Cursor mCursor;
    private final String PATCH_VERSION;

    public ChampionAdapter(Context context, Cursor cursor, ChampionClickListener listener, String patchVersion) {
        mContext = context;
        mCursor = cursor;
        mListener = listener;
        PATCH_VERSION = patchVersion;
    }

    @Override
    public ChampionAdapter.ChampionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.champion_item, parent, false);

        final String HTTP_ENTRY_THUMBNAIL_URL = "https://ddragon.leagueoflegends.com/cdn/" + PATCH_VERSION + "/img/champion";

        view.setTag(HTTP_ENTRY_THUMBNAIL_URL);

        return new ChampionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChampionAdapter.ChampionViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position))
            return;

        String championName = mCursor.getString(mCursor.getColumnIndex(Contract.ChampionEntry.COLUMN_CHAMPION_NAME));
        String championTitle = mCursor.getString(mCursor.getColumnIndex(Contract.ChampionEntry.COLUMN_CHAMPION_TITLE));
        String championThumbnail = mCursor.getString(mCursor.getColumnIndex(Contract.ChampionEntry.COLUMN_CHAMPION_THUMBNAIL));

        holder.mNameTv.setText(championName);
        holder.mTitleTv.setText(championTitle);

        String httpEntryUrl = (String) holder.itemView.getTag();

        Picasso.get()
                .load(httpEntryUrl + "/" + championThumbnail)
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

    public static class ChampionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mNameTv;
        TextView mTitleTv;
        ImageView mImage;

        public ChampionViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mNameTv = itemView.findViewById(R.id.champion_name_tv);
            mTitleTv = itemView.findViewById(R.id.champion_title_tv);
            mImage = itemView.findViewById(R.id.champion_image);
        }

        @Override
        public void onClick(View v) {
            mCursor.moveToPosition(getAdapterPosition());
            int id = mCursor.getInt(mCursor.getColumnIndex(Contract.ChampionEntry._ID));
            mListener.onChampionClick(id);
        }
    }
}
