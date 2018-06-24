package com.example.android.leaguestats;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.leaguestats.adapters.MasteryAdapter;
import com.example.android.leaguestats.database.Contract;
import com.example.android.leaguestats.database.Helper;
import com.example.android.leaguestats.interfaces.MasteryTaskCompleted;
import com.example.android.leaguestats.interfaces.QueryTaskCompleted;
import com.example.android.leaguestats.interfaces.SummonerTaskCompleted;
import com.example.android.leaguestats.models.Mastery;
import com.example.android.leaguestats.models.Summoner;
import com.example.android.leaguestats.utilities.MasteryAsyncTask;
import com.example.android.leaguestats.utilities.MasteryQueryAsyncTask;
import com.example.android.leaguestats.utilities.SummonerAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MasteryActivity extends AppCompatActivity
        implements MasteryAdapter.ListItemClickListener {

    private static final String LOG_TAG = MasteryActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MasteryAdapter mAdapter;
    private TextView mSummonerNameTv;
    private TextView mSummonerLevelTv;
    private ImageView mProfileIcon;
    private int mProfileIconId;
    private long mSummonerLevel;
    private long mSummonerId;
    private String mSummonerName;
    private String mEntryUrl;
    private ProgressBar mRecyclerIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mastery);

        mRecyclerView = findViewById(R.id.mastery_recycler_view);
        mAdapter = new MasteryAdapter(getApplicationContext(), new ArrayList<Mastery>(), MasteryActivity.this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MasteryActivity.this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mSummonerNameTv = findViewById(R.id.mastery_name_tv);
        mSummonerLevelTv = findViewById(R.id.mastery_level_tv);
        mProfileIcon = findViewById(R.id.mastery_profile);
        mRecyclerIndicator = findViewById(R.id.mastery_recycler_indicator);

        getMasteryData();
    }

    private void getMasteryData() {
        Intent intent = getIntent();
        mEntryUrl = intent.getStringExtra(getString(R.string.summoner_region_key));
        mSummonerName = intent.getStringExtra(getString(R.string.summoner_name_key));

        if (isNetworkAvailable()) {

            if (!TextUtils.isEmpty(mSummonerName)) {
                SummonerTaskCompleted taskCompleted = new SummonerTaskCompleted() {
                    @Override
                    public void summonerTaskCompleted(Summoner summoner) {
                        mProfileIconId = summoner.getProfileIconId();
                        mSummonerLevel = summoner.getSummonerLevel();
                        mSummonerId = summoner.getSummonerId();

                        mSummonerNameTv.setText(mSummonerName);
                        mSummonerLevelTv.setText(String.valueOf(mSummonerLevel) + getString(R.string.level));
                        Picasso.get()
                                .load("http://ddragon.leagueoflegends.com/cdn/8.11.1/img/profileicon/" + String.valueOf(mProfileIconId) + ".png")
                                .resize(100, 100)
                                .error(R.drawable.ic_launcher_background)
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .into(mProfileIcon);

                        getDataFromSql(mEntryUrl, String.valueOf(mSummonerId));

                    }
                };

                SummonerAsyncTask summonerAsyncTask = new SummonerAsyncTask(taskCompleted);
                summonerAsyncTask.execute(mEntryUrl, mSummonerName);
            } else {
                Toast.makeText(this, "Please enter name...", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No internet connection found.", Toast.LENGTH_LONG).show();
        }
    }

    private void getDataFromSql(String summonerRegion, String summonerId) {

        mRecyclerIndicator.setVisibility(View.VISIBLE);

        MasteryTaskCompleted masteryTaskCompleted = new MasteryTaskCompleted() {
            @Override
            public void masteryTaskCompleted(ArrayList<Mastery> masteries) {

                QueryTaskCompleted queryTaskCompleted = new QueryTaskCompleted() {
                    @Override
                    public void queryTaskCompleted(List<Mastery> queriedMasteries) {
                        mAdapter.swapData(queriedMasteries);
                        mRecyclerIndicator.setVisibility(View.INVISIBLE);
                    }
                };

                SQLiteDatabase db = new Helper(MasteryActivity.this).getReadableDatabase();
                MasteryQueryAsyncTask queryAsyncTask = new MasteryQueryAsyncTask(queryTaskCompleted, db);

                queryAsyncTask.execute(masteries);
            }
        };

        MasteryAsyncTask masteriesAsyncTask = new MasteryAsyncTask(masteryTaskCompleted);
        masteriesAsyncTask.execute(summonerRegion, summonerId);
    }

    @Override
    public void onListItemClick(int position, long championId) {

        Intent intent = new Intent(MasteryActivity.this, ChampionDetailActivity.class);

        Uri contentChampionUri = Contract.ChampionEntry.buildChampionUri(championId);

        intent.setData(contentChampionUri);

        startActivity(intent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = ((ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
