package com.example.android.leaguestats;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.android.leaguestats.utilities.LocaleUtils;
import com.example.android.leaguestats.utilities.PreferencesUtils;

public class MainActivity extends AppCompatActivity {

    private Button buttonPl, buttonEn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkIfLanguageIsSaved();

        buttonPl = findViewById(R.id.buttonPl);
        buttonEn = findViewById(R.id.buttonEn);

        buttonPl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserLanguage("pl_PL");
                startActivity(new Intent(MainActivity.this, NavigationActivity.class));
            }
        });

        buttonEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserLanguage("en_US");
                startActivity(new Intent(MainActivity.this, NavigationActivity.class));
            }
        });
    }

    private void checkIfLanguageIsSaved() {
        Intent intent = getIntent();
        if (!intent.hasExtra(getString(R.string.change_language_key))) {
            boolean isSaved = LocaleUtils.didUserSelectedLanguage(getBaseContext());
            if (isSaved) {
                startActivity(new Intent(MainActivity.this, NavigationActivity.class));
                // Prevent to go back to the previous activity
                finish();
            }
        }
    }

    private void saveUserLanguage(String language) {
        LocaleUtils.setLocale(getBaseContext(), language);
        PreferencesUtils.saveUserLanguage(getBaseContext(), language);
    }
}
