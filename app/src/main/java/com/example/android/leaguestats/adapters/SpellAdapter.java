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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class SpellAdapter extends RecyclerView.Adapter<SpellAdapter.ViewHolder> {

    private static final String HTTP_ENTRY_URL_SPELL = "http://ddragon.leagueoflegends.com/cdn/8.11.1/img/spell";
    private final Context mContext;
    private final List<Spell> mSpell;

    public SpellAdapter(Context context, List<Spell> spells) {
        this.mContext = context;
        this.mSpell = spells;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.spell_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mSpellNameTv.setText(mSpell.get(position).getName());
        holder.mSpellDescriptionTv.setText(mSpell.get(position).getDescription());
        holder.mSpellCostTv.setText(mSpell.get(position).getCost());
        holder.mSpellCooldownTv.setText(mSpell.get(position).getCooldown());

        Picasso.get()
                .load(HTTP_ENTRY_URL_SPELL + "/" + mSpell.get(position).getImage())
                .resize(125, 125)
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.mTarget);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout mLayout;
        TextView mSpellNameTv;
        TextView mSpellDescriptionTv;
        TextView mSpellCooldownTv;
        TextView mSpellCostTv;
        ImageView mSpellImage;
        Target mTarget;

        public ViewHolder(View itemView) {
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
