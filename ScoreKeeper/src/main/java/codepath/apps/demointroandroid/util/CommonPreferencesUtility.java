package codepath.apps.demointroandroid.util;

import android.content.Context;
import android.content.SharedPreferences;

import codepath.apps.demointroandroid.domain.ScoreKeeperPrefKeys;

public class CommonPreferencesUtility {

    public static void setCommonScoreKeeperData(SharedPreferences sharedPref, ActivityWithState theActivity) {
        //  SharedPreferences sharedPref = theActivity.getSharedPreferences(ScoreKeeperPrefKeys.SHARED_PREFERENCES.name(), Context.MODE_PRIVATE);
        theActivity.getScoreKeeperData().leftScore = sharedPref.getInt(ScoreKeeperPrefKeys.LEFT_SCORE.name(), 0);
        theActivity.getScoreKeeperData().rightScore = sharedPref.getInt(ScoreKeeperPrefKeys.RIGHT_SCORE.name(), 0);
        theActivity.getScoreKeeperData().pointsForGoal = sharedPref.getInt(ScoreKeeperPrefKeys.POINT_PER_GOAL.name(), 1);
        theActivity.getScoreKeeperData().resetScoreTo = sharedPref.getInt(ScoreKeeperPrefKeys.RESET_SCORE_TO.name(), 0);

        theActivity.getScoreKeeperData().leftTeamName = sharedPref.getString(ScoreKeeperPrefKeys.LEFT_TEAM_NAME.name(), "Team");
        theActivity.getScoreKeeperData().rightTeamName = sharedPref.getString(ScoreKeeperPrefKeys.RIGHT_TEAM_NAME.name(), "Team");

        theActivity.getScoreKeeperData().fileSaveFeatureDate = sharedPref.getString(ScoreKeeperPrefKeys.FILE_SAVE_FEATURE_DATE.name(), null);
        theActivity.getScoreKeeperData().fileSaveForToday = sharedPref.getBoolean(ScoreKeeperPrefKeys.FILE_SAVE_FEATURE_SWITCH.name(), false);
        if (theActivity.getScoreKeeperData().fileSaveFeatureDate == null || !theActivity.getScoreKeeperData().fileSaveFeatureDate.equals(ScoreKeeperUtils.getTodayAsNoTimeString())) {
            theActivity.getScoreKeeperData().fileSaveFeatureDate = null;
            theActivity.getScoreKeeperData().fileSaveForToday = false;
        }
        int winByPoints = sharedPref.getInt(ScoreKeeperPrefKeys.GAME_POINT.name(), -1);
        int winBySpread = sharedPref.getInt(ScoreKeeperPrefKeys.GAME_POINT_SPREAD.name(), -1);

        if (winByPoints > -1 && winBySpread > -1) {
            theActivity.getScoreKeeperData().setGamePoint(winByPoints, winBySpread);
        }
        theActivity.getScoreKeeperData().disableTiltFeature = sharedPref.getBoolean(ScoreKeeperPrefKeys.DISABLE_TILT_FEATURE.name(), false);
    }



    public static void storeCommonScoreKeeperDatas( SharedPreferences.Editor editor, ActivityWithState theActivity) {
        editor.putInt(ScoreKeeperPrefKeys.POINT_PER_GOAL.name(), theActivity.getScoreKeeperData().pointsForGoal);
        editor.putInt(ScoreKeeperPrefKeys.RESET_SCORE_TO.name(), theActivity.getScoreKeeperData().resetScoreTo);
        if (theActivity.getScoreKeeperData().gamePoint != null){
            editor.putInt(ScoreKeeperPrefKeys.GAME_POINT.name(), theActivity.getScoreKeeperData().gamePoint.getGamePoint());
            editor.putInt(ScoreKeeperPrefKeys.GAME_POINT_SPREAD.name(), theActivity.getScoreKeeperData().gamePoint.getPointSpread());
        } else {
            editor.putInt(ScoreKeeperPrefKeys.GAME_POINT.name(), -1);
            editor.putInt(ScoreKeeperPrefKeys.GAME_POINT_SPREAD.name(), -1);
        }
        editor.putBoolean(ScoreKeeperPrefKeys.DISABLE_TILT_FEATURE.name(), theActivity.getScoreKeeperData().disableTiltFeature);
        editor.putString(ScoreKeeperPrefKeys.FILE_SAVE_FEATURE_DATE.name(), theActivity.getScoreKeeperData().fileSaveFeatureDate);
        editor.putBoolean(ScoreKeeperPrefKeys.FILE_SAVE_FEATURE_SWITCH.name(), theActivity.getScoreKeeperData().fileSaveForToday);
        editor.commit();
    }

//    protected void clearState( SharedPreferences sharedPref) {
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.remove(ScoreKeeperPrefKeys.LEFT_BACKGROUND.name());
//        editor.remove(ScoreKeeperPrefKeys.RIGHT_BACKGROUND.name());
//        editor.remove(ScoreKeeperPrefKeys.LEFT_TEXT.name());
//        editor.remove(ScoreKeeperPrefKeys.RIGHT_TEXT.name());
//        editor.remove(ScoreKeeperPrefKeys.LEFT_SCORE.name());
//        editor.remove(ScoreKeeperPrefKeys.RIGHT_SCORE.name());
//        editor.remove(ScoreKeeperPrefKeys.POINT_PER_GOAL.name());
//        editor.remove(ScoreKeeperPrefKeys.RESET_SCORE_TO.name());
//        editor.remove(ScoreKeeperPrefKeys.LEFT_TEAM_NAME.name());
//        editor.remove(ScoreKeeperPrefKeys.RIGHT_TEAM_NAME.name());
//        editor.remove(ScoreKeeperPrefKeys.FILE_SAVE_FEATURE_DATE.name());
//        editor.remove(ScoreKeeperPrefKeys.FILE_SAVE_FEATURE_SWITCH.name());
//        editor.remove(ScoreKeeperPrefKeys.GAME_POINT.name());
//        editor.remove(ScoreKeeperPrefKeys.GAME_POINT_SPREAD.name());
//        editor.remove(ScoreKeeperPrefKeys.TILT_FEATURE_ENABLED.name());
//        editor.commit();
//    }
}
