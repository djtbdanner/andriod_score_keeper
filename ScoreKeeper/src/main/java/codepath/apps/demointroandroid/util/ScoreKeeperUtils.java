package codepath.apps.demointroandroid.util;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.Spinner;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import codepath.apps.demointroandroid.ScoreKeeperActivity;
import codepath.apps.demointroandroid.domain.ScoreKeeperColors;

public class ScoreKeeperUtils {

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

    public static boolean arrayContains(int[] array, int key) {
        Arrays.sort(array);
        return Arrays.binarySearch(array, key) >= 0;
    }

    public static int getIntWithDefault(String string, int defaultVal){

        if (!isEmptyOrNonInt(string)){
            return Integer.valueOf(string);
        }
        return defaultVal;
    }

    public static boolean isNumeric(String chars) {
        return TextUtils.isDigitsOnly(chars);
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

    public static boolean isEmptyOrNonInt(String s) {
        if (s == null) {
            return true;
        }
        if ("".equals(s)) {
            return true;
        }

        try {
            Integer.getInteger(s);
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    public static String getColorName(int colorcode) {
        if (colorcode == ScoreKeeperColors.BLACK) {
            return "black";
        }
        if (colorcode == ScoreKeeperColors.BLUE) {
            return "blue";
        }
        if (colorcode == ScoreKeeperColors.RED) {
            return "red";
        }
        if (colorcode == ScoreKeeperColors.YELLOW) {
            return "yellow";
        }
        if (colorcode == ScoreKeeperColors.GREEN) {
            return "green";
        }
        if (colorcode == ScoreKeeperColors.PURPLE) {
            return "purple";
        }
        if (colorcode == ScoreKeeperColors.ORANGE) {
            return "orange";
        }
        if (colorcode == ScoreKeeperColors.WHITE) {
            return "white";
        }
        if (colorcode == ScoreKeeperColors.PINK) {
            return "pink";
        }
        if (colorcode == ScoreKeeperColors.BROWN) {
            return "brown";
        }
        return "NA";

    }

    public static int getTextSize(int score) {
        int scoreSize = 200;
        if (score > 99) {
            scoreSize = 160;
        }
        if (score > 999) {
            scoreSize = 120;
        }

        if (score > 9999) {
            scoreSize = 100;
        }

        if (score > 99999) {
            scoreSize = 80;
        }

        return scoreSize;
    }

    public static String getTodayAsNoTimeString() {
        return sdf.format(new Date());
    }

    public static String getTeamInfo(ScoreKeeperActivity theActivity, boolean isLeft) {

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

        boolean appendParen = false;
        if (null != teamName && !"".equals(teamName)) {
            builder.append(teamName);
            builder.append(" (");
            appendParen = true;

        }
        builder.append(ScoreKeeperUtils.getColorName(txtColor)).append(" on ");
        builder.append(ScoreKeeperUtils.getColorName(bgColor));
        if (appendParen) {
            builder.append(")");
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
