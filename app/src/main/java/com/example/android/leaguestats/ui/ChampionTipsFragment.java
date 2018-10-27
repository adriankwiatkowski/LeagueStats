package com.example.android.leaguestats.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.utilities.PicassoUtils;
import com.example.android.leaguestats.viewmodels.ChampionModel;
import com.example.android.leaguestats.viewmodels.ChampionModelFactory;
import com.example.android.leaguestats.data.database.entity.ChampionEntry;

import java.util.List;

public class ChampionTipsFragment extends Fragment {

    private static final String LOG_TAG = ChampionTipsFragment.class.getSimpleName();
    private TextView mPlayingAsTv;
    private TextView mPlayingAgainstTv;
    private TextView mPlayingAsLabelTv;
    private TextView mPlayingAgainstLabelTv;
    private ImageView mSplashArtImage;

    public ChampionTipsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_champion_tips, container, false);

        mPlayingAsTv = rootView.findViewById(R.id.playing_as_tv);
        mPlayingAgainstTv = rootView.findViewById(R.id.playing_against_tv);
        mPlayingAsLabelTv = rootView.findViewById(R.id.playing_as_label_tv);
        mPlayingAgainstLabelTv = rootView.findViewById(R.id.playing_against_label_tv);

        if (rootView.findViewById(R.id.splash_art_tips_image) != null) {
            mSplashArtImage = rootView.findViewById(R.id.splash_art_tips_image);
        } else {
            mSplashArtImage = null;
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

            mPlayingAgainstLabelTv.setText(getString(R.string.playing_vs) + " " + championEntry.getName());

            List<String> vsTips = championEntry.getVsTips();
            for (int i = 0; i < vsTips.size(); i++) {
                mPlayingAgainstTv.append(String.valueOf(i + 1) + " " + vsTips.get(i));
                if (i != vsTips.size() - 1) {
                    mPlayingAgainstTv.append("\n");
                }
            }

            mPlayingAsLabelTv.setText(getString(R.string.playing_as) + " " + championEntry.getName());

            List<String> asTips = championEntry.getAsTips();
            for (int i = 0; i < asTips.size(); i++) {
                mPlayingAsTv.append(String.valueOf(i + 1) + " " + asTips.get(i));
                if (i != asTips.size() - 1) {
                    mPlayingAsTv.append("\n");
                }
            }
        }
    }
}
