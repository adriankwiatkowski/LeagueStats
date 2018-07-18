package com.example.android.leaguestats;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.leaguestats.database.Contract;
import com.example.android.leaguestats.database.Helper;
import com.example.android.leaguestats.interfaces.ChampionTaskCompleted;
import com.example.android.leaguestats.interfaces.IconTaskCompleted;
import com.example.android.leaguestats.interfaces.ItemTaskCompleted;
import com.example.android.leaguestats.interfaces.PatchTaskCompleted;
import com.example.android.leaguestats.interfaces.ResultTask;
import com.example.android.leaguestats.interfaces.SummonerSpellTaskCompleted;
import com.example.android.leaguestats.models.Champion;
import com.example.android.leaguestats.models.Icon;
import com.example.android.leaguestats.models.Item;
import com.example.android.leaguestats.models.SummonerSpell;
import com.example.android.leaguestats.utilities.AsyncTasks.ChampionsAsyncTask;
import com.example.android.leaguestats.utilities.AsyncTasks.SummonerSpellAsyncTask;
import com.example.android.leaguestats.utilities.DataUtils;
import com.example.android.leaguestats.utilities.AsyncTasks.IconAsyncTask;
import com.example.android.leaguestats.utilities.AsyncTasks.ItemAsyncTask;
import com.example.android.leaguestats.utilities.LocaleUtils;
import com.example.android.leaguestats.utilities.AsyncTasks.PatchAsyncTask;
import com.example.android.leaguestats.utilities.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private Button buttonPl, buttonEn;
    private TextView mSelectLanguageTv;
    private List<Champion> mChampionList;
    private List<Icon> mIconList;
    private List<Item> mItemList;
    private List<SummonerSpell> mSummonerSpells;
    private ProgressBar mChampionIndicator;
    private ProgressBar mIconIndicator;
    private ProgressBar mPatchIndicator;
    private ProgressBar mItemIndicator;
    private ProgressBar mSummonerSpellIndicator;
    private TextView mChampionIndicatorTv;
    private TextView mIconIndicatorTv;
    private TextView mPatchIndicatorTv;
    private TextView mItemIndicatorTv;
    private TextView mSummonerSpellIndicatorTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSelectLanguageTv = findViewById(R.id.select_language_tv);
        mChampionIndicator = findViewById(R.id.champion_indicator);
        mChampionIndicatorTv = findViewById(R.id.champion_indicator_tv);
        mIconIndicator = findViewById(R.id.icon_indicator);
        mIconIndicatorTv = findViewById(R.id.icon_indicator_tv);
        mPatchIndicator = findViewById(R.id.patch_version_indicator);
        mPatchIndicatorTv = findViewById(R.id.patch_version_indicator_tv);
        mItemIndicator = findViewById(R.id.item_indicator);
        mItemIndicatorTv = findViewById(R.id.item_indicator_tv);
        mSummonerSpellIndicator = findViewById(R.id.summoner_spell_indicator);
        mSummonerSpellIndicatorTv = findViewById(R.id.summoner_spell_indicator_tv);

        checkIfLanguageIsSaved();

        buttonPl = findViewById(R.id.buttonPl);
        buttonEn = findViewById(R.id.buttonEn);

        buttonPl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocaleUtils.setLocale(getBaseContext(), "pl");
                PreferencesUtils.saveUserLanguage(getBaseContext(), "pl");
                downloadStaticData("pl_PL");
            }
        });

        buttonEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocaleUtils.setLocale(getBaseContext(), "en");
                PreferencesUtils.saveUserLanguage(getBaseContext(), "en");
                downloadStaticData("en_US");
            }
        });
    }

    private void checkIfLanguageIsSaved() {
        Intent intent = getIntent();
        if (!intent.hasExtra(getString(R.string.change_language_key))) {
            boolean isSaved = LocaleUtils.didUserSelectedLanguage(getBaseContext());
            if (isSaved) {
                startActivity(new Intent(MainActivity.this, NavigationActivity.class));
                // Prevent to go back to the previous activity
                finish();
            }
        }
    }

    private void downloadStaticData(String language) {

        if (isNetworkAvailable()) {

            dropTable();

            String patchVersion = PreferencesUtils.getPatchVersion(this);

            downloadChampionData(language);
            downloadIconData();
            downloadPatchVersion();
            downloadItemData(language);
            downloadSummonerSpellData(language, patchVersion);

            showIndicator();
        } else {
            Toast.makeText(this, "No internet connection found.", Toast.LENGTH_LONG).show();
        }
    }

    private void downloadItemData(String language) {

        mItemList = new ArrayList<>();

        ItemTaskCompleted itemTaskCompleted = new ItemTaskCompleted() {
            @Override
            public void itemTaskCompleted(List<Item> items) {
                if (!(items == null || items.isEmpty())) {
                    mItemList = items;

                    DataUtils.saveItemData(MainActivity.this, mItemList);
                }

                hideIndicator(mItemIndicator, mItemIndicatorTv);
            }
        };

        ResultTask resultTask = new ResultTask() {
            @Override
            public void resultTask(int progress) {
                mItemIndicator.setProgress(progress);
                mItemIndicatorTv.setText(String.valueOf(progress) + "/" + mItemIndicator.getMax());
            }

            @Override
            public void maxProgress(int max) {
                mItemIndicator.setMax(max);
            }
        };

        ItemAsyncTask itemAsyncTask = new ItemAsyncTask(itemTaskCompleted, resultTask);
        itemAsyncTask.execute(language);
    }

    private void downloadChampionData(String language) {

        mChampionList = new ArrayList<>();

        ChampionTaskCompleted championTaskCompleted = new ChampionTaskCompleted() {
            @Override
            public void championTaskCompleted(List<Champion> champion) {

                if (!(champion == null || champion.isEmpty())) {
                    mChampionList = champion;

                    DataUtils.saveChampionData(MainActivity.this, mChampionList);

                    Log.d(LOG_TAG, String.valueOf(mChampionList.size()));

                    hideIndicator(mChampionIndicator, mChampionIndicatorTv);
                }
            }
        };

        ResultTask resultTask = new ResultTask() {
            @Override
            public void resultTask(int progress) {
                mChampionIndicator.setProgress(progress);
                mChampionIndicatorTv.setText(String.valueOf(progress) + "/" + mChampionIndicator.getMax());
            }

            @Override
            public void maxProgress(int max) {
                mChampionIndicator.setMax(max);
            }
        };

        ChampionsAsyncTask championsAsyncTask = new ChampionsAsyncTask(championTaskCompleted, resultTask);
        championsAsyncTask.execute(language);
    }

    private void downloadIconData() {

        mIconList = new ArrayList<>();

        IconTaskCompleted iconTaskCompleted = new IconTaskCompleted() {
            @Override
            public void iconTaskCompleted(List<Icon> icons) {
                if (!(icons == null || icons.isEmpty())) {

                    mIconList = icons;

                    DataUtils.saveIconData(MainActivity.this, mIconList);

                    hideIndicator(mIconIndicator, mIconIndicatorTv);
                }
            }

        };

        ResultTask resultTask = new ResultTask() {
            @Override
            public void resultTask(int progress) {
                mIconIndicator.setProgress(progress);
                mIconIndicatorTv.setText(String.valueOf(progress) + "/" + mIconIndicator.getMax());
            }

            @Override
            public void maxProgress(int max) {
                mIconIndicator.setMax(max);
            }
        };

        IconAsyncTask iconAsyncTask = new IconAsyncTask(iconTaskCompleted, resultTask);
        iconAsyncTask.execute();
    }

    private void downloadPatchVersion() {
        PatchTaskCompleted patchTaskCompleted = new PatchTaskCompleted() {
            @Override
            public void patchTaskCompleted(String patchVersion) {
                if (!TextUtils.isEmpty(patchVersion)) {
                    SharedPreferences preferences = getSharedPreferences(
                            getString(R.string.preferences_file_name), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(getString(R.string.patch_version_key), patchVersion);
                    editor.apply();

                    hideIndicator(mPatchIndicator, mPatchIndicatorTv);
                }
            }
        };

        ResultTask resultTask = new ResultTask() {
            @Override
            public void resultTask(int progress) {
                mPatchIndicator.setProgress(progress);
                mPatchIndicatorTv.setText(String.valueOf(progress) + "/" + mPatchIndicator.getMax());
            }

            @Override
            public void maxProgress(int max) {
                mPatchIndicator.setMax(max);
            }
        };

        PatchAsyncTask patchAsyncTask = new PatchAsyncTask(patchTaskCompleted, resultTask);
        patchAsyncTask.execute();
    }

    private void downloadSummonerSpellData(String language, String patchVersion) {

        mSummonerSpells = new ArrayList<>();

        SummonerSpellTaskCompleted summonerSpellTaskCompleted = new SummonerSpellTaskCompleted() {
            @Override
            public void summonerSpellTaskCompleted(List<SummonerSpell> summonerSpells) {
                if (!(summonerSpells == null || summonerSpells.isEmpty())) {

                    mSummonerSpells = summonerSpells;

                    DataUtils.saveSummonerSpellData(MainActivity.this, mSummonerSpells);

                    hideIndicator(mSummonerSpellIndicator, mSummonerSpellIndicatorTv);
                }
            }
        };

        ResultTask resultTask = new ResultTask() {
            @Override
            public void resultTask(int progress) {
                mSummonerSpellIndicator.setProgress(progress);
                mSummonerSpellIndicatorTv.setText(String.valueOf(progress) + "/" + mSummonerSpellIndicator.getMax());
            }

            @Override
            public void maxProgress(int max) {
                mSummonerSpellIndicator.setMax(max);
            }
        };

        SummonerSpellAsyncTask summonerSpellAsyncTask = new SummonerSpellAsyncTask(summonerSpellTaskCompleted, resultTask);
        summonerSpellAsyncTask.execute(language, patchVersion);
    }

    private void dropTable() {
        Helper helper = new Helper(this);
        SQLiteDatabase database = helper.getWritableDatabase();
        database.execSQL("DROP TABLE IF EXISTS " + Contract.ChampionEntry.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.IconEntry.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.ItemEntry.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.SummonerSpellEntry.TABLE_NAME);
        database.execSQL(Helper.SQL_CREATE_CHAMPION_TABLE);
        database.execSQL(Helper.SQL_CREATE_ICON_TABLE);
        database.execSQL(Helper.SQL_CREATE_ITEM_TABLE);
        database.execSQL(Helper.SQL_CREATE_SUMMONER_SPELL_TABLE);
    }

    private void showIndicator() {
        buttonPl.setVisibility(View.INVISIBLE);
        buttonEn.setVisibility(View.INVISIBLE);
        mSelectLanguageTv.setVisibility(View.INVISIBLE);
        mChampionIndicator.setVisibility(View.VISIBLE);
        mChampionIndicatorTv.setVisibility(View.VISIBLE);
        mIconIndicator.setVisibility(View.VISIBLE);
        mIconIndicatorTv.setVisibility(View.VISIBLE);
        mPatchIndicator.setVisibility(View.VISIBLE);
        mPatchIndicatorTv.setVisibility(View.VISIBLE);
        mItemIndicator.setVisibility(View.VISIBLE);
        mItemIndicatorTv.setVisibility(View.VISIBLE);
        mSummonerSpellIndicator.setVisibility(View.VISIBLE);
        mSummonerSpellIndicatorTv.setVisibility(View.VISIBLE);
    }

    private void hideIndicator(View... views) {

        for (View view : views) {
            view.setVisibility(View.INVISIBLE);
        }

        if (isInvisible(mChampionIndicator) && isInvisible(mChampionIndicatorTv)
                && isInvisible(mIconIndicator) && isInvisible(mIconIndicatorTv)
                && isInvisible(mPatchIndicator) && isInvisible(mPatchIndicatorTv)
                && isInvisible(mItemIndicator) && isInvisible(mItemIndicatorTv)
                && isInvisible(mSummonerSpellIndicator) && isInvisible(mSummonerSpellIndicatorTv)) {

            // Start NavigationActivity.
            startActivity(new Intent(MainActivity.this, NavigationActivity.class));
            // Prevent to go back to the previous activity
            finish();
        }
    }

    private boolean isInvisible(View view) {
        int invisible = View.INVISIBLE;
        return view.getVisibility() == invisible;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = ((ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
