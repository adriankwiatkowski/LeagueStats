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
import com.example.android.leaguestats.models.Champion;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class SpellAdapter extends RecyclerView.Adapter<SpellAdapter.ViewHolder> {

    private static final String HTTP_ENTRY_URL_SPELL = "http://ddragon.leagueoflegends.com/cdn/8.11.1/img/spell";
    private final Context mContext;
    private final List<String> mSpellName;
    private final List<String> mSpellDescription;
    private final List<String> mSpellImage;
    private final List<String> mSpellResource;
    private final List<String> mSpellCooldown;
    private final List<String> mSpellCost;

    public SpellAdapter(Context context, List<String> name, List<String> description,
                        List<String> image, List<String> resource, List<String> cooldown,
                        List<String> cost) {
        this.mContext = context;
        this.mSpellName = name;
        this.mSpellDescription = description;
        this.mSpellImage = image;
        this.mSpellResource = resource;
        this.mSpellCooldown = cooldown;
        this.mSpellCost = cost;
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
        holder.mSpellNameTv.setText(mSpellName.get(position));
        holder.mSpellDescriptionTv.setText(mSpellDescription.get(position));
        holder.mSpellCooldownTv.setText(mSpellCooldown.get(position));
        holder.mSpellCostTv.setText(mSpellCost.get(position));

        Picasso.get()
                .load(HTTP_ENTRY_URL_SPELL + "/" + mSpellImage.get(position))
                .resize(125, 125)
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.mTarget);
    }

    public void add(String name, String description, String image, String resource, String cooldown,
                    String cost) {
        mSpellName.add(name);
        mSpellDescription.add(description);
        mSpellImage.add(image);
        mSpellResource.add(resource);
        mSpellCooldown.add(cooldown);
        mSpellCost.add(cost);
        notifyDataSetChanged();
    }

    public void clear() {
        mSpellName.clear();
        mSpellDescription.clear();
        mSpellImage.clear();
        mSpellResource.clear();
        mSpellCooldown.clear();
        mSpellCost.clear();
        notifyDataSetChanged();
    }

    public void setData(List<String> name, List<String> description, List<String> image,
                        List<String> resource, List<String> cooldown, List<String> cost) {
        clear();
        for (int i = 0; i < name.size(); i++) {
            add(name.get(i), description.get(i), image.get(i), resource.get(i),
                    cooldown.get(i), cost.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return mSpellName.size();
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
