package com.example.keepcode;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new SettingsFragment())
                .commit();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String font = sharedPref.getString("Font", "Roboto");
        txtView = (TextView) findViewById(R.id.textView);
        txtView.setTypeface(getFontTypeface(font));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener()
        {
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
            {
                String font = sharedPref.getString("Font", "Roboto");
                txtView.setTypeface(getFontTypeface(font));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }

    public Typeface getFontTypeface(String fontName)
    {
        String file = "";
        if (fontName.equals("Open Sans"))
        {
            file = "OpenSans-Regular.ttf";
        }
        if (fontName.equals("Inconsolata"))
        {
            file = "progfont.ttf";
        }
        if (fontName.equals("Roboto"))
        {
            file = "Roboto-Regular.ttf";
        }
        return Typeface.createFromAsset(getAssets(), file);
    }
}