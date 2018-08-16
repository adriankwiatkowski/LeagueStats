package com.example.android.leaguestats;

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

import com.example.android.leaguestats.database.AppDatabase;
import com.example.android.leaguestats.database.SummonerSpellEntry;
import com.example.android.leaguestats.utilities.PreferencesUtils;
import com.example.android.leaguestats.viewModels.SummonerSpellSharedViewModel;
import com.example.android.leaguestats.viewModels.SummonerSpellSharedViewModelFactory;
import com.squareup.picasso.Picasso;

public class SummonerSpellDetailFragment extends Fragment {

    private static final String LOG_TAG = SummonerSpellDetailFragment.class.getSimpleName();
    private ImageView mSummonerSpellImage;
    private TextView mSummonerSpellNameTv;
    private TextView mSummonerSpellDescriptionTv;
    private TextView mSummonerSpellCooldownTv;
    private String mPatchVersion;
    private AppDatabase mDb;

    public SummonerSpellDetailFragment() {}

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

        mPatchVersion = PreferencesUtils.getPatchVersion(getContext());

        mDb = AppDatabase.getInstance(getActivity().getApplicationContext());

        setupViewModel();
    }

    private void setupViewModel() {
        Log.d(LOG_TAG, "Getting summonerSpell");
        SummonerSpellSharedViewModelFactory factory = new SummonerSpellSharedViewModelFactory(mDb);
        final SummonerSpellSharedViewModel viewModel =
                ViewModelProviders.of(getActivity(), factory).get(SummonerSpellSharedViewModel.class);
        viewModel.getSelected().observe(this, new Observer<SummonerSpellEntry>() {
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

        Picasso.get()
                .load("http://ddragon.leagueoflegends.com/cdn/" + mPatchVersion + "/img/spell/" + image)
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(mSummonerSpellImage);
    }
}
