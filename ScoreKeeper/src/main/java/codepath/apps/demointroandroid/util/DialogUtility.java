package codepath.apps.demointroandroid.util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import codepath.apps.demointroandroid.ColorActivity;
import codepath.apps.demointroandroid.R;
import codepath.apps.demointroandroid.ScoreKeeperActivity;
import codepath.apps.demointroandroid.ScoreKeeperPreferencesActivity;

public class DialogUtility {

    public static void showMenuPopUp(final ScoreKeeperActivity theActivity) {
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
        resetScore.setText(theActivity.getResources().getString(R.string.reset_score));
        layout.addView(resetScore);

        final Button colorButton = new Button(view.getContext());
        colorButton.setBackgroundColor(Color.DKGRAY);
        colorButton.setTextColor(Color.LTGRAY);
        colorButton.setText(theActivity.getResources().getString(R.string.colors));
        layout.addView(colorButton);

        final Button redButton = new Button(view.getContext());
        redButton.setBackgroundColor(Color.DKGRAY);
        redButton.setTextColor(Color.LTGRAY);
        redButton.setText(theActivity.getResources().getString(R.string.preferences));
        layout.addView(redButton);

        final Button instructions = new Button(view.getContext());
        instructions.setBackgroundColor(Color.DKGRAY);
        instructions.setTextColor(Color.LTGRAY);
        instructions.setText(theActivity.getResources().getString(R.string.help));
        layout.addView(instructions);

        final AlertDialog alertDialog = builder.create();
        alertDialog.setView(layout);

        resetScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                theActivity.isPaused = false;
                showAreYouSureDialog("Are you sure you want to reset the score?", theActivity);
            }
        });
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                theActivity.isPaused = false;
                Intent intent = new Intent(theActivity, ScoreKeeperPreferencesActivity.class);
//               Intent intent = new Intent(theActivity, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                theActivity.startActivity(intent);
            }
        });
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                theActivity.isPaused = false;
                Intent intent = new Intent(theActivity, ColorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                theActivity.startActivity(intent);
            }
        });
        instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                theActivity.isPaused = false;
                showInstructionDialog(theActivity);
            }
        });
        alertDialog.show();
    }

    private static void showAreYouSureDialog(String message, final ScoreKeeperActivity theActivity) {
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

        // switching the side of the positive and negative buttons for better user experience
        builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                theActivity.resetScore();
                theActivity.isPaused = false;


            }
        });
        builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
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
        if (left) {
            input.setText(theActivity.getScoreKeeperData().leftTeamName);

        } else {
            input.setText(theActivity.getScoreKeeperData().rightTeamName);
        }

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(20);
        input.setFilters(filterArray);

        builder.setView(input);
        final AlertDialog dialog = builder.create();
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent e) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    theActivity.isPaused = false;
                    String text = String.valueOf(input.getText());
                    if (left) {
                        theActivity.getScoreKeeperData().leftTeamName = text;
                        theActivity.textNameLeft.setText(text);
                    } else {
                        theActivity.getScoreKeeperData().rightTeamName = text;
                        theActivity.textNameRight.setText(text);
                    }
                    dialog.dismiss();
                    return true;
                }
                return false;
            }
        });


        dialog.show();
        input.setSelectAllOnFocus(true);
        input.requestFocus();
        input.selectAll();
    }

    private static void showInstructionDialog(final ScoreKeeperActivity theActivity) {
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
        t_view.setTextSize(14);
        t_view.setBackgroundColor(theActivity.getResources().getColor(R.color.greyBackground));
        t_view.setTextColor(theActivity.getResources().getColor(R.color.black));
        s_view.addView(t_view);

        AlertDialog alertDialog = builder.create();

        alertDialog.setMessage("Directions");
        alertDialog.setView(s_view);


        alertDialog.show();
    }

    static private String getInstructions() {
        StringBuilder buf = new StringBuilder();

        String lineSep = System.getProperty("line.separator");
        buf.append("Keep track of the score by tilting your phone to the left or right. Score can also be tracked by tapping (increase), swiping up (increase), swiping down (decrease), swiping right (increase) or swiping left (decrease). Swiping left or right always increases or decreases the score by one point no matter what you've set the points per goal in the preferences.");
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append("The app has a feature that pauses the tilt input if you move the phone erratically... like when cheering for you team. This is to help prevent accidentally adding points.");
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append("Full swipes from left to right or right to left will swap team sides.");
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append("Long clicks on the score or header brings up menus or text fields for editing the team name or choosing preferences.");
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append("Team names can be set by long clicking the the left or right title bar.");
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append("A menu to reset the score, set preferences or team colors can be accessed by long clicking the left or right score.");
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append("From the initial menu choose ...");
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append("- Reset Score");
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append("- Colors...");
        buf.append(lineSep);
        buf.append("    - Chose the background and text colors for each of the teams.");
        buf.append(lineSep);
        buf.append("    - There is an example of the scoreboard colors located at the bottom left and right of the Colors screen.");
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append("- Preferences...");
        buf.append(lineSep);
        buf.append("   - Set the Points Per Goal (e.g. basketball goal is 2 points - other games have different points per goal)");
        buf.append(lineSep);
        buf.append("    - Set the Initial Score (e.g. some volleyball tournaments start scoring at 4 points on each side)");
        buf.append(lineSep);
        buf.append("    - Set the Game Point/Margin (e.g. volleyball games are won with 25 points and require a point spread of 2)");
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append("- Save today's games");
        buf.append(lineSep);
        buf.append("    - This will save the game data to a file every time you reset the score. The file is stored in the device's download folder and can be opened and viewed with a spreadsheet program. This setting will automatically turn itself off after the end of the day (midnight).");
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append("- Disable Tilt Feature");
        buf.append(lineSep);
        buf.append("    - If you do not want the tilt feature, you can choose to turn that off here.");
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append("- Select Font...");
        buf.append(lineSep);
        buf.append("    - Select the font.");
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append(" - RESET ");
        buf.append(lineSep);
        buf.append("    - Reset to the default preferences.");
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append("Your team colors, score, team names and preferences are stored with every change so the app can be shut down or minimized at any time there is a pause in the game. Your colors and score will be waiting for you when the game starts back up.");
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append("Font credits...");
        buf.append(lineSep);
        buf.append(" - Team Spirit: Nick Curtis");
        buf.append(lineSep);
        buf.append(" - digital - 7(italic): http://www.styleseven.com/");
        buf.append(lineSep);
        buf.append(" - Handwriting: http://www.myscriptfont.com/ ");
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append(lineSep);
        buf.append("Hope you have fun with Score Keeper!");
        return buf.toString();
    }
}
