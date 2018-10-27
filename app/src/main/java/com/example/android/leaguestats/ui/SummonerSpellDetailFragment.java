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
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.data.database.entity.SummonerSpellEntry;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.utilities.PicassoUtils;
import com.example.android.leaguestats.utilities.LeaguePreferences;
import com.example.android.leaguestats.viewmodels.SummonerSpellModelFactory;
import com.example.android.leaguestats.viewmodels.SummonerSpellModel;
import com.squareup.picasso.Picasso;

public class SummonerSpellDetailFragment extends Fragment {

    private static final String LOG_TAG = SummonerSpellDetailFragment.class.getSimpleName();
    private ImageView mSummonerSpellImage;
    private TextView mSummonerSpellNameTv;
    private TextView mSummonerSpellDescriptionTv;
    private TextView mSummonerSpellCooldownTv;
    private String mPatchVersion;
    private SummonerSpellModel mViewModel;

    public SummonerSpellDetailFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        mPatchVersion = LeaguePreferences.getPatchVersion(getContext());

        setupViewModel();
    }

    private void setupViewModel() {
        SummonerSpellModelFactory factory =
                InjectorUtils.provideSummonerSpellModelFactory(getActivity().getApplicationContext());
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(SummonerSpellModel.class);
        mViewModel.getSummonerSpell().observe(getActivity(), new Observer<SummonerSpellEntry>() {
            @Override
            public void onChanged(@Nullable SummonerSpellEntry summonerSpellEntry) {
                Log.d(LOG_TAG, "Receiving database update from LiveData");
                updateUi(summonerSpellEntry);
            }
        });
    }

    private void updateUi(@Nullable SummonerSpellEntry spellEntry) {
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
}
