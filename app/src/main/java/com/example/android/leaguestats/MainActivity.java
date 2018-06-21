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
import com.example.android.leaguestats.utilities.NameAsyncTask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private EditText mUserNameEdit;
    private Button mSubmitButton;
    private Spinner mRegionSpinner;
    private String mSummonerRegion;
    private ArrayList<Champion> mChampions;
    private final String STRING_DIVIDER = "_,_";

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
                String summonerName = mUserNameEdit.getText().toString().trim();

                getMasteryData(summonerName);
            }
        });

        setupSpinner();
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

    private void getMasteryData(String summonerName) {
        if (isNetworkAvailable()) {
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
            Toast.makeText(this, "No internet connection found.", Toast.LENGTH_LONG).show();
        }
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

    private void downloadChampionData(String language) {
        Helper helper = new Helper(this);
        SQLiteDatabase database = helper.getWritableDatabase();
        database.execSQL("DROP TABLE IF EXISTS " + Contract.ChampionEntry.TABLE_NAME);
        database.execSQL(Helper.SQL_CREATE_TABLE);

        if (isNetworkAvailable()) {
            mChampions = new ArrayList<>();
            ChampionTaskCompleted championTaskCompleted = new ChampionTaskCompleted() {
                @Override
                public void championTaskCompleted(ArrayList<Champion> champion) {
                    mChampions = champion;

                    saveChampionData();

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

    private void saveChampionData() {
        ContentValues values = new ContentValues();
        for (Champion champion : mChampions) {

            // Save Champion ID and Key.
            values.put(Contract.ChampionEntry._ID, champion.getId());
            values.put(Contract.ChampionEntry.COLUMN_KEY, champion.getKey());

            // Save Champion Base Info.
            values.put(Contract.ChampionEntry.COLUMN_CHAMPION_NAME, champion.getChampionName());
            values.put(Contract.ChampionEntry.COLUMN_CHAMPION_TITLE, champion.getChampionTitle());
            values.put(Contract.ChampionEntry.COLUMN_CHAMPION_LORE, champion.getChampionLore());

            // Save Thumbnail.
            values.put(Contract.ChampionEntry.COLUMN_THUMBNAIL, champion.getThumbnail());

            // Save Splash Art Path.
            List<String> splashArtList = champion.getSplashArt();
            String splashArtString = stringListToString(splashArtList);
            values.put(Contract.ChampionEntry.COLUMN_SPLASH_ART, splashArtString);

            // Save Splash Art Name.
            List<String> splashArtNameList = champion.getSplashArtName();
            String splashArtNameString = stringListToString(splashArtNameList);
            values.put(Contract.ChampionEntry.COLUMN_SPLASH_ART_NAME, splashArtNameString);

            // Save Champion Info.
            values.put(Contract.ChampionEntry.COLUMN_DIFFICULTY, champion.getDifficulty());
            values.put(Contract.ChampionEntry.COLUMN_ATTACK, champion.getAttack());
            values.put(Contract.ChampionEntry.COLUMN_DEFENSE, champion.getDefense());
            values.put(Contract.ChampionEntry.COLUMN_MAGIC, champion.getMagic());

            // Save Enemy Tips.
            List<String> enemyTipsList = champion.getEnemyTips();
            String enemyTipsString = stringListToString(enemyTipsList);
            values.put(Contract.ChampionEntry.COLUMN_ENEMY_TIPS, enemyTipsString);

            // Save Ally Tips.
            List<String> allyTipsList = champion.getAllyTips();
            String allyTipsString = stringListToString(allyTipsList);
            values.put(Contract.ChampionEntry.COLUMN_ALLY_TIPS, allyTipsString);

            values.put(Contract.ChampionEntry.COLUMN_ATTACK_DAMAGE, champion.getAttackDamage());
            values.put(Contract.ChampionEntry.COLUMN_ATTACK_DAMAGE_PER_LEVEL, champion.getAttackDamagePerLevel());
            values.put(Contract.ChampionEntry.COLUMN_ATTACK_RANGE, champion.getAttackRange());
            values.put(Contract.ChampionEntry.COLUMN_ARMOR, champion.getArmor());
            values.put(Contract.ChampionEntry.COLUMN_ARMOR_PER_LEVEL, champion.getArmorPerLevel());
            values.put(Contract.ChampionEntry.COLUMN_HEALTH, champion.getHealth());
            values.put(Contract.ChampionEntry.COLUMN_HEALTH_PER_LEVEL, champion.getHealthPerLevel());
            values.put(Contract.ChampionEntry.COLUMN_HEALTH_REGEN, champion.getHealthRegen());
            values.put(Contract.ChampionEntry.COLUMN_HEALTH_REGEN_PER_LEVEL, champion.getHealthRegenPerLevel());
            values.put(Contract.ChampionEntry.COLUMN_MANA, champion.getMana());
            values.put(Contract.ChampionEntry.COLUMN_MANA_PER_LEVEL, champion.getManaPerLevel());
            values.put(Contract.ChampionEntry.COLUMN_MANA_REGEN, champion.getManaRegen());
            values.put(Contract.ChampionEntry.COLUMN_MANA_REGEN_PER_LEVEL, champion.getManaRegenPerLevel());
            values.put(Contract.ChampionEntry.COLUMN_ATTACK_SPEED_OFFSET, champion.getAttackSpeedOffset());
            values.put(Contract.ChampionEntry.COLUMN_ATTACK_SPEED_PER_LEVEL, champion.getAttackSpeedPerLevel());
            values.put(Contract.ChampionEntry.COLUMN_MOVE_SPEED, champion.getMoveSpeed());
            values.put(Contract.ChampionEntry.COLUMN_CRIT, champion.getCrit());
            values.put(Contract.ChampionEntry.COLUMN_CRIT_PER_LEVEL, champion.getCritPerLevel());
            values.put(Contract.ChampionEntry.COLUMN_MAGIC_RESIST, champion.getMagicResist());
            values.put(Contract.ChampionEntry.COLUMN_MAGIC_RESIST_PER_LEVEL, champion.getMagicResistPerLevel());

            List<String> spellNameList = champion.getSpellName();
            String spellNameString = stringListToString(spellNameList);
            values.put(Contract.ChampionEntry.COLUMN_SPELL_NAME, spellNameString);

            List<String> spellDescriptionList = champion.getSpellDescription();
            String spellDescriptionString = stringListToString(spellDescriptionList);
            values.put(Contract.ChampionEntry.COLUMN_SPELL_DESCRIPTION, spellDescriptionString);

            List<String> spellImageList = champion.getSpellImage();
            String spellImageString = stringListToString(spellImageList);
            values.put(Contract.ChampionEntry.COLUMN_SPELL_IMAGE, spellImageString);

            List<String> spellResourceList = champion.getSpellResource();
            String spellResourceString = stringListToString(spellResourceList);
            values.put(Contract.ChampionEntry.COLUMN_SPELL_RESOURCE, spellResourceString);

            List<String> spellCooldown = champion.getSpellCooldownList();
            String spellCooldownString = stringListToString(spellCooldown);
            values.put(Contract.ChampionEntry.COLUMN_SPELL_COOLDOWN, spellCooldownString);

            List<String> spellCost = champion.getSpellCostList();
            String spellCostString = stringListToString(spellCost);
            values.put(Contract.ChampionEntry.COLUMN_SPELL_COST, spellCostString);

            Uri uri = getContentResolver().insert(Contract.ChampionEntry.CONTENT_URI, values);
            Log.d(LOG_TAG, String.valueOf(uri));
        }
    }

    private String stringListToString(List<String> list) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i));
            if (i != list.size() - 1){
                builder.append(STRING_DIVIDER);
            }
        }

        return builder.toString();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = ((ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void openChampionList() {
        Intent intent = new Intent(MainActivity.this, ChampionListActivity.class);
        startActivity(intent);
    }
}