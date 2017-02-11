package codepath.apps.demointroandroid;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;

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
    float initialX;
    static String LEFT_SCORE = "L_S";
    static String RIGHT_SCORE = "R_S";
    static String POINT_PER_GOAL = "P_P_G";
    static String RESET_SCORE_TO = "R_S_T";
    static String LEFT_BACKGROUND = "L_B";
    static String RIGHT_BACKGROUND = "R_B";
    static String LEFT_TEXT = "L_T";
    static String RIGHT_TEXT = "R_T";

    protected int pointsForGoal = 1;
    protected int resetScoreTo = 0;
    private long lastShakeTime;
    protected boolean isPaused = false;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return MenuService.onOptionsItemSelected(item, this);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuService.disableSelectedMenuItems(menu, textLeft, textRight, pointsForGoal, resetScoreTo);
        return true;
    }

    private void switchSides() {
        int tempLeftScore = leftScore;
        int tempRightScore = rightScore;
        int tempLeftColor = ScoreKeeperUtils.getBackgroundColor(textLeft);
        int tempRightColor = ScoreKeeperUtils.getBackgroundColor(textRight);
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

    protected void resetScore() {
        leftScore = resetScoreTo;
        rightScore = resetScoreTo;
        displayScore(false);
    }

    protected void setColorOfItem(MenuItem item, int color) {
        if (item.getGroupId() == R.id.rightBG) {
            textRight.setBackgroundColor(color);
        } else if (item.getGroupId() == R.id.leftBG) {
            textLeft.setBackgroundColor(color);
        } else if (item.getGroupId() == R.id.rightText) {
            textRight.setTextColor(color);
        } else if (item.getGroupId() == R.id.leftText) {
            textLeft.setTextColor(color);
        }
        storeState();
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
                initialX = event.getX();
            case (MotionEvent.ACTION_MOVE):
                return true;
            case (MotionEvent.ACTION_UP):
                float center = width / 2;
                float currentX = event.getX();
                if (initialX < center && currentX > center) {
                    switchSides();
                    return true;
                }
                if (initialX > center && currentX < center) {
                    switchSides();
                    return true;
                }

                int addThis = 1;

                float xMove = initialX - currentX;
                if (Math.abs(xMove) > 200) {
                    addThis = pointsForGoal;
                    if (xMove > 0) {
                        addThis = -1 * pointsForGoal;
                    }
                } else {
                    if (initialY < event.getY() - 100) {
                        addThis = -1;
                    }
                }
                if (currentX < center) {
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
        return ScoreKeeperUtils.GREY_BG_COLOR == ScoreKeeperUtils.getBackgroundColor(textRight) || ScoreKeeperUtils.GREY_BG_COLOR == ScoreKeeperUtils.getBackgroundColor(textLeft);
    }

    protected void displayScore(boolean doVibrate) {
        if (doVibrate) {
            long[] pattern = ScoreKeeperUtils.getVibratePattern(pointsForGoal);
            v.vibrate(pattern, -1);
        }

        textLeft.setTextSize(ScoreKeeperUtils.getTextSize(leftScore));
        textRight.setTextSize(ScoreKeeperUtils.getTextSize(rightScore));

        textLeft.setText(String.valueOf(leftScore));
        textRight.setText(String.valueOf(rightScore));
        storeState();
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
        editor.putInt(LEFT_BACKGROUND, ScoreKeeperUtils.getBackgroundColor(textLeft));
        editor.putInt(RIGHT_BACKGROUND, ScoreKeeperUtils.getBackgroundColor(textRight));
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
        isReturnedCenter = false;
    }

    protected void clearState() {
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

    protected void initState() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        textLeft.setBackgroundColor(sharedPref.getInt(LEFT_BACKGROUND, ScoreKeeperUtils.RED));
        textRight.setBackgroundColor(sharedPref.getInt(RIGHT_BACKGROUND, ScoreKeeperUtils.BLUE));
        textLeft.setTextColor(sharedPref.getInt(LEFT_TEXT, ScoreKeeperUtils.WHITE));
        textRight.setTextColor(sharedPref.getInt(RIGHT_TEXT, ScoreKeeperUtils.WHITE));
        leftScore = sharedPref.getInt(LEFT_SCORE, 0);
        rightScore = sharedPref.getInt(RIGHT_SCORE, 0);
        pointsForGoal = sharedPref.getInt(POINT_PER_GOAL, 1);
        resetScoreTo = sharedPref.getInt(RESET_SCORE_TO, 0);
    }

    private boolean checkForShake(SensorEvent event) {

        if (getAcceleration(event) > 8.25f) {
            lastShakeTime = System.currentTimeMillis();
            textLeft.setBackgroundColor(ScoreKeeperUtils.GREY_BG_COLOR);
            textRight.setBackgroundColor(ScoreKeeperUtils.GREY_BG_COLOR);
            textRight.setTextColor(ScoreKeeperUtils.GREY_TXT_COLOR);
            textLeft.setTextColor(ScoreKeeperUtils.GREY_TXT_COLOR);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isPaused = false;
    }
}
