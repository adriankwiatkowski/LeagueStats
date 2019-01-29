package com.example.android.leaguestats.ui;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.adapters.SpellAdapter;
import com.example.android.leaguestats.adapters.SplashArtAdapter;
import com.example.android.leaguestats.data.database.entity.ChampionEntry;
import com.example.android.leaguestats.data.database.entity.Spell;
import com.example.android.leaguestats.models.Champion;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.utilities.LeaguePreferences;
import com.example.android.leaguestats.viewmodels.ChampionModel;
import com.example.android.leaguestats.viewmodels.ChampionModelFactory;

import java.text.DecimalFormat;
import java.util.List;

public class ChampionDetailFragment extends Fragment {

    private static final String LOG_TAG = ChampionDetailFragment.class.getSimpleName();

    private String mPatchVersion;

    private NestedScrollView mScrollView;

    private RecyclerView mSplashArtRecycler;
    private SplashArtAdapter mSplashArtAdapter;

    private TextView mChampionBioTv;
    private ProgressBar mDifficultyProgressBar, mAttackProgressBar, mDefenseProgressBar, mMagicProgressBar;

    private RecyclerView mSpellRecycler;
    private SpellAdapter mSpellAdapter;
    private TextView mHealthTv, mHealthRegenTv, mManaTv, mRangeTv, mArmorTv, mManaRegenTv,
            mMoveSpeedTv, mAttackDamageTv, mMagicResistTv, mAttackSpeedTv;

    private TextView mPlayingAsTv, mPlayingAgainstTv, mPlayingAsLabelTv, mPlayingAgainstLabelTv;

    private TextView mEmptyViewTv;
    private ViewGroup mContentView;

    public ChampionDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_champion_detail, container, false);

        mScrollView = rootView.findViewById(R.id.nested_scroll_view);

        mSplashArtRecycler = rootView.findViewById(R.id.splash_art_recycler_view);

        // Champion overview views.
        mChampionBioTv = rootView.findViewById(R.id.champion_bio_tv);
        mDifficultyProgressBar = rootView.findViewById(R.id.difficulty_progress_bar);
        mAttackProgressBar = rootView.findViewById(R.id.attack_progress_bar);
        mDefenseProgressBar = rootView.findViewById(R.id.defense_progress_bar);
        mMagicProgressBar = rootView.findViewById(R.id.magic_progress_bar);

        // Champion info views.
        mSpellRecycler = rootView.findViewById(R.id.spell_recycler_view);
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

        // Champion tips views.
        mPlayingAsTv = rootView.findViewById(R.id.playing_as_tv);
        mPlayingAgainstTv = rootView.findViewById(R.id.playing_against_tv);
        mPlayingAsLabelTv = rootView.findViewById(R.id.playing_as_label_tv);
        mPlayingAgainstLabelTv = rootView.findViewById(R.id.playing_against_label_tv);

        mEmptyViewTv = rootView.findViewById(R.id.empty_view_tv);
        mContentView = rootView.findViewById(R.id.champion_content_layout);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPatchVersion = LeaguePreferences.getPatchVersion(getContext());
        setupViewModel();

        mSplashArtRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mSplashArtRecycler.setHasFixedSize(true);
        mSplashArtRecycler.setItemAnimator(new DefaultItemAnimator());
        new LinearSnapHelper().attachToRecyclerView(mSplashArtRecycler);
        mSplashArtAdapter = new SplashArtAdapter(getContext(), null);
        mSplashArtRecycler.setAdapter(mSplashArtAdapter);

        mSpellRecycler.setHasFixedSize(true);
        mSpellRecycler.setItemAnimator(new DefaultItemAnimator());
        mSpellRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSpellAdapter = new SpellAdapter(getActivity(), mPatchVersion);
        mSpellRecycler.setAdapter(mSpellAdapter);
        mSpellRecycler.setNestedScrollingEnabled(false);
    }

    private void setupViewModel() {
        ChampionModelFactory factory =
                InjectorUtils.provideChampionModelFactory(getActivity().getApplicationContext());
        final ChampionModel viewModel =
                ViewModelProviders.of(getActivity(), factory).get(ChampionModel.class);
        viewModel.getChampion().observe(this, new Observer<ChampionEntry>() {
            @Override
            public void onChanged(@Nullable ChampionEntry championEntry) {
                Log.d(LOG_TAG, "Receiving database update from LiveData");
                updateUi(championEntry);
            }
        });
    }

    private void updateUi(@Nullable ChampionEntry championEntry) {
        mScrollView.fullScroll(View.FOCUS_UP);
        if (championEntry == null) {
            hideViews();
        } else {
            showViews();
            mSplashArtAdapter.setChampion(new Champion(championEntry));
            updateOverviewViews(championEntry);
            updateInfoViews(championEntry);
            updateTipsViews(championEntry);
        }
    }

    private void showViews() {
        mEmptyViewTv.setVisibility(View.INVISIBLE);
        mContentView.setVisibility(View.VISIBLE);
    }

    private void hideViews() {
        mEmptyViewTv.setVisibility(View.VISIBLE);
        mContentView.setVisibility(View.INVISIBLE);
    }

    private void updateOverviewViews(ChampionEntry championEntry) {
        mChampionBioTv.setText(championEntry.getLore());
        mDifficultyProgressBar.setProgress(championEntry.getDifficulty());
        mAttackProgressBar.setProgress(championEntry.getAttack());
        mDefenseProgressBar.setProgress(championEntry.getDefense());
        mMagicProgressBar.setProgress(championEntry.getMagic());
    }

    private void updateInfoViews(ChampionEntry championEntry) {
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

        List<Spell> spellList = championEntry.getSpells();
        for (int i = 0; i < spellList.size(); i++) {
            Spell spell = spellList.get(i);
            String tooltip = spell.getTooltip();
            StringBuilder builder = new StringBuilder(tooltip);
            final String OPENING_CURLY_BRACES = "{{";
            final String CLOSING_CURLY_BRACES = "}}";
            final String OPENING_SCALE_AP = "<scaleAP>";
            final String CLOSING_SCALE_AP = "</scaleAP>";
            final String OPENING_SPAN_COLOR = "<span class=\"color";
            final String CLOSING_SPAN_COLOR = "</span>";
            while (builder.indexOf(OPENING_CURLY_BRACES) > 0 || builder.indexOf(OPENING_SCALE_AP) > 0 || builder.indexOf(OPENING_SPAN_COLOR) > 0) {
                if (builder.indexOf(OPENING_CURLY_BRACES) > 0) {
                    int startIndex = builder.indexOf(OPENING_CURLY_BRACES);
                    int endIndex = builder.indexOf(CLOSING_CURLY_BRACES);
                    String placeholder = builder.substring(startIndex + 3, endIndex - 2);
                    int index;
                    String stringToReplace = "";
                    switch (placeholder) {
                        case "e":
                            index = Integer.parseInt(builder.substring(startIndex + 4, endIndex - 1));
                            stringToReplace = spell.getEffectBurn().get(index);
                            break;
                        case "a":
                        case "f":
                            String key = builder.substring(startIndex + 3, endIndex - 1);
                            String fallbackKey = key.replace('f', 'a');
                            List<String> varKeys = spell.getVarsKey();
                            index = 0;
                            boolean isFound = false;
                            for (int k = 0; k < varKeys.size(); k++) {
                                if (key.equals(varKeys.get(k)) || fallbackKey.equals(varKeys.get(k))) {
                                    index = k;
                                    isFound = true;
                                    break;
                                }
                            }
                            if (!isFound) {
                                Log.w(LOG_TAG, "Couldnt match varkey for: " + key);
                            } else {
                                stringToReplace = String.valueOf(spell.getVarsCoeff().get(index));
                            }
                            break;
                    }
                    builder.replace(startIndex, endIndex + 2, stringToReplace);
                } else if (builder.indexOf(OPENING_SCALE_AP) > 0) {
                    int startIndex = builder.indexOf(OPENING_SCALE_AP);
                    int endIndex = startIndex + OPENING_SCALE_AP.length();

                    @SuppressLint("ResourceType")
                    String color = "#" + getResources().getString(R.color.scaleAPColor).substring(2);

                    builder.replace(startIndex, endIndex, "<font color='" + color + "'>");
                    startIndex = builder.indexOf(CLOSING_SCALE_AP);
                    endIndex = startIndex + CLOSING_SCALE_AP.length();
                    builder.replace(startIndex, endIndex, "</font>");
                } else if (builder.indexOf(OPENING_SPAN_COLOR) > 0) {
                    int startIndex = builder.indexOf(OPENING_SPAN_COLOR);
                    int endIndex = builder.indexOf("\">", startIndex);
                    String substring = builder.substring(startIndex + OPENING_SPAN_COLOR.length(), endIndex);
                    builder.replace(startIndex, endIndex + 1, "<font color='#" + substring + "'");
                    endIndex = builder.indexOf(CLOSING_SPAN_COLOR);
                    builder.replace(endIndex, endIndex + CLOSING_SPAN_COLOR.length(), "</font>");
                }
            }
            spell.setTooltip(builder.toString());
        }
        mSpellAdapter.setData(spellList);
    }

    private void updateTipsViews(ChampionEntry championEntry) {
        mPlayingAgainstLabelTv.setText(getString(R.string.playing_vs, championEntry.getName()));
        mPlayingAgainstTv.setText("");
        mPlayingAsTv.setText("");
        List<String> vsTips = championEntry.getVsTips();
        for (int i = 0; i < vsTips.size(); i++) {
            mPlayingAgainstTv.append(String.valueOf(i + 1) + ". " + vsTips.get(i));
            if (i != vsTips.size() - 1) {
                mPlayingAgainstTv.append("\n");
            }
        }
        mPlayingAsLabelTv.setText(getString(R.string.playing_as, championEntry.getName()));
        List<String> asTips = championEntry.getAsTips();
        for (int i = 0; i < asTips.size(); i++) {
            mPlayingAsTv.append(String.valueOf(i + 1) + ". " + asTips.get(i));
            if (i != asTips.size() - 1) {
                mPlayingAsTv.append("\n");
            }
        }
    }
}
