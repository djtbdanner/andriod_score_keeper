package codepath.apps.demointroandroid.util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import codepath.apps.demointroandroid.ColorActivity;
import codepath.apps.demointroandroid.ScoreKeeperActivity;
import codepath.apps.demointroandroid.SettingsActivity;
import codepath.apps.demointroandroid.domain.ScoreKeeperColors;

public class DialogUtility {

    static void showEnterNumberdialog(String title, String message, final boolean pointsPerGoal, final ScoreKeeperActivity theActivity) {
        theActivity.isPaused = true;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(theActivity);
        alertDialog.setTitle(title);

        if (pointsPerGoal) {
            alertDialog.setMessage(message + " \nCurrent Setting: " + theActivity.getScoreKeeperData().pointsForGoal);
        } else {
            alertDialog.setMessage(message + " \nCurrent Setting: " + theActivity.getScoreKeeperData().resetScoreTo);
        }


        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                theActivity.isPaused = false;
            }
        });


        final EditText input = new EditText(theActivity);
        if (pointsPerGoal) {
            input.setFilters(new InputFilter[]{new InputFilterMinMax(1, 1000, theActivity)});
        } else {
            input.setFilters(new InputFilter[]{new InputFilterMinMax(0, 100, theActivity)});
        }
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
                        if (ScoreKeeperUtils.isEmptyOrNonInt(text)) {
                            showInvalidParamToast(theActivity);
                            return;
                        }
                        int val = Integer.valueOf(text);
                        if (pointsPerGoal) {
                            if (val == 0){
                                showInvalidParamToast(theActivity);
                            } else {
                                theActivity.getScoreKeeperData().pointsForGoal = val;
                            }
                        } else {
                            theActivity.getScoreKeeperData().resetScoreTo = val;
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

    private static void showInvalidParamToast(ScoreKeeperActivity theActivity) {
        Toast.makeText(theActivity.getBaseContext(), "Nothing was changed, input was empty or invalid.", Toast.LENGTH_SHORT).show();
    }

    public static void showMenuPopUp( final ScoreKeeperActivity theActivity){
        theActivity.isPaused = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(theActivity);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                theActivity.isPaused = false;
            }
        });

        View view = new View(theActivity.getApplicationContext());
        LinearLayout layout = new LinearLayout(view.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final Button resetScore = new Button(view.getContext());
        resetScore.setBackgroundColor(Color.DKGRAY);
        resetScore.setTextColor(Color.LTGRAY);
        resetScore.setHighlightColor(Color.BLUE);
        resetScore.setText("Reset Score");
        layout.addView(resetScore);

        final Button colorButton = new Button(view.getContext());
        colorButton.setBackgroundColor(Color.DKGRAY);
        colorButton.setTextColor(Color.LTGRAY);
        colorButton.setText("Colors...");
        layout.addView(colorButton);

        final Button redButton = new Button(view.getContext());
        redButton.setBackgroundColor(Color.DKGRAY);
        redButton.setTextColor(Color.LTGRAY);
        redButton.setText("Preferences...");
        layout.addView(redButton);

        final Button instructions = new Button(view.getContext());
        instructions.setBackgroundColor(Color.DKGRAY);
        instructions.setTextColor(Color.LTGRAY);
        instructions.setText("Help");
        layout.addView(instructions);

       final AlertDialog alertDialog = builder.create();
        alertDialog.setView(layout);

        resetScore.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                theActivity.isPaused = false;
                showAreYouSureDialog("Are you sure you want to reset the score?",  theActivity);
            }
        });
        redButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                theActivity.isPaused = false;
                Intent intent = new Intent(theActivity, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                theActivity.startActivity(intent);
            }
        });
        colorButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                theActivity.isPaused = false;
                Intent intent = new Intent(theActivity, ColorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                theActivity.startActivity(intent);
            }
        });
        instructions.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                theActivity.isPaused = false;
                showInstructionDialog(theActivity);
            }
        });
        alertDialog.show();
    }


//    static void showColorDialog( final ScoreKeeperActivity theActivity){
//        // Prepare grid view
//        GridView gridView = (GridView) theActivity.findViewById(R.id.myGrid);
//      //  grid.setAdapter(new customAdapter());
//
//
//        List<Integer> mList = new ArrayList<Integer>();
//        for (int i = 1; i < 10; i++) {
//            mList.add(i);
//        }
//
//        gridView.setAdapter(new ArrayAdapter(theActivity, android.R.layout.simple_list_item_1, mList));
//
//        gridView.setNumColumns(3);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // do something here
//            }
//        });
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(theActivity);
//        builder.setView(gridView);
//        builder.setTitle("Set Color");
//        builder.show();
//    }

    public static void showAreYouSureDialog(String message, final ScoreKeeperActivity theActivity) {
        theActivity.isPaused = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(theActivity);
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
                theActivity.resetScore();
                theActivity.isPaused = false;
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                theActivity.isPaused = false;
                dialog.dismiss();

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showEnterNameDialog(final ScoreKeeperActivity theActivity, final boolean left) {
        theActivity.isPaused = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(theActivity);
        builder.setTitle("Set " + (left ? "left" : "right") + " team name.");

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
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
        input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setMessage("Current Team Name is: " + (left ? theActivity.getScoreKeeperData().leftTeamName : theActivity.getScoreKeeperData().rightTeamName) + ".")

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String text = String.valueOf(input.getText());
                        if (left) {
                            theActivity.getScoreKeeperData().leftTeamName = text;
                        } else {
                            theActivity.getScoreKeeperData().rightTeamName = text;
                        }
                        theActivity.isPaused = false;
                        theActivity.displayScore(false);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("ClearName", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (left) {
                            theActivity.getScoreKeeperData().leftTeamName = "";
                        } else {
                            theActivity.getScoreKeeperData().rightTeamName = "";
                        }
                        theActivity.isPaused = false;
                        theActivity.displayScore(false);
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

//    public static void showWinParametersDialog(final ScoreKeeperActivity theActivity) {
//        theActivity.isPaused = true;
//        AlertDialog.Builder builder = new AlertDialog.Builder(theActivity);
//
//        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                dialog.dismiss();
//                theActivity.isPaused = false;
//            }
//        });
//
//        View view = new View(theActivity.getApplicationContext());
//        LinearLayout layout = new LinearLayout(view.getContext());
//        layout.setOrientation(LinearLayout.VERTICAL);
//
//        final TextView tView = new TextView(view.getContext());
//
//        tView.setText("Set the winning point value here. Currently " + (theActivity.getScoreKeeperData().gamePoint == null ? " no win parameters are set." : " winning score is " + theActivity.getScoreKeeperData().gamePoint.getGamePoint() + "."));
//        tView.setBackgroundColor(ScoreKeeperColors.WHITE);
//        tView.setTextColor(ScoreKeeperColors.BLACK);
//        tView.setTextSize(10);
//        layout.addView(tView);
//
//        final EditText winPointsBox = new EditText(view.getContext());
//        winPointsBox.setTextColor(ScoreKeeperColors.BLACK);
//        winPointsBox.setInputType(InputType.TYPE_CLASS_NUMBER);
//        winPointsBox.setFilters(new InputFilter[]{new InputFilterMinMax(1, 1000, theActivity)});
//        layout.addView(winPointsBox);
//
//        final TextView tViewII = new TextView(view.getContext());
//        tViewII.setBackgroundColor(ScoreKeeperColors.WHITE);
//        tViewII.setTextColor(ScoreKeeperColors.BLACK);
//        tViewII.setTextSize(10);
//        tViewII.setText("Set number of lead points required here.  Currently " + (theActivity.getScoreKeeperData().gamePoint == null ? " no win parameters are set." : " point spread is " + theActivity.getScoreKeeperData().gamePoint.getPointSpread() + "."));
//        layout.addView(tViewII);
//
//        final EditText pointSpreadBox = new EditText(view.getContext());
//        pointSpreadBox.setInputType(InputType.TYPE_CLASS_NUMBER);
//        pointSpreadBox.setTextColor(ScoreKeeperColors.BLACK);
//        pointSpreadBox.setWidth(80);
//        pointSpreadBox.setFilters(new InputFilter[]{new InputFilterMinMax(1, 100, theActivity)});
//        layout.addView(pointSpreadBox);
//
//        builder.setCancelable(true);
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                theActivity.isPaused = false;
//                String pointSpread = String.valueOf(pointSpreadBox.getText());
//                String winPoints = String.valueOf(winPointsBox.getText());
//                if (ScoreKeeperUtils.isEmptyOrNonInt(pointSpread) || ScoreKeeperUtils.isEmptyOrNonInt(winPoints)) {
//                    showInvalidParamToast(theActivity);
//                } else {
//                    theActivity.setGamePoint(Integer.valueOf(winPoints), Integer.valueOf(pointSpread));
//                    dialog.dismiss();
//                }
//            }
//        });
//
//        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                theActivity.isPaused = false;
//                theActivity.getScoreKeeperData().gamePoint = null;
//                dialog.dismiss();
//            }
//        });
//
//        AlertDialog alertDialog = builder.create();
//
//        alertDialog.setTitle("Set Win Parameters");
//        alertDialog.setView(layout);
//
//
//        alertDialog.show();
//    }

    public static void showInstructionDialog(final ScoreKeeperActivity theActivity) {
        theActivity.isPaused = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(theActivity);

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                theActivity.isPaused = false;
            }
        });

        builder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                theActivity.isPaused = false;
                dialog.dismiss();
            }
        });

        final ScrollView s_view = new ScrollView(theActivity.getApplicationContext());
        final TextView t_view = new TextView(theActivity.getApplicationContext());
        t_view.setText(getInstructions());
        t_view.setTextSize(10);
        t_view.setBackgroundColor(ScoreKeeperColors.GREY_BG_COLOR);
        t_view.setTextColor(ScoreKeeperColors.BLACK);
        s_view.addView(t_view);

        AlertDialog alertDialog = builder.create();

        alertDialog.setTitle("Directions");
        alertDialog.setView(s_view);


        alertDialog.show();
    }

    static private String getInstructions() {
        StringBuffer buf = new StringBuffer();

        String lineSep = System.getProperty("line.separator");
        buf.append("Keep track of team points by tilting your phone to the left or right.");
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append("You can choose the number of points per goal via menu (i.e. 2 points for basket ball goal). The number of points per goal will register with a left or right tilt or left or right swipe (left swipe will remove mistakes). Swiping up or down always registers one point only so you can keep track of the single point goals.");
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append("You can choose the team colors for each side via menu.");
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append("You can swap team sides at half time by swiping fully from left to right or right to left.");
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append("You can enter team names with a long click on the menu.");
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append("You can choose the reset points start point (i.e. some volleyball games start at 4 points) via the menu.");
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append("You can set the win parameters via the menu (i.e. volleyball win is 25 with a 2 point spread required). Note that once a team has won the scoring will no longer increase, however if you or the device add some points incorrectly, they can be rolled back with a swipe down on the score.");
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append("You can enable a game saving feature for a day via the menu. The game save feature will save the game scores to the download folder on your device and it can be opened with a spreadsheet program. The save to file feature, when enabled, will only be enabled for the day. Once the day is over the feature will be disabled unless you re-enable it.");
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append("Your team colors, score, points per goal, team names and reset start point are stored with every change so the app can be shut down or minimized at any time there is a pause in the game. Your colors and score will be stored and waiting for you when the game starts back up.");
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append("Hope you have fun with Score Keeper!");
        return buf.toString();
    }
}
