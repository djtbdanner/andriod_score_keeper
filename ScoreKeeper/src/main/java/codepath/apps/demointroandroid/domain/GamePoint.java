package codepath.apps.demointroandroid.domain;

public class GamePoint {
    private int gamePoint;
    private int pointSpread;

    public void setPointSpread(int pointSpread) {
        this.pointSpread = pointSpread;
    }

    public void setGamePoint(int gamePoint) {
        this.gamePoint = gamePoint;
    }


    public int getGamePoint() {
        return gamePoint;
    }

    public int getPointSpread() {
        return pointSpread;
    }

    @Override
    public String toString() {
        return "GamePoint{" +
                "gamePoint=" + gamePoint +
                ", pointSpread=" + pointSpread +
                '}';
    }



}
