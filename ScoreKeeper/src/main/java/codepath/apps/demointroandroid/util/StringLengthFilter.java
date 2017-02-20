package codepath.apps.demointroandroid.util;

import android.text.Spanned;
import android.widget.Toast;

import codepath.apps.demointroandroid.ScoreKeeperActivity;

class StringLengthFilter implements android.text.InputFilter {

    private ScoreKeeperActivity theActivity;


    StringLengthFilter(ScoreKeeperActivity theActivity) {
        this.theActivity = theActivity;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {


        if ("".equals(source)) {
            return null;
        }
        if (dstart == 0 && dend == 0) {
            return null;
        }
        String val = String.valueOf(dest);
        if (val == null || val.length() < 20) {
            return null;
        }

        Toast.makeText(theActivity.getApplicationContext(), "The team name has a limit of 20 characters.", Toast.LENGTH_SHORT).show();
        return "";
    }
}