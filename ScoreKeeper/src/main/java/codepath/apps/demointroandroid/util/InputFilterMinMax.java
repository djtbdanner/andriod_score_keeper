package codepath.apps.demointroandroid.util;

import android.text.Spanned;
import android.widget.Toast;

import codepath.apps.demointroandroid.ScoreKeeperActivity;

public class InputFilterMinMax implements android.text.InputFilter {
    private int min, max;
    private ScoreKeeperActivity theActivity;


    public InputFilterMinMax(int min, int max, ScoreKeeperActivity theActivity) {
        this.min = min;
        this.max = max;
        this.theActivity = theActivity;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        try {
            if ("".equals(source)){
                return null;
            }
            if (dstart == 0 && dend == 0){
                return null;
            }
            int input = Integer.parseInt(dest.toString() + source.toString());
            if (isInRange(input)) {
                return null;
            }
        } catch (NumberFormatException nfe) {
        }
        Toast.makeText(theActivity.getApplicationContext(), "The value you are trying to enter does not fall between " + min + " and " + max + ".", Toast.LENGTH_LONG).show();return "";
    }
    private boolean isInRange(int input) {
        System.out.println(min + " " + input + " " + max + " returning " + (min <= input && max >= input));
        return min <= input && max >= input;
    }
}
