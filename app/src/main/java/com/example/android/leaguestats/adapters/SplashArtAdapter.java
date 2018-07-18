package com.example.android.leaguestats.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class SplashArtAdapter extends RecyclerView.Adapter<SplashArtAdapter.ViewHolder> {

    private static final String LOG_TAG = SplashArtAdapter.class.getSimpleName();
    private static final String HTTP_ENTRY_URL_SPLASH_ART = "http://ddragon.leagueoflegends.com/cdn/img/champion/splash";
    private final Context mContext;
    private final List<String> mSplashArt;
    private final List<String> mSplashArtName;

    public SplashArtAdapter(Context context, List<String> splashArt, List<String> splashArtName) {
        mContext = context;
        mSplashArt = splashArt;
        mSplashArtName = splashArtName;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.splash_art_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int maxWidth = size.x;
        int maxHeight = size.y;

        int imageHeight;
        if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            imageHeight = maxHeight/3;
        } else {
            imageHeight = maxHeight;
        }

        // Set Splash Art on Text.
        Picasso.get()
                .load(HTTP_ENTRY_URL_SPLASH_ART + "/" + mSplashArt.get(position))
                .resize(maxWidth, imageHeight)
                .centerCrop()
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.mTarget);

        // Set Splash Art Name on Text;
        holder.mSplashArtNameTv.setText(mSplashArtName.get(position));
    }

    public void add(String splashArt, String splashArtName) {
        mSplashArt.add(splashArt);
        mSplashArtName.add(splashArtName);
        notifyDataSetChanged();
    }

    public void clear() {
        mSplashArt.clear();
        mSplashArtName.clear();
        notifyDataSetChanged();
    }

    public void setData(List<String> image, List<String> name) {
        clear();
        for (int i = 0; i < image.size(); i++) {
            add(image.get(i), name.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return mSplashArt.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mSplashArtNameTv;
        Target mTarget;

        public ViewHolder(final View itemView) {
            super(itemView);
            mSplashArtNameTv = itemView.findViewById(R.id.splash_art_name);

            mTarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Drawable imageDrawable = new BitmapDrawable(Resources.getSystem(), bitmap);
                    mSplashArtNameTv.setCompoundDrawablesWithIntrinsicBounds(null, imageDrawable, null, null);

                    Palette.from(bitmap)
                            .generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(@NonNull Palette palette) {
                                    Palette.Swatch textSwatch = palette.getVibrantSwatch();
                                    if (textSwatch == null) {
                                        return;
                                    }
                                    mSplashArtNameTv.setBackgroundColor(textSwatch.getRgb());
                                    mSplashArtNameTv.setTextColor(textSwatch.getTitleTextColor());

                                    // Prevent from changing background on every swap. It'd be weird experience.
                                    if (getAdapterPosition() == 0) {
                                        itemView.getRootView().setBackgroundColor(textSwatch.getRgb());
                                    }
                                }
                            });
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    mSplashArtNameTv.setCompoundDrawablesWithIntrinsicBounds(null, errorDrawable, null, null);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    mSplashArtNameTv.setCompoundDrawablesWithIntrinsicBounds(null, placeHolderDrawable, null, null);
                }
            };
        }
    }
}
