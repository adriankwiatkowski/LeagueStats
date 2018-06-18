package com.example.android.leaguestats;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.android.leaguestats.models.Stats;
import com.example.android.leaguestats.utilities.ChampionsAsyncTask;
import com.example.android.leaguestats.utilities.NameAsyncTask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private EditText mUserNameEdit;
    private Button mSubmitButton;
    private Spinner mRegionSpinner;
    private String mRegion;
    private ArrayList<Champion> mChampions;
    private final String STRING_DIVIDER = "_,_";

    private final String SQL_CREATE_TABLE = "CREATE TABLE " + Contract.ChampionEntry.TABLE_NAME + " (" +
            Contract.ChampionEntry._ID + " INTEGER PRIMARY KEY, " +
            Contract.ChampionEntry.COLUMN_CHAMPION_NAME + " TEXT NOT NULL, " +
            Contract.ChampionEntry.COLUMN_KEY + " TEXT NOT NULL, " +
            Contract.ChampionEntry.COLUMN_CHAMPION_TITLE + " TEXT NOT NULL, " +
            Contract.ChampionEntry.COLUMN_THUMBNAIL + " TEXT NOT NULL, " +
            Contract.ChampionEntry.COLUMN_SPLASH_ART + " TEXT NOT NULL, " +
            Contract.ChampionEntry.COLUMN_SPLASH_ART_NAME + " TEXT NOT NULL, " +
            Contract.ChampionEntry.COLUMN_DIFFICULTY + " INTEGER NOT NULL, " +
            Contract.ChampionEntry.COLUMN_ATTACK + " INTEGER NOT NULL, " +
            Contract.ChampionEntry.COLUMN_DEFENSE + " INTEGER NOT NULL, " +
            Contract.ChampionEntry.COLUMN_MAGIC + " INTEGER NOT NULL, " +
            Contract.ChampionEntry.COLUMN_ENEMY_TIPS + " TEXT NOT NULL, " +
            Contract.ChampionEntry.COLUMN_ALLY_TIPS + " TEXT NOT NULL, " +
            Contract.ChampionEntry.COLUMN_ARMOR_PER_LEVEL + " REAL NOT NULL, " +
            Contract.ChampionEntry.COLUMN_ATTACK_DAMAGE + " REAL NOT NULL, " +
            Contract.ChampionEntry.COLUMN_MANA_PER_LEVEL + " REAL NOT NULL, " +
            Contract.ChampionEntry.COLUMN_ATTACK_SPEED_OFFSET + " REAL NOT NULL, " +
            Contract.ChampionEntry.COLUMN_MANA + " REAL NOT NULL, " +
            Contract.ChampionEntry.COLUMN_ARMOR + " REAL NOT NULL, " +
            Contract.ChampionEntry.COLUMN_HEALTH + " REAL NOT NULL, " +
            Contract.ChampionEntry.COLUMN_HEALTH_REGEN_PER_LEVEL + " REAL NOT NULL, " +
            Contract.ChampionEntry.COLUMN_ATTACK_SPEED_PER_LEVEL + " REAL NOT NULL, " +
            Contract.ChampionEntry.COLUMN_ATTACK_RANGE + " REAL NOT NULL, " +
            Contract.ChampionEntry.COLUMN_MOVE_SPEED + " REAL NOT NULL, " +
            Contract.ChampionEntry.COLUMN_ATTACK_DAMAGE_PER_LEVEL + " REAL NOT NULL, " +
            Contract.ChampionEntry.COLUMN_MANA_REGEN_PER_LEVEL + " REAL NOT NULL, " +
            Contract.ChampionEntry.COLUMN_CRIT_PER_LEVEL + " REAL NOT NULL, " +
            Contract.ChampionEntry.COLUMN_MAGIC_RESIST_PER_LEVEL + " REAL NOT NULL, " +
            Contract.ChampionEntry.COLUMN_CRIT + " REAL NOT NULL, " +
            Contract.ChampionEntry.COLUMN_MANA_REGEN + " REAL NOT NULL, " +
            Contract.ChampionEntry.COLUMN_MAGIC_RESIST + " REAL NOT NULL, " +
            Contract.ChampionEntry.COLUMN_HEALTH_REGEN + " REAL NOT NULL, " +
            Contract.ChampionEntry.COLUMN_HEALTH_PER_LEVEL + " REAL NOT NULL, " +
            Contract.ChampionEntry.COLUMN_SPELL_NAME + " TEXT NOT NULL, " +
            Contract.ChampionEntry.COLUMN_SPELL_DESCRIPTION + " TEXT NOT NULL, " +
            Contract.ChampionEntry.COLUMN_SPELL_IMAGE + " TEXT NOT NULL, " +
            Contract.ChampionEntry.COLUMN_SPELL_RESOURCE + " TEXT NOT NULL, " +
            Contract.ChampionEntry.COLUMN_SPELL_COOLDOWN + " TEXT NOT NULL, " +
            Contract.ChampionEntry.COLUMN_SPELL_COST + " TEXT NOT NULL, " +
            Contract.ChampionEntry.COLUMN_CHAMPION_LORE + " TEXT NOT NULL);";

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
                    mRegion = Data.ENTRY_URL_SUMMONER_ARRAY[position];
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
                public void nameTaskCompleted(String userId) {
                    SharedPreferences sharedPreferences = getSharedPreferences(
                            getString(R.string.shared_preferences_name), MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    switch (mRegion) {
                        case Data.ENTRY_URL_SUMMONER_EUNE:
                            editor.putInt(getString(R.string.summoner_region_key), getResources().getInteger(R.integer.region_eune));
                            break;
                        case Data.ENTRY_URL_SUMMONER_EUW:
                            editor.putInt(getString(R.string.summoner_region_key), getResources().getInteger(R.integer.region_euw));
                            break;
                        case Data.ENTRY_URL_SUMMONER_NA:
                            editor.putInt(getString(R.string.summoner_region_key), getResources().getInteger(R.integer.region_na));
                            break;
                    }
                    editor.apply();

                    Intent intent = new Intent(MainActivity.this, MasteryActivity.class);
                    intent.putExtra(getString(R.string.champion_id_key), userId);
                    startActivity(intent);
                }
            };

            NameAsyncTask nameAsyncTask = new NameAsyncTask(taskCompleted, mRegion);
            nameAsyncTask.execute(summonerName);
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
        database.execSQL(SQL_CREATE_TABLE);

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

            // Save Stats.
            int statsSize = champion.getStats().size();
            for (int i = 0; i < statsSize; i++) {
                Stats stats = champion.getStats().get(i);

                values.put(Contract.ChampionEntry.COLUMN_ATTACK_DAMAGE, stats.getAttackDamage());
                values.put(Contract.ChampionEntry.COLUMN_ATTACK_DAMAGE_PER_LEVEL, stats.getAttackDamagePerLevel());
                values.put(Contract.ChampionEntry.COLUMN_ATTACK_RANGE, stats.getAttackRange());
                values.put(Contract.ChampionEntry.COLUMN_ARMOR, stats.getArmor());
                values.put(Contract.ChampionEntry.COLUMN_ARMOR_PER_LEVEL, stats.getArmorPerLevel());
                values.put(Contract.ChampionEntry.COLUMN_HEALTH, stats.getHealth());
                values.put(Contract.ChampionEntry.COLUMN_HEALTH_PER_LEVEL, stats.getHealthPerLevel());
                values.put(Contract.ChampionEntry.COLUMN_HEALTH_REGEN, stats.getHealthRegen());
                values.put(Contract.ChampionEntry.COLUMN_HEALTH_REGEN_PER_LEVEL, stats.getHealthRegenPerLevel());
                values.put(Contract.ChampionEntry.COLUMN_MANA, stats.getMana());
                values.put(Contract.ChampionEntry.COLUMN_MANA_PER_LEVEL, stats.getManaPerLevel());
                values.put(Contract.ChampionEntry.COLUMN_MANA_REGEN, stats.getManaRegen());
                values.put(Contract.ChampionEntry.COLUMN_MANA_REGEN_PER_LEVEL, stats.getManaRegenPerLevel());
                values.put(Contract.ChampionEntry.COLUMN_ATTACK_SPEED_OFFSET, stats.getAttackSpeedOffset());
                values.put(Contract.ChampionEntry.COLUMN_ATTACK_SPEED_PER_LEVEL, stats.getAttackSpeedPerLevel());
                values.put(Contract.ChampionEntry.COLUMN_MOVE_SPEED, stats.getMoveSpeed());
                values.put(Contract.ChampionEntry.COLUMN_CRIT, stats.getCrit());
                values.put(Contract.ChampionEntry.COLUMN_CRIT_PER_LEVEL, stats.getCritPerLevel());
                values.put(Contract.ChampionEntry.COLUMN_MAGIC_RESIST, stats.getMagicResist());
                values.put(Contract.ChampionEntry.COLUMN_MAGIC_RESIST_PER_LEVEL, stats.getMagicResistPerLevel());
            }

            List<String> spellNameList = new ArrayList<>();
            List<String> spellDescriptionList = new ArrayList<>();
            List<String> spellImageList = new ArrayList<>();
            List<String> spellResourceList = new ArrayList<>();
            List<String> spellCooldownList = new ArrayList<>();
            List<String> spellCostList = new ArrayList<>();
            // Save Spells.
            int spellSize = champion.getSpell().size();
            for (int i = 0; i < spellSize; i++) {

                String spellName = champion.getSpell().get(i).getName();
                if (TextUtils.isEmpty(spellName)) {
                    spellName = getString(R.string.unknown_name);
                }
                spellNameList.add(spellName);

                String spellDescription = champion.getSpell().get(i).getDescription();
                if (TextUtils.isEmpty(spellDescription)) {
                    spellDescription = getString(R.string.unknown_description);
                }
                spellDescriptionList.add(spellDescription);

                String spellImage = champion.getSpell().get(i).getImage();
                spellImageList.add(spellImage);

                String spellResource = champion.getSpell().get(i).getResource();
                if (TextUtils.isEmpty(spellResource)) {
                    spellResource = getString(R.string.unknown_resource);
                }
                spellResourceList.add(spellResource);

                List<Double> spellCooldown = champion.getSpell().get(i).getCooldownList();
                String spellCooldownString = doubleListToString(spellCooldown);
                spellCooldownList.add(spellCooldownString);

                List<Integer> spellCost = champion.getSpell().get(i).getCostList();
                String spellCostString = integerListToString(spellCost);
                spellCostList.add(spellCostString);
            }

            String spellName = stringListToString(spellNameList);
            values.put(Contract.ChampionEntry.COLUMN_SPELL_NAME, spellName);

            String spellDescription = stringListToString(spellDescriptionList);
            values.put(Contract.ChampionEntry.COLUMN_SPELL_DESCRIPTION, spellDescription);

            String spellImage = stringListToString(spellImageList);
            values.put(Contract.ChampionEntry.COLUMN_SPELL_IMAGE, spellImage);

            String spellResource = stringListToString(spellResourceList);
            values.put(Contract.ChampionEntry.COLUMN_SPELL_RESOURCE, spellResource);

            String spellCooldown = stringListToString(spellCooldownList);
            values.put(Contract.ChampionEntry.COLUMN_SPELL_COOLDOWN, spellCooldown);

            String spellCost = stringListToString(spellCostList);
            values.put(Contract.ChampionEntry.COLUMN_SPELL_COST, spellCost);

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

    private String doubleListToString(List<Double> list) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            builder.append(String.valueOf(list.get(i)));
            if (i != list.size() - 1) builder.append(STRING_DIVIDER);
        }

        return builder.toString();
    }

    private String integerListToString(List<Integer> list) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            builder.append(String.valueOf(list.get(i)));
            if (i != list.size() - 1)  {
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