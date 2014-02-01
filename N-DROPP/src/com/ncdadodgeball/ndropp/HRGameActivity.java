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
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/*	HRGameActivity
 * 	Manages the Head Referee's GUI and functions.
 */
public class HRGameActivity extends GameActivity {
	public static 	HRGameActivity	sInstance;
	ButtonListener	m_Listener;
	GameSettings 	m_Settings;
	GameClock		m_GameClock;
	ShotClock		m_HomeShotClock;
	ShotClock		m_AwayShotClock;
	Button			m_btStartPauseResume;
	Button			m_btHalftime;
	
	/** HRGameActivity -- CONSTRUCTOR
	 * 	
	 */
	public HRGameActivity(){
		m_Settings = new GameSettings();
		m_Listener = new ButtonListener();
		m_GameClock = null;
		m_HomeShotClock = null;
		m_AwayShotClock = null;
		m_btStartPauseResume = null;
		m_btHalftime = null;
	}
	
	@Override
	/** onCreate
	 * 	Set up HR interface
	 */
	 public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.D("onCreate HR");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.head_ref);
        Log.D("set layout");
        sInstance = this;
        TextView clockText = (TextView)findViewById(R.id.HR_txtGameClock);
        m_btStartPauseResume = (Button)findViewById(R.id.HR_btStartPauseResume);
        m_btHalftime = (Button) findViewById(R.id.HR_btHalftime);
        
        m_GameClock = new GameClock(m_btStartPauseResume, m_btHalftime, clockText, 20*Clock.SECOND);
        m_btStartPauseResume.setOnClickListener(m_Listener);
        m_btHalftime.setOnClickListener(m_Listener);
        Log.D("done create");
    }
	
	/*	ButtonListener
	 * 	onClickListener for GUI buttons
	 */
	class ButtonListener implements OnClickListener{

		/** onClick
		 * 	Find the view that was selected and initiate the associated event
		 */
		public void onClick(View view) {
			
			//START/PAUSE/RESUME BUTTON
			if(view.getId() == findViewById(R.id.HR_btStartPauseResume).getId())
				m_GameClock.onStartPauseResume();
			
			//PAUSE/RESUME BUTTON
			else if(view.getId() == m_btHalftime.getId())
				m_GameClock.onRolloverHalftime();
		}
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
	/** onRoundCompletedEvent
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
}
