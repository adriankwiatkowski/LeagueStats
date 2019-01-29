package com.example.android.leaguestats.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.data.database.entity.SummonerSpellEntry;
import com.example.android.leaguestats.data.glide.GlideApp;
import com.example.android.leaguestats.data.glide.GlideUtils;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.utilities.LeaguePreferences;
import com.example.android.leaguestats.viewmodels.SummonerSpellModel;
import com.example.android.leaguestats.viewmodels.SummonerSpellModelFactory;

public class SummonerSpellDetailFragment extends Fragment {

    private static final String LOG_TAG = SummonerSpellDetailFragment.class.getSimpleName();
    private NestedScrollView mScrollView;
    private ImageView mSummonerSpellImage;
    private TextView mSummonerSpellNameTv, mSummonerSpellDescriptionTv, mSummonerSpellCooldownTv;
    private TextView mSummonerSpellCooldownLabelTv;
    private TextView mEmptyTv;
    private SummonerSpellModel mViewModel;
    private String mPatchVersion;

    public SummonerSpellDetailFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_summoner_spell_detail, container, false);
        mScrollView = rootView.findViewById(R.id.nested_scroll_view);
        mSummonerSpellImage = rootView.findViewById(R.id.summoner_spell_detail_image);
        mSummonerSpellNameTv = rootView.findViewById(R.id.summoner_spell_detail_name);
        mSummonerSpellDescriptionTv = rootView.findViewById(R.id.summoner_spell_detail_description);
        mSummonerSpellCooldownTv = rootView.findViewById(R.id.summoner_spell_detail_cooldown);
        mSummonerSpellCooldownLabelTv = rootView.findViewById(R.id.summoner_spell_detail_cooldown_label);
        mEmptyTv = rootView.findViewById(R.id.empty_view_tv);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPatchVersion = LeaguePreferences.getPatchVersion(getContext());
        setupViewModel();
    }

    private void setupViewModel() {
        SummonerSpellModelFactory factory =
                InjectorUtils.provideSummonerSpellModelFactory(getActivity().getApplicationContext());
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(SummonerSpellModel.class);
        mViewModel.getSummonerSpell().observe(this, new Observer<SummonerSpellEntry>() {
            @Override
            public void onChanged(@Nullable SummonerSpellEntry summonerSpellEntry) {
                Log.d(LOG_TAG, "Receiving database update from LiveData");
                updateUi(summonerSpellEntry);
            }
        });
    }

    private void updateUi(@Nullable SummonerSpellEntry spellEntry) {
        mScrollView.fullScroll(View.FOCUS_UP);
        if (spellEntry != null) {
            showViews();

            GlideApp.with(this)
                    .load(GlideUtils.getSpellUrl(mPatchVersion, spellEntry.getImageId()))
                    .roundedImage()
                    .into(mSummonerSpellImage);

            String name = spellEntry.getName();
            String description = spellEntry.getDescription();
            int cooldown = spellEntry.getCooldown();

            mSummonerSpellNameTv.setText(name);
            mSummonerSpellDescriptionTv.setText(description);
            mSummonerSpellCooldownTv.setText(String.valueOf(cooldown));
        } else {
            hideViews();
        }
    }

    private void showViews() {
        mEmptyTv.setVisibility(View.INVISIBLE);
        mSummonerSpellImage.setVisibility(View.VISIBLE);
        mSummonerSpellNameTv.setVisibility(View.VISIBLE);
        mSummonerSpellDescriptionTv.setVisibility(View.VISIBLE);
        mSummonerSpellCooldownTv.setVisibility(View.VISIBLE);
        mSummonerSpellCooldownLabelTv.setVisibility(View.VISIBLE);
    }

    private void hideViews() {
        mEmptyTv.setVisibility(View.VISIBLE);
        mSummonerSpellImage.setVisibility(View.INVISIBLE);
        mSummonerSpellNameTv.setVisibility(View.INVISIBLE);
        mSummonerSpellDescriptionTv.setVisibility(View.INVISIBLE);
        mSummonerSpellCooldownTv.setVisibility(View.INVISIBLE);
        mSummonerSpellCooldownLabelTv.setVisibility(View.INVISIBLE);
    }
}
