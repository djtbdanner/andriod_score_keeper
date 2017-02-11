package codepath.apps.demointroandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        theActivity.isPaused = false;
                        String text = String.valueOf(input.getText());
                        if ("".equals(text) || text == null){
                            Toast.makeText(theActivity.getBaseContext(), "Nothing was changed.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int val =  Integer.valueOf(text);
                        if (val > 100){
                            Toast.makeText(theActivity.getBaseContext(), "The entered value of " + val + " doesn't work with this app, entry was changed to 100.", Toast.LENGTH_SHORT).show();
                            val = 100;

                        }
                        if (pointsPerGoal) {

                            if (val < 1){
                                Toast.makeText(theActivity.getBaseContext(), "Points per goal of less than one doesn't work with this app, entry was changd to 1.", Toast.LENGTH_SHORT).show();
                                val = 1;
                            }
                            theActivity.pointsForGoal = val;
                        } else {
                            if (val < 0){
                                Toast.makeText(theActivity.getBaseContext(), "Reset score to less than zero doesn't work with this app, entry was changd to 0.", Toast.LENGTH_SHORT).show();
                                val = 0;
                            }
                            theActivity.resetScoreTo = val;
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
