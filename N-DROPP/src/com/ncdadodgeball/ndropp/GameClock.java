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

import android.widget.Button;
import android.widget.TextView;

public class GameClock extends Clock {
	
	//class member variables
	Button btStartPauseResume;

	public GameClock(Button startPauseResume, TextView clockText, long duration, long tick) {
		super(clockText, duration, tick);
		btStartPauseResume = startPauseResume;
	}
	
	public GameClock(Button startPauseResume, TextView clockText, long duration, long tick, boolean countDown) {
		super(clockText, duration, tick, countDown);
		btStartPauseResume = startPauseResume;
	}

	@Override
	protected void onClockExpired() {
	}

}
