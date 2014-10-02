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

import com.ncdadodgeball.comm.BluetoothManager;
import com.ncdadodgeball.util.Clock;
import com.ncdadodgeball.util.Event;
import com.ncdadodgeball.util.GameClock;
import com.ncdadodgeball.util.GameSettings;
import com.ncdadodgeball.util.GridImageAdapter;
import com.ncdadodgeball.util.ShotClock;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/*	HRGameActivity
 * 	Manages the Head Referee's GUI and functions.
 */
public class HRGameActivity extends GameActivity {
	
	public static 	HRGameActivity	sInstance;
	
	ButtonListener	m_Listener;
	Button			m_btStartPauseResume;
	Button			m_btHalftimeOvertime;
	
	/** HRGameActivity -- CONSTRUCTOR
	 * 	
	 */
	public HRGameActivity(){
		super();
		m_Listener = new ButtonListener();
		m_btStartPauseResume = null;
		m_btHalftimeOvertime = null;
	}
	
	@Override
	/** onCreate
	 * 	Set up HR interface
	 */
	 public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr);
        
        sInstance = this;
        
        int screenW = getWindowManager().getDefaultDisplay().getWidth();
        int screenH = getWindowManager().getDefaultDisplay().getHeight();
        
        //set up navigation buttons (rulebook, penalty, settings)
        ImageView vNav = (ImageView)findViewById(R.id.HR_bt_rulebook);
        int side = (int)Global.NAV_IMG_HEIGHT_PERCENT * screenH;
        vNav.setMinimumHeight(side);
        vNav.setMinimumWidth(side);
        vNav.setOnClickListener(m_Listener);
        vNav = (ImageView)findViewById(R.id.HR_bt_penalty);
        vNav.setMinimumHeight(side);
        vNav.setMinimumWidth(side);
        vNav.setOnClickListener(m_Listener);
        vNav = (ImageView)findViewById(R.id.HR_bt_settings);
        vNav.setMinimumHeight(side);
        vNav.setMinimumWidth(side);
        vNav.setOnClickListener(m_Listener);
       
        //Set team names
        ((TextView)findViewById(R.id.HR_txt_home_acro)).setText("GVSU");
        ((TextView)findViewById(R.id.HR_txt_away_acro)).setText("MSU");
        
        //Load team logos
        ImageView vLogo = (ImageView)findViewById(R.id.HR_ic_home);
        Resources res = getResources();
        int imgID = res.getIdentifier(getString(R.string.file_ic_gvsu), "drawable", getString(R.string.app_package));		//TODO - team logo
        LayoutParams layout = vLogo.getLayoutParams();
        layout.width = (int)(screenW * Global.HR_LOGO_WIDTH_PERCENT);
        layout.height = (int) (layout.width * Global.LOGO_ASPECT_RATIO);
        vLogo.setBackgroundDrawable(res.getDrawable(imgID));
        
        vLogo = (ImageView)findViewById(R.id.HR_ic_away);
        imgID = res.getIdentifier(getString(R.string.file_ic_msu), "drawable", getString(R.string.app_package));			//TODO - team logo
        layout = vLogo.getLayoutParams();
        layout.width = (int)(screenW * Global.HR_LOGO_WIDTH_PERCENT);
        layout.height = (int)(layout.width * Global.LOGO_ASPECT_RATIO);
        vLogo.setBackgroundDrawable(res.getDrawable(imgID));
        
        //set up GridView dimensions
        int colWidth = (int)(( screenW * Global.HR_GRID_WIDTH_PERCENT) / 5 );
        int rowHeight = (int)(( screenH * Global.HR_GRID_HEIGHT_PERCENT) / 3 );
        int gridWidth = colWidth*5 + 5;
        int gridHeight = rowHeight*3 + 5;	// the +5 is a little extra buffer - otherwise gridView becomes scrollable
        
        //set up GridViews of players
        GridView vGrid = (GridView) findViewById(R.id.HR_grid_home);
        GameModel.instance().setHomeTeamGridView(vGrid);
        layout = vGrid.getLayoutParams();
        layout.width = gridWidth;
        layout.height = gridHeight;
        vGrid.setColumnWidth(colWidth);
        vGrid.setMinimumWidth(gridWidth);
        vGrid.setAdapter(new GridImageAdapter(this, 
        		getString(R.string.file_sil_blue), Global.HR_GRID_WIDTH_PERCENT, Global.HR_GRID_HEIGHT_PERCENT)); 	//TODO - determine team and color
        vGrid.setClickable(false);
        vGrid.setSelected(false);
        vGrid.setFocusable(false);
        
        vGrid = (GridView) findViewById(R.id.HR_grid_away);
        GameModel.instance().setAwayTeamGridView(vGrid);
        layout = vGrid.getLayoutParams();
        layout.width = gridWidth;
        layout.height = gridHeight;
        vGrid.setColumnWidth(colWidth);
        vGrid.setMinimumWidth(gridWidth);
        vGrid.setAdapter(new GridImageAdapter(this, 
        		getString(R.string.file_sil_green), Global.HR_GRID_WIDTH_PERCENT, Global.HR_GRID_HEIGHT_PERCENT)); 	//TODO - determine team and color
        vGrid.setClickable(false);
        vGrid.setSelected(false);
        vGrid.setFocusable(false);
        
        //set up game clock
        TextView clockText = (TextView)findViewById(R.id.HR_txt_game_clock);
        clockText.setTextSize(36);
        GameModel.instance().getGameClock().bindTextView(clockText, this);
        
        //set up clock buttons
        m_btStartPauseResume = (Button)findViewById(R.id.HR_bt_start_pause_resume);
        m_btHalftimeOvertime = (Button) findViewById(R.id.HR_bt_halftime_overtime);
        m_btStartPauseResume.setOnClickListener(m_Listener);
        m_btHalftimeOvertime.setOnClickListener(m_Listener);
    }
	
	
	/*	ButtonListener
	 * 	onClickListener for GUI buttons
	 */
	class ButtonListener implements OnClickListener{

		/** onClick
		 * 	Find the view that was selected and initiate the associated event
		 */
		public void onClick(View view) {
			
			int id = view.getId();
			Event e = new Event( 
							Event.TYPE.NONE, 
							GameSettings.instance().getStaffType(), 
							GameSettings.STAFF.NONE, 
							null);
			
			//RULEBOOK BUTTON
			if( id == findViewById(R.id.HR_bt_rulebook).getId() )
				e.setType( Event.TYPE.OPEN_RULEBOOK );
			
			//PENALTY BUTTON
			if( id == findViewById(R.id.HR_bt_penalty).getId() )
				e.setType( Event.TYPE.GAME_PENALTY );
				
			//SETTINGS BUTTON
			else if( id == findViewById(R.id.HR_bt_settings).getId() )
				e.setType( Event.TYPE.OPEN_SETTINGS );
			
			//START/PAUSE/RESUME BUTTON
			else if( id == findViewById(R.id.HR_bt_start_pause_resume).getId() )
				e.setType(Event.TYPE.GC_PAUSE_RESUME);
			
			//TODO - HALFTIME/OVERTIME BUTTON
			else if( id == m_btHalftimeOvertime.getId() ){
				//TODO -- m_GameClock.onRolloverHalftime();
			}
			
			if( e.getType() != Event.TYPE.NONE )
				EventHandler.instance().postEvent( e );
		}
    }
	
	@Override
	public void setContextAttributes() {
		GameSettings.instance().setStaffType(GameSettings.STAFF.HR);
	}
	
	private void onStartTenCountEvent(){
		
	}
	
	private void onEndTenCountEvent(){
		
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