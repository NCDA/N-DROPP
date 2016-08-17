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
	/** onCreate
	 * 	set up Hybrid Referee interface
	 */
	 public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       requestWindowFeature(Window.FEATURE_NO_TITLE);
       //setContentView(R.layout.shot_clock_ref);
   }

	@Override
	/** onAddPlayerEvent
	 * 	TODO
	 */
	protected void onAddPlayerEvent() {
		
	}

	@Override
	/** onRemovePlayerEvent
	 * 	TODO
	 */
	protected void onRemovePlayerEvent() {
		
	}

	@Override
	/** onTimeoutEvent
	 * 	TODO
	 */
	protected void onTimeoutEvent() {
		
	}

	@Override
	/** onOvertimeEvent
	 * 	TODO
	 */
	protected void onOvertimeEvent() {
		
	}

	@Override
	/** onHalftimeEvent
	 * 	TODO
	 */
	protected void onHalftimeEvent() {
		
	}

	@Override
	/** onPenaltyEvent
	 * 	TODO
	 */
	protected void onPenaltyEvent() {
		
	}

	@Override
	/** onRoundCompleteEvent
	 * 	TODO
	 */
	protected void onRoundCompleteEvent() {
		
	}

	@Override
	/** onGameOverEvent
	 * 	TODO
	 */
	protected void onGameOverEvent() {
		
	}

	@Override
	public void setContextAttributes() {
		// TODO Auto-generated method stub
		
	}
}
