package com.example.android.leaguestats.data.glide;

import android.support.annotation.NonNull;

import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.leaguestats.R;

@GlideExtension
public class LeagueGlideExtension {

    private LeagueGlideExtension() {}

    @GlideOption
    @NonNull
    public static RequestOptions roundedImage(RequestOptions options) {
        return options
                .placeholder(R.drawable.ic_launcher_background)
                .circleCrop()
                .transforms(new RoundedCorners(300));
    }

    @GlideOption
    @NonNull
    public static RequestOptions splashArt(RequestOptions options) {
        return options
                .placeholder(R.drawable.ic_launcher_background);
    }
}
