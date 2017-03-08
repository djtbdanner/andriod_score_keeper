package codepath.apps.demointroandroid.util;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import codepath.apps.demointroandroid.R;
import codepath.apps.demointroandroid.domain.ScoreKeeperPrefKeys;

public class CommonPreferencesUtility {

    public static void pullDataFromPreferences(SharedPreferences sharedPref, ActivityWithState theActivity) {
        //  SharedPreferences sharedPref = theActivity.getSharedPreferences(ScoreKeeperPrefKeys.SHARED_PREFERENCES.name(), Context.MODE_PRIVATE);
        theActivity.getScoreKeeperData().leftScore = sharedPref.getInt(ScoreKeeperPrefKeys.LEFT_SCORE.name(), 0);
        theActivity.getScoreKeeperData().rightScore = sharedPref.getInt(ScoreKeeperPrefKeys.RIGHT_SCORE.name(), 0);
        theActivity.getScoreKeeperData().pointsForGoal = sharedPref.getInt(ScoreKeeperPrefKeys.POINT_PER_GOAL.name(), 1);
        theActivity.getScoreKeeperData().resetScoreTo = sharedPref.getInt(ScoreKeeperPrefKeys.RESET_SCORE_TO.name(), 0);

        theActivity.getScoreKeeperData().leftTeamName = sharedPref.getString(ScoreKeeperPrefKeys.LEFT_TEAM_NAME.name(), "Us");
        theActivity.getScoreKeeperData().rightTeamName = sharedPref.getString(ScoreKeeperPrefKeys.RIGHT_TEAM_NAME.name(), "Them");

        theActivity.getScoreKeeperData().fileSaveFeatureDate = sharedPref.getString(ScoreKeeperPrefKeys.FILE_SAVE_FEATURE_DATE.name(), null);
        theActivity.getScoreKeeperData().fileSaveForToday = sharedPref.getBoolean(ScoreKeeperPrefKeys.FILE_SAVE_FEATURE_SWITCH.name(), false);
        if (theActivity.getScoreKeeperData().fileSaveFeatureDate == null || !theActivity.getScoreKeeperData().fileSaveFeatureDate.equals(ScoreKeeperUtils.getTodayAsNoTimeString())) {
            SharedPreferences defaultSharedPrefs = PreferenceManager.getDefaultSharedPreferences(theActivity.getBaseContext());
            defaultSharedPrefs.edit().putBoolean(theActivity.getResources().getString(R.string.save_todays_games_key), false);
            theActivity.getScoreKeeperData().fileSaveFeatureDate = null;
            theActivity.getScoreKeeperData().fileSaveForToday = false;
        }
        int winByPoints = sharedPref.getInt(ScoreKeeperPrefKeys.GAME_POINT.name(), -1);
        int winBySpread = sharedPref.getInt(ScoreKeeperPrefKeys.GAME_POINT_SPREAD.name(), -1);

        if (winByPoints > -1 && winBySpread > -1) {
            theActivity.getScoreKeeperData().setGamePoint(winByPoints, winBySpread);
        } else {
            theActivity.getScoreKeeperData().gamePoint = null;
        }
        theActivity.getScoreKeeperData().disableTiltFeature = sharedPref.getBoolean(ScoreKeeperPrefKeys.DISABLE_TILT_FEATURE.name(), false);
        theActivity.getScoreKeeperData().hasCelebratedWin = sharedPref.getBoolean(ScoreKeeperPrefKeys.HAS_CELEBRATED_WIN.name(), false);
        theActivity.getScoreKeeperData().fontName = sharedPref.getString(ScoreKeeperPrefKeys.FONT_NAME.name(), "Default");
    }


    public static void storeCommonScoreKeeperDatas(SharedPreferences.Editor editor, ActivityWithState theActivity) {
        // -- TODO -- update score keeper data to use the preferences properly
        SharedPreferences defaultSharedPrefs = PreferenceManager.getDefaultSharedPreferences(theActivity.getBaseContext());
        int pointsForGoal = Integer.valueOf(defaultSharedPrefs.getString(theActivity.getResources().getString(R.string.points_per_goal_key), "1"));
        int resetScoreTo = Integer.valueOf(defaultSharedPrefs.getString(theActivity.getResources().getString(R.string.initial_score_key), "0"));
        boolean gamePoint = defaultSharedPrefs.getBoolean(theActivity.getResources().getString(R.string.game_point_key), false);
        int gamePointScore = Integer.valueOf(defaultSharedPrefs.getString(theActivity.getResources().getString(R.string.game_point_score_key), "0"));
        int gamePointMargin = Integer.valueOf(defaultSharedPrefs.getString(theActivity.getResources().getString(R.string.game_point_margin_key), "0"));

        boolean disableTiltFeature = defaultSharedPrefs.getBoolean(theActivity.getResources().getString(R.string.disable_tilt_feature_key), false);
        boolean fileSaveFeature = defaultSharedPrefs.getBoolean(theActivity.getResources().getString(R.string.save_todays_games_key), false);
        String selectedFont = defaultSharedPrefs.getString(theActivity.getResources().getString(R.string.select_font_key), "Default");

//        editor.putInt(ScoreKeeperPrefKeys.POINT_PER_GOAL.name(), theActivity.getScoreKeeperData().pointsForGoal);
//        editor.putInt(ScoreKeeperPrefKeys.RESET_SCORE_TO.name(), theActivity.getScoreKeeperData().resetScoreTo);
        editor.putInt(ScoreKeeperPrefKeys.POINT_PER_GOAL.name(), pointsForGoal);
        editor.putInt(ScoreKeeperPrefKeys.RESET_SCORE_TO.name(), resetScoreTo);
        // if (theActivity.getScoreKeeperData().gamePoint != null){
        if (gamePoint) {
//            editor.putInt(ScoreKeeperPrefKeys.GAME_POINT.name(), theActivity.getScoreKeeperData().gamePoint.getGamePoint());
//            editor.putInt(ScoreKeeperPrefKeys.GAME_POINT_SPREAD.name(), theActivity.getScoreKeeperData().gamePoint.getPointSpread());
            editor.putInt(ScoreKeeperPrefKeys.GAME_POINT.name(), gamePointScore);
            editor.putInt(ScoreKeeperPrefKeys.GAME_POINT_SPREAD.name(), gamePointMargin);
        } else {
            editor.putInt(ScoreKeeperPrefKeys.GAME_POINT.name(), -1);
            editor.putInt(ScoreKeeperPrefKeys.GAME_POINT_SPREAD.name(), -1);
        }
//        editor.putBoolean(ScoreKeeperPrefKeys.DISABLE_TILT_FEATURE.name(), theActivity.getScoreKeeperData().disableTiltFeature);
        editor.putBoolean(ScoreKeeperPrefKeys.DISABLE_TILT_FEATURE.name(), disableTiltFeature);
        editor.putString(ScoreKeeperPrefKeys.FILE_SAVE_FEATURE_DATE.name(), theActivity.getScoreKeeperData().fileSaveFeatureDate);
        //editor.putBoolean(ScoreKeeperPrefKeys.FILE_SAVE_FEATURE_SWITCH.name(), theActivity.getScoreKeeperData().fileSaveForToday);
        editor.putBoolean(ScoreKeeperPrefKeys.FILE_SAVE_FEATURE_SWITCH.name(), fileSaveFeature);
        editor.putString(ScoreKeeperPrefKeys.LEFT_TEAM_NAME.name(), theActivity.getScoreKeeperData().leftTeamName);
        editor.putString(ScoreKeeperPrefKeys.RIGHT_TEAM_NAME.name(), theActivity.getScoreKeeperData().rightTeamName);
        editor.putBoolean(ScoreKeeperPrefKeys.HAS_CELEBRATED_WIN.name(), theActivity.getScoreKeeperData().hasCelebratedWin);
        //editor.putString(ScoreKeeperPrefKeys.FONT_NAME.name(), theActivity.getScoreKeeperData().fontName);
        editor.putString(ScoreKeeperPrefKeys.FONT_NAME.name(), selectedFont);
        editor.commit();
    }
}