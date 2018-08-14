package com.example.android.leaguestats;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.leaguestats.ViewModels.ChampionViewModelShared;
import com.example.android.leaguestats.ViewModels.ChampionViewModelSharedFactory;
import com.example.android.leaguestats.room.AppDatabase;
import com.example.android.leaguestats.room.ChampionEntry;
import com.example.android.leaguestats.utilities.DataUtils;
import com.example.android.leaguestats.utilities.SplashArtUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Arrays;
import java.util.List;

public class ChampionTipsFragment extends Fragment {

    private static final String LOG_TAG = ChampionTipsFragment.class.getSimpleName();
    private static final String HTTP_ENTRY_URL_SPLASH_ART = "http://ddragon.leagueoflegends.com/cdn/img/champion/splash";
    private TextView mTipsLabelTv;
    private TextView mPlayingAsTv;
    private TextView mPlayingAgainstTv;
    private TextView mPlayingAsLabelTv;
    private TextView mPlayingAgainstLabelTv;
    private ImageView mSplashArtImage;
    private Target mTarget;
    private AppDatabase mDb;

    public ChampionTipsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_champion_tips, container, false);
        Log.d(LOG_TAG, "onCreateView");

        mTipsLabelTv = rootView.findViewById(R.id.tips_label_tv);
        mPlayingAsTv = rootView.findViewById(R.id.playing_as_tv);
        mPlayingAgainstTv = rootView.findViewById(R.id.playing_against_tv);
        mPlayingAsLabelTv = rootView.findViewById(R.id.playing_as_label_tv);
        mPlayingAgainstLabelTv = rootView.findViewById(R.id.playing_against_label_tv);
        mSplashArtImage = rootView.findViewById(R.id.splash_art_tips_image);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "onActivityCreated");

        setupTarget();
        mDb = AppDatabase.getInstance(getActivity().getApplicationContext());
        setupViewModel();
    }

    private void setupViewModel() {
        ChampionViewModelSharedFactory factory = new ChampionViewModelSharedFactory(mDb);
        final ChampionViewModelShared viewModel =
                ViewModelProviders.of(getActivity(), factory).get(ChampionViewModelShared.class);
        viewModel.getSelected().observe(this, new Observer<ChampionEntry>() {
            @Override
            public void onChanged(@Nullable ChampionEntry championEntry) {
                viewModel.getSelected().removeObserver(this);
                Log.d(LOG_TAG, "Receiving database update from LiveData");
                updateUi(championEntry);
            }
        });
    }

    private void updateUi(ChampionEntry championEntry) {
        // Get Default Splash Art.
        String splashArt = championEntry.getSplashArt().get(0);

        int splashArtWidth = SplashArtUtils.getWidth(getContext());
        int splashArtHeight = SplashArtUtils.getHeight(getContext());

        // Load Default Splash Art.
        Picasso.get()
                .load(HTTP_ENTRY_URL_SPLASH_ART + "/" + splashArt)
                .resize(splashArtWidth, splashArtHeight)
                .centerCrop()
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(mTarget);

        mPlayingAgainstLabelTv.setText(getString(R.string.playing_vs) + " " + championEntry.getName());

        List<String> vsTips = championEntry.getVsTips();
        for (int i = 0; i < vsTips.size(); i++) {
            mPlayingAgainstTv.append(String.valueOf(i + 1) + " " + vsTips.get(i));
            if (i != vsTips.size() - 1) {
                mPlayingAgainstTv.append("\n");
            }
        }

        mPlayingAsLabelTv.setText(getString(R.string.playing_as) + " " + championEntry.getName());

        List<String> asTips = championEntry.getAsTips();
        for (int i = 0; i < asTips.size(); i++) {
            mPlayingAsTv.append(String.valueOf(i + 1) + " " + asTips.get(i));
            if (i != asTips.size() - 1) {
                mPlayingAsTv.append("\n");
            }
        }
    }

    private void setupTarget() {
        mTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                mSplashArtImage.setImageBitmap(bitmap);

                Palette.from(bitmap)
                        .generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(@NonNull Palette palette) {

                                Palette.Swatch textSwatch = palette.getVibrantSwatch();

                                if (textSwatch == null) {
                                    return;
                                }

                                mTipsLabelTv.setTextColor(textSwatch.getTitleTextColor());

                                mPlayingAgainstLabelTv.setTextColor(textSwatch.getTitleTextColor());
                                mPlayingAsLabelTv.setTextColor(textSwatch.getTitleTextColor());

                                mPlayingAgainstTv.setTextColor(textSwatch.getBodyTextColor());
                                mPlayingAsTv.setTextColor(textSwatch.getBodyTextColor());
                            }
                        });
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                mSplashArtImage.setImageDrawable(errorDrawable);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                mSplashArtImage.setImageDrawable(placeHolderDrawable);
            }
        };
    }
}
