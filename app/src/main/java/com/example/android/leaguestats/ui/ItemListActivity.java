package com.example.android.leaguestats.ui;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.viewmodels.ItemDetailModel;
import com.example.android.leaguestats.viewmodels.ItemDetailModelFactory;

public class ItemListActivity extends AppCompatActivity
        implements ItemListFragment.OnItemListSelected, ItemDetailFragment.OnItemDetailSelected {

    private static final String LOG_TAG = ItemListActivity.class.getSimpleName();
    private ItemDetailModel mItemDetailViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        if (savedInstanceState == null) {
            ItemListFragment itemListFragment = new ItemListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_container, itemListFragment).commit();
        }

        setupViewModel();

        handleIntent(getIntent());
    }

    private void setupViewModel() {

        ItemDetailModelFactory factory =
                InjectorUtils.provideItemDetailModelFactory(getApplicationContext());
        mItemDetailViewModel =
                ViewModelProviders.of(this, factory).get(ItemDetailModel.class);
    }

    @Override
    public void onItemSelected(long id) {

        initItem(id);

        ItemDetailFragment itemDetailFragment = new ItemDetailFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.item_container, itemDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onItemDetailSelected(long id) {
        initItem(id);
    }

    private void initItem(long id) {
        mItemDetailViewModel.initItem(id);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            mItemDetailViewModel.initItem(query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }
}
