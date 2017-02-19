package codepath.apps.demointroandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import codepath.apps.demointroandroid.domain.ScoreKeeperColors;
import codepath.apps.demointroandroid.domain.ScoreKeeperPrefKeys;
import codepath.apps.demointroandroid.util.ScoreKeeperUtils;

import static codepath.apps.demointroandroid.domain.ScoreKeeperColors.BLACK;
import static codepath.apps.demointroandroid.domain.ScoreKeeperColors.BLUE;
import static codepath.apps.demointroandroid.domain.ScoreKeeperColors.BROWN;
import static codepath.apps.demointroandroid.domain.ScoreKeeperColors.GREEN;
import static codepath.apps.demointroandroid.domain.ScoreKeeperColors.ORANGE;
import static codepath.apps.demointroandroid.domain.ScoreKeeperColors.PINK;
import static codepath.apps.demointroandroid.domain.ScoreKeeperColors.PURPLE;
import static codepath.apps.demointroandroid.domain.ScoreKeeperColors.RED;
import static codepath.apps.demointroandroid.domain.ScoreKeeperColors.WHITE;
import static codepath.apps.demointroandroid.domain.ScoreKeeperColors.YELLOW;
import static codepath.apps.demointroandroid.util.ScoreKeeperUtils.arrayContains;
import static codepath.apps.demointroandroid.util.ScoreKeeperUtils.getBackgroundColor;


public class ColorActivity extends Activity implements View.OnClickListener {


    TextView leftExample;
    TextView rightExample;
    int leftBackgroundColor;
    int leftTextColor;
    int rightBackgroundColor;
    int rightTextColor;

    static int[] RIGHT_BACKGROUND_COLOR_IDS = {R.id.blue_picker_right, R.id.black_picker_right, R.id.brown_picker_right, R.id.green_picker_right, R.id.orange_picker_right, R.id.pink_picker_right, R.id.purple_picker_right, R.id.red_picker_right, R.id.white_picker_right, R.id.yellow_picker_right};
    static int[] RIGHT_TEXT_COLOR_IDS = {R.id.blue_picker_right_2, R.id.black_picker_right_2, R.id.brown_picker_right_2, R.id.green_picker_right_2, R.id.orange_picker_right_2, R.id.pink_picker_right_2, R.id.purple_picker_right_2, R.id.red_picker_right_2, R.id.white_picker_right_2, R.id.yellow_picker_right_2};

    static int[] LEFT_BACKGROUND_COLOR_IDS = {R.id.blue_picker_left, R.id.black_picker_left, R.id.brown_picker_left, R.id.green_picker_left, R.id.orange_picker_left, R.id.pink_picker_left, R.id.purple_picker_left, R.id.red_picker_left, R.id.white_picker_left, R.id.yellow_picker_left};
    static int[] LEFT_TEXT_COLOR_IDS = {R.id.blue_picker_left_2, R.id.black_picker_left_2, R.id.brown_picker_left_2, R.id.green_picker_left_2, R.id.orange_picker_left_2, R.id.pink_picker_left_2, R.id.purple_picker_left_2, R.id.red_picker_left_2, R.id.white_picker_left_2, R.id.yellow_picker_left_2};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_keeper_colors);

        Button doneButton = (Button) findViewById(R.id.colors_done_button);
        doneButton.setOnClickListener(this);

        registerViews((ViewGroup) getWindow().getDecorView());

        leftExample = (TextView) findViewById(R.id.example_left);
        rightExample = (TextView) findViewById(R.id.example_right);
        initState();
        hideLeftTextColor(leftBackgroundColor);
        hideRightTextColor(rightBackgroundColor);
        hideLeftBackgroundColor(leftTextColor);
        hideRightBackgroundColor(rightTextColor);
    }

    private void registerViews(ViewGroup viewGroup) {
        for (int i = 0, N = viewGroup.getChildCount(); i < N; i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                registerViews((ViewGroup) child);
            } else if (child instanceof Button) {
                child.setOnClickListener(this);
            } else if (child instanceof TextView) {
                child.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View view) {
        System.out.println(view.getContext());

        if (isLeftBackgroundColorPicker(view)) {
            int color = getBackgroundColor(view);
            leftExample.setBackgroundColor(color);
            leftBackgroundColor = color;
            hideLeftTextColor(color);
        }
        if (isLeftTextColorPicker(view)) {
            int color = getBackgroundColor(view);
            leftExample.setTextColor(color);
            leftTextColor = color;
            hideLeftBackgroundColor(color);

        }
        if (isRightBackgroundColorPicker(view)) {
            int color = getBackgroundColor(view);
            rightExample.setBackgroundColor(color);
            rightBackgroundColor = color;
            hideRightTextColor(color);
        }
        if (isRightTextColorPicker(view)) {
            int color = getBackgroundColor(view);
            rightExample.setTextColor(color);
            rightTextColor = color;
            hideRightBackgroundColor(color);
        }

        if (R.id.colors_done_button == view.getId()) {
            setState();
            Intent intent = new Intent(this, ScoreKeeperActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(intent);
        }
    }

    void hideRightTextColor(int color) {
        for (int i = 0; i < RIGHT_TEXT_COLOR_IDS.length; i++) {
            findViewById(RIGHT_TEXT_COLOR_IDS[i]).setVisibility(View.VISIBLE);
        }
        if (BLACK == color)
            findViewById(R.id.black_picker_right_2).setVisibility(View.INVISIBLE);

        if (BLUE == color)
            findViewById(R.id.blue_picker_right_2).setVisibility(View.INVISIBLE);

        if (BROWN == color)
            findViewById(R.id.brown_picker_right_2).setVisibility(View.INVISIBLE);

        if (GREEN == color)
            findViewById(R.id.green_picker_right_2).setVisibility(View.INVISIBLE);

        if (ORANGE == color)
            findViewById(R.id.orange_picker_right_2).setVisibility(View.INVISIBLE);

        if (PINK == color)
            findViewById(R.id.pink_picker_right_2).setVisibility(View.INVISIBLE);

        if (PURPLE == color)
            findViewById(R.id.purple_picker_right_2).setVisibility(View.INVISIBLE);

        if (RED == color)
            findViewById(R.id.red_picker_right_2).setVisibility(View.INVISIBLE);

        if (WHITE == color)
            findViewById(R.id.white_picker_right_2).setVisibility(View.INVISIBLE);

        if (YELLOW == color)
            findViewById(R.id.yellow_picker_right_2).setVisibility(View.INVISIBLE);
    }
    void hideRightBackgroundColor(int color) {
        for (int i = 0; i < RIGHT_BACKGROUND_COLOR_IDS.length; i++) {
            findViewById(RIGHT_BACKGROUND_COLOR_IDS[i]).setVisibility(View.VISIBLE);
        }
        if (BLACK == color)
            findViewById(R.id.black_picker_right).setVisibility(View.INVISIBLE);

        if (BLUE == color)
            findViewById(R.id.blue_picker_right).setVisibility(View.INVISIBLE);

        if (BROWN == color)
            findViewById(R.id.brown_picker_right).setVisibility(View.INVISIBLE);

        if (GREEN == color)
            findViewById(R.id.green_picker_right).setVisibility(View.INVISIBLE);

        if (ORANGE == color)
            findViewById(R.id.orange_picker_right).setVisibility(View.INVISIBLE);

        if (PINK == color)
            findViewById(R.id.pink_picker_right).setVisibility(View.INVISIBLE);

        if (PURPLE == color)
            findViewById(R.id.purple_picker_right).setVisibility(View.INVISIBLE);

        if (RED == color)
            findViewById(R.id.red_picker_right).setVisibility(View.INVISIBLE);

        if (WHITE == color)
            findViewById(R.id.white_picker_right).setVisibility(View.INVISIBLE);

        if (YELLOW == color)
            findViewById(R.id.yellow_picker_right).setVisibility(View.INVISIBLE);
    }
    void hideLeftTextColor(int color) {
        for (int i = 0; i < LEFT_TEXT_COLOR_IDS.length; i++) {
            findViewById(LEFT_TEXT_COLOR_IDS[i]).setVisibility(View.VISIBLE);
        }

        if (BLACK == color)
            findViewById(R.id.black_picker_left_2).setVisibility(View.INVISIBLE);

        if (BLUE == color)
            findViewById(R.id.blue_picker_left_2).setVisibility(View.INVISIBLE);

        if (BROWN == color)
            findViewById(R.id.brown_picker_left_2).setVisibility(View.INVISIBLE);

        if (GREEN == color)
            findViewById(R.id.green_picker_left_2).setVisibility(View.INVISIBLE);

        if (ORANGE == color)
            findViewById(R.id.orange_picker_left_2).setVisibility(View.INVISIBLE);

        if (PINK == color)
            findViewById(R.id.pink_picker_left_2).setVisibility(View.INVISIBLE);

        if (PURPLE == color)
            findViewById(R.id.purple_picker_left_2).setVisibility(View.INVISIBLE);

        if (RED == color)
            findViewById(R.id.red_picker_left_2).setVisibility(View.INVISIBLE);

        if (WHITE == color)
            findViewById(R.id.white_picker_left_2).setVisibility(View.INVISIBLE);

        if (YELLOW == color)
            findViewById(R.id.yellow_picker_left_2).setVisibility(View.INVISIBLE);
    }
    void hideLeftBackgroundColor(int color) {
        for (int i = 0; i < LEFT_BACKGROUND_COLOR_IDS.length; i++) {
            findViewById(LEFT_BACKGROUND_COLOR_IDS[i]).setVisibility(View.VISIBLE);
        }

        if (BLACK == color)
            findViewById(R.id.black_picker_left).setVisibility(View.INVISIBLE);

        if (BLUE == color)
            findViewById(R.id.blue_picker_left).setVisibility(View.INVISIBLE);

        if (BROWN == color)
            findViewById(R.id.brown_picker_left).setVisibility(View.INVISIBLE);

        if (GREEN == color)
            findViewById(R.id.green_picker_left).setVisibility(View.INVISIBLE);

        if (ORANGE == color)
            findViewById(R.id.orange_picker_left).setVisibility(View.INVISIBLE);

        if (PINK == color)
            findViewById(R.id.pink_picker_left).setVisibility(View.INVISIBLE);

        if (PURPLE == color)
            findViewById(R.id.purple_picker_left).setVisibility(View.INVISIBLE);

        if (RED == color)
            findViewById(R.id.red_picker_left).setVisibility(View.INVISIBLE);

        if (WHITE == color)
            findViewById(R.id.white_picker_left).setVisibility(View.INVISIBLE);

        if (YELLOW == color)
            findViewById(R.id.yellow_picker_left).setVisibility(View.INVISIBLE);


    }
    boolean isLeftBackgroundColorPicker(View view) {
        return arrayContains(LEFT_BACKGROUND_COLOR_IDS, view.getId());
    }

    boolean isLeftTextColorPicker(View view) {
        return arrayContains(LEFT_TEXT_COLOR_IDS, view.getId());
    }

    boolean isRightBackgroundColorPicker(View view) {
        return arrayContains(RIGHT_BACKGROUND_COLOR_IDS, view.getId());
    }

    boolean isRightTextColorPicker(View view) {
        return arrayContains(RIGHT_TEXT_COLOR_IDS, view.getId());
    }

    protected void initState() {
        SharedPreferences sharedPref = this.getSharedPreferences(ScoreKeeperPrefKeys.SHARED_PREFERENCES.name(), Context.MODE_PRIVATE);
        leftBackgroundColor = sharedPref.getInt(ScoreKeeperPrefKeys.LEFT_BACKGROUND.name(), RED);
        rightBackgroundColor = sharedPref.getInt(ScoreKeeperPrefKeys.RIGHT_BACKGROUND.name(), BLUE);
        leftTextColor = sharedPref.getInt(ScoreKeeperPrefKeys.LEFT_TEXT.name(), WHITE);
        rightTextColor = sharedPref.getInt(ScoreKeeperPrefKeys.RIGHT_TEXT.name(), WHITE);

        leftExample.setBackgroundColor(leftBackgroundColor);
        rightExample.setBackgroundColor(rightBackgroundColor);
        leftExample.setTextColor(leftTextColor);
        rightExample.setTextColor(rightTextColor);
        rightExample.setText(String.valueOf(sharedPref.getInt(ScoreKeeperPrefKeys.RIGHT_SCORE.name(), 0)));
        leftExample.setText(String.valueOf(sharedPref.getInt(ScoreKeeperPrefKeys.LEFT_SCORE.name(), 0)));
        System.out.println(sharedPref.getString("dave", "nope"));
    }

    protected void setState() {
        SharedPreferences sharedPref = this.getSharedPreferences(ScoreKeeperPrefKeys.SHARED_PREFERENCES.name(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(ScoreKeeperPrefKeys.LEFT_BACKGROUND.name(), leftBackgroundColor);
        editor.putInt(ScoreKeeperPrefKeys.RIGHT_BACKGROUND.name(), rightBackgroundColor);
        editor.putInt(ScoreKeeperPrefKeys.LEFT_TEXT.name(), leftTextColor);
        editor.putInt(ScoreKeeperPrefKeys.RIGHT_TEXT.name(), rightTextColor);
        editor.commit();
    }
}

