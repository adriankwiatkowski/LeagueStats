package com.example.android.leaguestats;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.leaguestats.utilities.DataUtils;

public class SummonerSearchFragment extends Fragment {

    OnSubmitListener mCallback;

    public interface OnSubmitListener {
        void onMasteryListener(String summonerName, String entryString);
        void onHistoryListener(String summonerName, String entryString);
    }

    private static final String LOG_TAG = SummonerSearchFragment.class.getSimpleName();
    private EditText mUserNameEdit;
    private Button mMasteryButton;
    private Button mHistoryButton;
    private Spinner mRegionSpinner;
    private String mEntryRegion;

    public SummonerSearchFragment() {}

    public static SummonerSearchFragment newInstance() {
        SummonerSearchFragment summonerSearchFragment = new SummonerSearchFragment();
        return summonerSearchFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_summoner_search, container, false);

        mUserNameEdit = rootView.findViewById(R.id.user_name_edit);
        mMasteryButton = rootView.findViewById(R.id.show_mastery_button);
        mHistoryButton = rootView.findViewById(R.id.show_history_button);
        mRegionSpinner = rootView.findViewById(R.id.region_spinner);

        mMasteryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMastery();
            }
        });
        mHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHistory();
            }
        });

        setupSpinner();

        mUserNameEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    showMastery();
                    return true;
                }
                return false;
            }
        });

        return rootView;
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

    private void showMastery() {
        String summonerName = mUserNameEdit.getText().toString().trim();
        if (!(summonerName.isEmpty())) {
            mCallback.onMasteryListener(summonerName, mEntryRegion);
        } else {
            Toast.makeText(getContext(), R.string.enter_summoner_name, Toast.LENGTH_LONG).show();
        }
    }

    private void showHistory() {
        String summonerName = mUserNameEdit.getText().toString().trim();
        if (!(summonerName.isEmpty())) {
            mCallback.onHistoryListener(summonerName, mEntryRegion);
        } else {
            Toast.makeText(getContext(), R.string.enter_summoner_name, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnSubmitListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnSubmitListener");
        }
    }
}
