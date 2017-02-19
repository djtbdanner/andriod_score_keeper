package codepath.apps.demointroandroid.domain;

import codepath.apps.demointroandroid.util.ScoreKeeperUtils;

/**
 * Created by Owner on 2/18/2017.
 */

public class ScoreKeeperData {

    public  int leftScore = 0;
    public  int rightScore = 0;
    public  String leftTeamName;
    public  String rightTeamName;
    public  int pointsForGoal = 1;

    public  int resetScoreTo = 0;
    public  boolean fileSaveForToday = false;
    public  String fileSaveFeatureDate;
    public GamePoint gamePoint;
    public boolean disableTiltFeature = false;

    public void enableFileSave(){
        fileSaveFeatureDate = ScoreKeeperUtils.getTodayAsNoTimeString();
        fileSaveForToday = true;
    }

    public void disableFileSave (){
        fileSaveFeatureDate = null;
        fileSaveForToday = false;
    }

    public void setGamePoint(int winningPoint, int pointSpread){
        GamePoint gamePoint = new GamePoint();
        gamePoint.setGamePoint(winningPoint);
        gamePoint.setPointSpread(pointSpread);
        this.gamePoint = gamePoint;
    }


    @Override
    public String toString() {
        return "ScoreKeeperData{" +
                "leftScore=" + leftScore +
                ", rightScore=" + rightScore +
                ", leftTeamName='" + leftTeamName + '\'' +
                ", rightTeamName='" + rightTeamName + '\'' +
                ", pointsForGoal=" + pointsForGoal +
                ", resetScoreTo=" + resetScoreTo +
                ", fileSaveForToday=" + fileSaveForToday +
                ", fileSaveFeatureDate='" + fileSaveFeatureDate + '\'' +
                ", gamePoint=" + gamePoint +
                ", disableTiltFeature=" + disableTiltFeature +
                '}';
    }

}
