package com.example.android.leaguestats.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.data.database.entity.Spell;
import com.example.android.leaguestats.data.glide.GlideApp;
import com.example.android.leaguestats.data.glide.GlideUtils;

import java.util.ArrayList;
import java.util.List;

public class SpellAdapter extends RecyclerView.Adapter<SpellAdapter.SpellViewHolder> {

    private final Context mContext;
    private List<Spell> mSpellList;
    private final String PATCH_VERSION;

    public SpellAdapter(Context context, String patchVersion) {
        mContext = context;
        mSpellList = new ArrayList<>();
        PATCH_VERSION = patchVersion;
    }

    @NonNull
    @Override
    public SpellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.spell_item, parent, false);
        return new SpellViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpellViewHolder holder, int position) {
        Spell spell = mSpellList.get(position);
        holder.mSpellNameTv.setText(spell.getName());
        String tooltip = spell.getTooltip();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.mSpellTooltipTv.setText(Html.fromHtml(tooltip, Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.mSpellTooltipTv.setText(Html.fromHtml(tooltip));
        }
        holder.mSpellCostTv.setText(spell.getCostBurn());
        holder.mSpellCooldownTv.setText(spell.getCooldown());
        GlideApp.with(mContext)
                .load(GlideUtils.getSpellUrl(PATCH_VERSION, spell.getImageId()))
                .roundedImage()
                .into(holder.mSpellImage);
    }

    public void setData(final List<Spell> newList) {
        if (mSpellList.isEmpty()) {
            mSpellList = newList;
            notifyDataSetChanged();
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mSpellList.size();
                }

                @Override
                public int getNewListSize() {
                    return newList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldPosition, int newPosition) {
                    return mSpellList.get(oldPosition).getImageId().equals(newList.get(newPosition).getImageId());
                }

                @Override
                public boolean areContentsTheSame(int oldPosition, int newPosition) {
                    return mSpellList.get(oldPosition).getName().equals(newList.get(newPosition).getName());
                }

                @Nullable
                @Override
                public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                    // You can return particular field for changed item.
                    return super.getChangePayload(oldItemPosition, newItemPosition);
                }
            });
            mSpellList = newList;
            diffResult.dispatchUpdatesTo(this);
        }
    }

    @Override
    public int getItemCount() {
        return mSpellList.size();
    }

    class SpellViewHolder extends RecyclerView.ViewHolder {

        TextView mSpellNameTv;
        TextView mSpellTooltipTv;
        TextView mSpellCooldownTv;
        TextView mSpellCostTv;
        ImageView mSpellImage;

        SpellViewHolder(View itemView) {
            super(itemView);
            mSpellNameTv = itemView.findViewById(R.id.spell_name_tv);
            mSpellTooltipTv = itemView.findViewById(R.id.spell_tooltip_tv);
            mSpellCooldownTv = itemView.findViewById(R.id.spell_cooldown_tv);
            mSpellCostTv = itemView.findViewById(R.id.spell_cost_tv);
            mSpellImage = itemView.findViewById(R.id.spell_image);
        }
    }
}
