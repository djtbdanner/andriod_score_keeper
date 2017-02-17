package codepath.apps.demointroandroid;


import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


class MenuService {

    private static String SELECTED_IND = " (selected)";
    private static String TEAM_COLOR_IND = " (associated)";


    private static void setSubMenusEnabled(Menu myMenu, boolean isFileSaveEnabled) {

        int size = myMenu.size();
        for (int itemIndex = 0; itemIndex < size; itemIndex++) {
            if (myMenu.getItem(itemIndex).hasSubMenu()) {
                Menu subMenu = myMenu.getItem(itemIndex).getSubMenu();
                int subSize = subMenu.size();
                String menuTxt;
                for (int subIndex = 0; subIndex < subSize; subIndex++) {
                    MenuItem subMenuItem = subMenu.getItem(subIndex);
                    subMenuItem.setEnabled(true);
                    menuTxt = String.valueOf(subMenuItem.getTitle());
                    if (menuTxt != null && menuTxt.contains(SELECTED_IND)) {
                        menuTxt = menuTxt.substring(0, menuTxt.lastIndexOf(SELECTED_IND));
                        subMenuItem.setTitle(menuTxt);
                    }
                    if (menuTxt != null && menuTxt.contains(TEAM_COLOR_IND)) {
                        menuTxt = menuTxt.substring(0, menuTxt.lastIndexOf(TEAM_COLOR_IND));
                        subMenuItem.setTitle(menuTxt);
                    }
                }
            }
        }

        if (isFileSaveEnabled) {
            myMenu.findItem(R.id.menu_file).setTitle("Disable Game File Save");
        } else {
            myMenu.findItem(R.id.menu_file).setTitle("Enable Game File Save");
        }
    }

    static void disableSelectedMenuItems(Menu myMenu, TextView textLeft, TextView textRight, int pointsForGoal, int resetScoreTo, boolean isFileSaveEnabled, WinBy winBy) {
        if (myMenu == null) {
            return;
        }

        setSubMenusEnabled(myMenu, isFileSaveEnabled);

        int leftBGColor = ScoreKeeperUtils.getBackgroundColor(textLeft);
        int rightBGColor = ScoreKeeperUtils.getBackgroundColor(textRight);
        int leftTextColor = textLeft.getCurrentTextColor();
        int rightTextColor = textRight.getCurrentTextColor();
        // red
        if (ScoreKeeperUtils.RED == leftBGColor) {
            disableColorMenuItems(myMenu.findItem(R.id.L_B_red), myMenu.findItem(R.id.L_T_red));
        }
        if (ScoreKeeperUtils.RED == rightBGColor) {
            disableColorMenuItems(myMenu.findItem(R.id.R_B_red), myMenu.findItem(R.id.R_T_red));
        }
        if (ScoreKeeperUtils.RED == leftTextColor) {
            disableColorMenuItems(myMenu.findItem(R.id.L_T_red), myMenu.findItem(R.id.L_B_red));
        }
        if (ScoreKeeperUtils.RED == rightTextColor) {
            disableColorMenuItems(myMenu.findItem(R.id.R_T_red), myMenu.findItem(R.id.R_B_red));
        }
        //blue
        if (ScoreKeeperUtils.BLUE == leftBGColor) {
            disableColorMenuItems(myMenu.findItem(R.id.L_B_blue), myMenu.findItem(R.id.L_T_blue));
        }
        if (ScoreKeeperUtils.BLUE == rightBGColor) {
            disableColorMenuItems(myMenu.findItem(R.id.R_B_blue), myMenu.findItem(R.id.R_T_blue));
        }
        if (ScoreKeeperUtils.BLUE == leftTextColor) {
            disableColorMenuItems(myMenu.findItem(R.id.L_T_blue), myMenu.findItem(R.id.L_B_blue));
        }
        if (ScoreKeeperUtils.BLUE == rightTextColor) {
            disableColorMenuItems(myMenu.findItem(R.id.R_T_blue), myMenu.findItem(R.id.R_B_blue));
        }
        //yellow
        if (ScoreKeeperUtils.YELLOW == leftBGColor) {
            disableColorMenuItems(myMenu.findItem(R.id.L_B_yellow), myMenu.findItem(R.id.L_T_yellow));
        }
        if (ScoreKeeperUtils.YELLOW == rightBGColor) {
            disableColorMenuItems(myMenu.findItem(R.id.R_B_yellow), myMenu.findItem(R.id.R_T_yellow));
        }
        if (ScoreKeeperUtils.YELLOW == leftTextColor) {
            disableColorMenuItems(myMenu.findItem(R.id.L_T_yellow), myMenu.findItem(R.id.L_B_yellow));

        }
        if (ScoreKeeperUtils.YELLOW == rightTextColor) {
            disableColorMenuItems(myMenu.findItem(R.id.R_T_yellow), myMenu.findItem(R.id.R_B_yellow));

        }
        //purple
        if (ScoreKeeperUtils.PURPLE == leftBGColor) {
            disableColorMenuItems(myMenu.findItem(R.id.L_B_purple), myMenu.findItem(R.id.L_T_purple));
        }
        if (ScoreKeeperUtils.PURPLE == rightBGColor) {
            disableColorMenuItems(myMenu.findItem(R.id.R_B_purple), myMenu.findItem(R.id.R_T_purple));
        }
        if (ScoreKeeperUtils.PURPLE == leftTextColor) {
            disableColorMenuItems(myMenu.findItem(R.id.L_T_purple), myMenu.findItem(R.id.L_B_purple));
        }
        if (ScoreKeeperUtils.PURPLE == rightTextColor) {
            disableColorMenuItems(myMenu.findItem(R.id.R_T_purple), myMenu.findItem(R.id.R_B_purple));
        }
        //orange
        if (ScoreKeeperUtils.ORANGE == leftBGColor) {
            disableColorMenuItems(myMenu.findItem(R.id.L_B_orange), myMenu.findItem(R.id.L_T_orange));
        }
        if (ScoreKeeperUtils.ORANGE == rightBGColor) {
            disableColorMenuItems(myMenu.findItem(R.id.R_B_orange), myMenu.findItem(R.id.R_T_orange));
        }
        if (ScoreKeeperUtils.ORANGE == leftTextColor) {
            disableColorMenuItems(myMenu.findItem(R.id.L_T_orange), myMenu.findItem(R.id.L_B_orange));
        }
        if (ScoreKeeperUtils.ORANGE == rightTextColor) {
            disableColorMenuItems(myMenu.findItem(R.id.R_T_orange), myMenu.findItem(R.id.R_B_orange));
        }
        // black
        if (ScoreKeeperUtils.BLACK == leftBGColor) {
            disableColorMenuItems(myMenu.findItem(R.id.L_B_black), myMenu.findItem(R.id.L_T_black));
        }
        if (ScoreKeeperUtils.BLACK == rightBGColor) {
            disableColorMenuItems(myMenu.findItem(R.id.R_B_black), myMenu.findItem(R.id.R_T_black));
        }
        if (ScoreKeeperUtils.BLACK == leftTextColor) {
            disableColorMenuItems(myMenu.findItem(R.id.L_T_black), myMenu.findItem(R.id.L_B_black));
        }
        if (ScoreKeeperUtils.BLACK == rightTextColor) {
            disableColorMenuItems(myMenu.findItem(R.id.R_T_black), myMenu.findItem(R.id.R_B_black));
        }
        //white
        if (ScoreKeeperUtils.WHITE == leftBGColor) {
            disableColorMenuItems(myMenu.findItem(R.id.L_B_white), myMenu.findItem(R.id.L_T_white));
        }
        if (ScoreKeeperUtils.WHITE == rightBGColor) {
            disableColorMenuItems(myMenu.findItem(R.id.R_B_white), myMenu.findItem(R.id.R_T_white));
        }
        if (ScoreKeeperUtils.WHITE == leftTextColor) {
            disableColorMenuItems(myMenu.findItem(R.id.L_T_white), myMenu.findItem(R.id.L_B_white));
        }
        if (ScoreKeeperUtils.WHITE == rightTextColor) {
            disableColorMenuItems(myMenu.findItem(R.id.R_T_white), myMenu.findItem(R.id.R_B_white));
        }
        // pink
        if (ScoreKeeperUtils.PINK == leftBGColor) {

            disableColorMenuItems(myMenu.findItem(R.id.L_B_pink), myMenu.findItem(R.id.L_T_pink));
        }
        if (ScoreKeeperUtils.PINK == rightBGColor) {
            disableColorMenuItems(myMenu.findItem(R.id.R_B_pink), myMenu.findItem(R.id.R_T_pink));
        }
        if (ScoreKeeperUtils.PINK == leftTextColor) {
            disableColorMenuItems(myMenu.findItem(R.id.L_T_pink), myMenu.findItem(R.id.L_B_pink));
        }
        if (ScoreKeeperUtils.PINK == rightTextColor) {
            disableColorMenuItems(myMenu.findItem(R.id.R_T_pink), myMenu.findItem(R.id.R_B_pink));
        }
        // green
        if (ScoreKeeperUtils.GREEN == leftBGColor) {
            disableColorMenuItems(myMenu.findItem(R.id.L_B_green), myMenu.findItem(R.id.L_T_green));
        }
        if (ScoreKeeperUtils.GREEN == rightBGColor) {
            disableColorMenuItems(myMenu.findItem(R.id.R_B_green), myMenu.findItem(R.id.R_T_green));
        }
        if (ScoreKeeperUtils.GREEN == leftTextColor) {
            disableColorMenuItems(myMenu.findItem(R.id.L_T_green), myMenu.findItem(R.id.L_B_green));
        }
        if (ScoreKeeperUtils.GREEN == rightTextColor) {
            disableColorMenuItems(myMenu.findItem(R.id.R_T_green), myMenu.findItem(R.id.R_B_green));
        }

        if (pointsForGoal == 1) {
            myMenu.findItem(R.id.PPG_1).setEnabled(false);
        }
        if (pointsForGoal == 2) {
            myMenu.findItem(R.id.PPG_2).setEnabled(false);
        }
        if (pointsForGoal == 3) {
            myMenu.findItem(R.id.PPG_3).setEnabled(false);
        }
        if (pointsForGoal == 4) {
            myMenu.findItem(R.id.PPG_4).setEnabled(false);
        }
        if (pointsForGoal == 5) {
            myMenu.findItem(R.id.PPG_5).setEnabled(false);
        }
        if (pointsForGoal == 6) {
            myMenu.findItem(R.id.PPG_6).setEnabled(false);
        }
        if (pointsForGoal == 10) {
            myMenu.findItem(R.id.PPG_10).setEnabled(false);
        }
        if (resetScoreTo == 0) {
            myMenu.findItem(R.id.RS_0).setEnabled(false);
        }
        if (resetScoreTo == 4) {
            myMenu.findItem(R.id.RS_4).setEnabled(false);
        }

        if (winBy == null) {
            myMenu.findItem(R.id.W_B_None).setEnabled(false);
        } else {

            if (winBy.getWinningPoint() == 11 && winBy.getPointSpread() == 2) {
                myMenu.findItem(R.id._11_2).setEnabled(false);
            } else if (winBy.getWinningPoint() == 15 && winBy.getPointSpread() == 2) {
                myMenu.findItem(R.id._15_2).setEnabled(false);
            } else if (winBy.getWinningPoint() == 21 && winBy.getPointSpread() == 2) {
                myMenu.findItem(R.id._21_2).setEnabled(false);
            } else if (winBy.getWinningPoint() == 25 && winBy.getPointSpread() == 2) {
                myMenu.findItem(R.id._25_2).setEnabled(false);
            }
        }
    }

    private static void disableColorMenuItems(MenuItem main, MenuItem secondary) {
        main.setEnabled(false);
        main.setTitle(main.getTitle() + SELECTED_IND);
        secondary.setEnabled(false);
        secondary.setTitle(secondary.getTitle() + TEAM_COLOR_IND);
    }

    static boolean onOptionsItemSelected(MenuItem item, ScoreKeeperActivity theActivity) {
        String menuTitle = String.valueOf(item.getTitle());

        if ("Reset Score".equalsIgnoreCase(menuTitle)) {
            DialogUtility.showAreYouSureDialog("Are you sure you want to reset the score?", true, theActivity);
        } else if ("red".equalsIgnoreCase(menuTitle)) {
            theActivity.setColorOfItem(item, ScoreKeeperUtils.RED);
        } else if ("blue".equalsIgnoreCase(menuTitle)) {
            theActivity.setColorOfItem(item, ScoreKeeperUtils.BLUE);
        } else if ("yellow".equalsIgnoreCase(menuTitle)) {
            theActivity.setColorOfItem(item, ScoreKeeperUtils.YELLOW);
        } else if ("green".equalsIgnoreCase(menuTitle)) {
            theActivity.setColorOfItem(item, ScoreKeeperUtils.GREEN);
        } else if ("purple".equalsIgnoreCase(menuTitle)) {
            theActivity.setColorOfItem(item, ScoreKeeperUtils.PURPLE);
        } else if ("orange".equalsIgnoreCase(menuTitle)) {
            theActivity.setColorOfItem(item, ScoreKeeperUtils.ORANGE);
        } else if ("black".equalsIgnoreCase(menuTitle)) {
            theActivity.setColorOfItem(item, ScoreKeeperUtils.BLACK);
        } else if ("white".equalsIgnoreCase(menuTitle)) {
            theActivity.setColorOfItem(item, ScoreKeeperUtils.WHITE);
        } else if ("pink".equalsIgnoreCase(menuTitle)) {
            theActivity.setColorOfItem(item, ScoreKeeperUtils.PINK);
        } else if (item.getItemId() == R.id.PPG_Other) {
            DialogUtility.showEnterNumberdialog("Points Per Goal", "Enter the points per goal.", true, theActivity);
        } else if (item.getItemId() == R.id.RS_Other) {
            DialogUtility.showEnterNumberdialog("Reset Score Value", "Enter the initial score you want to show when you 'Reset Score'.", false, theActivity);
        } else if ("Reset Preferences".equalsIgnoreCase(menuTitle)) {
            DialogUtility.showAreYouSureDialog("Are you sure you want to reset your settings?", false, theActivity);
        } else if (ScoreKeeperUtils.isNumeric(menuTitle)) {

            int[] ppgItems = {R.id.PPG_1, R.id.PPG_2, R.id.PPG_3, R.id.PPG_4, R.id.PPG_5, R.id.PPG_6, R.id.PPG_10};
            if (ScoreKeeperUtils.arrayContains(ppgItems, item.getItemId())) {
                theActivity.pointsForGoal = Integer.valueOf(menuTitle);
            } else {
                theActivity.resetScoreTo = Integer.valueOf(menuTitle);
            }
        } else if (R.id.menu_file == item.getItemId()) {
            if (menuTitle != null && menuTitle.contains("Disable")) {
                theActivity.disableFileSave();
            } else {
                theActivity.enableFileSave();
            }
        } else if (R.id.menu_instructions == item.getItemId()) {
            DialogUtility.showInstructionDialog(theActivity);
        }

        if (item.getItemId() == R.id._11_2) {
            theActivity.setWinBy(11, 2);
        }
        if (item.getItemId() == R.id._15_2) {
            theActivity.setWinBy(15, 2);
        }
        if (item.getItemId() == R.id._21_2) {
            theActivity.setWinBy(21, 2);
        }
        if (item.getItemId() == R.id._25_2) {
            theActivity.setWinBy(25, 2);
        }
        if (item.getItemId() == R.id.W_B_Other) {
            DialogUtility.showWinParametersDialog(theActivity);
        }
        if (item.getItemId() == R.id.W_B_None) {
            theActivity.winBy = null;
        }
        return true;
    }
}
