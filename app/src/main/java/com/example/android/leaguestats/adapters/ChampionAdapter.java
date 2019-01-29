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
import com.example.android.leaguestats.models.Champion;

import java.util.ArrayList;
import java.util.List;

public class ChampionAdapter extends RecyclerView.Adapter<ChampionAdapter.ChampionViewHolder> implements Filterable {

    private final String PATCH_VERSION;
    private IdClickListener mListener;
    private Context mContext;
    private List<Champion> mChampionList;
    private long mLastClickTime = 0;

    private List<Champion> mChampionListFiltered;
    private ValueFilter<Champion> mFilter;

    public ChampionAdapter(Context context, IdClickListener listener, String patchVersion) {
        mContext = context;
        mChampionList = new ArrayList<>();
        mChampionListFiltered = mChampionList;
        mListener = listener;
        PATCH_VERSION = patchVersion;
    }

    @NonNull
    @Override
    public ChampionAdapter.ChampionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.champion_card_item, parent, false);
        return new ChampionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChampionAdapter.ChampionViewHolder holder, int position) {
        Champion champion = mChampionListFiltered.get(position);
        holder.nameTv.setText(champion.getName());
        GlideApp.with(mContext)
                .load(GlideUtils.getChampionUrl(PATCH_VERSION, champion.getChampionImageId()))
                .roundedImage()
                .into(holder.image);
    }

    public void setData(final List<Champion> newList) {
        if (mChampionList.isEmpty()) {
            mChampionList = newList;
            mChampionListFiltered = mChampionList;
            notifyDataSetChanged();
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mChampionList.size();
                }

                @Override
                public int getNewListSize() {
                    return newList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldPosition, int newPosition) {
                    return mChampionList.get(oldPosition).getId() == newList.get(newPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldPosition, int newPosition) {
                    return mChampionList.get(oldPosition).getName().equals(newList.get(newPosition).getName());
                }
            });
            mChampionList = newList;
            mChampionListFiltered = mChampionList;
            diffResult.dispatchUpdatesTo(this);
        }
    }

    @Override
    public int getItemCount() {
        return mChampionListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ValueFilter<Champion>(mChampionList) {
                @NonNull
                @Override
                protected List<Champion> filterResult(String query) {
                    List<Champion> filteredList = new ArrayList<>();
                    for (Champion champion : mChampionList) {
                        if (champion.getName().toLowerCase().startsWith(query.toLowerCase())) {
                            filteredList.add(champion);
                        }
                    }
                    return filteredList;
                }

                @Override
                protected void publishResults(@Nullable List<Champion> filteredList) {
                    mChampionListFiltered = filteredList;
                    notifyDataSetChanged();
                }
            };
        }
        return mFilter;
    }

    class ChampionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameTv;
        ImageView image;

        ChampionViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = itemView.findViewById(R.id.image);
            nameTv = itemView.findViewById(R.id.name);
        }

        @Override
        public void onClick(View v) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            mListener.onClick(mChampionListFiltered.get(getAdapterPosition()).getId());
        }
    }
}
