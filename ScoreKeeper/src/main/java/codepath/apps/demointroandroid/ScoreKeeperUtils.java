package codepath.apps.demointroandroid;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class ScoreKeeperUtils {

    static int GREY_BG_COLOR = 0xffD3D3D3;
    static int GREY_TXT_COLOR = 0xffA9A9A9;
    static int RED = 0xffff0000;
    static int BLUE = 0xff0000ff;
    static int YELLOW = 0xffffff00;
    static int GREEN = 0xff32cd32;
    static int PURPLE = 0xFF9400d3;
    static int ORANGE = 0xFFFFA500;
    static int BLACK = 0xff000000;
    static int WHITE = 0xffffffff;
    static int PINK = 0xffff1493;


    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd", Locale.US);
    static SimpleDateFormat stf = new SimpleDateFormat("hh:mm a", Locale.US);


    static int getBackgroundColor(TextView textView) {
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

    static boolean arrayContains(int[] array, int key) {
        Arrays.sort(array);
        return Arrays.binarySearch(array, key) >= 0;
    }

    static boolean isNumeric(String chars) {
        return TextUtils.isDigitsOnly(chars);
    }

    static long[] getVibratePattern(int points) {
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

    static long[] getWinningPattern() {
        // WIN in morse code
        int betweenCode = 200;
        int longCode = 500;
        int shortCode = 200;
        int betweenLetter = 700;
        return new long[]{0, shortCode, betweenCode, longCode, betweenCode, longCode, betweenLetter, shortCode, betweenCode, shortCode, betweenLetter, longCode, betweenCode, shortCode};
    }

    static boolean isEmptyOrNonInt(String s) {
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

    static String getColorName(int colorcode) {
        if (colorcode == BLACK) {
            return "black";
        }
        if (colorcode == BLUE) {
            return "blue";
        }
        if (colorcode == RED) {
            return "red";
        }
        if (colorcode == YELLOW) {
            return "yellow";
        }
        if (colorcode == GREEN) {
            return "green";
        }
        if (colorcode == PURPLE) {
            return "purple";
        }
        if (colorcode == ORANGE) {
            return "orange";
        }
        if (colorcode == WHITE) {
            return "white";
        }
        if (colorcode == PINK) {
            return "pink";
        }
        return "NA";

    }

    static int getTextSize(int score) {
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

    static String getTodayAsNoTimeString() {
        return sdf.format(new Date());
    }

    static String getTeamInfo(ScoreKeeperActivity theActivity, boolean isLeft) {

        String teamName;
        int bgColor;
        int txtColor;

        StringBuilder builder = new StringBuilder();

        if (isLeft) {
            teamName = theActivity.leftTeamName;
            txtColor = theActivity.textScoreLeft.getCurrentTextColor();
            bgColor = ScoreKeeperUtils.getBackgroundColor(theActivity.textScoreLeft);
        } else {
            teamName = theActivity.rightTeamName;
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

}
