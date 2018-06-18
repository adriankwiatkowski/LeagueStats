package com.example.android.leaguestats;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.leaguestats.database.Contract;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Arrays;
import java.util.List;

public class StrategyActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = StrategyActivity.class.getSimpleName();
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
    private final int CHAMPION_LOADER = 0;
    private final String STRING_DIVIDER = "_,_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strategy);

        mRootLayout = findViewById(R.id.strategy_root_layout);
        mTipsLabelTv = findViewById(R.id.tips_label_tv);
        mPlayingAsTv = findViewById(R.id.playing_as_tv);
        mPlayingAgainstTv = findViewById(R.id.playing_against_tv);
        mPlayingAsLabelTv = findViewById(R.id.playing_as_label_tv);
        mPlayingAgainstLabelTv = findViewById(R.id.playing_against_label_tv);
        mSplashArtImage = findViewById(R.id.splash_art_image);

        Intent intent = getIntent();
        mCurrentChampionUri = intent.getData();

        setUpTarget();

        if (mCurrentChampionUri == null) {
            Log.d(LOG_TAG, "ChampionUri is null");
        } else {
            getSupportLoaderManager().initLoader(CHAMPION_LOADER, null, this);
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

        return new CursorLoader(this,
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
            List<String> splashArtArray = Arrays.asList(championSplashArtString.split(STRING_DIVIDER));

            // Default Splash Art.
            String splashArtString = splashArtArray.get(0);

            // Load Default Splash Art.
            Picasso.get()
                    .load(HTTP_ENTRY_URL_SPLASH_ART + "/" + splashArtString)
                    .error(ContextCompat.getDrawable(this, R.drawable.ic_launcher_background))
                    .placeholder(R.color.colorPrimary)
                    .into(mTarget);

            String championName = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_CHAMPION_NAME));
            mPlayingAsLabelTv.setText(getString(R.string.playing_as) + " " + championName);

            String allyTipsString = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_ALLY_TIPS));
            mAsTipsArray = Arrays.asList(allyTipsString.split(STRING_DIVIDER));

            for (int i = 0; i < mAsTipsArray.size(); i++) {

                if (i != mAsTipsArray.size() - 1) {
                    mPlayingAsTv.append(String.valueOf(i + 1) + " " + mAsTipsArray.get(i) + "\n");
                } else {
                    mPlayingAsTv.append(String.valueOf(i + 1) + " " + mAsTipsArray.get(i));
                }
            }

            mPlayingAgainstLabelTv.setText(getString(R.string.playing_against) + " " + championName);

            String enemyTipsString = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_ENEMY_TIPS));
            mAgainstTipsArray = Arrays.asList(enemyTipsString.split(STRING_DIVIDER));

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
