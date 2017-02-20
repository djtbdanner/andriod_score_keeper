package codepath.apps.demointroandroid.util;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Spinner;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import codepath.apps.demointroandroid.R;
import codepath.apps.demointroandroid.ScoreKeeperActivity;

public class ScoreKeeperUtils {

    private static ScoreKeeperUtils instance;

    private ScoreKeeperUtils() {

    }
    public static ScoreKeeperUtils getInstance() {
        if (instance == null) {
            instance = new ScoreKeeperUtils();

        }
        return instance;
    }


    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd", Locale.US);
    static SimpleDateFormat stf = new SimpleDateFormat("hh:mm a", Locale.US);


    public static int getBackgroundColor(View view) {
        Drawable drawable = view.getBackground();
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

    public boolean intArrayContains(int[] array, int key) {
        Arrays.sort(array);
        return Arrays.binarySearch(array, key) >= 0;
    }

    public static int getIntWithDefault(String string, int defaultVal) {

        if (!isEmptyOrNonInt(string)) {
            return Integer.valueOf(string);
        }
        return defaultVal;
    }

    public static long[] getVibratePattern(int points) {
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

    public static long[] getWinningPattern() {
        // WIN in morse code
        int betweenCode = 200;
        int longCode = 500;
        int shortCode = 200;
        int betweenLetter = 700;
        return new long[]{0, shortCode, betweenCode, longCode, betweenCode, longCode, betweenLetter, shortCode, betweenCode, shortCode, betweenLetter, longCode, betweenCode, shortCode};
    }

    private static boolean isEmptyOrNonInt(String s) {
        if (s == null) {
            return true;
        }
        if ("".equals(s)) {
            return true;
        }
        try {
            Integer integer = Integer.getInteger(s);
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    private String getColorName(int colorcode, Resources resources) {

        if (colorcode == resources.getColor(R.color.black)) {
            return resources.getString(R.string.black);
        }
        if (colorcode == resources.getColor(R.color.blue)) {
            return resources.getString(R.string.blue);
        }
        if (colorcode == resources.getColor(R.color.brown)) {
            return resources.getString(R.string.brown);
        }

        if (colorcode == resources.getColor(R.color.green)) {
            return resources.getString(R.string.green);
        }

        if (colorcode == resources.getColor(R.color.orange)) {
            return resources.getString(R.string.orange);
        }

        if (colorcode == resources.getColor(R.color.pink)) {
            return resources.getString(R.string.pink);
        }

        if (colorcode == resources.getColor(R.color.purple)) {
            return resources.getString(R.string.purple);
        }

        if (colorcode == resources.getColor(R.color.red)) {
            return resources.getString(R.string.red);
        }
        if (colorcode == resources.getColor(R.color.white)) {
            return resources.getString(R.string.white);
        }
        if (colorcode == resources.getColor(R.color.yellow)) {
            return resources.getString(R.string.yellow);
        }
        return "NA";

    }

    public static int getTextSize(int score, int screenSize, int density) {


        int scoreSize = 200;
        if (Configuration.SCREENLAYOUT_SIZE_XLARGE == screenSize || Configuration.SCREENLAYOUT_SIZE_LARGE == screenSize) {
            scoreSize = 500;
        }


        if (score > 99) {
            scoreSize = 160;
            if (Configuration.SCREENLAYOUT_SIZE_XLARGE == screenSize || Configuration.SCREENLAYOUT_SIZE_LARGE == screenSize) {
                scoreSize = 360;
            }
            if (density <= DisplayMetrics.DENSITY_HIGH) {
                scoreSize = 140;
            }

        }
        if (score > 999) {
            scoreSize = 120;
            if (Configuration.SCREENLAYOUT_SIZE_XLARGE == screenSize || Configuration.SCREENLAYOUT_SIZE_LARGE == screenSize) {
                scoreSize = 260;
            }
            if (density <= DisplayMetrics.DENSITY_HIGH) {
                scoreSize = 100;
            }
        }

        if (score > 9999) {
            scoreSize = 100;
            if (Configuration.SCREENLAYOUT_SIZE_XLARGE == screenSize || Configuration.SCREENLAYOUT_SIZE_LARGE == screenSize) {
                scoreSize = 200;
            }
            if (density <= DisplayMetrics.DENSITY_HIGH) {
                scoreSize = 80;
            }
        }

        if (score > 99999) {
            scoreSize = 80;
            if (Configuration.SCREENLAYOUT_SIZE_XLARGE == screenSize || Configuration.SCREENLAYOUT_SIZE_LARGE == screenSize) {
                scoreSize = 150;
            }
            if (density <= DisplayMetrics.DENSITY_HIGH) {
                scoreSize = 70;
            }
        }

        return scoreSize;
    }


    public static String getTodayAsNoTimeString() {
        return sdf.format(new Date());
    }

    public String getTeamInfo(ScoreKeeperActivity theActivity, boolean isLeft) {

        String teamName;
        int bgColor;
        int txtColor;

        StringBuilder builder = new StringBuilder();

        if (isLeft) {
            teamName = theActivity.getScoreKeeperData().leftTeamName;
            txtColor = theActivity.textScoreLeft.getCurrentTextColor();
            bgColor = ScoreKeeperUtils.getBackgroundColor(theActivity.textScoreLeft);
        } else {
            teamName = theActivity.getScoreKeeperData().rightTeamName;
            txtColor = theActivity.textScoreRight.getCurrentTextColor();
            bgColor = ScoreKeeperUtils.getBackgroundColor(theActivity.textScoreRight);
        }

        if (null != teamName && !"".equals(teamName)) {
            builder.append(teamName);
        } else {
            builder.append(getColorName(txtColor, theActivity.getResources())).append(" on ");
            builder.append(getColorName(bgColor, theActivity.getResources()));
        }
        return builder.toString();

    }


    public static int getSpinnerIndexOfValue(Spinner spinner, String value) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                index = i;
                break;
            }
        }
        return index;
    }
}
