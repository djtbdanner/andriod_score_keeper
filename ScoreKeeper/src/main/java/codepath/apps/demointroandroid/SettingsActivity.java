package codepath.apps.demointroandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;


public class SettingsActivity extends Activity {

    Spinner gamePointPoint;
    Spinner gamePointMargin;
    TextView gamePointMarginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_keeper_settings);

        Button doneButton = (Button) findViewById(R.id.settings_done);
        doneButton.setOnClickListener(doneButtonListener);

        CheckBox checkGamePointButton = (CheckBox) findViewById(R.id.enable_game_point_button);
        checkGamePointButton.setOnClickListener(gamePointButtonListener);

        gamePointPoint = (Spinner) findViewById(R.id.win_parameters_spinner);
        gamePointMargin = (Spinner) findViewById(R.id.win_parameters_margin_spinner);
        gamePointMarginText = (TextView) findViewById(R.id.win_parameters_margin);
    }

    private SettingsActivity getMe() {
        return this;
    }


    private View.OnClickListener doneButtonListener = new View.OnClickListener() {

        public void onClick(View v) {
            Intent intent = new Intent(getMe(), ScoreKeeperActivity.class);
            getMe().startActivity(intent);

        }
    };
    private View.OnClickListener gamePointButtonListener = new View.OnClickListener() {

        public void onClick(View v) {
            System.out.println(((CheckBox) v).isChecked());
            int visibility = View.INVISIBLE;
            if (((CheckBox) v).isChecked()) {
                visibility = View.VISIBLE;
            }
            gamePointMargin.setVisibility(visibility);
            gamePointPoint.setVisibility(visibility);
            gamePointMarginText.setVisibility(visibility);
        }
    };
}

