package codepath.apps.demointroandroid;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import codepath.apps.demointroandroid.domain.ScoreKeeperData;
import codepath.apps.demointroandroid.domain.ScoreKeeperPrefKeys;
import codepath.apps.demointroandroid.util.ActivityWithState;
import codepath.apps.demointroandroid.util.CommonPreferencesUtility;
import codepath.apps.demointroandroid.util.DialogUtility;
import codepath.apps.demointroandroid.util.FileUtility;
import codepath.apps.demointroandroid.util.ScoreKeeperUtils;

public class ScoreKeeperActivity extends Activity implements SensorEventListener, ActivityWithState {

    long timestampForEvent;
    public TextView textScoreLeft;
    public TextView textScoreRight;
    public TextView textNameLeft;
    public TextView textNameRight;
    Vibrator v;
    boolean isTiltedLeft = false;
    boolean isTiltedRight = false;
    boolean isReturnedCenter = false;
    int width;
    float initialY;
    float initialX;
    private long lastShakeTime;
    public boolean isPaused = false;
    long downclicktime;
    float scrollX;
    float scrollY;
    private ScoreKeeperData scoreKeeperData;
    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    ScheduledFuture<?> future;

    public ScoreKeeperData getScoreKeeperData() {
        if (scoreKeeperData == null) {
            scoreKeeperData = new ScoreKeeperData();
        }
        return scoreKeeperData;
    }

    final Runnable shutDownTheApp = new Runnable() {
        @Override
        public void run() {
            if (Build.VERSION.SDK_INT >= 21) {
                getMe().finishAndRemoveTask();
            } else if (Build.VERSION.SDK_INT >= 16) {
                getMe().finishAffinity();
            } else {
                getMe().finish();
            }
            System.exit(0);
        }
    };

    public void showToastInThread(final String toast) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getBaseContext(), toast, Toast.LENGTH_LONG).show();
            }
        });
    }

    final Runnable alertAndShutDownTheApp = new Runnable() {
        @Override
        public void run() {
            if (!isPaused) {
                v.vibrate(500);
                showToastInThread("Due to inactivity Score Keeper will shut down in 15 seconds.");
            }
            if (future != null) {
                future.cancel(true);
            }
            future = scheduler.schedule(shutDownTheApp, 15, TimeUnit.SECONDS);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_keeper_view);

        textScoreLeft = (TextView) findViewById(R.id.textScoreLeft);
        textScoreRight = (TextView) findViewById(R.id.textScoreRight);
        textNameLeft = (TextView) findViewById(R.id.textNameLeft);
        textNameRight = (TextView) findViewById(R.id.textNameRight);

        textNameLeft.setOnLongClickListener(leftNameListener);
        textNameRight.setOnLongClickListener(rightNameListener);
        timestampForEvent = System.currentTimeMillis();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        width = metrics.widthPixels;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        initState();
        processListener();
        setFont();
        displayScore(false);
        showGotItDialogOnceOnly();
    }

    private void setFont() {

        Typeface typeface = ScoreKeeperUtils.getTypeface(getScoreKeeperData().fontName, getAssets());
        textScoreLeft.setTypeface(typeface);
        textNameLeft.setTypeface(typeface);
        textScoreRight.setTypeface(typeface);
        textNameRight.setTypeface(typeface);
    }

    private void switchSides() {

        findViewById(R.id.textWinLeft).setVisibility(View.INVISIBLE);
        findViewById(R.id.textWinRight).setVisibility(View.INVISIBLE);

        int tempLeftScore = getScoreKeeperData().leftScore;
        int tempRightScore = getScoreKeeperData().rightScore;
        int tempLeftColor = ScoreKeeperUtils.getBackgroundColor(textScoreLeft);
        int tempRightColor = ScoreKeeperUtils.getBackgroundColor(textScoreRight);
        int tempLeftTextColor = textScoreLeft.getCurrentTextColor();
        int tempRightTextColor = textScoreRight.getCurrentTextColor();
        String tempLeftTeamName = getScoreKeeperData().leftTeamName;
        String tempRightTeamName = getScoreKeeperData().rightTeamName;

        textScoreRight.setTextColor(tempLeftTextColor);
        textScoreRight.setBackgroundColor(tempLeftColor);
        textScoreLeft.setTextColor(tempRightTextColor);
        textScoreLeft.setBackgroundColor(tempRightColor);

        textNameLeft.setTextColor(tempRightColor);
        textNameLeft.setBackgroundColor(tempRightTextColor);
        textNameRight.setTextColor(tempLeftColor);
        textNameRight.setBackgroundColor(tempLeftTextColor);

        getScoreKeeperData().rightScore = tempLeftScore;
        getScoreKeeperData().leftScore = tempRightScore;

        getScoreKeeperData().rightTeamName = tempLeftTeamName;
        getScoreKeeperData().leftTeamName = tempRightTeamName;
        displayScore(false);
    }

    public void resetScore() {

        if (getScoreKeeperData().leftScore == getScoreKeeperData().resetScoreTo && getScoreKeeperData().rightScore == getScoreKeeperData().resetScoreTo) {
            Toast.makeText(this.getBaseContext(), "Score is already reset - no changes were made.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isFileSaveEnabled()) {
            FileUtility.saveToStorage(this);
        }

        getScoreKeeperData().leftScore = getScoreKeeperData().resetScoreTo;
        getScoreKeeperData().rightScore = getScoreKeeperData().resetScoreTo;
        displayScore(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int SCROLL_THRESHOLD = 10;

        int density = getResources().getDisplayMetrics().densityDpi;
        if (density <= DisplayMetrics.DENSITY_HIGH ){
            SCROLL_THRESHOLD = 5;
        }
        if (isScreenGrey()) {
            return false;
        }
        if (isPaused) {
            return false;
        }
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                downclicktime = System.currentTimeMillis();
                initialY = event.getY();
                initialX = event.getX();
                scrollX = initialX;
                scrollY = initialY;
                handler.postDelayed(mLongPressed, ViewConfiguration.getLongPressTimeout());
            case (MotionEvent.ACTION_MOVE):

                if ((Math.abs(scrollX - event.getX()) > SCROLL_THRESHOLD || Math.abs(scrollY - event.getY()) > SCROLL_THRESHOLD)) {
                    downclicktime = System.currentTimeMillis();
                    handler.removeCallbacks(mLongPressed);
                }
                scrollX = event.getX();
                scrollY = event.getY();
                return true;
            case (MotionEvent.ACTION_UP):
                handler.removeCallbacks(mLongPressed);
                // should have called the long click process
                if (System.currentTimeMillis() - 1000 > downclicktime) {
                    return false;
                }

                float center = width / 2;
                float currentX = event.getX();
                if (initialX < center && currentX > center) {
                    switchSides();
                    return true;
                }
                // swiped side to side
                if (initialX > center && currentX < center) {
                    switchSides();
                    return true;
                }

                int addThis = 1;
                float xMove = initialX - currentX;
                float isHorizantalThreashold = 200;
                if (density <= DisplayMetrics.DENSITY_HIGH ){
                    isHorizantalThreashold = 100;
                }
                if (Math.abs(xMove) > isHorizantalThreashold) {
                    // horizontal swipe
                    if (xMove > 0) {
                        addThis = -1 ;
                    }
                } else {
                    // vertical swipe or tap
                    addThis = getScoreKeeperData().pointsForGoal;
                    if (initialY < event.getY() - 100) {
                        addThis = -1 * getScoreKeeperData().pointsForGoal;
                    }
                }
                if (currentX < center) {
                    //   leftScore = leftScore + addThis;
                    verifyAndDisplayScore(false, true, addThis);
                } else {
                    //  rightScore = rightScore + addThis;
                    verifyAndDisplayScore(false, false, addThis);
                }
                return true;
        }
        return true;
    }

    private boolean isScreenGrey() {
        return getResources().getColor(R.color.greyBackground) == ScoreKeeperUtils.getBackgroundColor(textScoreRight) || getResources().getColor(R.color.greytext) == ScoreKeeperUtils.getBackgroundColor(textScoreLeft);
    }

    public void displayScore(boolean doVibrate) {

        if (doVibrate) {
            long[] pattern = ScoreKeeperUtils.getVibratePattern(getScoreKeeperData().pointsForGoal);
            v.vibrate(pattern, -1);
        }
        textScoreLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, 1000000);

        textScoreRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScoreKeeperUtils.getTextSize(textScoreRight, getScoreKeeperData().rightScore));
        textScoreLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScoreKeeperUtils.getTextSize(textScoreLeft, getScoreKeeperData().leftScore));

        checkScoresForZero();

        textScoreLeft.setText(String.valueOf(getScoreKeeperData().leftScore));
        textScoreRight.setText(String.valueOf(getScoreKeeperData().rightScore));
        textNameLeft.setText(getScoreKeeperData().leftTeamName);
        textNameRight.setText(getScoreKeeperData().rightTeamName);
        if (isGameWon(getScoreKeeperData().leftScore, getScoreKeeperData().rightScore)) {
            boolean leftWins = getScoreKeeperData().leftScore > getScoreKeeperData().rightScore;
            long[] pattern = ScoreKeeperUtils.getWinningPattern();
            showWinFlash(leftWins);
            if (!getScoreKeeperData().hasCelebratedWin) {
                v.vibrate(pattern, -1);
            }
            getScoreKeeperData().hasCelebratedWin = true;
        } else {
            findViewById(R.id.textWinLeft).setVisibility(View.INVISIBLE);
            findViewById(R.id.textWinRight).setVisibility(View.INVISIBLE);
            getScoreKeeperData().hasCelebratedWin = false;
        }

        storeState();
        resetShutDownTimer();
    }


    private void showWinFlash(boolean left) {
        TextView tView;
        String teamName = ScoreKeeperUtils.getInstance().getTeamInfo(this, left);
        int points;
        int backgroundColor;
        int textColor;
        if (left) {
            tView = (TextView) findViewById(R.id.textWinLeft);
            points = getScoreKeeperData().leftScore;
            backgroundColor = ScoreKeeperUtils.getBackgroundColor(textScoreLeft);
            textColor = textScoreLeft.getCurrentTextColor();
        } else {
            tView = (TextView) findViewById(R.id.textWinRight);
            points = getScoreKeeperData().rightScore;
            backgroundColor = ScoreKeeperUtils.getBackgroundColor(textScoreRight);
            textColor = textScoreRight.getCurrentTextColor();
        }
        boolean teamNameEndsWithS = false;
        if (teamName != null && teamName.toLowerCase().endsWith("s")) {
            teamNameEndsWithS = true;
        }
        tView.setVisibility(View.VISIBLE);
        tView.setBackgroundColor(backgroundColor);
        tView.setTextColor(textColor);
        tView.setText(teamName + " win" + (teamNameEndsWithS ? "" : "s") + " with " + points + " points!");
        tView.bringToFront();

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
        initState();
        processListener();
        setFont();
        displayScore(false);
    }

    public void storeState() {

        if (isScreenGrey()) {
            return;
        }
        SharedPreferences sharedPref = this.getSharedPreferences(ScoreKeeperPrefKeys.SHARED_PREFERENCES.name(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(ScoreKeeperPrefKeys.LEFT_BACKGROUND.name(), ScoreKeeperUtils.getBackgroundColor(textScoreLeft));
        editor.putInt(ScoreKeeperPrefKeys.RIGHT_BACKGROUND.name(), ScoreKeeperUtils.getBackgroundColor(textScoreRight));
        editor.putInt(ScoreKeeperPrefKeys.LEFT_TEXT.name(), textScoreLeft.getCurrentTextColor());
        editor.putInt(ScoreKeeperPrefKeys.RIGHT_TEXT.name(), textScoreRight.getCurrentTextColor());
        editor.putInt(ScoreKeeperPrefKeys.LEFT_SCORE.name(), getScoreKeeperData().leftScore);
        editor.putInt(ScoreKeeperPrefKeys.RIGHT_SCORE.name(), getScoreKeeperData().rightScore);

        CommonPreferencesUtility.storeCommonScoreKeeperDatas(editor, this);
    }


    protected void initState() {
        SharedPreferences sharedPref = this.getSharedPreferences(ScoreKeeperPrefKeys.SHARED_PREFERENCES.toString(), Context.MODE_PRIVATE);
        textScoreLeft.setBackgroundColor(sharedPref.getInt(ScoreKeeperPrefKeys.LEFT_BACKGROUND.name(), getResources().getColor(R.color.red)));
        textScoreRight.setBackgroundColor(sharedPref.getInt(ScoreKeeperPrefKeys.RIGHT_BACKGROUND.name(), getResources().getColor(R.color.blue)));
        textScoreLeft.setTextColor(sharedPref.getInt(ScoreKeeperPrefKeys.LEFT_TEXT.name(), getResources().getColor(R.color.white)));
        textScoreRight.setTextColor(sharedPref.getInt(ScoreKeeperPrefKeys.RIGHT_TEXT.name(), getResources().getColor(R.color.white)));
        textNameRight.setBackgroundColor(textScoreRight.getCurrentTextColor());
        textNameRight.setTextColor(ScoreKeeperUtils.getBackgroundColor(textScoreRight));
        textNameLeft.setBackgroundColor(textScoreLeft.getCurrentTextColor());
        textNameLeft.setTextColor(ScoreKeeperUtils.getBackgroundColor(textScoreLeft));

        CommonPreferencesUtility.pullDataFromPreferences(sharedPref, this);
    }

    private boolean checkForShake(SensorEvent event) {

        if (getAcceleration(event) > 8.25f) {
            lastShakeTime = System.currentTimeMillis();
            textScoreLeft.setBackgroundColor(getResources().getColor(R.color.greyBackground));
            textScoreRight.setBackgroundColor(getResources().getColor(R.color.greyBackground));
            textScoreRight.setTextColor(getResources().getColor(R.color.greytext));
            textScoreLeft.setTextColor(getResources().getColor(R.color.greytext));
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
            verifyAndDisplayScore(true, true, getScoreKeeperData().pointsForGoal);
        }

        // tilt right
        if (tiltedRight & !isTiltedRight && isReturnedCenter) {
            isTiltedLeft = false;
            isReturnedCenter = false;
            isTiltedRight = true;
            verifyAndDisplayScore(true, false, getScoreKeeperData().pointsForGoal);
        }

        // return to center
        if (returnedToCenter & !isReturnedCenter) {
            isTiltedLeft = false;
            isReturnedCenter = true;
            isTiltedRight = false;
        }
    }

    private void verifyAndDisplayScore(boolean shouldVibrate, boolean isLeft, int pointValue) {

        int potentialRightScore = getScoreKeeperData().rightScore;
        int potentialLeftScore = getScoreKeeperData().leftScore;

        if (isLeft) {
            potentialLeftScore = potentialLeftScore + pointValue;
        } else {
            potentialRightScore = potentialRightScore + pointValue;
        }

        if (pointValue > 0 && isGameWon(getScoreKeeperData().rightScore, getScoreKeeperData().leftScore)) {
            //  Toast.makeText(this.getApplicationContext(), "Game is already won based on the parameters from the menu setting 'Win Parameters'. Score will not change.", Toast.LENGTH_LONG).show();
            return;
        }

        getScoreKeeperData().rightScore = potentialRightScore;
        getScoreKeeperData().leftScore = potentialLeftScore;
        displayScore(shouldVibrate);
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

    Handler handler = new Handler();
    Runnable mLongPressed = new Runnable() {
        public void run() {
            storeState();
            DialogUtility.showMenuPopUp(getMe());
        }
    };

    ScoreKeeperActivity getMe() {
        return this;
    }

    private View.OnLongClickListener leftNameListener = new View.OnLongClickListener() {

        public boolean onLongClick(View v) {
            showNameDialog(true);
            return true;
        }
    };
    private View.OnLongClickListener rightNameListener = new View.OnLongClickListener() {

        public boolean onLongClick(View v) {
            showNameDialog(false);
            return true;
        }
    };

    private void showNameDialog(boolean isLeft) {
        storeState();
        DialogUtility.showEnterNameDialog(this, isLeft);
    }

    boolean isFileSaveEnabled() {
        return ScoreKeeperUtils.getTodayAsNoTimeString().equals(getScoreKeeperData().fileSaveFeatureDate) && getScoreKeeperData().fileSaveForToday;
    }

    private void checkScoresForZero() {
        if (getScoreKeeperData().leftScore < 0) {
            getScoreKeeperData().leftScore = 0;
        }
        if (getScoreKeeperData().rightScore < 0) {
            getScoreKeeperData().rightScore = 0;
        }
    }

    boolean isGameWon(int thePotentialLeftScore, int thePotentialRightScore) {
        if (getScoreKeeperData().gamePoint != null) {
            if (thePotentialLeftScore >= getScoreKeeperData().gamePoint.getGamePoint() || thePotentialRightScore >= getScoreKeeperData().gamePoint.getGamePoint()) {
                int diff = thePotentialLeftScore - thePotentialRightScore;
                if (Math.abs(diff) >= getScoreKeeperData().gamePoint.getPointSpread()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void processListener() {
        SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (!getScoreKeeperData().disableTiltFeature) {
            mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        } else {
            mSensorManager.unregisterListener(this);
        }
    }

    private void showGotItDialogOnceOnly() {

       SharedPreferences sharedPref = this.getSharedPreferences(ScoreKeeperPrefKeys.SHARED_PREFERENCES.name(), Context.MODE_PRIVATE);
        if ( !sharedPref.getBoolean("showedGotIt", false) ){
            DialogUtility.showGotItDialog(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("showedGotIt", true);
            editor.commit();
        }
    }

    private void resetShutDownTimer() {
        if (future != null) {
            future.cancel(true);
        }
        future =  scheduler.schedule(alertAndShutDownTheApp,getScoreKeeperData().shutDownMinutes, TimeUnit.MINUTES);
    }
}
