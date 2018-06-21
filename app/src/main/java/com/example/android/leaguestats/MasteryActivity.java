package com.example.android.leaguestats;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.leaguestats.adapters.MasteryAdapter;
import com.example.android.leaguestats.database.Contract;
import com.example.android.leaguestats.interfaces.MasteryTaskCompleted;
import com.example.android.leaguestats.models.Mastery;
import com.example.android.leaguestats.utilities.MasteryAsyncTask;

import java.util.ArrayList;

public class MasteryActivity extends AppCompatActivity {

    private static final String LOG_TAG = MasteryActivity.class.getSimpleName();
    private ListView mListView;
    private MasteryAdapter mAdapter;
    private long championId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mastery);

        mListView = findViewById(R.id.list_view);
        mAdapter = new MasteryAdapter(getApplicationContext(), new ArrayList<Mastery>());
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                championId = mAdapter.getItem(position).getChampionId();

                Intent intent = new Intent(MasteryActivity.this, ChampionDetailActivity.class);

                Uri contentChampionUri = Contract.ChampionEntry.buildChampionUri(championId);

                intent.setData(contentChampionUri);

                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        String summonerRegion = intent.getStringExtra(getString(R.string.summoner_region_key));
        String summonerId = intent.getStringExtra(getString(R.string.summoner_id_key));

        getData(summonerRegion, summonerId);
    }

    private void getData(String summonerRegion, String summonerId) {
        MasteryTaskCompleted masteryTaskCompleted = new MasteryTaskCompleted() {
            @Override
            public void masteryTaskCompleted(ArrayList<Mastery> masteries) {
                mAdapter.setData(masteries);
            }
        };

        MasteryAsyncTask masteriesAsyncTask = new MasteryAsyncTask(masteryTaskCompleted);
        masteriesAsyncTask.execute(summonerRegion, summonerId);
    }
}