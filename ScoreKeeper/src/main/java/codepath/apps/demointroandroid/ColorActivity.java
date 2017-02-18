package codepath.apps.demointroandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import static codepath.apps.demointroandroid.ScoreKeeperUtils.BLACK;
import static codepath.apps.demointroandroid.ScoreKeeperUtils.BLUE;
import static codepath.apps.demointroandroid.ScoreKeeperUtils.BROWN;
import static codepath.apps.demointroandroid.ScoreKeeperUtils.GREEN;
import static codepath.apps.demointroandroid.ScoreKeeperUtils.LEFT_SCORE;
import static codepath.apps.demointroandroid.ScoreKeeperUtils.ORANGE;
import static codepath.apps.demointroandroid.ScoreKeeperUtils.PINK;
import static codepath.apps.demointroandroid.ScoreKeeperUtils.PURPLE;
import static codepath.apps.demointroandroid.ScoreKeeperUtils.RED;
import static codepath.apps.demointroandroid.ScoreKeeperUtils.RIGHT_SCORE;
import static codepath.apps.demointroandroid.ScoreKeeperUtils.SHARED_PREFERENCES;
import static codepath.apps.demointroandroid.ScoreKeeperUtils.WHITE;
import static codepath.apps.demointroandroid.ScoreKeeperUtils.YELLOW;
import static codepath.apps.demointroandroid.ScoreKeeperUtils.arrayContains;
import static codepath.apps.demointroandroid.ScoreKeeperUtils.getBackgroundColor;


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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
        SharedPreferences sharedPref = this.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        leftBackgroundColor = sharedPref.getInt(ScoreKeeperUtils.LEFT_BACKGROUND, ScoreKeeperUtils.RED);
        rightBackgroundColor = sharedPref.getInt(ScoreKeeperUtils.RIGHT_BACKGROUND, ScoreKeeperUtils.BLUE);
        leftTextColor = sharedPref.getInt(ScoreKeeperUtils.LEFT_TEXT, ScoreKeeperUtils.WHITE);
        rightTextColor = sharedPref.getInt(ScoreKeeperUtils.RIGHT_TEXT, ScoreKeeperUtils.WHITE);

        leftExample.setBackgroundColor(leftBackgroundColor);
        rightExample.setBackgroundColor(rightBackgroundColor);
        leftExample.setTextColor(leftTextColor);
        rightExample.setTextColor(rightTextColor);
        rightExample.setText(String.valueOf(sharedPref.getInt(RIGHT_SCORE, 0)));
        leftExample.setText(String.valueOf(sharedPref.getInt(LEFT_SCORE, 0)));
        System.out.println(sharedPref.getString("dave", "nope"));
    }

    protected void setState() {
        SharedPreferences sharedPref = this.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(ScoreKeeperUtils.LEFT_BACKGROUND, leftBackgroundColor);
        editor.putInt(ScoreKeeperUtils.RIGHT_BACKGROUND, rightBackgroundColor);
        editor.putInt(ScoreKeeperUtils.LEFT_TEXT, leftTextColor);
        editor.putInt(ScoreKeeperUtils.RIGHT_TEXT, rightTextColor);
        editor.commit();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Color Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}

