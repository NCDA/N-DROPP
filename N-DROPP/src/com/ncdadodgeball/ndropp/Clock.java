package com.ncdadodgeball.ndropp;

import java.text.DecimalFormat;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Clock{
	
	public static final long SECONDS = 1000;
	public static final long MINUTES = 60000;
	public static final long MILLISEC = 10;

	Timer			c_Timer;
	TextView		c_ClockText;
	Button			c_btPauseResume;
	boolean			c_bCountUp;			//true == clock counts up
	boolean			c_bIsRunning;		//true == clock is running
	boolean			c_bIsPaused;		//true == clock is paused
	boolean			c_bPauseTrigger;	//true == onTick has recognized that the clock is paused
	long			c_lMaxTime;
	long			c_lPauseTime;
	
	public Clock(TextView view, Button pauseButton, boolean countUp, long duration){
		c_Timer = new Timer(duration);
		c_ClockText = view;
		c_btPauseResume = pauseButton;
		c_bCountUp = countUp;
		c_lMaxTime = c_lPauseTime = duration;
		c_bIsRunning = c_bIsPaused = c_bPauseTrigger = false;
	}

	public void cancelClock(){
		c_Timer.cancel();
		c_bIsRunning = false;
	}
	
	public void startClock(){
		if(c_bIsRunning)
			c_Timer.cancel();
		else
			c_bIsRunning = true;
		c_Timer = new Timer(c_lMaxTime);
		c_Timer.start();
		c_bIsPaused = c_bPauseTrigger = false;
		c_btPauseResume.setBackgroundColor(Color.WHITE);
		c_btPauseResume.setClickable(true);
	}
	
	
	public void pauseClock(){
		c_bIsPaused = true;
		c_bIsRunning = false;
	}
	
	public void resumeClock(){
		cancelClock();
		c_Timer = new Timer(c_lPauseTime);	//create timer with new time
		c_bIsPaused = c_bPauseTrigger = false;
		c_Timer.start();
		c_bIsRunning = true;
	}
	
	public boolean isRunning(){
		return c_bIsRunning;
	}
	
	public boolean isPaused(){
		return c_bIsPaused;
	}
	
	public void setClockDuration(int seconds){
		c_lMaxTime = seconds * 1000;
	}

	
	
	
	
	class Timer extends CountDownTimer{
		public Timer(long duration){
			super(duration, 100);
		}

		@Override
		public void onFinish() {
			if(c_bIsRunning){
				c_bIsRunning = false;
				cancel();
				c_ClockText.setText("FINISHED!");
				c_btPauseResume.setBackgroundColor(Color.GRAY);
				c_btPauseResume.setClickable(false);
			}
		}

		@Override
		public void onTick(long millisUntilFinished) {		
			//if the clock was paused, stop the clock and save current time
			if(c_bIsPaused){
				if(!c_bPauseTrigger){
					c_lPauseTime = millisUntilFinished;
					c_bPauseTrigger = true;
					cancel();
					c_bIsRunning = false;
				}
				return;
			}
			
			long minutes, seconds, millis;

			//COUNT UP TIMER
			if(c_bCountUp){
				long time = c_lMaxTime - millisUntilFinished;
				minutes = time/MINUTES;
				seconds = (time-(minutes*MINUTES))/SECONDS;
				millis  = (time-(minutes*MINUTES)-(seconds*SECONDS))/MILLISEC;
			}
			
			//COUNT DOWN TIMER
			else{
				minutes = millisUntilFinished/MINUTES;
				seconds = (millisUntilFinished-(minutes*MINUTES))/SECONDS;
				millis  = (millisUntilFinished-(minutes*MINUTES)-(seconds*SECONDS))/MILLISEC;
			}
			
			//display time
			DecimalFormat df = new DecimalFormat("00");
			if(c_lMaxTime > MINUTES)
				c_ClockText.setText(df.format(minutes) + ":" + df.format(seconds) + ":" + df.format(millis));
			else
				c_ClockText.setText(df.format(seconds) + ":" + df.format(millis));
		}	
	}
}
