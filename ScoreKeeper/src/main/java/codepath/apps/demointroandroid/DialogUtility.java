package codepath.apps.demointroandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;

class DialogUtility {

    static void showEnterNumberdialog(String title, String message, final boolean pointsPerGoal, final ScoreKeeperActivity theActivity) {
        theActivity.isPaused = true;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(theActivity);
        alertDialog.setTitle(title);

        if (pointsPerGoal) {
            alertDialog.setMessage(message + " \nCurrent Setting: " + theActivity.pointsForGoal);
        } else {
            alertDialog.setMessage(message + " \nCurrent Setting: " + theActivity.resetScoreTo);
        }


        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                theActivity.isPaused = false;
            }
        });


        final EditText input = new EditText(theActivity);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        theActivity.isPaused = false;
                        String text = String.valueOf(input.getText());
                        if (pointsPerGoal) {
                            theActivity.pointsForGoal = ("").equals(text) || null == text ? 1 : Integer.valueOf(text);
                        } else {
                            theActivity.resetScoreTo = ("").equals(text) || null == text ? 1 : Integer.valueOf(text);
                        }
                    }
                });


        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        theActivity.isPaused = false;
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }


    static void showAreYouSureDialog(String title, String message, final boolean scoreOnly, final ScoreKeeperActivity theActivity) {
        theActivity.isPaused = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(theActivity);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                theActivity.isPaused = false;
            }
        });

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (scoreOnly) {
                    theActivity.resetScore();
                } else {
                    theActivity.clearState();
                    theActivity.initState();
                    theActivity.displayScore(false);
                }
                theActivity.isPaused = false;
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                theActivity.isPaused = false;
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
