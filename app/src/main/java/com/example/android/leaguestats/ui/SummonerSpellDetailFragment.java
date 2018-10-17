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
import com.example.android.leaguestats.data.database.entity.SummonerSpellEntry;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.utilities.PicassoUtils;
import com.example.android.leaguestats.utilities.LeaguePreferences;
import com.example.android.leaguestats.viewmodels.SummonerSpellDetailModel;
import com.example.android.leaguestats.viewmodels.SummonerSpellDetailModelFactory;
import com.squareup.picasso.Picasso;

public class SummonerSpellDetailFragment extends Fragment {

    private static final String LOG_TAG = SummonerSpellDetailFragment.class.getSimpleName();
    private ImageView mSummonerSpellImage;
    private TextView mSummonerSpellNameTv;
    private TextView mSummonerSpellDescriptionTv;
    private TextView mSummonerSpellCooldownTv;
    private String mPatchVersion;
    private int mSummonerSpellId;
    private SummonerSpellDetailModel mViewModel;
    private static final String SUMMONER_SPELL_EXTRA_ID = "SUMMONER_SPELL_EXTRA_ID";

    public SummonerSpellDetailFragment() {}

    public static SummonerSpellDetailFragment newInstance(int id) {
        SummonerSpellDetailFragment fragment = new SummonerSpellDetailFragment();
        Bundle args = new Bundle();
        args.putInt(SUMMONER_SPELL_EXTRA_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mSummonerSpellId = args.getInt(SUMMONER_SPELL_EXTRA_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_summoner_spell_detail, container, false);

        mSummonerSpellImage = rootView.findViewById(R.id.summoner_spell_detail_image);
        mSummonerSpellNameTv = rootView.findViewById(R.id.summoner_spell_detail_name);
        mSummonerSpellDescriptionTv = rootView.findViewById(R.id.summoner_spell_detail_description);
        mSummonerSpellCooldownTv = rootView.findViewById(R.id.summoner_spell_detail_cooldown);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "onActivityCreated");

        if (savedInstanceState != null && savedInstanceState.containsKey(SUMMONER_SPELL_EXTRA_ID)) {
            mSummonerSpellId = savedInstanceState.getInt(SUMMONER_SPELL_EXTRA_ID);
        }

        mPatchVersion = LeaguePreferences.getPatchVersion(getContext());

        setupViewModel();
    }

    private void setupViewModel() {
        Log.d(LOG_TAG, "Getting summonerSpell");
        SummonerSpellDetailModelFactory factory =
                InjectorUtils.provideSummonerSpellDetailModelFactory(getActivity().getApplicationContext(), mSummonerSpellId);
        mViewModel = ViewModelProviders.of(this, factory).get(SummonerSpellDetailModel.class);
        mViewModel.getSummonerSpell().observe(this, new Observer<SummonerSpellEntry>() {
            @Override
            public void onChanged(@Nullable SummonerSpellEntry summonerSpellEntry) {
                Log.d(LOG_TAG, "Receiving database update from LiveData");
                updateUi(summonerSpellEntry);
            }
        });
    }

    private void updateUi(@Nullable SummonerSpellEntry spellEntry) {
        Log.d(LOG_TAG, "updating Ui");
        String image = spellEntry.getImage();
        String name = spellEntry.getName();
        String description = spellEntry.getDescription();
        int cooldown = spellEntry.getCooldown();

        mSummonerSpellNameTv.setText(name);
        mSummonerSpellDescriptionTv.setText(description);
        mSummonerSpellCooldownTv.setText(String.valueOf(cooldown));

        PicassoUtils.setSpellImage(mSummonerSpellImage, image, mPatchVersion);

        Picasso.get()
                .load("http://ddragon.leagueoflegends.com/cdn/" + mPatchVersion + "/img/spell/" + image)
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(mSummonerSpellImage);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SUMMONER_SPELL_EXTRA_ID, mSummonerSpellId);
    }
}
