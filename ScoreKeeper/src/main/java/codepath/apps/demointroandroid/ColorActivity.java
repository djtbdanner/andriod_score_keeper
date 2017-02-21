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

import codepath.apps.demointroandroid.domain.ScoreKeeperPrefKeys;
import codepath.apps.demointroandroid.util.ScoreKeeperUtils;

import static codepath.apps.demointroandroid.util.ScoreKeeperUtils.getBackgroundColor;


public class ColorActivity extends Activity implements View.OnClickListener {


    TextView leftExampleTextView;
    TextView rightExampleTextView;

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

        leftExampleTextView = (TextView) findViewById(R.id.example_left);
        rightExampleTextView = (TextView) findViewById(R.id.example_right);
        initState();
        hideLeftBackgroundColor(leftExampleTextView.getCurrentTextColor());
        hideRightBackgroundColor(rightExampleTextView.getCurrentTextColor());
        hideLeftTextColor(ScoreKeeperUtils.getBackgroundColor(leftExampleTextView));
        hideRightTextColor(ScoreKeeperUtils.getBackgroundColor(rightExampleTextView));
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
        if (isLeftBackgroundColorPicker(view)) {
            int color = getBackgroundColor(view);
            leftExampleTextView.setBackgroundColor(color);
            hideLeftTextColor(color);
        }
        if (isLeftTextColorPicker(view)) {
            int color = getBackgroundColor(view);
            leftExampleTextView.setTextColor(color);
            hideLeftBackgroundColor(color);

        }
        if (isRightBackgroundColorPicker(view)) {
            int color = getBackgroundColor(view);
            rightExampleTextView.setBackgroundColor(color);
            hideRightTextColor(color);
        }
        if (isRightTextColorPicker(view)) {
            int color = getBackgroundColor(view);
            rightExampleTextView.setTextColor(color);
            hideRightBackgroundColor(color);
        }

        if (R.id.colors_done_button == view.getId()) {
            setState();
            Intent intent = new Intent(this, ScoreKeeperActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(intent);
        }
    }

    int theColor(int id) {
        return getResources().getColor(id);
    }

    boolean isLeftBackgroundColorPicker(View view) {
        return ScoreKeeperUtils.getInstance().intArrayContains(LEFT_BACKGROUND_COLOR_IDS, view.getId());
    }

    boolean isLeftTextColorPicker(View view) {
        return ScoreKeeperUtils.getInstance().intArrayContains(LEFT_TEXT_COLOR_IDS, view.getId());
    }

    boolean isRightBackgroundColorPicker(View view) {
        return ScoreKeeperUtils.getInstance().intArrayContains(RIGHT_BACKGROUND_COLOR_IDS, view.getId());
    }

    boolean isRightTextColorPicker(View view) {
        return ScoreKeeperUtils.getInstance().intArrayContains(RIGHT_TEXT_COLOR_IDS, view.getId());
    }

    protected void initState() {
        SharedPreferences sharedPref = this.getSharedPreferences(ScoreKeeperPrefKeys.SHARED_PREFERENCES.name(), Context.MODE_PRIVATE);
        int leftBackgroundColor = sharedPref.getInt(ScoreKeeperPrefKeys.LEFT_BACKGROUND.name(), theColor(R.color.red));
        int rightBackgroundColor = sharedPref.getInt(ScoreKeeperPrefKeys.RIGHT_BACKGROUND.name(), theColor(R.color.blue));
        int leftTextColor = sharedPref.getInt(ScoreKeeperPrefKeys.LEFT_TEXT.name(), theColor(R.color.white));
        int rightTextColor = sharedPref.getInt(ScoreKeeperPrefKeys.RIGHT_TEXT.name(), theColor(R.color.white));

        leftExampleTextView.setBackgroundColor(leftBackgroundColor);
        rightExampleTextView.setBackgroundColor(rightBackgroundColor);
        leftExampleTextView.setTextColor(leftTextColor);
        rightExampleTextView.setTextColor(rightTextColor);
        rightExampleTextView.setText(String.valueOf(sharedPref.getInt(ScoreKeeperPrefKeys.RIGHT_SCORE.name(), 0)));
        leftExampleTextView.setText(String.valueOf(sharedPref.getInt(ScoreKeeperPrefKeys.LEFT_SCORE.name(), 0)));

    }

    protected void setState() {
        SharedPreferences sharedPref = this.getSharedPreferences(ScoreKeeperPrefKeys.SHARED_PREFERENCES.name(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(ScoreKeeperPrefKeys.LEFT_BACKGROUND.name(), ScoreKeeperUtils.getBackgroundColor(leftExampleTextView));
        editor.putInt(ScoreKeeperPrefKeys.RIGHT_BACKGROUND.name(), ScoreKeeperUtils.getBackgroundColor(rightExampleTextView));
        editor.putInt(ScoreKeeperPrefKeys.LEFT_TEXT.name(), leftExampleTextView.getCurrentTextColor());
        editor.putInt(ScoreKeeperPrefKeys.RIGHT_TEXT.name(), rightExampleTextView.getCurrentTextColor());
        editor.apply();
    }

    void hideRightTextColor(int color) {
        for (int RIGHT_TEXT_COLOR_ID : RIGHT_TEXT_COLOR_IDS) {
            findViewById(RIGHT_TEXT_COLOR_ID).setVisibility(View.VISIBLE);
        }
        if (theColor(R.color.black) == color)
            findViewById(R.id.black_picker_right_2).setVisibility(View.INVISIBLE);

        if (theColor(R.color.blue) == color)
            findViewById(R.id.blue_picker_right_2).setVisibility(View.INVISIBLE);

        if (theColor(R.color.brown) == color)
            findViewById(R.id.brown_picker_right_2).setVisibility(View.INVISIBLE);

        if (theColor(R.color.green) == color)
            findViewById(R.id.green_picker_right_2).setVisibility(View.INVISIBLE);

        if (theColor(R.color.orange) == color)
            findViewById(R.id.orange_picker_right_2).setVisibility(View.INVISIBLE);

        if (theColor(R.color.pink) == color)
            findViewById(R.id.pink_picker_right_2).setVisibility(View.INVISIBLE);

        if (theColor(R.color.purple) == color)
            findViewById(R.id.purple_picker_right_2).setVisibility(View.INVISIBLE);

        if (theColor(R.color.red) == color)
            findViewById(R.id.red_picker_right_2).setVisibility(View.INVISIBLE);

        if (theColor(R.color.white) == color)
            findViewById(R.id.white_picker_right_2).setVisibility(View.INVISIBLE);

        if (theColor(R.color.yellow) == color)
            findViewById(R.id.yellow_picker_right_2).setVisibility(View.INVISIBLE);
    }

    void hideRightBackgroundColor(int color) {
        for (int RIGHT_BACKGROUND_COLOR_ID : RIGHT_BACKGROUND_COLOR_IDS) {
            findViewById(RIGHT_BACKGROUND_COLOR_ID).setVisibility(View.VISIBLE);
        }
        if (theColor(R.color.black) == color)
            findViewById(R.id.black_picker_right).setVisibility(View.INVISIBLE);

        if (theColor(R.color.blue) == color)
            findViewById(R.id.blue_picker_right).setVisibility(View.INVISIBLE);

        if (theColor(R.color.brown) == color)
            findViewById(R.id.brown_picker_right).setVisibility(View.INVISIBLE);

        if (theColor(R.color.green) == color)
            findViewById(R.id.green_picker_right).setVisibility(View.INVISIBLE);

        if (theColor(R.color.orange) == color)
            findViewById(R.id.orange_picker_right).setVisibility(View.INVISIBLE);

        if (theColor(R.color.pink) == color)
            findViewById(R.id.pink_picker_right).setVisibility(View.INVISIBLE);

        if (theColor(R.color.purple) == color)
            findViewById(R.id.purple_picker_right).setVisibility(View.INVISIBLE);

        if (theColor(R.color.red) == color)
            findViewById(R.id.red_picker_right).setVisibility(View.INVISIBLE);

        if (theColor(R.color.white) == color)
            findViewById(R.id.white_picker_right).setVisibility(View.INVISIBLE);

        if (theColor(R.color.yellow) == color)
            findViewById(R.id.yellow_picker_right).setVisibility(View.INVISIBLE);
    }

    void hideLeftTextColor(int color) {
        for (int LEFT_TEXT_COLOR_ID : LEFT_TEXT_COLOR_IDS) {
            findViewById(LEFT_TEXT_COLOR_ID).setVisibility(View.VISIBLE);
            // ((TextView) findViewById(LEFT_TEXT_COLOR_ID)).setBackgroundResource(R.drawable.rounded_corners);
        }

        if (theColor(R.color.black) == color)
            findViewById(R.id.black_picker_left_2).setVisibility(View.INVISIBLE);

        if (theColor(R.color.blue) == color)
            findViewById(R.id.blue_picker_left_2).setVisibility(View.INVISIBLE);

        if (theColor(R.color.brown) == color)
            findViewById(R.id.brown_picker_left_2).setVisibility(View.INVISIBLE);

        if (theColor(R.color.green) == color)
            findViewById(R.id.green_picker_left_2).setVisibility(View.INVISIBLE);

        if (theColor(R.color.orange) == color)
            findViewById(R.id.orange_picker_left_2).setVisibility(View.INVISIBLE);

        if (theColor(R.color.pink) == color)
            findViewById(R.id.pink_picker_left_2).setVisibility(View.INVISIBLE);

        if (theColor(R.color.purple) == color)
            findViewById(R.id.purple_picker_left_2).setVisibility(View.INVISIBLE);

        if (theColor(R.color.red) == color)
            findViewById(R.id.red_picker_left_2).setVisibility(View.INVISIBLE);

        if (theColor(R.color.white) == color)
            findViewById(R.id.white_picker_left_2).setVisibility(View.INVISIBLE);

        if (theColor(R.color.yellow) == color)
            findViewById(R.id.yellow_picker_left_2).setVisibility(View.INVISIBLE);
    }

    void hideLeftBackgroundColor(int color) {
        for (int LEFT_BACKGROUND_COLOR_ID : LEFT_BACKGROUND_COLOR_IDS) {
            findViewById(LEFT_BACKGROUND_COLOR_ID).setVisibility(View.VISIBLE);
        }
        if (theColor(R.color.black) == color)
            findViewById(R.id.black_picker_left).setVisibility(View.INVISIBLE);

        if (theColor(R.color.blue) == color)
            findViewById(R.id.blue_picker_left).setVisibility(View.INVISIBLE);

        if (theColor(R.color.brown) == color)
            findViewById(R.id.brown_picker_left).setVisibility(View.INVISIBLE);

        if (theColor(R.color.green) == color)
            findViewById(R.id.green_picker_left).setVisibility(View.INVISIBLE);

        if (theColor(R.color.orange) == color)
            findViewById(R.id.orange_picker_left).setVisibility(View.INVISIBLE);

        if (theColor(R.color.pink) == color)
            findViewById(R.id.pink_picker_left).setVisibility(View.INVISIBLE);

        if (theColor(R.color.purple) == color)
            findViewById(R.id.purple_picker_left).setVisibility(View.INVISIBLE);

        if (theColor(R.color.red) == color)
            findViewById(R.id.red_picker_left).setVisibility(View.INVISIBLE);

        if (theColor(R.color.white) == color)
            findViewById(R.id.white_picker_left).setVisibility(View.INVISIBLE);

        if (theColor(R.color.yellow) == color)
            findViewById(R.id.yellow_picker_left).setVisibility(View.INVISIBLE);
    }
}

