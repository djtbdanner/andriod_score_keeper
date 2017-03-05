package codepath.apps.demointroandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.prefs.PreferenceChangeEvent;

import codepath.apps.demointroandroid.domain.ScoreKeeperData;
import codepath.apps.demointroandroid.domain.ScoreKeeperPrefKeys;
import codepath.apps.demointroandroid.util.ActivityWithState;
import codepath.apps.demointroandroid.util.CommonPreferencesUtility;
import codepath.apps.demointroandroid.util.ScoreKeeperUtils;

public class ScoreKeeperPreferencesActivity extends PreferenceActivity implements ActivityWithState {
    Preference gamePointMargin;
    Preference gamePoingScore;
    SharedPreferences defaultSharedPreferences;

    private ScoreKeeperData scoreKeeperData;

    public ScoreKeeperData getScoreKeeperData() {
        if (scoreKeeperData == null) {
            scoreKeeperData = new ScoreKeeperData();
        }
        return scoreKeeperData;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildTheScreen();
    }

    @Override
    protected void onStop(){
        super.onStop();
        SharedPreferences sharedPref = this.getSharedPreferences(ScoreKeeperPrefKeys.SHARED_PREFERENCES.toString(), Context.MODE_PRIVATE);
        CommonPreferencesUtility.storeCommonScoreKeeperDatas(sharedPref.edit(), this);
        //--TODO-- need to use the actual preferences instead of doing this
        Intent intent = new Intent(this, ScoreKeeperActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
    }

    private void buildTheScreen() {

        addPreferencesFromResource(R.layout.score_keeper_preferences);
        Preference button = getPreferenceManager().findPreference(getResources().getString(R.string.default_button_key));
        button.setOnPreferenceClickListener(preferenceResetClickListener);

        Preference exit = getPreferenceManager().findPreference(getResources().getString(R.string.exit_button_key));
        exit.setOnPreferenceClickListener(preferenceExitListener);

        Preference gamePointSelector = getPreferenceManager().findPreference(getResources().getString(R.string.game_point_key));
        gamePointSelector.setOnPreferenceChangeListener(gamePointMarginChangeListener);

        gamePointMargin = getPreferenceManager().findPreference(getResources().getString(R.string.game_point_margin_key));
        gamePointMargin.setOnPreferenceChangeListener(preferenceChangeListener);
        Preference isp = getPreferenceManager().findPreference(getResources().getString(R.string.initial_score_key));
        isp.setOnPreferenceChangeListener(preferenceChangeListener);
        Preference ppg = getPreferenceManager().findPreference(getResources().getString(R.string.points_per_goal_key));
        ppg.setOnPreferenceChangeListener(preferenceChangeListener);
        Preference fontPreference = getPreferenceManager().findPreference(getResources().getString(R.string.select_font_key));
        fontPreference.setOnPreferenceChangeListener(preferenceChangeListener);
        gamePoingScore = getPreferenceManager().findPreference(getResources().getString(R.string.game_point_score_key));
        gamePoingScore.setOnPreferenceChangeListener(preferenceChangeListener);

        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if (getScoreKeeperData().fileSaveFeatureDate != null && getScoreKeeperData().fileSaveFeatureDate != ScoreKeeperUtils.getTodayAsNoTimeString()){
            getScoreKeeperData().fileSaveFeatureDate = null;
            defaultSharedPreferences.edit().putBoolean(getResources().getString(R.string.save_todays_games_key), false);
            defaultSharedPreferences.edit().commit();
        }


        setGamePointSummaryValues(defaultSharedPreferences.getBoolean(getResources().getString(R.string.game_point_key), false));

        Preference fileSavePreference = getPreferenceManager().findPreference(getResources().getString(R.string.save_todays_games_key));
        fileSavePreference.setOnPreferenceChangeListener(fileSaveListener);

    }

    private void setGamePointSummaryValues(boolean showTheSummaries) {
        if (showTheSummaries) {
            gamePoingScore.setSummary(defaultSharedPreferences.getString(getResources().getString(R.string.game_point_score_key), ""));
            gamePointMargin.setSummary(defaultSharedPreferences.getString(getResources().getString(R.string.game_point_margin_key), ""));
        } else {
            gamePoingScore.setSummary("");
            gamePointMargin.setSummary("");
        }
    }

    private Preference.OnPreferenceClickListener preferenceResetClickListener = new Preference.OnPreferenceClickListener() {

        @Override
        public boolean onPreferenceClick(Preference p) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();
            editor.clear();
            editor.commit();
            PreferenceManager.setDefaultValues(getBaseContext(), R.layout.score_keeper_preferences, true);
            preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            setPreferenceScreen(null);
            buildTheScreen();
            return true;
        }
    };

    private Preference.OnPreferenceClickListener preferenceExitListener = new Preference.OnPreferenceClickListener() {

        @Override
        public boolean onPreferenceClick(Preference p) {
            finish();
            return true;
        }
    };


    private Preference.OnPreferenceChangeListener preferenceChangeListener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference p, Object newValue) {
            String currentSummary = p.getSummary().toString();
            if (currentSummary != null) {
                if (currentSummary.contains(":")) {
                    currentSummary = currentSummary.substring(0, currentSummary.indexOf(":") + 2);
                } else {
                    currentSummary = "";
                }
            } else {
                currentSummary = "";
            }
            currentSummary = currentSummary + newValue.toString();

            p.setSummary(currentSummary);
            return true;
        }
    };

    private Preference.OnPreferenceChangeListener gamePointMarginChangeListener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference p, Object newValue) {
            setGamePointSummaryValues(Boolean.valueOf(newValue.toString()));
            return true;
        }
    };

    private Preference.OnPreferenceChangeListener fileSaveListener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference p, Object newValue) {
            if (Boolean.valueOf(newValue.toString())) {
                getScoreKeeperData().fileSaveFeatureDate = ScoreKeeperUtils.getTodayAsNoTimeString();
            } else {
                getScoreKeeperData().fileSaveFeatureDate = null;
            }
            return true;
        }
    };

}
