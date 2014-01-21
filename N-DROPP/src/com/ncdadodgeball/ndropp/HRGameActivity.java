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

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HRGameActivity extends Activity {
	public static 	HRGameActivity	sInstance;
	ButtonListener	m_Listener;
	GameSettings 	m_Settings;
	GameClock		m_GameClock;
	ShotClock		m_HomeShotClock;
	ShotClock		m_AwayShotClock;
	Button			m_btStartPauseResume;
	Button			m_btHalftime;
	
//	public GameActivity(GameSettings settings){
//		m_Settings = settings;
//		m_ClockText = (TextView)findViewById(R.id.txtClock);
//		m_ShotClock = new Clock(m_ClockText, true, 15000);
//		m_Button = null;
//	}
	
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
	 public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.D("onCreate HR");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.head_ref);
        Log.D("set layout");
        sInstance = this;
        TextView clockText = (TextView)findViewById(R.id.txtHRGameClock);
        m_btStartPauseResume = (Button)findViewById(R.id.btStartPauseResume);
        m_btHalftime = (Button) findViewById(R.id.btHalftime);
        
        m_GameClock = new GameClock(m_btStartPauseResume, m_btHalftime, clockText, 20*Clock.SECOND, Clock.CENTISEC);
        m_btStartPauseResume.setOnClickListener(m_Listener);
        m_btHalftime.setOnClickListener(m_Listener);
        Log.D("done create");
    }
	
	class ButtonListener implements OnClickListener{

		public void onClick(View view) {
			
			//START/PAUSE/RESUME BUTTON
			if(view.getId() == findViewById(R.id.btStartPauseResume).getId())
				m_GameClock.ieStartPauseResume();
			
			//PAUSE/RESUME BUTTON
			else if(view.getId() == m_btHalftime.getId())
				m_GameClock.ieRolloverHalftime();
		}
    }
}
