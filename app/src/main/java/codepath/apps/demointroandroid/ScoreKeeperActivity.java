package codepath.apps.demointroandroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.prefs.Preferences;

public class ScoreKeeperActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor accelerometer;

    private WindowManager mWindowManager;
    long timestampForEvent;
    private TextView textLeft;
    private TextView textRight;
    private Display mDisplay;
    private float mSensorX;
    private float mSensorY;
    Vibrator v;
    boolean isTiltedLeft = false;
    boolean isTiltedRight = false;
    boolean hasTiltedUpOrDown = false;
    boolean hasTiltedLeftOrRight = false;
    int leftScore = 0;
    int rightScore = 0;
    private float x1, x2;
    static final int MIN_DISTANCE = 150;
    int height;
    int width;
    float initialY;
    PowerManager.WakeLock wl;
    String leftOrRight; //TODO refactor this

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        if ("Right Color".equalsIgnoreCase(String.valueOf(item.getTitle()))){
            leftOrRight = "Right";
        }
        if ("Left Color".equalsIgnoreCase(String.valueOf(item.getTitle()))){
            leftOrRight = "Left";
        }
        if ("reset".equalsIgnoreCase(String.valueOf(item.getTitle()))) {
            leftScore = 0;
            rightScore = 0;
            displayScore();
        } else if ("red".equalsIgnoreCase(String.valueOf(item.getTitle()))) {
            setBgColor(leftOrRight, 0xffff0000);
            leftOrRight = null;
        } else if ("blue".equalsIgnoreCase(String.valueOf(item.getTitle()))) {
            setBgColor(leftOrRight, 0xff0000ff);
            leftOrRight = null;
        } else if ("yellow".equalsIgnoreCase(String.valueOf(item.getTitle()))) {
            setBgColor(leftOrRight, 0xffffff00);
            leftOrRight = null;
        } else if ("green".equalsIgnoreCase(String.valueOf(item.getTitle()))) {
            setBgColor(leftOrRight, 0xff00ff00);
            leftOrRight = null;
        } else if ("purple".equalsIgnoreCase(String.valueOf(item.getTitle()))) {
            setBgColor(leftOrRight, 0xFF551A8B);
            leftOrRight = null;
        } else if ("orange".equalsIgnoreCase(String.valueOf(item.getTitle()))) {
            setBgColor(leftOrRight, 0xFFFFA500);
            leftOrRight = null;
        } else if ("black".equalsIgnoreCase(String.valueOf(item.getTitle()))) {
            setBgColor(leftOrRight, 0xff000000);
            leftOrRight = null;
        }

        return true;
    }

    private void setBgColor(String leftOrRightChoice, int color ) {
        if ("Right".equalsIgnoreCase(leftOrRightChoice)){
            textRight.setBackgroundColor(color);
        } else {
            textLeft.setBackgroundColor(color);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                initialY = event.getY();
            case (MotionEvent.ACTION_MOVE):
                System.out.println("Action was MOVE");
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
                displayScore();
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
        displayScore();


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        height = metrics.heightPixels;
        width = metrics.widthPixels;

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Tag");
        wl.acquire();
    }

    private void displayScore() {
        textLeft.setText(String.valueOf(leftScore));
        textRight.setText(String.valueOf(rightScore));
    }

    public void initListeners() {
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (System.currentTimeMillis() - 600 < timestampForEvent) {
            return;
        }
        timestampForEvent = System.currentTimeMillis();

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {


            float x = Math.round(event.values[0] * 10) / 10;
            float y = Math.round(event.values[1] * 10) / 10;
            if (Math.abs(x) > Math.abs(y)) {
                hasTiltedUpOrDown = false;
                if (x < 0 && !isTiltedRight) {
                    System.out.println("You tilt the device right");
                    isTiltedRight = true;
                    hasTiltedLeftOrRight = true;
                }
                if (x > 0 && !isTiltedLeft) {
                    System.out.println("You tilt the device left");
                    isTiltedLeft = true;
                    hasTiltedLeftOrRight = true;
                }
            } else {
                if (y < 0 && !hasTiltedUpOrDown && hasTiltedLeftOrRight) {
                    System.out.println("You tilt the device up");
                    v.vibrate(100);
                    hasTiltedUpOrDown = true;
                    rightScore = rightScore + 1;
                }
                if (y > 0 && !hasTiltedUpOrDown && hasTiltedLeftOrRight) {
                    System.out.println("You tilt the device down");
                    v.vibrate(100);
                    hasTiltedUpOrDown = true;
                    leftScore = leftScore + 1;
                }
                displayScore();
            }
            if (x > (-2) && x < (2) && y > (-2) && y < (2)) {
                hasTiltedUpOrDown = false;
            }
        }
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
