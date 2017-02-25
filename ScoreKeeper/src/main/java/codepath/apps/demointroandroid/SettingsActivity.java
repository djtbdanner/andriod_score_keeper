package codepath.apps.demointroandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import codepath.apps.demointroandroid.domain.GamePoint;
import codepath.apps.demointroandroid.domain.ScoreKeeperData;
import codepath.apps.demointroandroid.domain.ScoreKeeperPrefKeys;
import codepath.apps.demointroandroid.util.ActivityWithState;
import codepath.apps.demointroandroid.util.CommonPreferencesUtility;
import codepath.apps.demointroandroid.util.ScoreKeeperUtils;


public class SettingsActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener, ActivityWithState {

    Spinner gamePointSpinner;
    Spinner gamePointMarginSpinner;
    TextView gamePointMarginText;
    private ScoreKeeperData scoreKeeperData;
    Spinner pointsPerGoalSpinner;
    Spinner resetScoreToSpinner;
    CheckBox gamePointCheckBox;
    CheckBox saveTodaysGameCheckBox;
    CheckBox disableTiltFeatureCheckBox;
    Spinner fontSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_keeper_settings);
        registerViews((ViewGroup) getWindow().getDecorView());
        gamePointSpinner = (Spinner) findViewById(R.id.game_point_spinner);
        gamePointMarginSpinner = (Spinner) findViewById(R.id.game_point_margin_spinner);
        gamePointMarginText = (TextView) findViewById(R.id.win_by_label);
        pointsPerGoalSpinner = (Spinner) findViewById(R.id.points_per_goal_spinner);
        resetScoreToSpinner = (Spinner) findViewById(R.id.reset_score_spinner);
        gamePointCheckBox = (CheckBox) findViewById(R.id.game_point_checkbox);
        saveTodaysGameCheckBox = (CheckBox) findViewById(R.id.enable_game_save_button);
        disableTiltFeatureCheckBox = (CheckBox) findViewById(R.id.disable_tilt_feature_checkbox);
        fontSpinner = (Spinner) findViewById(R.id.font_picker);

        SharedPreferences sharedPref = this.getSharedPreferences(ScoreKeeperPrefKeys.SHARED_PREFERENCES.name(), Context.MODE_PRIVATE);
        CommonPreferencesUtility.setCommonScoreKeeperData(sharedPref, this);

        setValuesOnFields(false);

    }

    private void registerViews(ViewGroup viewGroup) {
        for (int i = 0, N = viewGroup.getChildCount(); i < N; i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup && !(child instanceof Spinner)) {
                registerViews((ViewGroup) child);
            } else if (child instanceof Button) {
                child.setOnClickListener(this);
            } else if (child instanceof TextView) {
                child.setOnClickListener(this);
            } else if (child instanceof Spinner) {
                ((Spinner) child).setOnItemSelectedListener(this);
           } //else if (child instanceof CheckBox) {
//                child.setOnClickListener(this);
//            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.game_point_checkbox:
                processGamePointMarginSwitch(view);
                setScoreKeeperDataValues();
                break;
            case R.id.settings_done:
                saveAndReturnToScoreKeeper();
                break;
            case R.id.settings_reset:
                setValuesOnFields(true);
                break;
            default:
                setScoreKeeperDataValues();
                break;
        }
    }


    void setScoreKeeperDataValues() {

        if (gamePointCheckBox.isChecked()) {
            GamePoint winBy = new GamePoint();
            winBy.setGamePoint(ScoreKeeperUtils.getIntWithDefault(gamePointSpinner.getSelectedItem().toString(), 25));
            winBy.setPointSpread(ScoreKeeperUtils.getIntWithDefault(gamePointMarginSpinner.getSelectedItem().toString(), 1));
            getScoreKeeperData().gamePoint = winBy;
        } else {
            getScoreKeeperData().gamePoint = null;
        }

        getScoreKeeperData().pointsForGoal = ScoreKeeperUtils.getIntWithDefault(pointsPerGoalSpinner.getSelectedItem().toString(), 1);
        getScoreKeeperData().resetScoreTo = ScoreKeeperUtils.getIntWithDefault(resetScoreToSpinner.getSelectedItem().toString(), 0);

        if (saveTodaysGameCheckBox.isChecked()) {
            getScoreKeeperData().enableFileSave();
        } else {
            getScoreKeeperData().disableFileSave();
        }

        getScoreKeeperData().disableTiltFeature = disableTiltFeatureCheckBox.isChecked();
        getScoreKeeperData().fontName = fontSpinner.getSelectedItem().toString();

    }

    void saveAndReturnToScoreKeeper() {
        SharedPreferences sharedPref = this.getSharedPreferences(ScoreKeeperPrefKeys.SHARED_PREFERENCES.name(), Context.MODE_PRIVATE);
        CommonPreferencesUtility.storeCommonScoreKeeperDatas(sharedPref.edit(), this);

        Intent intent = new Intent(this, ScoreKeeperActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
    }


    void processGamePointMarginSwitch(View v) {
        int visibility;
        if (((CheckBox) v).isChecked()) {
            visibility = View.VISIBLE;
            if (getScoreKeeperData().gamePoint != null) {
                gamePointSpinner.setSelection(ScoreKeeperUtils.getSpinnerIndexOfValue(gamePointSpinner, String.valueOf(getScoreKeeperData().gamePoint.getGamePoint())));
                gamePointMarginSpinner.setSelection(ScoreKeeperUtils.getSpinnerIndexOfValue(gamePointMarginSpinner, String.valueOf(getScoreKeeperData().gamePoint.getPointSpread())));
            }
        } else {
            visibility = View.INVISIBLE;
            gamePointSpinner.setSelection(0);
            gamePointMarginSpinner.setSelection(0);
        }
        gamePointMarginSpinner.setVisibility(visibility);
        gamePointSpinner.setVisibility(visibility);
        gamePointMarginText.setVisibility(visibility);
    }


    void setValuesOnFields(boolean isReset) {

        pointsPerGoalSpinner.setSelection(isReset ? 0 : ScoreKeeperUtils.getSpinnerIndexOfValue(pointsPerGoalSpinner, String.valueOf(getScoreKeeperData().pointsForGoal)));
        resetScoreToSpinner.setSelection(isReset ? 0 : ScoreKeeperUtils.getSpinnerIndexOfValue(resetScoreToSpinner, String.valueOf(getScoreKeeperData().resetScoreTo)));
        fontSpinner.setSelection(isReset ? 0 : ScoreKeeperUtils.getSpinnerIndexOfValue(fontSpinner, String.valueOf(getScoreKeeperData().fontName)));

        setFontOnLabel();

        if (isReset && gamePointCheckBox.isChecked()) {
            gamePointCheckBox.toggle();
        } else if (!gamePointCheckBox.isChecked() && getScoreKeeperData().gamePoint != null) {
            gamePointCheckBox.toggle();
        }
        processGamePointMarginSwitch(gamePointCheckBox);

        if (isReset && saveTodaysGameCheckBox.isChecked()) {
            saveTodaysGameCheckBox.toggle();
        } else if (!saveTodaysGameCheckBox.isChecked() && getScoreKeeperData().fileSaveForToday) {
            saveTodaysGameCheckBox.toggle();
        }
        if (isReset && disableTiltFeatureCheckBox.isChecked()) {
            disableTiltFeatureCheckBox.toggle();
        } else if (!disableTiltFeatureCheckBox.isChecked() && getScoreKeeperData().disableTiltFeature) {
            disableTiltFeatureCheckBox.toggle();
        }
    }

    private void setFontOnLabel() {
        TextView fontLabel = (TextView) findViewById(R.id.font_picker_label);
        fontLabel.setTypeface(ScoreKeeperUtils.getTypeface(getScoreKeeperData().fontName, getAssets() ));
    }

    public ScoreKeeperData getScoreKeeperData() {
        if (scoreKeeperData == null) {
            scoreKeeperData = new ScoreKeeperData();
        }
        return scoreKeeperData;
    }

    // when something from a spinner is selected
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        setScoreKeeperDataValues();
        if (adapterView.getId() == R.id.font_picker){
            setFontOnLabel();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // No Need to do anything here
    }
}

