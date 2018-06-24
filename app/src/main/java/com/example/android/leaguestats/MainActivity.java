package com.example.android.leaguestats;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.leaguestats.database.Contract;
import com.example.android.leaguestats.database.Helper;
import com.example.android.leaguestats.interfaces.ChampionTaskCompleted;
import com.example.android.leaguestats.interfaces.IconTaskCompleted;
import com.example.android.leaguestats.interfaces.ResultTask;
import com.example.android.leaguestats.models.Champion;
import com.example.android.leaguestats.models.Icon;
import com.example.android.leaguestats.utilities.ChampionsAsyncTask;
import com.example.android.leaguestats.utilities.Data;
import com.example.android.leaguestats.utilities.IconAsyncTask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private EditText mUserNameEdit;
    private Button mSubmitButton;
    private Spinner mRegionSpinner;
    private String mSummonerRegion;
    private ArrayList<Champion> mChampionsList;
    private List<Icon> mIconList;
    private ProgressBar mChampionIndicator;
    private ProgressBar mIconIndicator;
    private TextView mChampionIndicatorTv;
    private TextView mIconIndicatorTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserNameEdit = findViewById(R.id.user_name_edit);
        mSubmitButton = findViewById(R.id.button_submit);
        mRegionSpinner = findViewById(R.id.region_spinner);
        mChampionIndicator = findViewById(R.id.champion_indicator);
        mChampionIndicatorTv = findViewById(R.id.champion_indicator_tv);
        mIconIndicator = findViewById(R.id.icon_indicator);
        mIconIndicatorTv = findViewById(R.id.icon_indicator_tv);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMastery();
            }
        });

        setupSpinner();

        mUserNameEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    openMastery();
                    return true;
                }
                return false;
            }
        });
    }

    private void setupSpinner() {
        ArrayAdapter regionSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.string_region_array, android.R.layout.simple_spinner_item);

        regionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mRegionSpinner.setAdapter(regionSpinnerAdapter);

        mRegionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    mSummonerRegion = Data.ENTRY_URL_SUMMONER_ARRAY[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(MainActivity.this, "Please select region to continue...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openMastery() {

        final String summonerName = mUserNameEdit.getText().toString().trim();

        Intent intent = new Intent(MainActivity.this, MasteryActivity.class);

        intent.putExtra(getString(R.string.summoner_region_key), mSummonerRegion);
        intent.putExtra(getString(R.string.summoner_name_key), summonerName);

        startActivity(intent);
    }

    private void showIndicator() {
        mUserNameEdit.setVisibility(View.INVISIBLE);
        mSubmitButton.setVisibility(View.INVISIBLE);
        mRegionSpinner.setVisibility(View.INVISIBLE);
        mChampionIndicator.setVisibility(View.VISIBLE);
        mChampionIndicatorTv.setVisibility(View.VISIBLE);
        mIconIndicator.setVisibility(View.VISIBLE);
        mIconIndicatorTv.setVisibility(View.VISIBLE);
    }

    private void hideIndicator(View... views) {

        for (View view : views) {
            view.setVisibility(View.INVISIBLE);
        }

        int invisible = View.INVISIBLE;

        if (mChampionIndicator.getVisibility() == invisible && mChampionIndicatorTv.getVisibility() == invisible
                && mIconIndicator.getVisibility() == invisible && mIconIndicatorTv.getVisibility() == invisible) {
            mUserNameEdit.setVisibility(View.VISIBLE);
            mSubmitButton.setVisibility(View.VISIBLE);
            mRegionSpinner.setVisibility(View.VISIBLE);
        }
    }

    private void downloadStaticData(String language) {

        if (isNetworkAvailable()) {

            dropTable();

            downloadChampionData(language);
            downloadIconData();

            showIndicator();
        } else {
            Toast.makeText(this, "No internet connection found.", Toast.LENGTH_LONG).show();
        }
    }

    private void downloadChampionData(String language) {

        mChampionsList = new ArrayList<>();

        ChampionTaskCompleted championTaskCompleted = new ChampionTaskCompleted() {
            @Override
            public void championTaskCompleted(ArrayList<Champion> champion) {

                mChampionsList = champion;

                Data.saveChampionData(MainActivity.this, mChampionsList);

                Toast.makeText(MainActivity.this, "Data has been downloaded.", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, String.valueOf(mChampionsList.size()));

                hideIndicator(mChampionIndicator, mChampionIndicatorTv);
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

                mIconList = icons;

                Data.saveIconData(MainActivity.this, mIconList);

                hideIndicator(mIconIndicator, mIconIndicatorTv);
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

    private void openChampionList() {
        Intent intent = new Intent(MainActivity.this, ChampionListActivity.class);
        startActivity(intent);
    }

    private void dropTable() {
        Helper helper = new Helper(this);
        SQLiteDatabase database = helper.getWritableDatabase();
        database.execSQL("DROP TABLE IF EXISTS " + Contract.ChampionEntry.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.IconEntry.TABLE_NAME);
        database.execSQL(Helper.SQL_CREATE_CHAMPION_TABLE);
        database.execSQL(Helper.SQL_CREATE_ICON_TABLE);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = ((ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.download_data:
                downloadStaticData("en_US");
                return true;
            case R.id.open_champion_list:
                openChampionList();
                return true;
            case R.id.download_pl_data:
                downloadStaticData("pl_PL");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}