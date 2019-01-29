package com.example.android.leaguestats.adapters;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.data.glide.GlideApp;
import com.example.android.leaguestats.data.glide.GlideUtils;
import com.example.android.leaguestats.interfaces.IdClickListener;
import com.example.android.leaguestats.models.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> implements Filterable {

    private final String PATCH_VERSION;
    private IdClickListener mListener;
    private Context mContext;
    private List<Item> mItemList;
    private long mLastClickTime = 0;

    private List<Item> mItemListFiltered;
    private ValueFilter<Item> mFilter;

    public ItemAdapter(Context context, IdClickListener listener, String patchVersion) {
        mContext = context;
        mItemList = new ArrayList<>();
        mItemListFiltered = mItemList;
        mListener = listener;
        PATCH_VERSION = patchVersion;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_card_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {
        Item item = mItemListFiltered.get(position);
        holder.nameTv.setText(item.getName());
        holder.descriptionTv.setText(item.getPlainText());
        holder.goldTv.setText(mContext.getString(R.string.gold_args, item.getTotalGold()));
        GlideApp.with(mContext)
                .load(GlideUtils.getItemUrl(PATCH_VERSION, item.getItemImageId()))
                .roundedImage()
                .into(holder.image);
    }

    public void setData(final List<Item> newList) {
        if (mItemList.isEmpty()) {
            mItemList = newList;
            mItemListFiltered = mItemList;
            notifyDataSetChanged();
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mItemList.size();
                }

                @Override
                public int getNewListSize() {
                    return newList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldPosition, int newPosition) {
                    return mItemList.get(oldPosition).getId() == newList.get(newPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldPosition, int newPosition) {
                    return mItemList.get(oldPosition).getName().equals(newList.get(newPosition).getName());
                }

                @Nullable
                @Override
                public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                    // You can return particular field for changed item.
                    return super.getChangePayload(oldItemPosition, newItemPosition);
                }
            });
            mItemList = newList;
            mItemListFiltered = mItemList;
            diffResult.dispatchUpdatesTo(this);
        }
    }

    @Override
    public int getItemCount() {
        return mItemListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ValueFilter<Item>(mItemList) {
                @NonNull
                @Override
                protected List<Item> filterResult(String query) {
                    List<Item> filteredList = new ArrayList<>();
                    for (Item item : mItemList) {
                        if (item.getName().toLowerCase().startsWith(query.toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                    return filteredList;
                }

                @Override
                protected void publishResults(@Nullable List<Item> filteredList) {
                    mItemListFiltered = filteredList;
                    notifyDataSetChanged();
                }
            };
        }
        return mFilter;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView nameTv;
        TextView descriptionTv;
        TextView goldTv;

        ItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = itemView.findViewById(R.id.image);
            nameTv = itemView.findViewById(R.id.name_tv);
            descriptionTv = itemView.findViewById(R.id.description_tv);
            goldTv = itemView.findViewById(R.id.gold_tv);
        }

        @Override
        public void onClick(View view) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            mListener.onClick(mItemListFiltered.get(getAdapterPosition()).getId());
        }
    }
}
