package codepath.apps.demointroandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
            } else if (child instanceof View) {
                child.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (isLeftBackgroundColorPicker(view)) {
            int color = getBackgroundColor(view);
            if (color == leftExampleTextView.getCurrentTextColor()) {
                showMessage();
                return;
            }
            leftExampleTextView.setBackgroundColor(color);
            hideLeftTextColor(color);
        }
        if (isLeftTextColorPicker(view)) {
            int color = getBackgroundColor(view);
            if (color == ScoreKeeperUtils.getBackgroundColor(leftExampleTextView)) {
                showMessage();
                return;
            }
            leftExampleTextView.setTextColor(color);
            hideLeftBackgroundColor(color);

        }
        if (isRightBackgroundColorPicker(view)) {
            int color = getBackgroundColor(view);
            if (color == rightExampleTextView.getCurrentTextColor()) {
                showMessage();
                return;
            }
            rightExampleTextView.setBackgroundColor(color);
            hideRightTextColor(color);
        }
        if (isRightTextColorPicker(view)) {
            int color = getBackgroundColor(view);
            if (color == ScoreKeeperUtils.getBackgroundColor(rightExampleTextView)) {
                showMessage();
                return;
            }
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
        try {
            return getResources().getColor(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
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
        for (int RIGHT_BACKGROUND_COLOR_ID : RIGHT_BACKGROUND_COLOR_IDS) {
            setRound(findViewById(RIGHT_BACKGROUND_COLOR_ID));
        }
        if (theColor(R.color.black) == color)
            makeViewSquare(findViewById(R.id.black_picker_right));

        if (theColor(R.color.blue) == color)
            makeViewSquare(findViewById(R.id.blue_picker_right));

        if (theColor(R.color.brown) == color)
            makeViewSquare(findViewById(R.id.brown_picker_right));

        if (theColor(R.color.green) == color)
            makeViewSquare(findViewById(R.id.green_picker_right));

        if (theColor(R.color.orange) == color)
            makeViewSquare(findViewById(R.id.orange_picker_right));

        if (theColor(R.color.pink) == color)
            makeViewSquare(findViewById(R.id.pink_picker_right));

        if (theColor(R.color.purple) == color)
            makeViewSquare(findViewById(R.id.purple_picker_right));

        if (theColor(R.color.red) == color)
            makeViewSquare(findViewById(R.id.red_picker_right));

        if (theColor(R.color.white) == color)
            makeViewSquare(findViewById(R.id.white_picker_right));

        if (theColor(R.color.yellow) == color)
            makeViewSquare(findViewById(R.id.yellow_picker_right));
    }

    void hideRightBackgroundColor(int color) {
        for (int RIGHT_TEXT_COLOR_ID : RIGHT_TEXT_COLOR_IDS) {
            setRound(findViewById(RIGHT_TEXT_COLOR_ID));
        }
        if (theColor(R.color.black) == color)
            makeViewSquare(findViewById(R.id.black_picker_right_2));

        if (theColor(R.color.blue) == color)
            makeViewSquare(findViewById(R.id.blue_picker_right_2));

        if (theColor(R.color.brown) == color)
            makeViewSquare(findViewById(R.id.brown_picker_right_2));

        if (theColor(R.color.green) == color)
            makeViewSquare(findViewById(R.id.green_picker_right_2));

        if (theColor(R.color.orange) == color)
            makeViewSquare(findViewById(R.id.orange_picker_right_2));

        if (theColor(R.color.pink) == color)
            makeViewSquare(findViewById(R.id.pink_picker_right_2));

        if (theColor(R.color.purple) == color)
            makeViewSquare(findViewById(R.id.purple_picker_right_2));

        if (theColor(R.color.red) == color)
            makeViewSquare(findViewById(R.id.red_picker_right_2));

        if (theColor(R.color.white) == color)
            makeViewSquare(findViewById(R.id.white_picker_right_2));

        if (theColor(R.color.yellow) == color)
            makeViewSquare(findViewById(R.id.yellow_picker_right_2));
    }

    void hideLeftTextColor(int color) {
        for (int LEFT_BACKGROUND_COLOR_ID : LEFT_BACKGROUND_COLOR_IDS) {
            setRound(findViewById(LEFT_BACKGROUND_COLOR_ID));
        }
        int highLightColor = theColor(R.color.black);
        if (theColor(R.color.black) == color)
            makeViewSquare(findViewById(R.id.black_picker_left));

        if (theColor(R.color.blue) == color)
            makeViewSquare(findViewById(R.id.blue_picker_left));

        if (theColor(R.color.brown) == color)
            makeViewSquare(findViewById(R.id.brown_picker_left));

        if (theColor(R.color.green) == color)
            makeViewSquare(findViewById(R.id.green_picker_left));

        if (theColor(R.color.orange) == color)
            makeViewSquare(findViewById(R.id.orange_picker_left));

        if (theColor(R.color.pink) == color)
            makeViewSquare(findViewById(R.id.pink_picker_left));

        if (theColor(R.color.purple) == color)
            makeViewSquare(findViewById(R.id.purple_picker_left));

        if (theColor(R.color.red) == color)
            makeViewSquare(findViewById(R.id.red_picker_left));

        if (theColor(R.color.white) == color)
            makeViewSquare(findViewById(R.id.white_picker_left));

        if (theColor(R.color.yellow) == color)
            makeViewSquare(findViewById(R.id.yellow_picker_left));
    }

    void hideLeftBackgroundColor(int color) {
        for (int LEFT_TEXT_COLOR_ID : LEFT_TEXT_COLOR_IDS) {
            setRound(findViewById(LEFT_TEXT_COLOR_ID));
        }
        int highLightColor = ScoreKeeperUtils.getBackgroundColor(leftExampleTextView);
        if (theColor(R.color.black) == color)
            makeViewSquare(findViewById(R.id.black_picker_left_2));

        if (theColor(R.color.blue) == color)
            makeViewSquare(findViewById(R.id.blue_picker_left_2));

        if (theColor(R.color.brown) == color)
            makeViewSquare(findViewById(R.id.brown_picker_left_2));

        if (theColor(R.color.green) == color)
            makeViewSquare(findViewById(R.id.green_picker_left_2));

        if (theColor(R.color.orange) == color)
            makeViewSquare(findViewById(R.id.orange_picker_left_2));

        if (theColor(R.color.pink) == color)
            makeViewSquare(findViewById(R.id.pink_picker_left_2));

        if (theColor(R.color.purple) == color)
            makeViewSquare(findViewById(R.id.purple_picker_left_2));

        if (theColor(R.color.red) == color)
            makeViewSquare(findViewById(R.id.red_picker_left_2));

        if (theColor(R.color.white) == color)
            makeViewSquare(findViewById(R.id.white_picker_left_2));

        if (theColor(R.color.yellow) == color)
            makeViewSquare(findViewById(R.id.yellow_picker_left_2));
    }

    private void setRound(View view) {
        if (Build.VERSION.SDK_INT > 19) {
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(ScoreKeeperUtils.getBackgroundColor(view));
            gd.setCornerRadius(45);
            //   gd.setStroke(3, theColor(R.color.black));
            view.setBackground(gd);
        }
    }


    private void makeViewSquare(View view) {
        if (Build.VERSION.SDK_INT > 19) {
            Drawable drawable = (Drawable) view.getBackground();
            if (drawable != null) {
                if (drawable instanceof GradientDrawable) {
                    GradientDrawable gd = (GradientDrawable) drawable;
                    gd.setCornerRadius(0);
                    int bgColor = ScoreKeeperUtils.getBackgroundColor(view);
                    if (bgColor == theColor(R.color.white) || bgColor == theColor(R.color.yellow)) {
                        gd.setStroke(3, theColor(R.color.black));
                    } else {
                        gd.setStroke(3, theColor(R.color.white));
                    }
                }
            }
        }
    }

    void showMessage() {
        Toast.makeText(this.getBaseContext(), "Please choose another color.", Toast.LENGTH_SHORT).show();
    }

}

