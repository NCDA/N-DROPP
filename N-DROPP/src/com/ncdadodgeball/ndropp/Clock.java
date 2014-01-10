/**************************************************************************************************
 * _____   __     _______________________________________ 
 * ___  | / /     ___  __ \__  __ \_  __ \__  __ \__  __ \
 * __   |/ /________  / / /_  /_/ /  / / /_  /_/ /_  /_/ /
 * _  /|  /_/_____/  /_/ /_  _, _// /_/ /_  ____/_  ____/
 * /_/ |_/        /_____/ /_/ |_| \____/ /_/     /_/
 * 
 * National Collegiate Dodgeball Association (NCDA)
 * NCDA - Dodgeball Referee Officiating Application
 * http://www.ncdadodgeball.com
 * Copyright 2014. All Rights Reserved.
 *************************************************************************************************/
package com.ncdadodgeball.ndropp;

import android.os.CountDownTimer;
import android.widget.TextView;


/* Clock
 * 		Clock class is meant to basically be a timer.  This class manages a CountDownTimer defined
 *  by the innerd class Timer.  The Clock class manages this timer and a textview associated with
 *  displaying the time of the clock.  Any other modifications to the visual appearance of the
 *  clock text should be done by a derived class.
 */
public abstract class Clock {
	
	// public static constants
	public static final long HOUR = 3600000;
	public static final long MINUTE = 60000;
	public static final long SECOND = 1000;
	public static final long CENTISEC = 10;
	
	// class member variables
	Timer tTimer;
	TextView vClockText;
	
	int hours, minutes, seconds, centisec = 0;		//current time values
	long duration, tick, pausedTime;				//total duration and tick values
	boolean bCountDown;								//true == clock counts down (from duration->zero)
	boolean bRunning;								//true == timer is currently running
	boolean bPaused;								//true == time paused
	boolean bFinished;								//true == timer reached zero
	

	
	/** Clock ( clockText, duration, tick )  -- CONSTRUCTOR
	 * @param clockText : TextView widget to display the clock's time
	 * @param duration : total countdown time in milliseconds
	 * @param tick : tick time in milliseconds
	 * 
	 * Initialize clock variables. Default to count down timer.
	 */
	public Clock(TextView clockText, long duration, long tick){
		vClockText = clockText;
		tTimer = new Timer(duration, tick);
		this.duration = duration;
		this.tick = tick;
		bCountDown = true;
		bPaused = bFinished = false;
	}
	
	/** Clock ( clockText, duration, tick, countDown ) -- CONSTRUCTOR
	 * @param clockText : TextView widget to display clock's time
	 * @param duration : total countdown time in milliseconds
	 * @param tick : tick time in milliseconds
	 * @param countDown : true if clock should count down
	 * 
	 * Initialize clock variables.
	 */
	public Clock(TextView clockText, long duration, long tick, boolean countDown){
		vClockText = clockText;
		tTimer = new Timer(duration, tick);
		this.duration = duration;
		this.tick = tick;
		bCountDown = countDown;
		bPaused = bFinished = false;
	}
	
	/** startClock()
	 * 	Starts the timer
	 */
	protected void startClock(){
		tTimer.start();
		bRunning = true;
		bPaused = bFinished = false;
	}
	
	/** pauseClock
	 * 	Halts the current timer, saves the current time, and creates a new timer
	 * 	that will start at the last saved time.
	 */
	protected void pauseClock(){
		pausedTime = getTime();
		tTimer.cancel();
		tTimer = new Timer( pausedTime, tick );
		bRunning = false;
		bPaused = true;
	}

	/** resumeClock
	 * 	Starts the timer. Assumed to be invoked after the timer has been paused at
	 *  least once beforehand.
	 */
	protected void resumeClock(){
		tTimer.start();
		bRunning = true;
		bPaused = false;
	}
	
	/** resetClock()
	 *  Halts the current timer. Creates a new timer to start from the top of the
	 *  initially defined time (original timer duration).
	 */
	protected void resetClock(){
		tTimer.cancel();
		vClockText.setText(getSecondsTimeString(duration));
		tTimer = new Timer(duration, tick);
		bPaused = bFinished = bRunning = false;
	}
	
	/** isRunning()
	 * @return bRunning : true if the timer is currently running
	 */
	public boolean isRunning(){
		return bRunning;
	}
	
	/** getHoursSegment
	 * @return hours : number of hours the timer has left until expiration
	 * NOTE: does not take into account total current time
	 */
	public long getHoursSegment(){
		return hours;
	}
	
	/** getMinutesSegment
	 * @return minutes : number of minutes the timer has left until expiration
	 * NOTE: does not take into account total current time
	 */
	public long getMinutesSegment(){
		return minutes;
	}
	
	/** getSecondsSegment
	 * @return seconds : number of seconds the timer has left until expiration
	 * NOTE: does not take into account total current time
	 */
	public long getSecondsSegment(){
		return seconds;
	}
	
	/** getCentisecSegment
	 * @return centisec : number of centi-seconds the timer has left until expiration
	 * NOTE: does not take into account total current time
	 */
	public long getCentisecSegment(){
		return centisec;
	}
	
	/** getTime
	 * @return time (milliseconds) : total time left on the timer before it expires
	 */
	private long getTime(){
		return (hours*HOUR) + (minutes*MINUTE) + (seconds*SECOND) + (centisec*CENTISEC);
	}
	
	/** getHoursTimeString
	 * @return total time remaining on the timer in the format "hours:minutes:seconds"
	 */
	public String getHoursTimeString(){
		StringBuilder strTime = new StringBuilder();
		strTime.append(String.format("%02d", hours));
		strTime.append(":");
		strTime.append(String.format("%02d", minutes));
		strTime.append(":");
		strTime.append(String.format("%02d", seconds));
		return strTime.toString();
	}
	
	/** getMinutesTimeString
	 * @return total time remaining on the timer in the format "minutes:seconds:centisec"
	 */
	public String getMinutesTimeString(){
		StringBuilder strTime = new StringBuilder();
		strTime.append(String.format("%02d", minutes));
		strTime.append(":");
		strTime.append(String.format("%02d", seconds));
		strTime.append(":");
		strTime.append(String.format("%02d", centisec));
		return strTime.toString();
	}
	
	/** getSecondsTimeString
	 * @return total time remaining on the timer in the format "seconds:centisec"
	 */
	public String getSecondsTimeString(){
		StringBuilder strTime = new StringBuilder();
		strTime.append(String.format("%02d", seconds));
		strTime.append(":");
		strTime.append(String.format("%02d", centisec));
		return strTime.toString();
	}
	
	/** getSecondsTimeString
	 * @param time : time in milliseconds to convert to a string of format "seconds:centisec"
	 * 				time must be under 60 seconds to hold significance
	 * @return string representing the given time in the format "seconds:centisec"
	 * 				If <time> is greater than 60 seconds, returns "00:00"
	 */
	public String getSecondsTimeString(long time){
		if( time >= MINUTE )
			return "00:00";
		
		StringBuilder strTime = new StringBuilder();
		strTime.append(String.format("%02d", (int)(time/SECOND)));
		time -= (long)((int)((time/SECOND) * SECOND));
		strTime.append(":");
		strTime.append(String.format("%02d", (int)(time/CENTISEC)));
		return strTime.toString();
	}
	
	/** onClockExpired
	 * 	Classes derived from Clock must override this function which is used to
	 *  notify the derived class of the timer's expiration.
	 */
	abstract protected void onClockExpired();
	
	/* Timer extends CountDownTimer
	 * Private Timer class. This creates a new thread to run the timer.
	 */
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

