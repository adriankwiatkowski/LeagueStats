package com.example.android.leaguestats;


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

import com.example.android.leaguestats.models.Summoner;
import com.example.android.leaguestats.utilities.PicassoUtils;
import com.example.android.leaguestats.utilities.PreferencesUtils;

public class SummonerInfoFragment extends Fragment {

    private static final String LOG_TAG = SummonerInfoFragment.class.getSimpleName();
    public static final String TAG = "summoner-info-fragment";
    private ImageView mProfileIcon;
    private TextView mSummonerNameTv;
    private TextView mSummonerLevelTv;
    private ProgressBar mProgressIndicator;
    private Summoner mSummoner;
    private String mPatchVersion;
    private static final String SUMMONER_KEY = "SUMMONER_KEY";

    public SummonerInfoFragment() {}

    public static SummonerInfoFragment newInstance(Summoner summoner) {
        SummonerInfoFragment summonerInfoFragment = new SummonerInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable(SUMMONER_KEY, summoner);
        summonerInfoFragment.setArguments(args);
        return summonerInfoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(SUMMONER_KEY)) {
            mSummoner = bundle.getParcelable(SUMMONER_KEY);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_summoner_info, container, false);
        Log.d(LOG_TAG, "onCreateView");

        mProfileIcon = rootView.findViewById(R.id.summoner_info_profile);
        mSummonerNameTv = rootView.findViewById(R.id.summoner_info_name_tv);
        mSummonerLevelTv = rootView.findViewById(R.id.summoner_info_level_tv);
        // TODO
        mProgressIndicator = rootView.findViewById(R.id.summoner_info_indicator);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "onActivityCreated");

        mPatchVersion = PreferencesUtils.getPatchVersion(getContext());

        bindDataToViews();
    }

    private void bindDataToViews() {
        mSummonerNameTv.setText(mSummoner.getSummonerName());
        mSummonerLevelTv.setText(getString(R.string.level) + " " + String.valueOf(mSummoner.getSummonerLevel()));

        String iconPath = String.valueOf(mSummoner.getProfileIconId());

        PicassoUtils.getIconCreator(iconPath, mPatchVersion, 200, 200).into(mProfileIcon);
    }

    public void bindDataToViews(Summoner summoner) {
        mSummoner = summoner;
        bindDataToViews();
    }
}
