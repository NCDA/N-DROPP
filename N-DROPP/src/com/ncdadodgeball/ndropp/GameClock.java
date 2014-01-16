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

import android.graphics.Color;
import android.widget.Button;
import android.widget.TextView;

public class GameClock extends Clock {
	
	//constants
	private final String STR_START = "Start";
	private final String STR_PAUSE = "Stop";
	private final String STR_RESUME = "Resume";
		
	// States
	private enum ClockState { PausedTop, Running, Paused, Expired };
	
	//class member variables
	private Button 		btStartPauseResume;
	private ClockState 	state;

	public GameClock(Button startPauseResume, TextView clockText, long duration, long tick) {
		super(clockText, ClockTextFormat.MinutesString, duration, tick);
		btStartPauseResume = startPauseResume;
		state = ClockState.PausedTop;
	}
	
	public GameClock(Button startPauseResume, TextView clockText, long duration, long tick, boolean countDown) {
		super(clockText, ClockTextFormat.MinutesString, duration, tick, countDown);
		btStartPauseResume = startPauseResume;
		state = ClockState.PausedTop;
	}

	
	/** ieStartPauseResume()
	 * 	Input-event caused by a button press for the button controlling "start",
	 *  "pause", and "resume".  This changes the state of the clock.
	 */
	public void ieStartPauseResume(){
		if( state == ClockState.PausedTop ){
			state = ClockState.Running;
			btStartPauseResume.setText(STR_PAUSE);
			startClock();
		}
		else if( state == ClockState.Running ){
			state = ClockState.Paused;
			btStartPauseResume.setText(STR_RESUME);
			pauseClock();
		}
		else if ( state == ClockState.Paused ){
			state = ClockState.Running;
			btStartPauseResume.setText(STR_PAUSE);
			startClock();
		}
		else if ( state == ClockState.Expired ){
			String message = "ERROR: Malformed GameClock state. In Expired state and received" +
							 "input event ieStartPauseResume";
			Log.E(message);
			throw new RuntimeException(message);
		}
	}
	
	@Override
	protected void onClockExpired() {
		state = ClockState.Expired;
		btStartPauseResume.setClickable(false);
		btStartPauseResume.setBackgroundColor(Color.GRAY);
	}
	
	public void ieRolloverHalftime(){
		if( state != ClockState.Paused || state != ClockState.Expired){
			String message = "ERROR: Malformed GameClock state exception. Cannot rollover halftime.";
			Log.E(message);
			throw new RuntimeException(message);
		}
		
		state = ClockState.PausedTop;
		setTime(getTime() + getDuration());
		btStartPauseResume.setText(STR_START);
		btStartPauseResume.setClickable(true);
		btStartPauseResume.setBackgroundColor(Color.WHITE);
	}

}
