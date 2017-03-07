package nick.ncdareferee;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.view.GestureDetector;
import android.view.*;
import android.widget.Spinner;
import android.widget.Toast;

public class ShotClock extends Activity {

    TextView myCounter;
    Button btn10;
    Button btn15;
    Button btnReset;
    public long resetTime = 10000;
    CountDownTimer countDownTimer;
    Boolean tapToggle = false;
    Boolean tapToggle2 = false;
    public long saveTime = 15000;
    Context context = this;
    public long origTime;
    long lightVibe = 100;
    long mediumVibe = 200;
    long[] pulseVibe = {0, 100, 100, 100, 100};
    long[] longVibe = {0, 300, 300, 300, 300, 300, 300};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shot_clock);

        // Link buttons from XML doc
        myCounter = (TextView) findViewById(R.id.clockText);
        btn10 = (Button) findViewById(R.id.button10);
        btn15 = (Button) findViewById(R.id.button15);
        btnReset = (Button) findViewById(R.id.reset);
        // Link Spinner
        final Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        // Declare vibrator service
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Set listeners for the buttons and link actions.
        btn15.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //vibe.cancel();
                origTime = 15000;
                saveTime = 15000;
                runTimer();
            }
        });
        myCounter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                timerToggle();
            }
        });
        btn10.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                vibe.cancel();
                origTime = 10000;
                saveTime = 10000;
                runTimer();
            }
        });
        btnReset.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                vibe.cancel();
                long spinValue = Long.parseLong(spinner.getSelectedItem().toString());
                spinValue = (spinValue * 1000);
                origTime = spinValue;
                saveTime = spinValue;
                runTimer();
            }
        });
        // Set swipe left and right to reset timer
        myCounter.setOnTouchListener(new OnSwipeTouchListener(ShotClock.this) {

            @Override
            public void onClick() {
                super.onClick();
                timerToggle();
            }
            @Override
            public void onSwipeLeft() {
                vibe.cancel();
                super.onSwipeLeft();
                saveTime = origTime;
                runTimer();
            }
            @Override
            public void onSwipeRight() {
                vibe.cancel();
                super.onSwipeRight();
                saveTime = origTime;
                runTimer();
            }
        });

    }

// If timer is running and you tap on the time, it stops.
// If timer is not running and you tasp on it, it continues.
    public void timerToggle() {
        tapToggle = !tapToggle;
        if (tapToggle) {
            countDownTimer.cancel();
        } else {
            runTimer();
        }
    }
/* Runs an Android CountDown Timer
    The timer will vibrate
*/

    public void runTimer() {
        // Declare vibrator service
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //cancel the old countDownTimer
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(saveTime, 10) {

            @Override
            public void onFinish() {
                myCounter.setText("00:000");
                vibe.vibrate(longVibe, -1);
            }

            @Override
            // This is a messy implementation to vibe 'around' each second on 9,8,7,6,5,4,3,2,1
            // Since the 'millisUntilFinished' is not accurate enough to use exact measurements,
            // I had to use 15ms as a buffer to get the if statements to fire correctly
            public void onTick(long millisUntilFinished) {
                if ((millisUntilFinished < 9005 &&  millisUntilFinished > 8990)
                        || (millisUntilFinished < 8005 &&  millisUntilFinished > 7990)
                        || (millisUntilFinished < 7005 &&  millisUntilFinished > 6990)
                        ||(millisUntilFinished < 6005 &&  millisUntilFinished > 5990))
                {
                    // Toasts just here for testing since I'm using an emulator.
                    //Toast.makeText(getApplicationContext(), "Toast on: " + millisUntilFinished, Toast.LENGTH_LONG).show();
                    vibe.vibrate(mediumVibe);
                }
                else if ((millisUntilFinished < 5005 &&  millisUntilFinished > 4990)
                        || (millisUntilFinished < 4005 &&  millisUntilFinished > 3990)
                        || (millisUntilFinished < 3005 &&  millisUntilFinished > 2990)
                        ||(millisUntilFinished < 2005 &&  millisUntilFinished > 1990))
                {
                    // Toasts just here for testing since I'm using an emulator.
                    // Toast.makeText(getApplicationContext(), "Toast on: " + millisUntilFinished, Toast.LENGTH_LONG).show();
                    vibe.vibrate(pulseVibe, -1);
                }
                myCounter.setText(String.format("%02d", (millisUntilFinished / 1000)) + ":" + (String.format("%03d", (millisUntilFinished % 1000))));
                saveTime = millisUntilFinished;
            }
        };

        countDownTimer.start();
    }
}
/*
    Class to implement swipe listeners for swipe left and swipe right commands

 */

    class OnSwipeTouchListener implements View.OnTouchListener {

        private GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context c) {
            gestureDetector = new GestureDetector(c, new GestureListener());
        }

        public boolean onTouch(final View view, final MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                onClick();
                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                onDoubleClick();
                return super.onDoubleTap(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                onLongClick();
                super.onLongPress(e);
            }

            // Determines the fling velocity and then fires the appropriate swipe event accordingly
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                        }
                    } else {
                        if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffY > 0) {
                                onSwipeDown();
                            } else {
                                onSwipeUp();
                            }
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        public void onSwipeRight() {
        }

        public void onSwipeLeft() {
        }

        public void onSwipeUp() {
        }

        public void onSwipeDown() {
        }

        public void onClick() {

        }

        public void onDoubleClick() {

        }

        public void onLongClick() {

        }
    }



