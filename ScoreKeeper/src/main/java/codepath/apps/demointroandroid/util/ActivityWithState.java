package codepath.apps.demointroandroid.util;

import codepath.apps.demointroandroid.domain.ScoreKeeperData;
public interface ActivityWithState {
    public ScoreKeeperData getScoreKeeperData();
    public void setScoreKeeperData(ScoreKeeperData scoreKeeperData);
}
