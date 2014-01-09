package com.ncdadodgeball.ndropp;

import android.graphics.Color;
import android.widget.Button;
import android.widget.TextView;

public class ShotClock extends Clock {
	
	private final String STR_START = "Start";
	private final String STR_RESET = "Reset";
	private final String STR_PAUSE = "Pause";
	private final String STR_RESUME = "Resume";
	private final String STR_RESTART = "Restart";
	
	public enum ClockState { PausedTop, RollingTop, Paused, Resumed, Expired };

	ClockState state;
	Button btResetStart, btPauseResume;
	TextView vClockText;
	
	
	public ShotClock(Button resetStart, Button pauseResume, TextView clockText, long duration, long tick) {
		super(clockText, duration, tick);
		btResetStart = resetStart;
		btPauseResume = pauseResume;
		vClockText = clockText;
		state = ClockState.PausedTop;
		btResetStart.setText("Start");
		btResetStart.setClickable(true);
		btPauseResume.setText("Pause");
		btPauseResume.setClickable(false);
	}
	
	
	public ShotClock( Button resetStart, Button pauseResume, TextView clockText, long duration, long tick, boolean countDown){
		super(clockText, duration, tick, countDown);
		btResetStart = resetStart;
		btPauseResume = pauseResume;
		vClockText = clockText;
		state = ClockState.PausedTop;
		btResetStart.setText("Start");
		btResetStart.setClickable(true);
		btPauseResume.setText("Pause");
		btPauseResume.setClickable(false);
	}
	
	
	/* input event Reset/Start/Restart
	 * 
	 */
	public void ieResetStartRestart(){
		
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
			vClockText.setTextColor(Color.WHITE);
			resetClock();
			startClock();
		}
	}
	
	/* input event PauseResumeReset
	 * 
	 */
	public void iePauseResumeReset(){
		
		if(state == ClockState.PausedTop){
			throw new RuntimeException("ERROR: malformed state. In PausedTop state and received input event iePauseResumeReset");
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
			vClockText.setTextColor(Color.WHITE);
			resetClock();
		}
	}

	/* input event clockExpired
	 * 
	 */
	@Override
	protected void onClockExpired() {
		state = ClockState.Expired;
		btResetStart.setText(STR_RESTART);
		btPauseResume.setText(STR_RESET);
		vClockText.setTextColor(Color.RED);
	}
	
	

}
