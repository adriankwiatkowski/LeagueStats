package com.example.android.leaguestats.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.models.Spell;
import com.example.android.leaguestats.utilities.PicassoUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class SpellAdapter extends RecyclerView.Adapter<SpellAdapter.SpellViewHolder> {

    private final Context mContext;
    private final List<Spell> mSpell;
    private final String PATCH_VERSION;

    public SpellAdapter(Context context, List<Spell> spells, String patchVersion) {
        this.mContext = context;
        this.mSpell = spells;
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
        holder.mSpellNameTv.setText(mSpell.get(position).getName());
        holder.mSpellDescriptionTv.setText(mSpell.get(position).getDescription());
        holder.mSpellCostTv.setText(mSpell.get(position).getCost());
        holder.mSpellCooldownTv.setText(mSpell.get(position).getCooldown());

        String spellPath = mSpell.get(position).getImage();
        PicassoUtils.getSpellCreator(spellPath, PATCH_VERSION, 125, 125).into(holder.mTarget);
    }

    public void add(Spell spell) {
        mSpell.add(spell);
        notifyDataSetChanged();
    }

    public void clear() {
        mSpell.clear();
        notifyDataSetChanged();
    }

    public void setData(List<Spell> spells) {
        clear();
        mSpell.addAll(spells);
    }

    @Override
    public int getItemCount() {
        return mSpell.size();
    }

    public static class SpellViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout mLayout;
        TextView mSpellNameTv;
        TextView mSpellDescriptionTv;
        TextView mSpellCooldownTv;
        TextView mSpellCostTv;
        ImageView mSpellImage;
        Target mTarget;

        public SpellViewHolder(View itemView) {
            super(itemView);
            mLayout = itemView.findViewById(R.id.spell_item_layout);
            mSpellNameTv = itemView.findViewById(R.id.spell_name_tv);
            mSpellDescriptionTv = itemView.findViewById(R.id.spell_description_tv);
            mSpellCooldownTv = itemView.findViewById(R.id.spell_cooldown_tv);
            mSpellCostTv = itemView.findViewById(R.id.spell_cost_tv);
            mSpellImage = itemView.findViewById(R.id.spell_image);

            mTarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                    mSpellImage.setImageBitmap(bitmap);

                    Palette.from(bitmap)
                            .generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(@NonNull Palette palette) {
                                    Palette.Swatch swatch = palette.getVibrantSwatch();
                                    if (swatch == null) {
                                        return;
                                    }
                                    setTextBodyColor(mSpellNameTv, swatch);
                                    setTextBodyColor(mSpellDescriptionTv, swatch);
                                    setTextBodyColor(mSpellCooldownTv, swatch);
                                    setTextBodyColor(mSpellCostTv, swatch);
                                    mLayout.setBackgroundColor(swatch.getRgb());
                                }
                            });
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    mSpellImage.setImageDrawable(errorDrawable);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    mSpellImage.setImageDrawable(placeHolderDrawable);
                }

                void setTextBodyColor(TextView textView, Palette.Swatch swatch) {
                    textView.setTextColor(swatch.getBodyTextColor());
                }
            };
        }
    }
}
