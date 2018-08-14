package com.example.android.leaguestats.utilities;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class SplashArtUtils {

    public static int getWidth(Context context) {

        // Get screen size.
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int maxWidth = size.x;

        return maxWidth;
    }

    public static int getHeight(Context context) {

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
