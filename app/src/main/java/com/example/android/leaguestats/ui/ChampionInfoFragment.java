package com.example.android.leaguestats.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.utilities.PicassoUtils;
import com.example.android.leaguestats.utilities.LeaguePreferences;
import com.example.android.leaguestats.viewmodels.ChampionModel;
import com.example.android.leaguestats.viewmodels.ChampionModelFactory;
import com.example.android.leaguestats.adapters.SpellAdapter;
import com.example.android.leaguestats.models.Spell;
import com.example.android.leaguestats.data.database.entity.ChampionEntry;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ChampionInfoFragment extends Fragment {

    private static final String LOG_TAG = ChampionInfoFragment.class.getSimpleName();
    private ImageView mSplashArtImage;
    private RecyclerView mSpellRecyclerView;
    private SpellAdapter mSpellAdapter;
    private TextView mHealthTv, mHealthRegenTv, mManaTv, mRangeTv, mArmorTv, mManaRegenTv, mMoveSpeedTv,
            mAttackDamageTv, mMagicResistTv, mAttackSpeedTv;
    private static final int SPELL_COUNT = 4;
    private String mPatchVersion;

    public ChampionInfoFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_champion_info, container, false);

        mSpellRecyclerView = rootView.findViewById(R.id.spell_recycler_view);

        if (rootView.findViewById(R.id.splash_art_info_image) != null) {
            mSplashArtImage = rootView.findViewById(R.id.splash_art_info_image);
        } else {
            mSplashArtImage = null;
        }

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

        mPatchVersion = LeaguePreferences.getPatchVersion(getContext());

        mSpellRecyclerView.setHasFixedSize(true);
        mSpellRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSpellRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSpellAdapter = new SpellAdapter(getActivity(), new ArrayList<Spell>(), mPatchVersion);
        mSpellRecyclerView.setAdapter(mSpellAdapter);

        mSpellRecyclerView.setNestedScrollingEnabled(false);

        setupViewModel();
    }

    private void setupViewModel() {
        ChampionModelFactory factory =
                InjectorUtils.provideChampionModelFactory(getActivity().getApplicationContext());
        final ChampionModel viewModel =
                ViewModelProviders.of(getActivity(), factory).get(ChampionModel.class);
        viewModel.getChampion().observe(getActivity(), new Observer<ChampionEntry>() {
            @Override
            public void onChanged(@Nullable ChampionEntry championEntry) {
                Log.d(LOG_TAG, "Receiving database update from LiveData");
                updateUi(championEntry);
            }
        });
    }

    private void updateUi(ChampionEntry championEntry) {
        if (championEntry != null) {

            // In two pane there isnt image.
            if (mSplashArtImage != null) {
                // Get Default Splash Art.
                String splashArt = championEntry.getSplashArt().get(0);

                PicassoUtils.setSplashArt(mSplashArtImage, getContext(), splashArt);
            }

            double attackSpeedDouble = 0.625 / (1 + championEntry.getAttackSpeedOffset());
            DecimalFormat decimalFormat = new DecimalFormat("#.###");
            String attackSpeed = decimalFormat.format(attackSpeedDouble);

            mHealthTv.setText(String.valueOf(championEntry.getHealth()));
            mHealthRegenTv.setText(String.valueOf(championEntry.getHealthRegen()));
            mManaTv.setText(String.valueOf(championEntry.getMana()));
            mRangeTv.setText(String.valueOf(championEntry.getAttackRange()));
            mArmorTv.setText(String.valueOf(championEntry.getArmor()));
            mManaRegenTv.setText(String.valueOf(championEntry.getManaRegen()));
            mMoveSpeedTv.setText(String.valueOf(championEntry.getMoveSpeed()));
            mAttackDamageTv.setText(String.valueOf(championEntry.getAttackDamage()));
            mMagicResistTv.setText(String.valueOf(championEntry.getMagicResist()));
            mAttackSpeedTv.setText(String.valueOf(attackSpeed));

            List<String> spellNameList = championEntry.getSpellName();
            setMessageIfListEmpty(spellNameList, getString(R.string.unknown_name));

            List<String> spellDescriptionList = championEntry.getSpellDescription();
            setMessageIfListEmpty(spellDescriptionList, getString(R.string.unknown_description));

            List<String> spellImageList = championEntry.getSpellImage();

            List<String> spellResourceList = championEntry.getSpellResource();
            setMessageIfListEmpty(spellResourceList, getString(R.string.unknown_resource));

            List<Double> spellCooldownList = championEntry.getSpellCooldown();

            List<Double> spellCostList = championEntry.getSpellCost();

            List<Integer> spellMaxRankList = championEntry.getSpellMaxRank();

            List<Spell> spellList = new ArrayList<>();

            for (int i = 0; i < SPELL_COUNT; i++) {
                String spellCooldown = getSpellString(spellCooldownList, spellMaxRankList, i);
                String spellCost = getSpellString(spellCostList, spellMaxRankList, i);
                spellList.add(new Spell(spellNameList.get(i), spellDescriptionList.get(i),
                        spellImageList.get(i), spellResourceList.get(i),
                        spellCooldown, spellCost));
            }

            mSpellAdapter.setData(spellList);
        }
    }

    private String getSpellString(List<Double> costList, List<Integer> maxRankList, int iteratorCount) {
        List<Double> list = new ArrayList<>();
        int maxRank = maxRankList.get(iteratorCount);
        int index = 0;
        for (int j = 0; j < iteratorCount; j++) {
            index += maxRankList.get(j);
        }
        for (int j = 0; j < maxRank; j++) {
            int spellIndex = index + j;
            list.add(costList.get(spellIndex));
        }
        return getStringFromSpellList(list);
    }

    private String getStringFromSpellList(List<Double> list) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i)).append('/');
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    private void setMessageIfListEmpty(List<String> list, String message) {
        if (list == null || list.isEmpty()) {
            list = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                list.add(message);
            }
        }
    }
}
