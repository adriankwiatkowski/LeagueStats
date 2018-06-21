package com.example.android.leaguestats;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.leaguestats.adapters.MasteryAdapter;
import com.example.android.leaguestats.database.Contract;
import com.example.android.leaguestats.database.Helper;
import com.example.android.leaguestats.interfaces.MasteryTaskCompleted;
import com.example.android.leaguestats.interfaces.QueryTaskCompleted;
import com.example.android.leaguestats.models.Mastery;
import com.example.android.leaguestats.utilities.MasteryAsyncTask;
import com.example.android.leaguestats.utilities.QueryAsyncTask;

import java.util.ArrayList;
import java.util.List;

public class MasteryActivity extends AppCompatActivity
        implements MasteryAdapter.ListItemClickListener {

    private static final String LOG_TAG = MasteryActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MasteryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mastery);

        mRecyclerView = findViewById(R.id.mastery_recycler_view);
        mAdapter = new MasteryAdapter(getApplicationContext(), new ArrayList<Mastery>(), MasteryActivity.this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MasteryActivity.this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        Intent intent = getIntent();
        String summonerRegion = intent.getStringExtra(getString(R.string.summoner_region_key));
        String summonerId = intent.getStringExtra(getString(R.string.summoner_id_key));

        getData(summonerRegion, summonerId);
    }

    private void getData(String summonerRegion, String summonerId) {
        MasteryTaskCompleted masteryTaskCompleted = new MasteryTaskCompleted() {
            @Override
            public void masteryTaskCompleted(ArrayList<Mastery> masteries) {

                QueryTaskCompleted queryTaskCompleted = new QueryTaskCompleted() {
                    @Override
                    public void queryTaskCompleted(List<Mastery> queriedMasteries) {
                        mAdapter.swapData(queriedMasteries);
                    }
                };
                SQLiteDatabase db = new Helper(MasteryActivity.this).getReadableDatabase();
                QueryAsyncTask queryAsyncTask = new QueryAsyncTask(queryTaskCompleted, db);
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
}
