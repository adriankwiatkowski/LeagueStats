package com.example.android.leaguestats.ui;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.viewmodels.ItemModel;
import com.example.android.leaguestats.viewmodels.ItemModelFactory;

public class ItemActivity extends AppCompatActivity implements ItemListFragment.OnItemSelectedListener {

    private static final String ITEM_LIST_FRAGMENT_TAG = "ITEM_LIST_FRAGMENT_TAG";
    private static final String ITEM_DETAIL_FRAGMENT_TAG = "ITEM_DETAIL_FRAGMENT_TAG";

    private NavigationController mNavigationController;

    private ItemModel mViewModel;
    private Toolbar mToolbar;
    private boolean mTwoPane;

    private SearchView mSearchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        mNavigationController = new NavigationController(this);

        mTwoPane = findViewById(R.id.item_detail_container) != null;
        mSearchView = findViewById(R.id.search_view);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupViewModel();
        setupSearchView();

        if (savedInstanceState == null) {
            if (mTwoPane) {
                mNavigationController.addFragment(R.id.item_container, new ItemListFragment(), ITEM_LIST_FRAGMENT_TAG);
                mNavigationController.addFragment(R.id.item_detail_container, new ItemDetailFragment(), ITEM_DETAIL_FRAGMENT_TAG);
            } else {
                mNavigationController.addFragment(R.id.item_container, new ItemListFragment(), ITEM_LIST_FRAGMENT_TAG);
            }
        }
    }

    private void setupViewModel() {
        ItemModelFactory factory = InjectorUtils.provideItemModelFactory(getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(ItemModel.class);
    }

    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.onActionViewExpanded();
        mSearchView.clearFocus();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mViewModel.initItem(query);
                addDetailFragment();
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                ItemListFragment itemListFragment =
                        (ItemListFragment) getSupportFragmentManager().findFragmentByTag(ITEM_LIST_FRAGMENT_TAG);
                if (itemListFragment != null && itemListFragment.isVisible()) {
                    itemListFragment.filterResults(query);
                }
                return false;
            }
        });
    }

    @Override
    public void onItemSelected(int id) {
        mViewModel.initItem(id);
        addDetailFragment();
    }

    private void addDetailFragment() {
        if (mTwoPane) {
            mNavigationController.addDetailFragment(
                    R.id.item_detail_container, new ItemDetailFragment(),
                    ITEM_DETAIL_FRAGMENT_TAG, mTwoPane);
        } else {
            mNavigationController.addDetailFragment(
                    R.id.item_container, new ItemDetailFragment(),
                    ITEM_DETAIL_FRAGMENT_TAG, mTwoPane);
        }
    }
}
