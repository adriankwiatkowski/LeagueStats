package com.example.android.leaguestats;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.android.leaguestats.adapters.SpellAdapter;
import com.example.android.leaguestats.adapters.SplashArtAdapter;
import com.example.android.leaguestats.database.Contract;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChampionStatsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = ChampionStatsActivity.class.getSimpleName();
    private static final String DB_SIGN = " = ?";
    private RecyclerView mSplashArtRecyclerView;
    private SplashArtAdapter mSplashArtAdapter;
    private RecyclerView.LayoutManager mSplashArtLayoutManager;
    private RecyclerView mSpellRecyclerView;
    private SpellAdapter mSpellAdapter;
    private RecyclerView.LayoutManager mSpellLayoutManager;
    private TextView mHealthTv, mHelathRegenTv, mManaTv, mRangeTv, mArmorTv, mManaRegenTv, mMoveSpeedTv,
            mAttackDamageTv, mMagicResistTv, mAttackSpeedTv;
    private Uri mCurrentChampionUri;
    private static final int CHAMPION_LOADER = 0;
    private final String STRING_DIVIDER = "_,_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_stats);

        mSpellRecyclerView = findViewById(R.id.spell_recycler_view);
        mSpellRecyclerView.setHasFixedSize(true);
        mSpellRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mSpellLayoutManager = new LinearLayoutManager(this);
        mSpellRecyclerView.setLayoutManager(mSpellLayoutManager);

        mSpellAdapter = new SpellAdapter(this, new ArrayList<String>(), new ArrayList<String>(),
                new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
        mSpellRecyclerView.setAdapter(mSpellAdapter);

        mSplashArtRecyclerView = findViewById(R.id.splash_art_recycler_view);
        mSplashArtRecyclerView.setHasFixedSize(true);
        mSplashArtRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mSplashArtLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mSplashArtRecyclerView.setLayoutManager(mSplashArtLayoutManager);

        mSplashArtAdapter = new SplashArtAdapter(this, new ArrayList<String>(), new ArrayList<String>());
        mSplashArtRecyclerView.setAdapter(mSplashArtAdapter);

        mHealthTv = findViewById(R.id.health_tv);
        mHelathRegenTv = findViewById(R.id.health_regen_tv);
        mManaTv = findViewById(R.id.mana_tv);
        mRangeTv = findViewById(R.id.range_tv);
        mArmorTv = findViewById(R.id.armor_tv);
        mManaRegenTv = findViewById(R.id.mana_regen_tv);
        mMoveSpeedTv = findViewById(R.id.move_speed_tv);
        mAttackDamageTv = findViewById(R.id.attack_damage_tv);
        mMagicResistTv = findViewById(R.id.magic_resist_tv);
        mAttackSpeedTv = findViewById(R.id.attack_speed_tv);

        Intent intent = getIntent();
        mCurrentChampionUri = intent.getData();

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
                Contract.ChampionEntry.COLUMN_ARMOR_PER_LEVEL,
                Contract.ChampionEntry.COLUMN_ATTACK_DAMAGE,
                Contract.ChampionEntry.COLUMN_MANA_PER_LEVEL,
                Contract.ChampionEntry.COLUMN_ATTACK_SPEED_OFFSET,
                Contract.ChampionEntry.COLUMN_MANA,
                Contract.ChampionEntry.COLUMN_ARMOR,
                Contract.ChampionEntry.COLUMN_HEALTH,
                Contract.ChampionEntry.COLUMN_HEALTH_REGEN_PER_LEVEL,
                Contract.ChampionEntry.COLUMN_ATTACK_SPEED_PER_LEVEL,
                Contract.ChampionEntry.COLUMN_ATTACK_RANGE,
                Contract.ChampionEntry.COLUMN_MOVE_SPEED,
                Contract.ChampionEntry.COLUMN_ATTACK_DAMAGE_PER_LEVEL,
                Contract.ChampionEntry.COLUMN_MANA_REGEN_PER_LEVEL,
                Contract.ChampionEntry.COLUMN_CRIT_PER_LEVEL,
                Contract.ChampionEntry.COLUMN_MAGIC_RESIST_PER_LEVEL,
                Contract.ChampionEntry.COLUMN_CRIT,
                Contract.ChampionEntry.COLUMN_MANA_REGEN,
                Contract.ChampionEntry.COLUMN_MAGIC_RESIST,
                Contract.ChampionEntry.COLUMN_HEALTH_REGEN,
                Contract.ChampionEntry.COLUMN_HEALTH_PER_LEVEL,
                Contract.ChampionEntry.COLUMN_CHAMPION_NAME,
                Contract.ChampionEntry.COLUMN_SPELL_NAME,
                Contract.ChampionEntry.COLUMN_SPELL_DESCRIPTION,
                Contract.ChampionEntry.COLUMN_SPELL_IMAGE,
                Contract.ChampionEntry.COLUMN_SPELL_RESOURCE,
                Contract.ChampionEntry.COLUMN_SPELL_COOLDOWN,
                Contract.ChampionEntry.COLUMN_SPELL_COST,
                Contract.ChampionEntry.COLUMN_CHAMPION_TITLE};

        return new CursorLoader(this,
                mCurrentChampionUri,
                PROJECTION,
                Contract.ChampionEntry._ID + DB_SIGN,
                new String[]{championId},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount()  < 1) {
            return;
        }

        if (cursor.moveToFirst()) {

            String championSplashArtString = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_SPLASH_ART));
            List<String> splashArtArray = Arrays.asList(championSplashArtString.split(STRING_DIVIDER));

            String splashArtNameString = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_SPLASH_ART_NAME));
            List<String> splashArtNameArray = Arrays.asList(splashArtNameString.split(STRING_DIVIDER));

            mSplashArtAdapter.setData(splashArtArray, splashArtNameArray);

            double armorPerLevel = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_ARMOR_PER_LEVEL));
            double mpPerLevel = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_MANA_PER_LEVEL));
            double hpRegenPerLevel = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_HEALTH_REGEN_PER_LEVEL));
            double attackSpeedPerLevel = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_ATTACK_SPEED_PER_LEVEL));
            double attackDamagePerLevel = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_ATTACK_DAMAGE_PER_LEVEL));
            double mpRegenPerLevel = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_MANA_REGEN_PER_LEVEL));
            double critPerLevel = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_CRIT_PER_LEVEL));
            double magicResistPerLevel = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_MAGIC_RESIST_PER_LEVEL));
            double hpPerLevel = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_HEALTH_PER_LEVEL));
            double crit = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_CRIT));

            double health = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_HEALTH));
            double healthRegen = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_HEALTH_REGEN));
            double mana = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_MANA));
            double range = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_ATTACK_RANGE));
            double armor = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_ARMOR));
            double manaRegen = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_MANA_REGEN));
            double moveSpeed = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_MOVE_SPEED));
            double attackDamage = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_ATTACK_DAMAGE));
            double magicResist = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_MAGIC_RESIST));
            double attackSpeedOffset = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_ATTACK_SPEED_OFFSET));

            double attackSpeedDouble = 0.625 / (1 + attackSpeedOffset);
            DecimalFormat decimalFormat = new DecimalFormat("#.###");
            String attackSpeed = decimalFormat.format(attackSpeedDouble);

            String spellName = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_SPELL_NAME));
            List<String> spellNameList = Arrays.asList(spellName.split(STRING_DIVIDER));
            Log.i(LOG_TAG, spellName);
            if (spellNameList.size() == 0) {
                spellNameList = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    spellNameList.add(getString(R.string.unknown_name));
                }
            }

            String spellDescription = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_SPELL_DESCRIPTION));
            List<String> spellDescriptionList = Arrays.asList(spellDescription.split(STRING_DIVIDER));
            Log.i(LOG_TAG, spellDescription);
            if (spellDescriptionList.size() == 0) {
                spellDescriptionList = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    spellDescriptionList.add(getString(R.string.unknown_description));
                }
            }

            String spellImage = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_SPELL_IMAGE));
            List<String> spellImageList = Arrays.asList(spellImage.split(STRING_DIVIDER));
            Log.i(LOG_TAG, spellImage);

            String spellResource = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_SPELL_RESOURCE));
            List<String> spellResourceList = Arrays.asList(spellResource.split(STRING_DIVIDER));
            Log.i(LOG_TAG, spellResource);
            if (spellResourceList.size() == 0) {
                spellResourceList = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    spellResourceList.add(getString(R.string.unknown_resource));
                }
            }

            String cooldownString = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_SPELL_COOLDOWN));
            List<String> spellCooldownList = Arrays.asList(cooldownString.split(STRING_DIVIDER));
            Log.i(LOG_TAG, String.valueOf(spellCooldownList));

            String costString = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_SPELL_COST));
            List<String> spellCostList = Arrays.asList(costString.split(STRING_DIVIDER));
            Log.i(LOG_TAG, String.valueOf(spellCostList));

            mSpellAdapter.setData(spellNameList, spellDescriptionList, spellImageList, spellResourceList, spellCooldownList, spellCostList);
            mSpellRecyclerView.setAdapter(mSpellAdapter);

            mHealthTv.setText(String.valueOf(health));
            mHelathRegenTv.setText(String.valueOf(healthRegen));
            mManaTv.setText(String.valueOf(mana));
            mRangeTv.setText(String.valueOf(range));
            mArmorTv.setText(String.valueOf(armor));
            mManaRegenTv.setText(String.valueOf(manaRegen));
            mMoveSpeedTv.setText(String.valueOf(moveSpeed));
            mAttackDamageTv.setText(String.valueOf(attackDamage));
            mMagicResistTv.setText(String.valueOf(magicResist));
            mAttackSpeedTv.setText(String.valueOf(attackSpeed));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mHealthTv.setText("");
        mHelathRegenTv.setText("");
        mManaTv.setText("");
        mRangeTv.setText("");
        mArmorTv.setText("");
        mManaRegenTv.setText("");
        mMoveSpeedTv.setText("");
        mAttackDamageTv.setText("");
        mMagicResistTv.setText("");
        mAttackSpeedTv.setText("");
    }
}
