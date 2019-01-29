package com.example.android.leaguestats.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.utilities.DataUtils;

public class SummonerSearchFragment extends Fragment {

    public interface OnSummonerListener {
        void onSummonerSearch(String entryUrlString, String summonerName);
    }

    private OnSummonerListener mCallback;
    private EditText mSummonerEdit;
    private Spinner mRegionSpinner;
    private Button mSubmitButton;
    private String mEntryRegion;

    public SummonerSearchFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_summoner_search, container, false);

        mSummonerEdit = rootView.findViewById(R.id.summoner_name_edit);
        mRegionSpinner = rootView.findViewById(R.id.summoner_region_spinner);
        mSubmitButton = rootView.findViewById(R.id.summoner_search_button);

        setupSpinner();

        mSummonerEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    searchSummoner();
                    return true;
                }
                return false;
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchSummoner();
            }
        });

        return rootView;
    }

    private void searchSummoner() {
        String summonerName = mSummonerEdit.getText().toString().trim();
        if (!(summonerName.isEmpty())) {
            mCallback.onSummonerSearch(mEntryRegion, summonerName);
        } else {
            Toast.makeText(getContext(), R.string.enter_summoner_name, Toast.LENGTH_LONG).show();
        }
    }

    private void setupSpinner() {
        ArrayAdapter regionSpinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.string_region_array, android.R.layout.simple_spinner_item);

        regionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mRegionSpinner.setAdapter(regionSpinnerAdapter);

        mRegionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    mEntryRegion = DataUtils.ENTRY_URL_SUMMONER_ARRAY[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnSummonerListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnSummonerListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }
}
