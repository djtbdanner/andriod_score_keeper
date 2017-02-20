package codepath.apps.demointroandroid.util;

import android.test.ActivityTestCase;

import org.junit.Test;

public class ScoreKeeperUtilsTest extends ActivityTestCase
{


    @Test
    public void arrayIntContainsTrue() {
        int nums[] = {1,2,3,4};
        assertTrue(ScoreKeeperUtils.getInstance().intArrayContains(nums, 1));
    }

    @Test
    public void arrayIntContainsFalse() {
        int nums[] = {1,2,3,4};
        assertFalse(ScoreKeeperUtils.getInstance().intArrayContains(nums, 7));
    }


}
