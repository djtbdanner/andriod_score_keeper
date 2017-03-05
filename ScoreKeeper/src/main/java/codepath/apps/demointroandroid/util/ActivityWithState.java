package codepath.apps.demointroandroid.util;

import android.content.Context;
import android.content.res.Resources;

import codepath.apps.demointroandroid.domain.ScoreKeeperData;
public interface ActivityWithState {
    ScoreKeeperData getScoreKeeperData();
    Context getBaseContext();
    Resources getResources();
    //void setScoreKeeperData(ScoreKeeperData scoreKeeperData);
}
