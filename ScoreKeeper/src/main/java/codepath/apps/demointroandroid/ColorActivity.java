package codepath.apps.demointroandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;


public class ColorActivity extends Activity {

    Spinner gamePointPoint;
    Spinner gamePointMargin;
    TextView gamePointMarginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_keeper_colors);

        Button doneButton = (Button) findViewById(R.id.colors_done_button);
        doneButton.setOnClickListener(doneButtonListener);
    }

    private ColorActivity getMe() {
        return this;
    }


    private View.OnClickListener doneButtonListener = new View.OnClickListener() {

        public void onClick(View v) {
            Intent intent = new Intent(getMe(), ScoreKeeperActivity.class);
            getMe().startActivity(intent);

        }
    };
}

