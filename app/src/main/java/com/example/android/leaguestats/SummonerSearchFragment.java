package com.example.android.leaguestats;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.leaguestats.utilities.DataUtils;

public class SummonerSearchFragment extends Fragment {

    // TODO USE THIS FRAGMENT!
    private EditText mUserNameEdit;
    private Spinner mRegionSpinner;
    private String mEntryRegion;

    public SummonerSearchFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_summoner_search, container, false);

        mUserNameEdit = rootView.findViewById(R.id.user_name_edit);
        mRegionSpinner = rootView.findViewById(R.id.region_spinner);

        setupSpinner();

        // Show keyboard.
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        mUserNameEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    //showMastery();
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

    private void hideKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        IBinder iBinder = getView().getRootView().getWindowToken();
        imm.hideSoftInputFromWindow(iBinder, 0);
    }
}
