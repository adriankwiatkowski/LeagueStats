package com.example.android.leaguestats;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.leaguestats.database.Contract;
import com.example.android.leaguestats.database.Helper;
import com.example.android.leaguestats.interfaces.ChampionTaskCompleted;
import com.example.android.leaguestats.interfaces.NameTaskCompleted;
import com.example.android.leaguestats.models.Champion;
import com.example.android.leaguestats.utilities.ChampionsAsyncTask;
import com.example.android.leaguestats.utilities.Data;
import com.example.android.leaguestats.utilities.NameAsyncTask;
import com.example.android.leaguestats.utilities.QueryHandler;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private EditText mUserNameEdit;
    private Button mSubmitButton;
    private Spinner mRegionSpinner;
    private String mSummonerRegion;
    private ArrayList<Champion> mChampions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserNameEdit = findViewById(R.id.user_name_edit);
        mSubmitButton = findViewById(R.id.button_submit);
        mRegionSpinner = findViewById(R.id.region_spinner);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMasteryData();
            }
        });

        setupSpinner();

        mUserNameEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    getMasteryData();
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
                downloadChampionData("en_US");
                return true;
            case R.id.open_champion_list:
                openChampionList();
                return true;
            case R.id.download_pl_data:
                downloadChampionData("pl_PL");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getMasteryData() {
        if (isNetworkAvailable()) {

            String summonerName = mUserNameEdit.getText().toString().trim();

            if (!TextUtils.isEmpty(summonerName)) {
                NameTaskCompleted taskCompleted = new NameTaskCompleted() {
                    @Override
                    public void nameTaskCompleted(String summonerId) {
                        Intent intent = new Intent(MainActivity.this, MasteryActivity.class);
                        intent.putExtra(getString(R.string.summoner_region_key), mSummonerRegion);
                        intent.putExtra(getString(R.string.summoner_id_key), summonerId);
                        startActivity(intent);
                    }
                };

                NameAsyncTask nameAsyncTask = new NameAsyncTask(taskCompleted);
                nameAsyncTask.execute(mSummonerRegion, summonerName);
            } else {
                Toast.makeText(this, "Please enter name...", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No internet connection found.", Toast.LENGTH_LONG).show();
        }
    }

    private void downloadChampionData(String language) {

        dropTable();

        if (isNetworkAvailable()) {

            mChampions = new ArrayList<>();

            ChampionTaskCompleted championTaskCompleted = new ChampionTaskCompleted() {
                @Override
                public void championTaskCompleted(ArrayList<Champion> champion) {
                    mChampions = champion;

                    Data.saveChampionData(MainActivity.this, mChampions);

                    Toast.makeText(MainActivity.this, "Data has been downloaded.", Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, String.valueOf(mChampions.size()));
                }
            };

            ChampionsAsyncTask championsAsyncTask = new ChampionsAsyncTask(championTaskCompleted);
            championsAsyncTask.execute(language);
        } else {
            Toast.makeText(this, "No internet connection found.", Toast.LENGTH_LONG).show();
        }
    }

    private void openChampionList() {
        Intent intent = new Intent(MainActivity.this, ChampionListActivity.class);
        startActivity(intent);
    }

    private void dropTable() {
        Helper helper = new Helper(this);
        SQLiteDatabase database = helper.getWritableDatabase();
        database.execSQL("DROP TABLE IF EXISTS " + Contract.ChampionEntry.TABLE_NAME);
        database.execSQL(Helper.SQL_CREATE_TABLE);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = ((ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}