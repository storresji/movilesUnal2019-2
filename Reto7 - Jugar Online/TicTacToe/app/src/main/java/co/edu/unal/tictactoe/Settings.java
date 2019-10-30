package co.edu.unal.tictactoe;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import android.preference.CheckBoxPreference;

import com.flask.colorpicker.OnColorChangedListener;

public class Settings extends PreferenceActivity {

    private int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        final CheckBoxPreference soundPreference = (CheckBoxPreference) findPreference("sound");

        final SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        final ListPreference difficultyLevelPref = (ListPreference) findPreference("difficulty_level");
        String difficulty = prefs.getString("difficulty_level",
                getResources().getString(R.string.difficulty_expert));
        difficultyLevelPref.setSummary((CharSequence) difficulty);
        final SharedPreferences.Editor ed = prefs.edit();

        difficultyLevelPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                difficultyLevelPref.setSummary((CharSequence) newValue);
                // Since we are handling the pref, we must save it

                ed.putString("difficulty_level", newValue.toString());
                ed.commit();
                return true;
            }
        });


        final EditTextPreference victoryMessagePref = (EditTextPreference)
                findPreference("victory_message");
        String winner = prefs.getString("victory_message", getResources().getString(R.string.result_no_winner));
        victoryMessagePref.setSummary((CharSequence) winner);

        victoryMessagePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                victoryMessagePref.setSummary((CharSequence) newValue);

                //SharedPreferences.Editor ed = prefs.edit();
                ed.putString("victory_message", newValue.toString());
                ed.commit();
                return true;
            }
        });

        final Preference dialogPreference = findPreference("color_board");
        dialogPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                // dialog code here
                color = prefs.getInt("color_board",Color.LTGRAY);
                new ColorPickerDialog(Settings.this, new SetColorBoard(), color).show();
                return true;
            }
        });

        final Preference resetPreference = findPreference("reset_conf");
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
        });
    }
    public class SetColorBoard implements ColorPickerDialog.OnColorChangedListener {

        @Override
        public void colorChanged(int color) {
            final SharedPreferences prefs =
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor ed = prefs.edit();
            ed.putInt("color_board", color);
            ed.commit();
        }
    }

    public void setColor(int color){
        this.color = color;
    }

    public int getColor(){
        return color;
    }
}


