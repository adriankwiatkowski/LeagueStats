package com.example.android.leaguestats.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.models.Summoner;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.utilities.PicassoUtils;
import com.example.android.leaguestats.utilities.LeaguePreferences;
import com.example.android.leaguestats.viewmodels.SummonerModel;
import com.example.android.leaguestats.viewmodels.SummonerModelFactory;

public class SummonerInfoFragment extends Fragment {

    private static final String LOG_TAG = SummonerInfoFragment.class.getSimpleName();
    public static final String TAG = "summoner-info-fragment";
    private ImageView mProfileIcon;
    private TextView mSummonerNameTv;
    private TextView mSummonerLevelTv;
    private TextView mSummonerRankTv;
    private ProgressBar mProgressIndicator;
    private String mPatchVersion;

    public SummonerInfoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_summoner_info, container, false);
        Log.d(LOG_TAG, "onCreateView");

        mProfileIcon = rootView.findViewById(R.id.summoner_info_profile);
        mSummonerNameTv = rootView.findViewById(R.id.summoner_info_name_tv);
        mSummonerLevelTv = rootView.findViewById(R.id.summoner_info_level_tv);
        mSummonerRankTv = rootView.findViewById(R.id.summoner_info_rank_tv);
        mProgressIndicator = rootView.findViewById(R.id.summoner_info_indicator);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "onActivityCreated");

        mPatchVersion = LeaguePreferences.getPatchVersion(getContext());

        setupViewModel();
    }

    private void setupViewModel() {
        SummonerModelFactory factory =
                InjectorUtils.provideSummonerModelFactory(getActivity().getApplicationContext());
        SummonerModel summonerModel =
                ViewModelProviders.of(getActivity(), factory).get(SummonerModel.class);
        summonerModel.getSummoner().observe(getActivity(), new Observer<Summoner>() {
            @Override
            public void onChanged(@Nullable Summoner summoner) {
                Log.d(LOG_TAG, "Getting summoner");
                updateUi(summoner);
            }
        });
    }

    private void updateUi(@Nullable Summoner summoner) {
        hideIndicator();
        if (summoner != null) {
            mSummonerNameTv.setText(summoner.getSummonerName());
            mSummonerLevelTv.setText(getString(R.string.level) + " " + String.valueOf(summoner.getSummonerLevel()));

            String iconPath = String.valueOf(summoner.getProfileIconId());

            PicassoUtils.setIconImage(mProfileIcon, iconPath, mPatchVersion,
                    R.dimen.icon_width, R.dimen.icon_height);
        }
    }

    public void showIndicator() {
        mProgressIndicator.setVisibility(View.VISIBLE);

        mSummonerNameTv.setVisibility(View.INVISIBLE);
        mSummonerLevelTv.setVisibility(View.INVISIBLE);
        mProfileIcon.setVisibility(View.INVISIBLE);
    }

    private void hideIndicator() {
        mProgressIndicator.setVisibility(View.INVISIBLE);

        mSummonerNameTv.setVisibility(View.VISIBLE);
        mSummonerLevelTv.setVisibility(View.VISIBLE);
        mProfileIcon.setVisibility(View.VISIBLE);
    }

    public void showHighestRank(String highestRank) {
        mSummonerRankTv.setText(highestRank);
    }
}
