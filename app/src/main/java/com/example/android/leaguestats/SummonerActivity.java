package com.example.android.leaguestats;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.leaguestats.adapters.SummonerPagerAdapter;
import com.example.android.leaguestats.models.Summoner;
import com.example.android.leaguestats.utilities.DataUtils;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.utilities.LocaleUtils;
import com.example.android.leaguestats.viewModels.SummonerModel;
import com.example.android.leaguestats.viewModels.SummonerModelFactory;

public class SummonerActivity extends AppCompatActivity
        implements SummonerMasteryFragment.MasteryListener,
        SummonerHistoryFragment.HistoryListener {

    private static final String LOG_TAG = SummonerActivity.class.getSimpleName();
    private SummonerModel mSummonerModel;
    private String mEntryUrlString = "";
    private String mSummonerName = "";
    private Summoner mSummoner;
    private static final String ENTRY_URL_STRING_KEY = "ENTRY_URL_STRING_KEY";
    private static final String SUMMONER_NAME_KEY = "SUMMONER_NAME_KEY";
    private EditText mSummonerEditText;
    private Spinner mSummonerSpinner;
    private Button mSummonerSubmitButton;
    private FragmentManager mFragmentManager;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner);

        mSummonerEditText = findViewById(R.id.summoner_edit_text);
        mSummonerSpinner = findViewById(R.id.summoner_spinner);
        mSummonerSubmitButton = findViewById(R.id.summoner_submit_button);

        mFragmentManager = getSupportFragmentManager();

        mSummonerSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSummoner();
            }
        });

        mSummonerEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    showSummoner();
                    return true;
                }
                return false;
            }
        });

        setupSpinner();
        setupViewPager();

        if (savedInstanceState == null) {
            showKeyboard();
        } else {
            if (savedInstanceState.containsKey(ENTRY_URL_STRING_KEY) &&
                    savedInstanceState.containsKey(SUMMONER_NAME_KEY)) {
                mEntryUrlString = savedInstanceState.getString(ENTRY_URL_STRING_KEY);
                mSummonerName = savedInstanceState.getString(SUMMONER_NAME_KEY);
            }
            hideKeyboard();
        }

        SummonerModelFactory factory =
                InjectorUtils.provideSummonerModelFactory(this.getApplicationContext());
        mSummonerModel = ViewModelProviders.of(this, factory).get(SummonerModel.class);
        Log.d(LOG_TAG, "mSummonerModel provided");

        if (mSummonerModel.getSummoner() != null) {
            Log.d(LOG_TAG, "Getting summoner");
            mSummoner = mSummonerModel.getSummoner().getValue();
        }
    }

    private void setupViewPager() {
        mViewPager = findViewById(R.id.summoner_viewpager);
        SummonerPagerAdapter summonerPagerAdapter =
                new SummonerPagerAdapter(this, mFragmentManager);
        mViewPager.setAdapter(summonerPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.summoner_tab);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onChampionClick(String championId) {
        Intent intent = new Intent(SummonerActivity.this, ChampionDetailActivity.class);
        intent.putExtra(ChampionDetailActivity.CHAMPION_ID_KEY, championId);
        startActivity(intent);
    }

    @Override
    public void onHistoryChampionListener(String championId) {

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
                LocaleUtils.changeLanguage(SummonerActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupSpinner() {
        ArrayAdapter regionSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.string_region_array, android.R.layout.simple_spinner_item);

        regionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mSummonerSpinner.setAdapter(regionSpinnerAdapter);

        mSummonerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    mEntryUrlString = DataUtils.ENTRY_URL_SUMMONER_ARRAY[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showSummoner() {
        String summonerName = mSummonerEditText.getText().toString().trim();
        if (!summonerName.isEmpty()) {
            initSummoner(mEntryUrlString, summonerName);
            // TODO
            //hideViews();
            hideKeyboard();
        } else {
            Toast.makeText(this, R.string.enter_summoner_name, Toast.LENGTH_LONG).show();
        }
    }

    private void initSummoner(final String entryUrlString, String summonerName) {
        mSummonerModel.setSummoner(entryUrlString, summonerName);
        mSummonerModel.getSummoner().observe(this, new Observer<Summoner>() {
            @Override
            public void onChanged(@Nullable Summoner summoner) {
                if (summoner != null) {
                    Log.d(LOG_TAG, "Receiving new summoner");
                    mSummoner = summoner;
                    mEntryUrlString = entryUrlString;

                    initSummonerInfoFragment(summoner);

                    initSummonerMasteryFragment();

                    initSummonerHistoryFragment();
                } else {
                    Toast.makeText(SummonerActivity.this, getString(R.string.summoner_not_found)
                            + " " + getString(R.string.check_spelling_and_region), Toast.LENGTH_LONG).show();
                    //onBackPressed();
                }
            }
        });
    }

    private void initSummonerInfoFragment(Summoner summoner) {
        SummonerInfoFragment summonerInfoFragment = (SummonerInfoFragment)
                mFragmentManager.findFragmentByTag(SummonerInfoFragment.TAG);

        if (summonerInfoFragment != null) {
            summonerInfoFragment.bindDataToViews(summoner);
        } else {
            SummonerInfoFragment newSummonerInfoFragment =
                    SummonerInfoFragment.newInstance(summoner);
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.add(R.id.summoner_container, newSummonerInfoFragment, SummonerInfoFragment.TAG);
            transaction.commit();
        }
    }

    private void initSummonerMasteryFragment() {
        SummonerPagerAdapter summonerPagerAdapter = (SummonerPagerAdapter) mViewPager.getAdapter();
        SummonerMasteryFragment summonerMasteryFragment = (SummonerMasteryFragment)
                summonerPagerAdapter.getFragment(SummonerPagerAdapter.SUMMONER_MASTERY_POSITION);

        if (summonerMasteryFragment != null) {
            Log.d(LOG_TAG, "summonerMasteryFragment not null");
            summonerMasteryFragment.initMasteryData(mEntryUrlString, mSummoner.getSummonerId());
        } else {
            Log.d(LOG_TAG, "summonerMasteryFragment null");
        }
    }

    private void initSummonerHistoryFragment() {
        SummonerPagerAdapter summonerPagerAdapter = (SummonerPagerAdapter) mViewPager.getAdapter();
        SummonerHistoryFragment summonerHistoryFragment = (SummonerHistoryFragment)
                summonerPagerAdapter.getFragment(SummonerPagerAdapter.SUMMONER_HISTORY_POSITION);
        if (summonerHistoryFragment != null) {
            Log.d(LOG_TAG, "summonerHistoryFragment not null");
            summonerHistoryFragment.initHistoryData(mEntryUrlString, mSummoner.getAccountId(), mSummoner.getSummonerId());
        } else {
            Log.d(LOG_TAG, "summonerHistoryFragment null");
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void hideViews() {
        mSummonerEditText.setVisibility(View.VISIBLE);
        mSummonerSpinner.setVisibility(View.VISIBLE);
        mSummonerSubmitButton.setVisibility(View.VISIBLE);
        mSummonerEditText.setVisibility(View.GONE);
        mSummonerSpinner.setVisibility(View.GONE);
        mSummonerSubmitButton.setVisibility(View.GONE);
    }

    private void showKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(ENTRY_URL_STRING_KEY, mEntryUrlString);
        outState.putString(SUMMONER_NAME_KEY, mSummonerName);
    }
}