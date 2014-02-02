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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/* GameClock
 * 		GameClock uses the Clock class to maintain a timer for the dodgeball game's match times.
 * This class keeps track of the state the timer is in. This class additionally takes a
 * button widgets which is used by the GameClock to change the visual appearance of the button
 * based on input events from the user and timer.
 */
public class GameClock extends Clock {
	
	//constants
	private final String STR_START = "Start";
	private final String STR_PAUSE = "Stop";
	private final String STR_RESUME = "Resume";
		
	// States
	private enum ClockState { PausedTop, Running, Paused, Expired };
	
	//class member variables
	private Button 		btStartPauseResume;
	private Button		btHalftimeOvertime;
	private ClockState 	state;
	private boolean 	bHasHalftime;
	private boolean		bIsFirstHalf;

	/**	Gameclock( startPauseResume, halftimeOvertime, clockText, duration ) -- CONSTRUCTOR
	 * 
	 * @param startPauseResume : Button for starting, pausing, and resuming the GameClock timer
	 * @param halftimeOvertime : Button for initiating halftime and/or overtime
	 * @param clockText : Textview to display the text of the game clock
	 * @param duration : time (milliseconds) of the length of one half (length of game if no halftime)
	 * 
	 *	Create GameClock object - defaults to a countdown timer. If the current game has halftime,
	 *	the duration should be set to the length of one half.
	 */
	public GameClock(Button startPauseResume, Button halftimeOvertime, TextView clockText, long duration) {
		super(clockText, ClockTextFormat.MinutesString, duration, true);
		btStartPauseResume = startPauseResume;
		state = ClockState.PausedTop;
		bIsFirstHalf = true;
		btHalftimeOvertime = halftimeOvertime;
		btHalftimeOvertime.setClickable(false);
		btHalftimeOvertime.setBackgroundColor(Color.GRAY);
		bHasHalftime = AppGlobals.gGameSettings.isHalftimeEnabled();
	}
	
//	public GameClock(Button startPauseResume, Button halftime, TextView clockText, long duration, boolean countDown) {
//		super(clockText, ClockTextFormat.MinutesString, duration, tick, countDown);
//		btStartPauseResume = startPauseResume;
//		state = ClockState.PausedTop;
//		bIsFirstHalf = true;
//		btHalftime = halftime;
//		btHalftime.setVisibility(View.INVISIBLE);
//		bHasHalftime = AppGlobals.gGameSettings.isHalftimeEnabled();
//	}

	
	/** onStartPauseResume()
	 * 	Input-event caused by a button press for the button controlling "start",
	 *  "pause", and "resume".  This changes the state of the clock.
	 */
	public void onStartPauseResume(){
		if( state == ClockState.PausedTop ){
			state = ClockState.Running;
			btStartPauseResume.setText(STR_PAUSE);
			startClock();
			
			//grey out halftimeOvertime button if it's clickable
			if( btHalftimeOvertime.isClickable() ){
				btHalftimeOvertime.setClickable(false);
				btHalftimeOvertime.setBackgroundColor(Color.GRAY);
			}
		}
		else if( state == ClockState.Running ){
			state = ClockState.Paused;
			btStartPauseResume.setText(STR_RESUME);
			pauseClock();
			
			//check time, if we're within halftime range (20% of duration of half), show halftime button
			if( bHasHalftime && bIsFirstHalf && (getTime() <= 0.2*getDuration()) ){
				btHalftimeOvertime.setBackgroundColor(Color.WHITE);
				btHalftimeOvertime.setClickable(true);
			}
		}
		else if ( state == ClockState.Paused ){
			state = ClockState.Running;
			btStartPauseResume.setText(STR_PAUSE);
			startClock();
			
			//disable halftimeOvertime button if it's showing
			if( btHalftimeOvertime.isClickable() ){
				btHalftimeOvertime.setClickable(false);
				btHalftimeOvertime.setBackgroundColor(Color.GRAY);
			}
		}
		else if ( state == ClockState.Expired ){
			String message = "ERROR: Malformed GameClock state. In Expired state and received" +
							 "input event ieStartPauseResume";
			Log.E(message);
			throw new RuntimeException(message);
		}
	}
	
	@Override
	/** onClockExpired
	 *	Called by the base Clock class when the clock expires. Changes the state of
	 *	the clock to Expired and changes GUI accordingly
	 */
	protected void onClockExpired() {
		state = ClockState.Expired;
		btStartPauseResume.setClickable(false);
		btStartPauseResume.setBackgroundColor(Color.GRAY);
		
		//show halftime button if it's still first half
		if( bHasHalftime && bIsFirstHalf ){
			btHalftimeOvertime.setBackgroundColor(Color.WHITE);
			btHalftimeOvertime.setClickable(true);
		}
		// TODO - show overtime text
	}
	
	/**	onRolloverHalftime
	 * 	Input event to rollover the first half to the next half and pause for halftime.
	 * 	Takes the current time on the clock and adds it to the duration of a half.
	 */
	public void onRolloverHalftime(){
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
		
		bIsFirstHalf=false;
	}
}
