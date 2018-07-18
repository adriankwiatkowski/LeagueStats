package com.example.android.leaguestats;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.leaguestats.database.Contract;
import com.example.android.leaguestats.utilities.DataUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Arrays;
import java.util.List;

public class ChampionTipsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = ChampionTipsFragment.class.getSimpleName();
    private static final String HTTP_ENTRY_URL_SPLASH_ART = "http://ddragon.leagueoflegends.com/cdn/img/champion/splash";
    private static final String DB_SIGN = " = ?";
    private ScrollView mRootLayout;
    private TextView mTipsLabelTv;
    private TextView mPlayingAsTv;
    private TextView mPlayingAgainstTv;
    private TextView mPlayingAsLabelTv;
    private TextView mPlayingAgainstLabelTv;
    private ImageView mSplashArtImage;
    private List<String> mAsTipsArray;
    private List<String> mAgainstTipsArray;
    private Target mTarget;
    private Uri mCurrentChampionUri;
    private static final int CHAMPION_LOADER = 2;
    private static final String CHAMPION_URI_KEY = "CHAMPION_URI_KEY";

    public ChampionTipsFragment() {
    }

    public static ChampionTipsFragment newInstance(Uri championUri) {

        ChampionTipsFragment championTipsFragment = new ChampionTipsFragment();

        Bundle args = new Bundle();
        args.putParcelable(CHAMPION_URI_KEY, championUri);
        championTipsFragment.setArguments(args);

        return championTipsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(CHAMPION_URI_KEY)) {
            mCurrentChampionUri = bundle.getParcelable(CHAMPION_URI_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_champion_tips, container, false);
        Log.d(LOG_TAG, "onCreateView");

        mRootLayout = rootView.findViewById(R.id.strategy_root_layout);
        mTipsLabelTv = rootView.findViewById(R.id.tips_label_tv);
        mPlayingAsTv = rootView.findViewById(R.id.playing_as_tv);
        mPlayingAgainstTv = rootView.findViewById(R.id.playing_against_tv);
        mPlayingAsLabelTv = rootView.findViewById(R.id.playing_as_label_tv);
        mPlayingAgainstLabelTv = rootView.findViewById(R.id.playing_against_label_tv);
        mSplashArtImage = rootView.findViewById(R.id.splash_art_image);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "onActivityCreated");

        setUpTarget();
        if (mCurrentChampionUri == null) {
            Log.d(LOG_TAG, "ChampionUri is null");
        } else {
            getActivity().getSupportLoaderManager().initLoader(CHAMPION_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String championId = String.valueOf(ContentUris.parseId(mCurrentChampionUri));

        final String[] PROJECTION = {
                Contract.ChampionEntry._ID,
                Contract.ChampionEntry.COLUMN_SPLASH_ART,
                Contract.ChampionEntry.COLUMN_SPLASH_ART_NAME,
                Contract.ChampionEntry.COLUMN_CHAMPION_NAME,
                Contract.ChampionEntry.COLUMN_ENEMY_TIPS,
                Contract.ChampionEntry.COLUMN_ALLY_TIPS};

        return new CursorLoader(getActivity(),
                mCurrentChampionUri,
                PROJECTION,
                Contract.ChampionEntry._ID + DB_SIGN,
                new String[]{championId},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {

            String championSplashArtString = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_SPLASH_ART));
            List<String> splashArtArray = Arrays.asList(championSplashArtString.split(DataUtils.STRING_DIVIDER));

            // Default Splash Art.
            String splashArtString = splashArtArray.get(0);

            // Get screen size.
            Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int maxWidth = size.x;
            int maxHeight = size.y;

            int imageHeight;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                imageHeight = maxHeight / 3;
            } else {
                imageHeight = maxHeight;
            }

            // Load Default Splash Art.
            Picasso.get()
                    .load(HTTP_ENTRY_URL_SPLASH_ART + "/" + splashArtString)
                    .resize(maxWidth, imageHeight)
                    .centerCrop()
                    .error(R.drawable.ic_launcher_background)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(mTarget);

            String championName = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_CHAMPION_NAME));
            mPlayingAsLabelTv.setText(getString(R.string.playing_as) + " " + championName);

            String allyTipsString = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_ALLY_TIPS));
            mAsTipsArray = Arrays.asList(allyTipsString.split(DataUtils.STRING_DIVIDER));

            for (int i = 0; i < mAsTipsArray.size(); i++) {

                if (i != mAsTipsArray.size() - 1) {
                    mPlayingAsTv.append(String.valueOf(i + 1) + " " + mAsTipsArray.get(i) + "\n");
                } else {
                    mPlayingAsTv.append(String.valueOf(i + 1) + " " + mAsTipsArray.get(i));
                }
            }

            mPlayingAgainstLabelTv.setText(getString(R.string.playing_against) + " " + championName);

            String enemyTipsString = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_ENEMY_TIPS));
            mAgainstTipsArray = Arrays.asList(enemyTipsString.split(DataUtils.STRING_DIVIDER));

            for (int i = 0; i < mAgainstTipsArray.size(); i++) {

                if (i != mAgainstTipsArray.size() - 1) {
                    mPlayingAgainstTv.append(String.valueOf(i + 1) + " " + mAgainstTipsArray.get(i) + "\n");
                } else {
                    mPlayingAgainstTv.append(String.valueOf(i + 1) + " " + mAgainstTipsArray.get(i));
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPlayingAsLabelTv.setText("");
        mPlayingAsTv.setText("");
        mPlayingAgainstLabelTv.setText("");
        mPlayingAgainstTv.setText("");
        mAsTipsArray.clear();
        mAgainstTipsArray.clear();
    }

    private void setUpTarget() {
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

                                mRootLayout.setBackgroundColor(textSwatch.getRgb());

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
