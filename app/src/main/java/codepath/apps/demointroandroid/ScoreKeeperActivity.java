package codepath.apps.demointroandroid;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;

import java.lang.reflect.Field;

public class ScoreKeeperActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor accelerometer;

    private WindowManager mWindowManager;
    long timestampForEvent;
    private TextView textLeft;
    private TextView textRight;
    private Display mDisplay;
    Vibrator v;
    boolean isTiltedLeft = false;
    boolean isTiltedRight = false;
    //    boolean isTiltedUp = false;
//    boolean isTiltedDown = false;
    boolean isReturnedCenter = false;
    int leftScore = 0;
    int rightScore = 0;
    int height;
    int width;
    float initialY;
    PowerManager.WakeLock wl;
    String leftOrRight; //TODO refactor this
    static String RIGHT_TEXT = "R_T";
    static String LEFT_TEXT = "L_T";
    static String RIGHT_BACKGROUND = "R_B";
    static String LEFT_BACKGROUND = "L_B";
    static String LEFT_SCORE = "L_S";
    static String RIGHT_SCORE = "R_S";
    static String POINT_PER_GOAL = "P_P_G";

    static int GREY_BG_COLOR = 0xffD3D3D3;
    static int GREY_TXT_COLOR = 0xffA9A9A9;

    int pointsForGoal = 1;
    private long lastShakeTime;
    private boolean isShaking;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if ("Right Color".equalsIgnoreCase(String.valueOf(item.getTitle()))) {
            leftOrRight = RIGHT_BACKGROUND;
        }
        if ("Left Color".equalsIgnoreCase(String.valueOf(item.getTitle()))) {
            leftOrRight = LEFT_BACKGROUND;
        }
        if ("Left Text Color".equalsIgnoreCase(String.valueOf(item.getTitle()))) {
            leftOrRight = LEFT_TEXT;
        }
        if ("Right Text Color".equalsIgnoreCase(String.valueOf(item.getTitle()))) {
            leftOrRight = RIGHT_TEXT;
        }
        if ("Switch Sides".equalsIgnoreCase(String.valueOf(item.getTitle()))) {
            switchSides();
        }
        if ("Reset Score".equalsIgnoreCase(String.valueOf(item.getTitle()))) {
            resetScore();
        } else if ("red".equalsIgnoreCase(String.valueOf(item.getTitle()))) {
            setBgColor(leftOrRight, 0xffff0000);
        } else if ("blue".equalsIgnoreCase(String.valueOf(item.getTitle()))) {
            setBgColor(leftOrRight, 0xff0000ff);
        } else if ("yellow".equalsIgnoreCase(String.valueOf(item.getTitle()))) {
            setBgColor(leftOrRight, 0xffffff00);
        } else if ("green".equalsIgnoreCase(String.valueOf(item.getTitle()))) {
            setBgColor(leftOrRight, 0xff00ff00);
        } else if ("purple".equalsIgnoreCase(String.valueOf(item.getTitle()))) {
            setBgColor(leftOrRight, 0xFF551A8B);
        } else if ("orange".equalsIgnoreCase(String.valueOf(item.getTitle()))) {
            setBgColor(leftOrRight, 0xFFFFA500);
        } else if ("black".equalsIgnoreCase(String.valueOf(item.getTitle()))) {
            setBgColor(leftOrRight, 0xff000000);
        } else if ("white".equalsIgnoreCase(String.valueOf(item.getTitle()))) {
            setBgColor(leftOrRight, 0xffffffff);
        } else if ("1".equalsIgnoreCase(String.valueOf(item.getTitle()))) {
            pointsForGoal = 1;
        } else if ("1".equalsIgnoreCase(String.valueOf(item.getTitle()))) {
            pointsForGoal = 1;
        } else if ("2".equalsIgnoreCase(String.valueOf(item.getTitle()))) {
            pointsForGoal = 2;
        } else if ("6".equalsIgnoreCase(String.valueOf(item.getTitle()))) {
            pointsForGoal = 6;
        }
        return true;
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
        leftScore = 0;
        rightScore = 0;
        displayScore(false);
        //   hasTiltedLeftOrRight = false;
    }

    private void setBgColor(String leftOrRightChoice, int color) {
        if (RIGHT_BACKGROUND.equalsIgnoreCase(leftOrRightChoice)) {
            textRight.setBackgroundColor(color);
        } else if (LEFT_BACKGROUND.equalsIgnoreCase(leftOrRightChoice)) {
            textLeft.setBackgroundColor(color);
        } else if (RIGHT_TEXT.equalsIgnoreCase(leftOrRightChoice)) {
            textRight.setTextColor(color);
        } else if (LEFT_TEXT.equalsIgnoreCase(leftOrRightChoice)) {
            textLeft.setTextColor(color);
        }
        leftOrRight = null;

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

        if (GREY_BG_COLOR == getBackgroundColor(textRight) || GREY_BG_COLOR == getBackgroundColor(textLeft)) {
            return false;
        }
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                initialY = event.getY();
            case (MotionEvent.ACTION_MOVE):
                //System.out.println("Action was MOVE");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daves_view);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();
        textLeft = (TextView) findViewById(R.id.textAcc1);
        textRight = (TextView) findViewById(R.id.textAcc2);

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        initListeners();

        timestampForEvent = System.currentTimeMillis();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        height = metrics.heightPixels;
        width = metrics.widthPixels;

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Tag");
        wl.acquire();

        initState();
        displayScore(false);
    }

    private void displayScore(boolean doVibrate) {

        if (doVibrate) {
            long[] pattern = {0, 100};

            if (pointsForGoal == 2) {
                long[] pattern2 = {0, 100, 150, 100};
                pattern = pattern2;
            }

            if (pointsForGoal == 6) {
                long[] pattern6 = {0, 100, 150, 100, 150, 100, 150, 100, 150, 100, 150, 100};
                pattern = pattern6;
            }

            v.vibrate(pattern, -1);
        }


        if (leftScore > 99) {
            textLeft.setTextSize(190);
        } else {
            textLeft.setTextSize(200);
        }
        if (rightScore > 99) {
            textRight.setTextSize(190);
        } else {
            textRight.setTextSize(200);
        }
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

            if (checkForShake(event)) {
                return;
            }
            ;

            if (System.currentTimeMillis() - 100 < timestampForEvent) {
                return;
            }
            if (System.currentTimeMillis() - 2000 < lastShakeTime) {
                return;
            }

            if (GREY_BG_COLOR == getBackgroundColor(textRight) || GREY_BG_COLOR == getBackgroundColor(textLeft)) {
                initState();
            }

            timestampForEvent = System.currentTimeMillis();

            checkAndProcessTilt(event);
        }
    }

    private void storeState() {

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(LEFT_BACKGROUND, getBackgroundColor(textLeft));
        editor.putInt(RIGHT_BACKGROUND, getBackgroundColor(textRight));
        editor.putInt(LEFT_TEXT, textLeft.getCurrentTextColor());
        editor.putInt(RIGHT_TEXT, textRight.getCurrentTextColor());
        editor.putInt(LEFT_SCORE, leftScore);
        editor.putInt(RIGHT_SCORE, rightScore);
        editor.putInt(POINT_PER_GOAL, pointsForGoal);
        editor.commit();
    }

    private void initState() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        textLeft.setBackgroundColor(sharedPref.getInt(LEFT_BACKGROUND, 0xffff0000));
        textRight.setBackgroundColor(sharedPref.getInt(RIGHT_BACKGROUND, 0xffffffff));
        textLeft.setTextColor(sharedPref.getInt(LEFT_TEXT, 0xff0000ff));
        textRight.setTextColor(sharedPref.getInt(RIGHT_TEXT, 0xffffffff));
        leftScore = sharedPref.getInt(LEFT_SCORE, 0);
        rightScore = sharedPref.getInt(RIGHT_SCORE, 0);
        pointsForGoal = sharedPref.getInt(POINT_PER_GOAL, 1);
    }

    private boolean checkForShake(SensorEvent event) {

        float SHAKE_THRESHOLD = 10.25f;
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        double acceleration = Math.sqrt(Math.pow(x, 2) +
                Math.pow(y, 2) +
                Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;

//        if (Math.abs(acceleration) >10)
//        System.out.println(acceleration);
        if (acceleration > SHAKE_THRESHOLD) {
            lastShakeTime = System.currentTimeMillis();
            setBgColor(LEFT_BACKGROUND, GREY_BG_COLOR);
            setBgColor(RIGHT_BACKGROUND, GREY_BG_COLOR);
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


        if (tiltedLeft & !isTiltedLeft && isReturnedCenter) {
            isTiltedLeft = true;
            isReturnedCenter = false;
            isTiltedRight = false;
            System.out.println("You tilted the device left");
            leftScore = leftScore + pointsForGoal;
            displayScore(true);
        }

        if (tiltedRight & !isTiltedRight && isReturnedCenter) {
            isTiltedLeft = false;
            isReturnedCenter = false;
            isTiltedRight = true;
            System.out.println("You tilted the device right");

            rightScore = rightScore + pointsForGoal;
            displayScore(true);
        }

        if (returnedToCenter & !isReturnedCenter) {
            isTiltedLeft = false;
            isReturnedCenter = true;
            isTiltedRight = false;
            System.out.println("You returned to center");
        }


//        float x = Math.round(event.values[0] * 10) / 10;
//        float y = Math.round(event.values[1] * 10) / 10;
//        float z = Math.round(event.values[2] * 10) / 10;
//
//        if (x != xx || y != yy || z != zz){
//            System.out.println("~~~~~ X: " + x + " Y:" + y + " Z:" + z);
//            xx = x;
//            yy = y;
//            zz = z;
//        }
//
//        if (Math.abs(x) > Math.abs(y)) { // left or right tilt
//
//            if (x < 0 && !isTiltedRight) {
//                System.out.println("You tilt the device right");
//                isTiltedRight = true;
//                isTiltedDown = false;
//                isTiltedLeft = false;
//                isTiltedUp = false;
//            }
//            if (x > 0 && !isTiltedLeft) {
//                System.out.println("You tilt the device left");
//                isTiltedLeft = true;
//                isTiltedDown = false;
//                isTiltedUp = false;
//                isTiltedRight = false;
//            }
//            hasTiltedLeftOrRight = true;
//
//        } else { // up or down tilt (points)
//            if (y < 5 && Math.abs(x) > 2 && hasTiltedLeftOrRight && !isTiltedUp) {
//                System.out.println("You tilt the device up");
//                rightScore = rightScore + pointsForGoal;
//                displayScore(true);
//                isTiltedDown = false;
//                isTiltedLeft = false;
//                isTiltedUp = true;
//                isTiltedRight = false;
//            }
//            if (y > 5  && Math.abs(x) > 2 && hasTiltedLeftOrRight && !isTiltedDown) {
//                System.out.println("You tilt the device down");
//                leftScore = leftScore + pointsForGoal;
//                displayScore(true);
//                isTiltedDown = true;
//                isTiltedLeft = false;
//                isTiltedUp = false;
//                isTiltedRight = false;
//            }
//            hasTiltedLeftOrRight = false;
//        }
    }

    @Override
    public void onStop() {
        super.onStop();
        wl.release();
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }
}
