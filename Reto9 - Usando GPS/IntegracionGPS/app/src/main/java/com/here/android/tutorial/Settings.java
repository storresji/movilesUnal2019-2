package com.here.android.tutorial;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Settings extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        final SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        final SharedPreferences.Editor ed = prefs.edit();

        final EditTextPreference victoryMessagePref = (EditTextPreference)
                findPreference("radio");
        String winner = prefs.getString("radio", getResources().getString(R.string.radio));
        victoryMessagePref.setSummary((CharSequence) winner);

        victoryMessagePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                victoryMessagePref.setSummary((CharSequence) newValue);

                //SharedPreferences.Editor ed = prefs.edit();
                ed.putString("radio", newValue.toString());
                ed.commit();
                return true;
            }
        });

        /*final Preference resetPreference = findPreference("reset_conf");
        resetPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences.Editor ed = prefs.edit();
                ed.putString("difficulty_level", getResources().getString(R.string.difficulty_expert));
                ed.putString("victory_message", getResources().getString(R.string.result_human_wins));
                ed.putInt("color_board", Color.LTGRAY);
                ed.putBoolean("sound",true);
                ed.commit();
                difficultyLevelPref.setSummary((CharSequence) getResources().getString(R.string.difficulty_expert));
                victoryMessagePref.setSummary((CharSequence) getResources().getString(R.string.result_human_wins));
                soundPreference.setChecked(true);
                color = Color.LTGRAY;
                return true;
            }
        });*/
    }

}


