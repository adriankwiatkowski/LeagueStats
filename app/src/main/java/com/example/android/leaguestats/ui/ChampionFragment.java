package com.example.android.leaguestats.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.adapters.ChampionPagerAdapter;
import com.example.android.leaguestats.data.database.entity.ChampionEntry;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.viewmodels.ChampionModel;
import com.example.android.leaguestats.viewmodels.ChampionModelFactory;

public class ChampionFragment extends Fragment {

    private static final String LOG_TAG = ChampionFragment.class.getSimpleName();
    public static final String CHAMPION_ID_KEY = "champion_id_key";
    private ChampionModel mViewModel;

    public static final String CHAMPION_OVERVIEW_TAG = "champion-overview-tag";
    public static final String CHAMPION_INFO_TAG = "champion-info-tag";
    public static final String CHAMPION_TIPS_TAG = "champion-tips-tag";

    private boolean mTwoPane;

    public ChampionFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_champion, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupViewModel();

        mTwoPane = getView().findViewById(R.id.champion_detail_info_container) != null;

        if (mTwoPane) {
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (fragmentManager.findFragmentByTag(CHAMPION_OVERVIEW_TAG) == null) {
                transaction.add(R.id.champion_detail_overview_container, new ChampionOverviewFragment(), CHAMPION_OVERVIEW_TAG);
            }
            if (fragmentManager.findFragmentByTag(CHAMPION_INFO_TAG) == null) {
                transaction.add(R.id.champion_detail_info_container, new ChampionInfoFragment(), CHAMPION_INFO_TAG);
            }
            if (fragmentManager.findFragmentByTag(CHAMPION_TIPS_TAG) == null) {
                transaction.add(R.id.champion_detail_tips_container, new ChampionTipsFragment(), CHAMPION_TIPS_TAG);
            }
            transaction.commit();
        } else {
            setupViewPager();
        }

//        handleIntent(getIntent());
    }

    private void setupViewModel() {
        ChampionModelFactory factory =
                InjectorUtils.provideChampionModelFactory(getContext().getApplicationContext());
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(ChampionModel.class);
        mViewModel.getChampion().observe(getActivity(), new Observer<ChampionEntry>() {
            @Override
            public void onChanged(@Nullable ChampionEntry championEntry) {
                Log.d(LOG_TAG, "Receiving database update from LiveData");
                updateUi(championEntry);
            }
        });
    }

    private void setupViewPager() {
        View rootView = getView();
        ViewPager viewPager = rootView.findViewById(R.id.champion_viewpager);
        ChampionPagerAdapter championPagerAdapter =
                new ChampionPagerAdapter(getContext(), getChildFragmentManager());
        viewPager.setAdapter(championPagerAdapter);

        TabLayout tabLayout = rootView.findViewById(R.id.champion_tab);
        tabLayout.setupWithViewPager(viewPager);
    }

//    TODO check all commented code.
//    @Override
//    protected void onNewIntent(Intent intent) {
//        setIntent(intent);
//        handleIntent(intent);
//    }
//
//
//
//    private void handleIntent(Intent intent) {
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            mViewModel.initChampion(query);
//        } else if (intent.hasExtra(CHAMPION_ID_KEY)) {
//            long championId = intent.getLongExtra(CHAMPION_ID_KEY, -1);
//            if (championId != - 1) {
//                mViewModel.initChampion(championId);
//            }
//        }
//    }
//

    private void updateUi(ChampionEntry championEntry) {
        /*
        if (championEntry != null) {
            String championName = championEntry.getName();
            String championTitle = championEntry.getTitle();

            getSupportActionBar().setTitle(championName);
            getSupportActionBar().setSubtitle(championTitle);
        }
        */
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.search_menu, menu);
//
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//
//        return true;
//    }

}
