package com.example.android.leaguestats.adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.database.ItemEntry;
import com.example.android.leaguestats.utilities.DataUtils;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private static ItemClickListener mListener;

    public interface ItemClickListener {
        void onItemClick(ItemEntry itemEntry);
    }

    private static final String LOG_TAG = ItemAdapter.class.getSimpleName();
    private Context mContext;
    private List<ItemEntry> mList;
    private final String PATCH_VERSION;
    private long mLastClickTime = 0;

    public ItemAdapter(Context context, List<ItemEntry> list, ItemClickListener listener, String patchVersion) {
        mContext = context;
        mList = list;
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
        holder.mNameTv.setText(mList.get(position).getName());
        holder.mCostTv.setText(String.valueOf(mList.get(position).getTotalGold()));

        String image = mList.get(position).getImage();

        String httpEntryUrl = (String) holder.itemView.getTag();

        Picasso.get()
                .load(httpEntryUrl + "/" + image)
                .resize(48, 48)
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.mImage);
    }

    public void add(ItemEntry itemEntry) {
        mList.add(itemEntry);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void setData(List<ItemEntry> list) {
        clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
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
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            mListener.onItemClick(mList.get(getAdapterPosition()));
        }
    }
}
