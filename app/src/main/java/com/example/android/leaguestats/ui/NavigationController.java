package com.example.android.leaguestats.ui;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.android.leaguestats.R;

class NavigationController {

    private final FragmentManager mFragmentManager;

    NavigationController(AppCompatActivity appCompatActivity) {
        mFragmentManager = appCompatActivity.getSupportFragmentManager();
    }

    void addFragment(int containerId, Fragment fragment, String tag) {
        mFragmentManager.beginTransaction()
                .add(containerId, fragment, tag)
                .commit();
    }

    void addDetailFragment(int containerId, Fragment fragment, String tag, boolean twoPane) {
        addDetailFragment(containerId, fragment, tag, twoPane, true);
    }

    void addDetailFragment(int containerId, Fragment fragment, String tag,
                           boolean twoPane, boolean addToBackStack) {
        if (twoPane) {
            mFragmentManager.beginTransaction()
                    .replace(containerId, fragment, tag)
                    .commit();
        } else {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                        R.anim.slide_in_left, R.anim.slide_out_right);
            }
            Fragment detailFragment = mFragmentManager.findFragmentByTag(tag);
            if (detailFragment != null) {
                removeFragment(detailFragment);
            }
            if (addToBackStack) {
                transaction.addToBackStack(null);
            }
            transaction.replace(containerId, fragment, tag)
                    .commit();
        }
    }

    void removeFragment(Fragment fragment) {
        mFragmentManager.beginTransaction()
                .remove(fragment)
                .commit();
        mFragmentManager.popBackStack();
    }
}
