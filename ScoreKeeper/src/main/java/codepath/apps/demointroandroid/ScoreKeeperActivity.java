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
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreKeeperActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor accelerometer;

    long timestampForEvent;
    TextView textScoreLeft;
    TextView textScoreRight;
    private TextView textNameLeft;
    private TextView textNameRight;
    Vibrator v;
    boolean isTiltedLeft = false;
    boolean isTiltedRight = false;
    boolean isReturnedCenter = false;
    int leftScore = 0;
    int rightScore = 0;
    String leftTeamName;
    String rightTeamName;
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
    static String RIGHT_TEAM_NAME = "R_T_N";
    static String LEFT_TEAM_NAME = "L_T_N";
    static String FILE_SAVE_FEATURE_DATE ="F_S_F_D";
    static String FILE_SAVE_FEATURE_SWITCH = "F_S_F_S";
    static String WIN_BY_POINTS = "W_B_P";
    static String WIN_BY_SPREAD = "W_B_S";

    protected int pointsForGoal = 1;
    protected int resetScoreTo = 0;
    private long lastShakeTime;
    protected boolean isPaused = false;
    protected boolean fileSaveForToday = false;
    protected String fileSaveFeatureDate;
    protected WinBy winBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_keeper_view);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        textScoreLeft = (TextView) findViewById(R.id.textScoreLeft);
        textScoreRight = (TextView) findViewById(R.id.textScoreRight);
        textNameLeft = (TextView) findViewById(R.id.textNameLeft);
        textNameRight = (TextView) findViewById(R.id.textNameRight);

        textNameLeft.setOnLongClickListener(leftNameListener);
        textNameRight.setOnLongClickListener(rightNameListener);

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
        MenuService.disableSelectedMenuItems(menu, textScoreLeft, textScoreRight, pointsForGoal, resetScoreTo, isFileSaveEnabled(), winBy);
        return true;
    }

    private void switchSides() {
        int tempLeftScore = leftScore;
        int tempRightScore = rightScore;
        int tempLeftColor = ScoreKeeperUtils.getBackgroundColor(textScoreLeft);
        int tempRightColor = ScoreKeeperUtils.getBackgroundColor(textScoreRight);
        int tempLeftTextColor = textScoreLeft.getCurrentTextColor();
        int tempRightTextColor = textScoreRight.getCurrentTextColor();
        String tempLeftTeamName = leftTeamName;
        String tempRightTeamName = rightTeamName;

        textScoreRight.setTextColor(tempLeftTextColor);
        textScoreRight.setBackgroundColor(tempLeftColor);
        textScoreLeft.setTextColor(tempRightTextColor);
        textScoreLeft.setBackgroundColor(tempRightColor);

        textNameLeft.setTextColor(tempRightColor);
        textNameLeft.setBackgroundColor(tempRightTextColor);
        textNameRight.setTextColor(tempLeftColor);
        textNameRight.setBackgroundColor(tempLeftTextColor);

        rightScore = tempLeftScore;
        leftScore = tempRightScore;

        rightTeamName = tempLeftTeamName;
        leftTeamName = tempRightTeamName;
        displayScore(false);
    }

    protected void resetScore() {

        if (leftScore == resetScoreTo && rightScore == resetScoreTo){
            Toast.makeText(this.getBaseContext(), "Score is already reset - no changes were made.", Toast.LENGTH_LONG).show();
            return;
        }
        if (isFileSaveEnabled()) {
            FileUtility.saveToStorage(this);
        }

        leftScore = resetScoreTo;
        rightScore = resetScoreTo;
        displayScore(false);

    }

    protected void setColorOfItem(MenuItem item, int color) {
        if (item.getGroupId() == R.id.rightBG) {
            textScoreRight.setBackgroundColor(color);
             textNameRight.setTextColor(color);
        } else if (item.getGroupId() == R.id.leftBG) {
            textScoreLeft.setBackgroundColor(color);
            textNameLeft.setTextColor(color);
        } else if (item.getGroupId() == R.id.rightText) {
            textScoreRight.setTextColor(color);
            textNameRight.setBackgroundColor(color);
        } else if (item.getGroupId() == R.id.leftText) {
            textScoreLeft.setTextColor(color);
            textNameLeft.setBackgroundColor(color);
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
        return ScoreKeeperUtils.GREY_BG_COLOR == ScoreKeeperUtils.getBackgroundColor(textScoreRight) || ScoreKeeperUtils.GREY_BG_COLOR == ScoreKeeperUtils.getBackgroundColor(textScoreLeft);
    }

    protected void displayScore(boolean doVibrate) {


        if (doVibrate) {
            long[] pattern = ScoreKeeperUtils.getVibratePattern(pointsForGoal);
            v.vibrate(pattern, -1);
        }

        textScoreLeft.setTextSize(ScoreKeeperUtils.getTextSize(leftScore));
        textScoreRight.setTextSize(ScoreKeeperUtils.getTextSize(rightScore));

        checkScoresForZero();

        textScoreLeft.setText(String.valueOf(leftScore));
        textScoreRight.setText(String.valueOf(rightScore));
        textNameLeft.setText(leftTeamName);
        textNameRight.setText(rightTeamName);

      if (isGameWon(leftScore, rightScore)){
          boolean leftWins = leftScore > rightScore;
          String winningTeam = ScoreKeeperUtils.getTeamInfo(this, leftWins);
          Toast.makeText(this.getApplicationContext(), winningTeam + " WINS!!!!", Toast.LENGTH_LONG).show();
          long[] pattern = ScoreKeeperUtils.getWinningPattern();
          v.vibrate(pattern, -1);
      }

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
        editor.putInt(LEFT_BACKGROUND, ScoreKeeperUtils.getBackgroundColor(textScoreLeft));
        editor.putInt(RIGHT_BACKGROUND, ScoreKeeperUtils.getBackgroundColor(textScoreRight));
        editor.putInt(LEFT_TEXT, textScoreLeft.getCurrentTextColor());
        editor.putInt(RIGHT_TEXT, textScoreRight.getCurrentTextColor());
        editor.putInt(LEFT_SCORE, leftScore);
        editor.putInt(RIGHT_SCORE, rightScore);
        editor.putInt(POINT_PER_GOAL, pointsForGoal);
        editor.putInt(RESET_SCORE_TO, resetScoreTo);
        editor.putString(RIGHT_TEAM_NAME, rightTeamName);
        editor.putString(LEFT_TEAM_NAME, leftTeamName);
        editor.putString(FILE_SAVE_FEATURE_DATE, fileSaveFeatureDate);
        editor.putBoolean(FILE_SAVE_FEATURE_SWITCH, fileSaveForToday);
        if (winBy != null){
            editor.putInt(WIN_BY_POINTS, winBy.getWinningPoint());
            editor.putInt(WIN_BY_SPREAD, winBy.getPointSpread());
        }
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
        editor.remove(LEFT_TEAM_NAME);
        editor.remove(RIGHT_TEAM_NAME);
        editor.remove(FILE_SAVE_FEATURE_DATE);
        editor.remove(FILE_SAVE_FEATURE_SWITCH);
        editor.remove(WIN_BY_POINTS);
        editor.remove(WIN_BY_SPREAD);
        editor.commit();
    }

    protected void initState() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        textScoreLeft.setBackgroundColor(sharedPref.getInt(LEFT_BACKGROUND, ScoreKeeperUtils.RED));
        textScoreRight.setBackgroundColor(sharedPref.getInt(RIGHT_BACKGROUND, ScoreKeeperUtils.BLUE));
        textScoreLeft.setTextColor(sharedPref.getInt(LEFT_TEXT, ScoreKeeperUtils.WHITE));
        textScoreRight.setTextColor(sharedPref.getInt(RIGHT_TEXT, ScoreKeeperUtils.WHITE));

        textNameRight.setBackgroundColor(textScoreRight.getCurrentTextColor());
        textNameRight.setTextColor(ScoreKeeperUtils.getBackgroundColor(textScoreRight));
        textNameLeft.setBackgroundColor(textScoreLeft.getCurrentTextColor());
        textNameLeft.setTextColor(ScoreKeeperUtils.getBackgroundColor(textScoreLeft));


        leftScore = sharedPref.getInt(LEFT_SCORE, 0);
        rightScore = sharedPref.getInt(RIGHT_SCORE, 0);
        pointsForGoal = sharedPref.getInt(POINT_PER_GOAL, 1);
        resetScoreTo = sharedPref.getInt(RESET_SCORE_TO, 0);

        leftTeamName = sharedPref.getString(LEFT_TEAM_NAME, "Team");
        rightTeamName = sharedPref.getString(RIGHT_TEAM_NAME, "Team");

        fileSaveFeatureDate = sharedPref.getString(FILE_SAVE_FEATURE_DATE, null);
        fileSaveForToday = sharedPref.getBoolean(FILE_SAVE_FEATURE_SWITCH, false);
        if (fileSaveFeatureDate == null || !fileSaveFeatureDate.equals(ScoreKeeperUtils.getTodayAsNoTimeString())){
            fileSaveFeatureDate = null;
            fileSaveForToday = false;
        }
        int winByPoints = sharedPref.getInt(WIN_BY_POINTS, -1);
        int winBySpread = sharedPref.getInt(WIN_BY_SPREAD, -1);

        if (winByPoints > -1 && winBySpread >-1){
            setWinBy(winByPoints, winBySpread);
        }

    }

    private boolean checkForShake(SensorEvent event) {

        if (getAcceleration(event) > 8.25f) {
            lastShakeTime = System.currentTimeMillis();
            textScoreLeft.setBackgroundColor(ScoreKeeperUtils.GREY_BG_COLOR);
            textScoreRight.setBackgroundColor(ScoreKeeperUtils.GREY_BG_COLOR);
            textScoreRight.setTextColor(ScoreKeeperUtils.GREY_TXT_COLOR);
            textScoreLeft.setTextColor(ScoreKeeperUtils.GREY_TXT_COLOR);
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
            verifyAndDisplayScore(true, true, pointsForGoal);
        }

        // tilt right
        if (tiltedRight & !isTiltedRight && isReturnedCenter) {
            isTiltedLeft = false;
            isReturnedCenter = false;
            isTiltedRight = true;
            verifyAndDisplayScore(true, false, pointsForGoal);
        }

        // return to center
        if (returnedToCenter & !isReturnedCenter) {
            isTiltedLeft = false;
            isReturnedCenter = true;
            isTiltedRight = false;
        }
    }

    private void verifyAndDisplayScore(boolean shouldVibrate, boolean isLeft, int pointValue) {

        int potentialRightScore = rightScore;
        int potentialLeftScore = leftScore;

        if (isLeft){
            potentialLeftScore = potentialLeftScore + pointValue;
        } else {
            potentialRightScore = potentialRightScore + pointValue;
        }

        if (pointValue > 0 && isGameWon(rightScore, leftScore)){
            Toast.makeText(this.getApplicationContext(), "Game is already won based on the parameters from the menu setting 'Win Parameters'. Score will not change.", Toast.LENGTH_LONG).show();
            return;
        }

        rightScore = potentialRightScore;
        leftScore = potentialLeftScore;
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

    private View.OnLongClickListener leftNameListener = new View.OnLongClickListener() {

        public boolean onLongClick(View v) {
            showDialog(true);
            return true;
        }
    };
    private View.OnLongClickListener rightNameListener = new View.OnLongClickListener() {

        public boolean onLongClick(View v) {
            showDialog(false);
            return true;
        }
    };

    private void showDialog(boolean isLeft){
        DialogUtility.showEnterNameDialog(this, isLeft);
    }

    boolean isFileSaveEnabled(){
        if (ScoreKeeperUtils.getTodayAsNoTimeString().equals(fileSaveFeatureDate)){
           return fileSaveForToday;
        }
        return false;
    }

    void enableFileSave(){
        fileSaveFeatureDate = ScoreKeeperUtils.getTodayAsNoTimeString();
        fileSaveForToday = true;
    }

    void disableFileSave (){
        fileSaveFeatureDate = null;
        fileSaveForToday = false;
    }

    private void checkScoresForZero() {
        boolean showToast = false;
        if (leftScore < 0){
            leftScore = 0;
            showToast = true;
        }
        if (rightScore < 0){
            rightScore = 0;
            showToast = true;
        }
        if (showToast){
            Toast.makeText(this.getBaseContext(), "Score of less than zero isn't realistic.", Toast.LENGTH_SHORT).show();
        }
    }

    void setWinBy(int winningPoint, int pointSpread){
        WinBy winBy = new WinBy();
        winBy.setWinningPoint(winningPoint);
        winBy.setPointSpread(pointSpread);
        this.winBy = winBy;
    }

    boolean isGameWon(int thePotentialLeftScore, int thePotentialRightScore){
        if (winBy != null) {
            if (thePotentialLeftScore >= winBy.getWinningPoint() || thePotentialRightScore >= winBy.getWinningPoint()) {
                int diff = thePotentialLeftScore - thePotentialRightScore;
                if (Math.abs(diff) >= winBy.getPointSpread()) {
                    return true;
                }
            }
        }
        return false;
    }
}
