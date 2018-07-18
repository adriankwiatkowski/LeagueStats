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
import com.example.android.leaguestats.utilities.DataUtils;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private static ItemClickListener mListener;

    public interface ItemClickListener {
        void onItemClick(int itemId);
    }

    private static final String LOG_TAG = ItemAdapter.class.getSimpleName();
    private Context mContext;
    private Cursor mCursor;
    private final String PATCH_VERSION;

    public ItemAdapter(Context context, Cursor cursor, ItemClickListener listener, String patchVersion) {
        mContext = context;
        mCursor = cursor;
        mListener = listener;
        PATCH_VERSION = patchVersion;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);

        final String HTTP_ENTRY_URL = "http://ddragon.leagueoflegends.com/cdn/" + PATCH_VERSION + "/img/item";

        view.setTag(HTTP_ENTRY_URL);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position))
            return;

        String name = mCursor.getString(mCursor.getColumnIndex(Contract.ItemEntry.COLUMN_NAME));
        int cost = mCursor.getInt(mCursor.getColumnIndex(Contract.ItemEntry.COLUMN_TOTAL_GOLD));
        String image = mCursor.getString(mCursor.getColumnIndex(Contract.ItemEntry.COLUMN_IMAGE));

        holder.mNameTv.setText(name);
        holder.mCostTv.setText(String.valueOf(cost));

        String httpEntryUrl = (String) holder.itemView.getTag();

        Picasso.get()
                .load(httpEntryUrl + "/" + image)
                .resize(48, 48)
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

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mNameTv;
        TextView mCostTv;
        ImageView mImage;

        public ItemViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mNameTv = itemView.findViewById(R.id.item_name_tv);
            mCostTv = itemView.findViewById(R.id.item_cost_tv);
            mImage = itemView.findViewById(R.id.item_image);
        }

        @Override
        public void onClick(View v) {
            if (!mCursor.moveToPosition(getAdapterPosition())) return;
            int id = mCursor.getInt(mCursor.getColumnIndex(Contract.ItemEntry._ID));
            mListener.onItemClick(id);
        }
    }
}
