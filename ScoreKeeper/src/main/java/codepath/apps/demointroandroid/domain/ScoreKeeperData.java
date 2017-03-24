package codepath.apps.demointroandroid.domain;

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
    public boolean hasCelebratedWin = false;
    public String fontName;
    public long shutDownMinutes;

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
