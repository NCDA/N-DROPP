package nick.ncdareferee;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Select_Role extends AppCompatActivity {
    private Button BtLaunchShotClock1;
    private Button BtLaunchShotClock2;
    private Button BtLaunchHeadRef;
    private Button BtLaunchAsstRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select__role);

        // Create Buttons for main menu
        BtLaunchShotClock1 = (Button) findViewById(R.id.button_ShotClock1);
        BtLaunchShotClock2 = (Button) findViewById(R.id.button_ShotClock2);
        BtLaunchHeadRef = (Button) findViewById(R.id.button_HeadRef);
        BtLaunchAsstRef = (Button) findViewById(R.id.button_AsstRef);

        // Create listeners for the buttons I created above so that they move between activities
        // on click
        BtLaunchShotClock1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                launchShotClock();
            }
        });
        BtLaunchShotClock2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                launchShotClock();
            }
        });
        BtLaunchHeadRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                launchReferee();
            }
        });
        BtLaunchAsstRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                launchReferee();
            }
        });
    }

    // Intents to launch activities on button presses.
    private void launchShotClock() {

        Intent intent = new Intent(this, ShotClock.class);
        startActivity(intent);
    }
    private void launchReferee() {

        Intent intent = new Intent(this, Referee.class);
        startActivity(intent);
    }

}
