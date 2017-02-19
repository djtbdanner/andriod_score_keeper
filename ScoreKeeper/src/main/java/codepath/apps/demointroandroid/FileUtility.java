package codepath.apps.demointroandroid;

import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;

class FileUtility {

    static void saveToStorage(ScoreKeeperActivity theActivity) {
        try {

            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String path = directory.getPath();

            Date d = new Date();
            String formattedDate = ScoreKeeperUtils.sdf.format(d);
            String fileName = "ScoreKeeper" + formattedDate + ".csv";
            File f = new File(path + File.separator + fileName);
            if (!f.exists()) {
                boolean success = f.createNewFile();
                if (!success){
                    throw new Exception("Unable to create file: " + fileName);
                }
                writeStringToFile("Team, Score, Team, Score, Finish Time", f);
            }

            String dataRow = ScoreKeeperUtils.getTeamInfo(theActivity, true) + "," +
                    theActivity.leftScore + "," +
                    ScoreKeeperUtils.getTeamInfo(theActivity, false).replace(",", " ") + "," +
                    theActivity.rightScore + "," +
                    ScoreKeeperUtils.stf.format(d);
            writeStringToFile(dataRow, f);

            Toast.makeText(theActivity.getBaseContext(), "The scores from this game were saved in " + fileName + ". The file is in your downloads folder. You can turn off the file feature in the menu.", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            System.out.println(e.toString());
            Toast.makeText(theActivity.getBaseContext(), "Sorry, an error prevent the app from saving a file with the scores (may be due to permissions granted to the app). You can turn off the file feature in the menu.", Toast.LENGTH_LONG).show();
        }
    }

    private static void writeStringToFile(String val, File file) throws Exception {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            out.println(val);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }



}
