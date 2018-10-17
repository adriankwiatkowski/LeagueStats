package com.example.android.leaguestats.utilities;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.android.leaguestats.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

public final class PicassoUtils {

    private static final String HTTP_ENTRY_URL_SPLASH_ART = "http://ddragon.leagueoflegends.com/cdn/img/champion/splash";
    private static final String HTTP_ENTRY_DRAGON_URL = "http://ddragon.leagueoflegends.com/cdn/";
    private static final String SPELL_PATH = "/img/spell";
    private static final String CHAMPION_THUMBNAIL_PATH = "/img/champion";
    private static final String ITEM_PATH = "/img/item";
    private static final String ICON_PATH = "/img/profileicon";
    private static final int ERROR_DRAWABLE = R.drawable.ic_launcher_background;
    private static final int PLACEHOLDER = R.drawable.ic_launcher_foreground;

    public static void setSplashArt(ImageView imageView, Context context, String path) {
        Picasso.get()
                .load(HTTP_ENTRY_URL_SPLASH_ART + "/" + path)
                .resize(getWidth(context), getHeight(context))
                .centerCrop()
                .error(ERROR_DRAWABLE)
                .placeholder(PLACEHOLDER)
                .into(imageView);
    }

    public static void setSplashArt(Target target, Context context, String path) {
        Picasso.get()
                .load(HTTP_ENTRY_URL_SPLASH_ART + "/" + path)
                .resize(getWidth(context), getHeight(context))
                .centerCrop()
                .error(ERROR_DRAWABLE)
                .placeholder(PLACEHOLDER)
                .into(target);
    }

    public static void setSpellImage(ImageView imageView, String path, String patchVersion) {
        Picasso.get()
                .load(HTTP_ENTRY_DRAGON_URL + patchVersion + SPELL_PATH + "/" + path)
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageView);
    }

    public static void setChampionThumbnail(ImageView imageView, String path, String patchVersion) {
        Picasso.get()
                .load(HTTP_ENTRY_DRAGON_URL + patchVersion + CHAMPION_THUMBNAIL_PATH + "/" + path)
                .error(ERROR_DRAWABLE)
                .placeholder(PLACEHOLDER)
                .into(imageView);
    }

    // TODO Check .fit() :).
    public static void setChampionThumbnail(ImageView imageView, String path, String patchVersion,
                                            int widthResId, int heightResId) {
        Picasso.get()
                .load(HTTP_ENTRY_DRAGON_URL + patchVersion + CHAMPION_THUMBNAIL_PATH + "/" + path)
                .resizeDimen(widthResId, heightResId)
                .centerCrop()
                .error(ERROR_DRAWABLE)
                .placeholder(PLACEHOLDER)
                .into(imageView);
    }

    public static void setChampionThumbnail(Target target, String path, String patchVersion,
                                            int widthResId, int heightResId) {
        Picasso.get()
                .load(HTTP_ENTRY_DRAGON_URL + patchVersion + CHAMPION_THUMBNAIL_PATH + "/" + path)
                .resizeDimen(widthResId, heightResId)
                .centerCrop()
                .error(ERROR_DRAWABLE)
                .placeholder(PLACEHOLDER)
                .into(target);
    }

    public static void setSpellImage(ImageView imageView, String path, String patchVersion,
                                     int widthResId, int heightResId) {
        Picasso.get()
                .load(HTTP_ENTRY_DRAGON_URL + patchVersion + SPELL_PATH + "/" + path)
                .resizeDimen(widthResId, heightResId)
                .error(ERROR_DRAWABLE)
                .placeholder(PLACEHOLDER)
                .into(imageView);
    }

    public static void setSpellImage(Target target, String path, String patchVersion,
                                     int widthResId, int heightResId) {
        Picasso.get()
                .load(HTTP_ENTRY_DRAGON_URL + patchVersion + SPELL_PATH + "/" + path)
                .resizeDimen(widthResId, heightResId)
                .error(ERROR_DRAWABLE)
                .placeholder(PLACEHOLDER)
                .into(target);
    }

    public static void setItemImage(ImageView imageView, String path, String patchVersion,
                                    int widthResId, int heightResId) {
        Picasso.get()
                .load(HTTP_ENTRY_DRAGON_URL + patchVersion + ITEM_PATH + "/" + path)
                .resizeDimen(widthResId, heightResId)
                .error(ERROR_DRAWABLE)
                .placeholder(PLACEHOLDER)
                .into(imageView);
    }

    public static void setIconImage(ImageView imageView, String path, String patchVersion,
                                              int widthResId, int heightResId) {
        Picasso.get()
                .load(HTTP_ENTRY_DRAGON_URL + patchVersion + ICON_PATH + "/" + path + ".png")
                .resizeDimen(widthResId, heightResId)
                .error(ERROR_DRAWABLE)
                .placeholder(PLACEHOLDER)
                .into(imageView);
    }

    private static int getWidth(Context context) {

        // Get screen size.
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int maxWidth = size.x;

        return maxWidth;
    }

    private static int getHeight(Context context) {

        // Get screen size.
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int maxHeight = size.y;

        int imageHeight;
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            imageHeight = maxHeight / 3;
        } else {
            imageHeight = maxHeight;
        }

        return imageHeight;
    }
}
