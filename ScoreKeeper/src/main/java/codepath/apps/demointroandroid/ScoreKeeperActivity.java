package codepath.apps.demointroandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.view.MotionEventCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ScoreKeeperActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor accelerometer;

    long timestampForEvent;
    private TextView textLeft;
    private TextView textRight;
    Vibrator v;
    boolean isTiltedLeft = false;
    boolean isTiltedRight = false;
    boolean isReturnedCenter = false;
    int leftScore = 0;
    int rightScore = 0;
    int height;
    int width;
    float initialY;
    String leftOrRightMenuSelected;
    static String RIGHT_TEXT = "R_T";
    static String LEFT_TEXT = "L_T";
    static String RIGHT_BACKGROUND = "R_B";
    static String LEFT_BACKGROUND = "L_B";
    static String LEFT_SCORE = "L_S";
    static String RIGHT_SCORE = "R_S";
    static String POINT_PER_GOAL = "P_P_G";
    static String RESET_SCORE_TO = "R_S_T";

    static int GREY_BG_COLOR = 0xffD3D3D3;
    static int GREY_TXT_COLOR = 0xffA9A9A9;

    int pointsForGoal = 1;
    int resetScoreTo = 0;
    private long lastShakeTime;
    private boolean isPaused = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String menuTitle = String.valueOf(item.getTitle());

        if ("Right Color".equalsIgnoreCase(menuTitle)) {
            leftOrRightMenuSelected = RIGHT_BACKGROUND;
        }
        if ("Left Color".equalsIgnoreCase(menuTitle)) {
            leftOrRightMenuSelected = LEFT_BACKGROUND;
        }
        if ("Left Text Color".equalsIgnoreCase(menuTitle)) {
            leftOrRightMenuSelected = LEFT_TEXT;
        }
        if ("Right Text Color".equalsIgnoreCase(menuTitle)) {
            leftOrRightMenuSelected = RIGHT_TEXT;
        }
        if ("Switch Sides".equalsIgnoreCase(menuTitle)) {
            switchSides();
        }
        if ("Reset Score".equalsIgnoreCase(menuTitle)) {
            showAreYouSureDialog("Reset Score", "Are you sure you want to reset the score?", true);
        } else if ("red".equalsIgnoreCase(menuTitle)) {
            setColorOfItem(leftOrRightMenuSelected, 0xffff0000);
        } else if ("blue".equalsIgnoreCase(menuTitle)) {
            setColorOfItem(leftOrRightMenuSelected, 0xff0000ff);
        } else if ("yellow".equalsIgnoreCase(menuTitle)) {
            setColorOfItem(leftOrRightMenuSelected, 0xffffff00);
        } else if ("green".equalsIgnoreCase(menuTitle)) {
            setColorOfItem(leftOrRightMenuSelected, 0xff00ff00);
        } else if ("purple".equalsIgnoreCase(menuTitle)) {
            setColorOfItem(leftOrRightMenuSelected, 0xFF551A8B);
        } else if ("orange".equalsIgnoreCase(menuTitle)) {
            setColorOfItem(leftOrRightMenuSelected, 0xFFFFA500);
        } else if ("black".equalsIgnoreCase(menuTitle)) {
            setColorOfItem(leftOrRightMenuSelected, 0xff000000);
        } else if ("white".equalsIgnoreCase(menuTitle)) {
            setColorOfItem(leftOrRightMenuSelected, 0xffffffff);
        } else if ("Other...".equalsIgnoreCase(menuTitle)) {
            showEnterNumberdialog("Points Per Goal", "Enter the points per goal.", true);
        } else if ("Reset Score To...".equalsIgnoreCase(menuTitle)) {
            showEnterNumberdialog("Reset Score Value", "Enter the initial score you want to show when you 'Reset Score'.", false);
        } else if ("Reset Preferences".equalsIgnoreCase(menuTitle)){
            showAreYouSureDialog("Reset EVERYTHING", "Are you sure you want to reset your settings?", false);
        } else if (isNumeric(menuTitle)) {
            pointsForGoal = Integer.valueOf(menuTitle);
        }
        return true;
    }

    private boolean isNumeric(String chars) {
        return TextUtils.isDigitsOnly(chars);
    }

    private void switchSides() {
        int tempLeftScore = leftScore;
        int tempRightScore = rightScore;
        int tempLeftColor = getBackgroundColor(textLeft);
        int tempRightColor = getBackgroundColor(textRight);
        int tempLeftTextColor = textLeft.getCurrentTextColor();
        int tempRightTextColor = textRight.getCurrentTextColor();

        textRight.setTextColor(tempLeftTextColor);
        textRight.setBackgroundColor(tempLeftColor);
        textLeft.setTextColor(tempRightTextColor);
        textLeft.setBackgroundColor(tempRightColor);

        rightScore = tempLeftScore;
        leftScore = tempRightScore;
        displayScore(false);
    }

    private void resetScore() {
        leftScore = resetScoreTo;
        rightScore = resetScoreTo;
        displayScore(false);
    }

    private void setColorOfItem(String leftOrRightChoice, int color) {
        if (RIGHT_BACKGROUND.equalsIgnoreCase(leftOrRightChoice)) {
            textRight.setBackgroundColor(color);
        } else if (LEFT_BACKGROUND.equalsIgnoreCase(leftOrRightChoice)) {
            textLeft.setBackgroundColor(color);
        } else if (RIGHT_TEXT.equalsIgnoreCase(leftOrRightChoice)) {
            textRight.setTextColor(color);
        } else if (LEFT_TEXT.equalsIgnoreCase(leftOrRightChoice)) {
            textLeft.setTextColor(color);
        }
        leftOrRightMenuSelected = null;
        storeState();
    }

    public static int getBackgroundColor(TextView textView) {
        Drawable drawable = textView.getBackground();
        if (drawable instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) drawable;
            if (Build.VERSION.SDK_INT >= 11) {
                return colorDrawable.getColor();
            }
            try {
                Field field = colorDrawable.getClass().getDeclaredField("mState");
                field.setAccessible(true);
                Object object = field.get(colorDrawable);
                field = object.getClass().getDeclaredField("mUseColor");
                field.setAccessible(true);
                return field.getInt(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isScreenGrey()) {
            return false;
        }
        if (isPaused) {
            return false;
        }
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                initialY = event.getY();
            case (MotionEvent.ACTION_MOVE):
                return true;
            case (MotionEvent.ACTION_UP):
                int addThis = 1;
                if (initialY < event.getY()) {
                    addThis = -1;
                }
                if (event.getX() < width / 2) {
                    leftScore = leftScore + addThis;
                } else {
                    rightScore = rightScore + addThis;
                }
                displayScore(false);
                return true;
        }
        return true;
    }

    private boolean isScreenGrey() {
        return GREY_BG_COLOR == getBackgroundColor(textRight) || GREY_BG_COLOR == getBackgroundColor(textLeft);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_keeper_view);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        textLeft = (TextView) findViewById(R.id.textAcc1);
        textRight = (TextView) findViewById(R.id.textAcc2);

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        initListeners();

        timestampForEvent = System.currentTimeMillis();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        height = metrics.heightPixels;
        width = metrics.widthPixels;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initState();
        displayScore(false);
    }

    private void displayScore(boolean doVibrate) {
        if (doVibrate) {
            long[] pattern = getVibratePattern(pointsForGoal);
            v.vibrate(pattern, -1);
        }

        int leftScoreSize = 200;
        int rightScoreSize = 200;

        if (leftScore > 99) {
            leftScoreSize = 180;
        }
        if (rightScore > 99) {
            rightScoreSize = 180;
        }
        if (leftScore > 999) {
            leftScoreSize = 140;
        }
        if (rightScore > 999) {
            rightScoreSize = 140;
        }
        if (leftScore > 9999) {
            leftScoreSize = 100;
        }
        if (rightScore > 9999) {
            rightScoreSize = 100;
        }
        if (leftScore > 99999) {
            leftScoreSize = 80;
        }
        if (rightScore > 99999) {
            rightScoreSize = 80;
        }

        textLeft.setTextSize(leftScoreSize);
        textRight.setTextSize(rightScoreSize);

        textLeft.setText(String.valueOf(leftScore));
        textRight.setText(String.valueOf(rightScore));
        storeState();
    }

    private long[] getVibratePattern(int points) {
        List<Long> vibrateList = new ArrayList<Long>();

        if (points > 6) {
            return new long[]{0, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 25, 25, 25, 25, 10, 10, 5, 5, 5, 5, 5, 5, 5, 5};
        }

        vibrateList.add(0L);
        for (int i = 0; i < points; i++) {
            if (i > 0L) {
                vibrateList.add(150L);
            }
            vibrateList.add(100L);
        }
        long[] pattern = new long[vibrateList.size()];
        for (int j = 0; j < vibrateList.size(); j++) {
            pattern[j] = vibrateList.get(j);

        }
        return pattern;
    }

    public void initListeners() {
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            if (isPaused) {
                return;
            }

            if (checkForShake(event)) {
                return;
            }

            if (System.currentTimeMillis() - 100 < timestampForEvent) {
                return;
            }
            if (System.currentTimeMillis() - 2000 < lastShakeTime) {
                return;
            }

            if (isScreenGrey()) {
                initState();
            }

            timestampForEvent = System.currentTimeMillis();

            checkAndProcessTilt(event);
        }
    }

    private void storeState() {

        if (isScreenGrey()) {
            return;
        }
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(LEFT_BACKGROUND, getBackgroundColor(textLeft));
        editor.putInt(RIGHT_BACKGROUND, getBackgroundColor(textRight));
        editor.putInt(LEFT_TEXT, textLeft.getCurrentTextColor());
        editor.putInt(RIGHT_TEXT, textRight.getCurrentTextColor());
        editor.putInt(LEFT_SCORE, leftScore);
        editor.putInt(RIGHT_SCORE, rightScore);
        editor.putInt(POINT_PER_GOAL, pointsForGoal);
        editor.putInt(RESET_SCORE_TO, resetScoreTo);
        editor.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;
    }

    private void clearState (){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(LEFT_BACKGROUND);
        editor.remove(RIGHT_BACKGROUND);
        editor.remove(LEFT_TEXT);
        editor.remove(RIGHT_TEXT);
        editor.remove(LEFT_SCORE);
        editor.remove(RIGHT_SCORE);
        editor.remove(POINT_PER_GOAL);
        editor.remove(RESET_SCORE_TO);
        editor.commit();
    }

    private void initState() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        textLeft.setBackgroundColor(sharedPref.getInt(LEFT_BACKGROUND, 0xffff0000));
        textRight.setBackgroundColor(sharedPref.getInt(RIGHT_BACKGROUND, 0xff0000ff));
        textLeft.setTextColor(sharedPref.getInt(LEFT_TEXT, 0xffffffff));
        textRight.setTextColor(sharedPref.getInt(RIGHT_TEXT, 0xffffffff));
        leftScore = sharedPref.getInt(LEFT_SCORE, 0);
        rightScore = sharedPref.getInt(RIGHT_SCORE, 0);
        pointsForGoal = sharedPref.getInt(POINT_PER_GOAL, 1);
        resetScoreTo = sharedPref.getInt(RESET_SCORE_TO, 0);
    }

    private boolean checkForShake(SensorEvent event) {

        if (getAcceleration(event) > 8.25f) {
            lastShakeTime = System.currentTimeMillis();
            setColorOfItem(LEFT_BACKGROUND, GREY_BG_COLOR);
            setColorOfItem(RIGHT_BACKGROUND, GREY_BG_COLOR);
            textRight.setTextColor(GREY_TXT_COLOR);
            textLeft.setTextColor(GREY_TXT_COLOR);
            return true;
        } else {
            return false;
        }
    }

    private double getAcceleration(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        return Math.sqrt(Math.pow(x, 2) +
                Math.pow(y, 2) +
                Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;
    }

    private void checkAndProcessTilt(SensorEvent event) {

        float yAxis = Math.round(event.values[1] * 10) / 10;

        boolean tiltedLeft = yAxis > 5;
        boolean tiltedRight = yAxis < -5;
        boolean returnedToCenter = -3 < yAxis && yAxis < 3;

        //tilt left
        if (tiltedLeft & !isTiltedLeft && isReturnedCenter) {
            isTiltedLeft = true;
            isReturnedCenter = false;
            isTiltedRight = false;
            leftScore = leftScore + pointsForGoal;
            displayScore(true);
        }

        // tilt right
        if (tiltedRight & !isTiltedRight && isReturnedCenter) {
            isTiltedLeft = false;
            isReturnedCenter = false;
            isTiltedRight = true;
            rightScore = rightScore + pointsForGoal;
            displayScore(true);
        }

        // return to center
        if (returnedToCenter & !isReturnedCenter) {
            isTiltedLeft = false;
            isReturnedCenter = true;
            isTiltedRight = false;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // no need to implement anything here
    }

    public void showAreYouSureDialog(String title, String message, final boolean scoreOnly) {
        isPaused = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                isPaused = false;
            }
        });

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(scoreOnly) {
                    resetScore();
                } else {
                    clearState();
                    initState();
                    displayScore(false);
                }
                isPaused = false;
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                isPaused = false;
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showEnterNumberdialog(String title, String message, final boolean pointsPerGoal) {
        isPaused = true;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                isPaused = false;
            }
        });


        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        isPaused = false;
                        String text = String.valueOf(input.getText());
                        if (pointsPerGoal) {
                            pointsForGoal =  ("").equals(text) || null == text ? 1 : Integer.valueOf(text);
                        } else {
                            resetScoreTo = ("").equals(text) || null == text ? 1 : Integer.valueOf(text);
                        }
                    }
                });

        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        isPaused = false;
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isPaused = false;
    }
}
