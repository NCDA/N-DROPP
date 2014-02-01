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
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.TextView;


/* ShotClock
 * 		ShotClock uses the Clock class to maintain a timer in the realm of a dodgeball shotclock
 * timer.  This class keeps track of the state the timer is in. This class additionally takes 2
 * button widgets which is used by the ShotClock to change their visual appearance based on
 * input events from the user and timer events.
 */
public class ShotClock extends Clock {
	
	//constants
	private final String STR_START = "Start";
	private final String STR_RESET = "Reset";
	private final String STR_PAUSE = "Pause";
	private final String STR_RESUME = "Resume";
	private final String STR_RESTART = "Restart";
	
	//states
	private enum ClockState { PausedTop, RollingTop, Paused, Resumed, Expired };

	//class member variables
	private ClockState state;
	private Button btResetStart, btPauseResume;
	
	/** ShotClock
	 * @param resetStart : button to represent the shot clock's reset/start/restart button
	 * @param pauseResume : button to represent the shot clock's pause/resume/reset button
	 * @param clockText : @see @Clock
	 * @param duration : @see @Clock
	 * 
	 * Initialize state and class variables.
	 */
	public ShotClock(Button resetStart, Button pauseResume, TextView clockText, long duration) {
		super(clockText, ClockTextFormat.SecondsString, duration);
		btResetStart = resetStart;
		btPauseResume = pauseResume;
		state = ClockState.PausedTop;
		btResetStart.setText("Start");
		btResetStart.setClickable(true);
		btPauseResume.setText("Pause");
		btPauseResume.setClickable(false);
	}
	
	/** ShotClock
	 * @param resetStart : button to represent the shot clock's reset/start/restart button
	 * @param pauseResume : button to represent the shot clock's pause/resume/reset button
	 * @param clockText : @see @Clock
	 * @param duration : @see @Clock
	 * @param countDown : @see @Clock
	 * 
	 * Initialize state and class variables.
	 */
	public ShotClock( Button resetStart, Button pauseResume, TextView clockText, long duration, boolean countDown){
		super(clockText, ClockTextFormat.SecondsString, duration, countDown);
		btResetStart = resetStart;
		btPauseResume = pauseResume;
		state = ClockState.PausedTop;
		btResetStart.setText("Start");
		btResetStart.setClickable(true);
		btPauseResume.setText("Pause");
		btPauseResume.setClickable(false);
	}
	
	
	/** onResetStartRestart
	 * 		This function is called when the button that represents the ShotClock's
	 * Reset/Start/Restart button. Based on this input event, the clock is moved to a
	 * different state and the appropriate clock changes & visual changes are made.
	 */
	public void onResetStartRestart(){
		
		if(state == ClockState.PausedTop){
			state = ClockState.RollingTop;
			btResetStart.setText(STR_RESET);
			btPauseResume.setText(STR_PAUSE);
			btPauseResume.setClickable(true);
			btPauseResume.setBackgroundColor(Color.WHITE);
			resetClock();
			startClock();
		}
		
		else if(state == ClockState.RollingTop){
			resetClock();
			startClock();
		}
		
		else if(state == ClockState.Paused){
			state = ClockState.PausedTop;
			btResetStart.setText(STR_START);
			btPauseResume.setText(STR_PAUSE);
			btPauseResume.setClickable(false);
			btPauseResume.setBackgroundColor(Color.GRAY);
			resetClock();
		}
		
		else if(state == ClockState.Resumed){
			state = ClockState.RollingTop;
			resetClock();
			startClock();
		}
		else if(state == ClockState.Expired){
			state = ClockState.RollingTop;
			btResetStart.setText(STR_RESET);
			btPauseResume.setText(STR_PAUSE);
			getClockText().setTextColor(Color.WHITE);
			resetClock();
			startClock();
		}
	}
	
	/** onPauseResumeReset
	 * 		This function is called when the button that represents the ShotClock's
	 * Pause/Resume/Reset button. Based on this input event, the clock is moved to a
	 * different state and the appropriate clock changes & visual changes are made.
	 */
	public void onPauseResumeReset(){
		
		if(state == ClockState.PausedTop){
			String message = "ERROR: Malformed ShotClock state. In PausedTop state and" +
							 "received input event iePauseResumeReset";
			Log.E(message);
			throw new RuntimeException(message);
		}
		
		else if(state == ClockState.RollingTop){
			state = ClockState.Paused;
			btPauseResume.setText(STR_RESUME);
			pauseClock();
		}
		
		else if(state == ClockState.Paused){
			state = ClockState.Resumed;
			btPauseResume.setText(STR_PAUSE);
			startClock();
		}
		
		else if(state == ClockState.Resumed){
			state = ClockState.Paused;
			btPauseResume.setText(STR_RESUME);
			pauseClock();
		}
		
		else if(state == ClockState.Expired){
			state = ClockState.PausedTop;
			btResetStart.setText(STR_START);
			btPauseResume.setClickable(false);
			btPauseResume.setBackgroundColor(Color.GRAY);
			getClockText().setTextColor(Color.WHITE);
			resetClock();
		}
	}

	/** onClockExpired
	 *	The subclass triggers this function when the timer expires. Here, we're
	 *	able to make the necessary state & visual changes to the timer.
	 */
	@Override
	protected void onClockExpired() {
		state = ClockState.Expired;
		btResetStart.setText(STR_RESTART);
		btPauseResume.setText(STR_RESET);
		getClockText().setTextColor(Color.RED);
	}
}
