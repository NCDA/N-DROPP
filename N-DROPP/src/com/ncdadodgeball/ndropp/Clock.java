package com.ncdadodgeball.ndropp;

import android.os.CountDownTimer;
import android.widget.TextView;

public abstract class Clock {
	
	public static final long HOUR = 3600000;
	public static final long MINUTE = 60000;
	public static final long SECOND = 1000;
	public static final long CENTISEC = 10;
	
	Timer tTimer;
	TextView vClockText;
	
	int hours, minutes, seconds, centisec = 0;		//current time values
	long duration, tick, pausedTime;				//total duration and tick values
	boolean bCountDown;								//true == clock counts down (from duration->zero)
	boolean bRunning;								//true == timer is currently running
	boolean bPaused;								//true == time paused
	boolean bFinished;								//true == timer reached zero
	

	
	/* Clock ( duration, tick )
	 * @param duration : total countdown time in milliseconds
	 * @param tick : tick time in milliseconds
	 * 
	 * Default to count down timer.
	 */
	public Clock(TextView clockText, long duration, long tick){
		vClockText = clockText;
		tTimer = new Timer(duration, tick);
		this.duration = duration;
		this.tick = tick;
		bCountDown = true;
		bPaused = bFinished = false;
	}
	
	/* Clock ( duration, tick, countDown )
	 * @param duration : total countdown time in milliseconds
	 * @param tick : tick time in milliseconds
	 * @param countDown : true if clock should count down
	 */
	public Clock(TextView clockText, long duration, long tick, boolean countDown){
		vClockText = clockText;
		tTimer = new Timer(duration, tick);
		this.duration = duration;
		this.tick = tick;
		bCountDown = countDown;
		bPaused = bFinished = false;
	}
	
	private void cancelClock(){
		tTimer.cancel();
		bRunning = false;
	}
	
	protected void startClock(){
		tTimer.start();
		bRunning = true;
		bPaused = bFinished = false;
	}
	
	protected void pauseClock(){
		pausedTime = getTime();
		tTimer.cancel();
		tTimer = new Timer( pausedTime, tick );
		bRunning = false;
		bPaused = true;
	}

	protected void resumeClock(){
		tTimer.start();
		bRunning = true;
		bPaused = false;
	}
	
	protected void resetClock(){
		tTimer.cancel();
		vClockText.setText(getSecondsTimeString(duration));
		tTimer = new Timer(duration, tick);
		bPaused = bFinished = bRunning = false;
	}
	
	public boolean isRunning(){
		return bRunning;
	}
	
	public long getHours(){
		return hours;
	}
	
	public long getMinutes(){
		return minutes;
	}
	
	public long getSeconds(){
		return seconds;
	}
	
	public long getCentisec(){
		return centisec;
	}
	
	private long getTime(){
		return (hours*HOUR) + (minutes*MINUTE) + (seconds*SECOND) + (centisec*CENTISEC);
	}
	
	public String getHoursTimeString(){
		StringBuilder strTime = new StringBuilder();
		strTime.append(String.format("%02d", hours));
		strTime.append(":");
		strTime.append(String.format("%02d", minutes));
		strTime.append(":");
		strTime.append(String.format("%02d", seconds));
		return strTime.toString();
	}
	
	public String getMinutesTimeString(){
		StringBuilder strTime = new StringBuilder();
		strTime.append(String.format("%02d", minutes));
		strTime.append(":");
		strTime.append(String.format("%02d", seconds));
		strTime.append(":");
		strTime.append(String.format("%02d", centisec));
		return strTime.toString();
	}
	
	public String getSecondsTimeString(){
		StringBuilder strTime = new StringBuilder();
		strTime.append(String.format("%02d", seconds));
		strTime.append(":");
		strTime.append(String.format("%02d", centisec));
		return strTime.toString();
	}
	
	public String getSecondsTimeString(long time){
		StringBuilder strTime = new StringBuilder();
		strTime.append(String.format("%02d", (int)(time/SECOND)));
		time -= (long)((int)((time/SECOND) * SECOND));
		strTime.append(":");
		strTime.append(String.format("%02d", (int)(time/CENTISEC)));
		return strTime.toString();
	}
	
	abstract protected void onClockExpired();
	
	class Timer extends CountDownTimer
	{
		public Timer(long duration, long tick) {
			super(duration, tick);
		}

		@Override
		public void onFinish() {
			vClockText.setText("00:00");
			MainActivity.LogD("Time's up!");
			bRunning = false;
			bFinished = true;
			onClockExpired();
		}

		@Override
		public void onTick(long tte) {
			//update time values
			if(!bCountDown)			//if we're counting up, flip the remaining time
				tte = duration-tte;
			
			hours = (int)(tte/HOUR);
			tte -= (hours*HOUR);
			
			minutes = (int)(tte/MINUTE);
			tte -= (minutes*MINUTE);
			
			seconds = (int)(tte/SECOND);
			tte -= (seconds*SECOND);
			
			centisec = (int)(tte/CENTISEC);
			
			vClockText.setText(getSecondsTimeString());
			//MainActivity.LogD(getSecondsTimeString());
		}
	}
}

