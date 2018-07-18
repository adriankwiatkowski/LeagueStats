package com.example.android.leaguestats;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.leaguestats.adapters.SpellAdapter;
import com.example.android.leaguestats.adapters.SplashArtAdapter;
import com.example.android.leaguestats.database.Contract;
import com.example.android.leaguestats.models.Spell;
import com.example.android.leaguestats.utilities.DataUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChampionInfoFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = ChampionInfoFragment.class.getSimpleName();
    private static final String DB_SIGN = " = ?";
    private RecyclerView mSplashArtRecyclerView;
    private SplashArtAdapter mSplashArtAdapter;
    private RecyclerView mSpellRecyclerView;
    private SpellAdapter mSpellAdapter;
    private TextView mHealthTv, mHealthRegenTv, mManaTv, mRangeTv, mArmorTv, mManaRegenTv, mMoveSpeedTv,
            mAttackDamageTv, mMagicResistTv, mAttackSpeedTv;
    private Uri mCurrentChampionUri;
    private static final int CHAMPION_INFO_LOADER = 1;
    private static final String CHAMPION_URI_KEY = "CHAMPION_URI_KEY";
    private String SPELL_LAYOUT_MANAGER_STATE_KEY = "spellLayoutManagerStateKey";
    private String SPLASH_ART_LAYOUT_MANAGER_STATE_KEY = "splashArtLayoutManagerStateKey";

    public ChampionInfoFragment() {
    }

    public static ChampionInfoFragment newInstance(Uri championUri) {

        ChampionInfoFragment championInfoFragment = new ChampionInfoFragment();

        Bundle args = new Bundle();
        args.putParcelable(CHAMPION_URI_KEY, championUri);
        championInfoFragment.setArguments(args);

        return championInfoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");

        Bundle args = getArguments();
        if (args != null && args.containsKey(CHAMPION_URI_KEY)) {
            mCurrentChampionUri = getArguments().getParcelable(CHAMPION_URI_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_champion_info, container, false);
        Log.d(LOG_TAG, "onCreateView");

        mSpellRecyclerView = rootView.findViewById(R.id.spell_recycler_view);

        mSplashArtRecyclerView = rootView.findViewById(R.id.splash_art_recycler_view);

        mHealthTv = rootView.findViewById(R.id.health_tv);
        mHealthRegenTv = rootView.findViewById(R.id.health_regen_tv);
        mManaTv = rootView.findViewById(R.id.mana_tv);
        mRangeTv = rootView.findViewById(R.id.range_tv);
        mArmorTv = rootView.findViewById(R.id.armor_tv);
        mManaRegenTv = rootView.findViewById(R.id.mana_regen_tv);
        mMoveSpeedTv = rootView.findViewById(R.id.move_speed_tv);
        mAttackDamageTv = rootView.findViewById(R.id.attack_damage_tv);
        mMagicResistTv = rootView.findViewById(R.id.magic_resist_tv);
        mAttackSpeedTv = rootView.findViewById(R.id.attack_speed_tv);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "onActivityCreated");

        mSpellRecyclerView.setHasFixedSize(true);
        mSpellRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSpellRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSpellAdapter = new SpellAdapter(getActivity(), new ArrayList<Spell>());
        mSpellRecyclerView.setAdapter(mSpellAdapter);

        mSplashArtRecyclerView.setHasFixedSize(true);
        mSplashArtRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSplashArtRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mSplashArtAdapter = new SplashArtAdapter(getActivity(), new ArrayList<String>(), new ArrayList<String>());
        mSplashArtRecyclerView.setAdapter(mSplashArtAdapter);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SPELL_LAYOUT_MANAGER_STATE_KEY)) {
                mSpellRecyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(SPELL_LAYOUT_MANAGER_STATE_KEY));
            }
            if (savedInstanceState.containsKey(SPLASH_ART_LAYOUT_MANAGER_STATE_KEY)) {
                mSplashArtRecyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(SPLASH_ART_LAYOUT_MANAGER_STATE_KEY));
            }
        }

        if (mCurrentChampionUri == null) {
            Log.d(LOG_TAG, "ChampionUri is null");
        } else {
            getActivity().getSupportLoaderManager().initLoader(CHAMPION_INFO_LOADER, null, this);
        }

        mSplashArtRecyclerView.setNestedScrollingEnabled(false);
        mSpellRecyclerView.setNestedScrollingEnabled(false);
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
                Contract.ChampionEntry.COLUMN_MAGIC_RESIST_PER_LEVEL,
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

        return new CursorLoader(getContext(),
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
            List<String> splashArtArray = Arrays.asList(championSplashArtString.split(DataUtils.STRING_DIVIDER));

            String splashArtNameString = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_SPLASH_ART_NAME));
            List<String> splashArtNameArray = Arrays.asList(splashArtNameString.split(DataUtils.STRING_DIVIDER));

            mSplashArtAdapter.setData(splashArtArray, splashArtNameArray);

            double armorPerLevel = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_ARMOR_PER_LEVEL));
            double mpPerLevel = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_MANA_PER_LEVEL));
            double hpRegenPerLevel = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_HEALTH_REGEN_PER_LEVEL));
            double attackSpeedPerLevel = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_ATTACK_SPEED_PER_LEVEL));
            double attackDamagePerLevel = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_ATTACK_DAMAGE_PER_LEVEL));
            double mpRegenPerLevel = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_MANA_REGEN_PER_LEVEL));
            double magicResistPerLevel = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_MAGIC_RESIST_PER_LEVEL));
            double hpPerLevel = cursor.getDouble(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_HEALTH_PER_LEVEL));

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
            List<String> spellNameList = Arrays.asList(spellName.split(DataUtils.STRING_DIVIDER));
            if (spellNameList.size() == 0) {
                spellNameList = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    spellNameList.add(getString(R.string.unknown_name));
                }
            }

            String spellDescription = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_SPELL_DESCRIPTION));
            List<String> spellDescriptionList = Arrays.asList(spellDescription.split(DataUtils.STRING_DIVIDER));
            if (spellDescriptionList.size() == 0) {
                spellDescriptionList = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    spellDescriptionList.add(getString(R.string.unknown_description));
                }
            }

            String spellImage = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_SPELL_IMAGE));
            List<String> spellImageList = Arrays.asList(spellImage.split(DataUtils.STRING_DIVIDER));

            String spellResource = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_SPELL_RESOURCE));
            List<String> spellResourceList = Arrays.asList(spellResource.split(DataUtils.STRING_DIVIDER));
            Log.i(LOG_TAG, spellResource);
            if (spellResourceList.size() == 0) {
                spellResourceList = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    spellResourceList.add(getString(R.string.unknown_resource));
                }
            }

            String cooldownString = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_SPELL_COOLDOWN));
            List<String> spellCooldownList = Arrays.asList(cooldownString.split(DataUtils.STRING_DIVIDER));
            Log.i(LOG_TAG, String.valueOf(spellCooldownList));

            String costString = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_SPELL_COST));
            List<String> spellCostList = Arrays.asList(costString.split(DataUtils.STRING_DIVIDER));
            Log.i(LOG_TAG, String.valueOf(spellCostList));


            List<Spell> spellList = new ArrayList<>();
            for (int i = 0; i < 4; i++) {

                List<String> cooldownList = new ArrayList<>();

                // last spell has only 3 levels.
                if (i == 3) {
                    for (int j = 0; j < 3; j++) {
                        cooldownList.add(spellCooldownList.get(i * 5 + j));
                    }
                } else {
                    for (int j = 0; j < 5; j++) {
                        cooldownList.add(spellCooldownList.get(i * 5 + j));
                    }
                }

                String cooldownSpellString = stringForSpellList(cooldownList, i);

                List<String> costList = new ArrayList<>();

                if (i == 3) {
                    for (int j = 0; j < 3; j++) {
                        costList.add(spellCostList.get(i * 5 + j));
                    }
                } else {
                    for (int j = 0; j < 5; j++) {
                        costList.add(spellCostList.get(i * 5 + j));
                    }
                }

                String costSpellString = stringForSpellList(costList, i);

                spellList.add(new Spell(spellNameList.get(i), spellDescriptionList.get(i),
                        spellImageList.get(i), spellResourceList.get(i),
                        cooldownSpellString, costSpellString));
            }

            mSpellAdapter.setData(spellList);

            mHealthTv.setText(String.valueOf(health));
            mHealthRegenTv.setText(String.valueOf(healthRegen));
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
        mHealthRegenTv.setText("");
        mManaTv.setText("");
        mRangeTv.setText("");
        mArmorTv.setText("");
        mManaRegenTv.setText("");
        mMoveSpeedTv.setText("");
        mAttackDamageTv.setText("");
        mMagicResistTv.setText("");
        mAttackSpeedTv.setText("");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(SPELL_LAYOUT_MANAGER_STATE_KEY, mSpellRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelable(SPLASH_ART_LAYOUT_MANAGER_STATE_KEY, mSplashArtRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    private String stringForSpellList(List<String> list, int position) {
        StringBuilder builder = new StringBuilder();
        if (position != 3) {
            for (int i = 0; i < 5; i++) {
                builder.append(list.get(i)).append('/');
            }
        } else {
            for (int i = 0; i < 3; i++) {
                builder.append(list.get(i)).append('/');
            }
        }
        builder.deleteCharAt(builder.length() - 1);

        return builder.toString();
    }
}
