package com.example.keepcode;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import net.cryptobrewery.syntaxview.SyntaxView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        String file = "";

        TextView txtView1 = (TextView) findViewById(R.id.textView2);
        TextView txtView2 = (TextView) findViewById(R.id.textView3);
        TextView txtView3 = (TextView) findViewById(R.id.textView4);

        // Set the fonts
        Typeface tf1 = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        txtView1.setTypeface(tf1);
        Typeface tf2 = Typeface.createFromAsset(getAssets(), "progfont.ttf");
        txtView2.setTypeface(tf2);
        Typeface tf3 = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        txtView3.setTypeface(tf3);
    }

    public abstract class MySettingsFragment extends PreferenceFragmentCompat
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.root_preferences);
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        }
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
}