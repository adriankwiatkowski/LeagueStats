package com.example.android.leaguestats.adapters;

import android.content.Context;
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
import com.example.android.leaguestats.models.SummonerSpell;

import java.util.ArrayList;
import java.util.List;

public class SummonerSpellAdapter extends RecyclerView.Adapter<SummonerSpellAdapter.SummonerSpellViewHolder> implements Filterable {

    private final String PATCH_VERSION;
    private IdClickListener mListener;
    private Context mContext;
    private List<SummonerSpell> mSummonerSpellList;

    private List<SummonerSpell> mSummonerSpellListFiltered;
    private ValueFilter<SummonerSpell> mFilter;

    public SummonerSpellAdapter(Context context, IdClickListener listener, String patchVersion) {
        mContext = context;
        mSummonerSpellList = new ArrayList<>();
        mSummonerSpellListFiltered = mSummonerSpellList;
        mListener = listener;
        PATCH_VERSION = patchVersion;
    }

    @NonNull
    @Override
    public SummonerSpellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.summoner_spell_card_item, parent, false);
        return new SummonerSpellViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SummonerSpellViewHolder holder, int position) {
        SummonerSpell summonerSpell = mSummonerSpellListFiltered.get(position);
        holder.nameTv.setText(summonerSpell.getName());
        holder.descriptionTv.setText(summonerSpell.getDescription());
        GlideApp.with(mContext)
                .load(GlideUtils.getSpellUrl(PATCH_VERSION, summonerSpell.getSummonerSpellImageId()))
                .roundedImage()
                .into(holder.image);
    }

    public void setData(final List<SummonerSpell> newList) {
        if (mSummonerSpellList.isEmpty()) {
            mSummonerSpellList = newList;
            mSummonerSpellListFiltered = mSummonerSpellList;
            notifyDataSetChanged();
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mSummonerSpellList.size();
                }

                @Override
                public int getNewListSize() {
                    return newList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldPosition, int newPosition) {
                    return mSummonerSpellList.get(oldPosition).getId() == newList.get(newPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldPosition, int newPosition) {
                    return mSummonerSpellList.get(oldPosition).getName().equals(newList.get(newPosition).getName());
                }

                @Nullable
                @Override
                public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                    // You can return particular field for changed item.
                    return super.getChangePayload(oldItemPosition, newItemPosition);
                }
            });
            mSummonerSpellList = newList;
            mSummonerSpellListFiltered = mSummonerSpellList;
            diffResult.dispatchUpdatesTo(this);
        }
    }

    @Override
    public int getItemCount() {
        return mSummonerSpellListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ValueFilter<SummonerSpell>(mSummonerSpellList) {
                @NonNull
                @Override
                protected List<SummonerSpell> filterResult(String query) {
                    List<SummonerSpell> filteredList = new ArrayList<>();
                    for (SummonerSpell summonerSpell : mSummonerSpellList) {
                        if (summonerSpell.getName().toLowerCase().startsWith(query.toLowerCase())) {
                            filteredList.add(summonerSpell);
                        }
                    }
                    return filteredList;
                }

                @Override
                protected void publishResults(@Nullable List<SummonerSpell> filteredList) {
                    mSummonerSpellListFiltered = filteredList;
                    notifyDataSetChanged();
                }
            };
        }
        return mFilter;
    }

    class SummonerSpellViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView nameTv;
        TextView descriptionTv;

        SummonerSpellViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = itemView.findViewById(R.id.image);
            nameTv = itemView.findViewById(R.id.name_tv);
            descriptionTv = itemView.findViewById(R.id.description_tv);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(mSummonerSpellList.get(getAdapterPosition()).getId());
        }
    }
}
