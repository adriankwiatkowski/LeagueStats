package com.example.android.leaguestats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.leaguestats.utilities.LocaleUtils;

public class ItemActivity extends AppCompatActivity implements ItemListFragment.OnItemSelected {

    private static final String LOG_TAG = ItemActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        if (savedInstanceState == null) {
            ItemListFragment itemListFragment = new ItemListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_container, itemListFragment).commit();
        }
    }

    @Override
    public void onItemSelected() {
        ItemDetailFragment itemDetailFragment = new ItemDetailFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.item_container, itemDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_language:
                LocaleUtils.changeLanguage(ItemActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
