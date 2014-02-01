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

import android.os.Bundle;
import android.view.Window;

public class HybridRefGameActivity extends GameActivity {

	@Override
	 public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       requestWindowFeature(Window.FEATURE_NO_TITLE);
       //setContentView(R.layout.shot_clock_ref);
   }

	@Override
	protected void onAddPlayerEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onRemovePlayerEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onTimeoutEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onOvertimeEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onHalftimeEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onPenaltyEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onRoundCompleteEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onGameOverEvent() {
		// TODO Auto-generated method stub
		
	}
}
