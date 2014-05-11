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

import com.ncdadodgeball.ndropp.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/*	SCRGameActivity
 *	Shotclock Referee Game Activity sets up the Shot Clock referee's interface and all associated functions
 */
public class SCRGameActivity extends  GameActivity{
	
	public static SCRGameActivity	sInstance;
	
	//class member variables
	private ButtonListener	m_Listener;
	private ShotClock		m_ShotClock;
	private GameClock		m_GameClock;
	private Button			m_bt_start_reset;
	private Button			m_btPauseResume;
	private Button			m_bt_add_player;
	private Button			m_bt_remove_player;
	private GridView		m_vTeamGrid;
	private int				m_nPlayersOnCourt;
	
	/** SCRGameActivity -- CONSTRUCTOR
	 *	Set up game
	 */
	public SCRGameActivity(){
		m_Listener = new ButtonListener();
		m_ShotClock = null;
		m_bt_start_reset = null;
		m_btPauseResume = null;
		m_nPlayersOnCourt = Global.NUM_MAX_STARTING_PLAYERS;
	}
	
	@Override
	/** onCreate
	 * 	set up SCR UI and features
	 */
	 public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.shot_clock_ref);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        
        sInstance = this;
        
        int screenW = getWindowManager().getDefaultDisplay().getWidth();
        int screenH = getWindowManager().getDefaultDisplay().getHeight();
        
        //set up navigation buttons (rulebook, settings)
        ImageView vNav = (ImageView)findViewById(R.id.SCR_bt_settings);
        int side = (int)Global.NAV_IMG_HEIGHT_PERCENT * screenH;
        vNav.setMinimumHeight(side);
        vNav.setMinimumWidth(side);
        vNav.setOnClickListener(m_Listener);
        vNav = (ImageView)findViewById(R.id.SCR_bt_rulebook);
        vNav.setMinimumHeight(side);
        vNav.setMinimumWidth(side);
        vNav.setOnClickListener(m_Listener);
        
        //set up shot clock
        TextView clockText = (TextView)findViewById(R.id.SCR_txt_shot_clock);
        m_bt_start_reset 	= (Button)findViewById(R.id.SCR_bt_start_reset);
        m_btPauseResume	= (Button)findViewById(R.id.SCR_bt_pause_sc);
        m_ShotClock = new ShotClock(m_bt_start_reset, m_btPauseResume, clockText, 15*Clock.SECOND, false);
        m_bt_start_reset.setOnClickListener(m_Listener);
        m_btPauseResume.setOnClickListener(m_Listener);
        m_btPauseResume.setClickable(false);
        m_btPauseResume.setBackgroundColor(Color.GRAY);
    	clockText.setTextSize(36);
        
        //set up GridView dimensions
        int colWidth = (int)(( screenW * Global.SCR_GRID_WIDTH_PERCENT) / 5 );
        int rowHeight = (int)(( screenH * Global.SCR_GRID_HEIGHT_PERCENT) / 3 );
        int gridWidth = colWidth*5 + 5;
        int gridHeight = rowHeight*3 + 5;	// the +5 is a little extra buffer - otherwise gridView becomes scrollable
        
        //grab correct team logo
        ImageView vLogo = (ImageView) findViewById(R.id.SCR_ic_team);
        Resources res = getResources();
        int imgID = res.getIdentifier(getString(R.string.file_ic_gvsu), "drawable", getString(R.string.app_package));		//TODO
        LayoutParams layout = vLogo.getLayoutParams();
        layout.width = (int)(screenW * Global.SCR_LOGO_WIDTH_PERCENT);
        layout.height = (int) (layout.width * Global.LOGO_ASPECT_RATIO);
        vLogo.setBackgroundDrawable(res.getDrawable(imgID));
        
        //set team descriptors
        //TODO
        ((TextView) findViewById(R.id.SCR_txt_home_away)).setText(getString(R.string.lbl_home_team));
        ((TextView) findViewById(R.id.SCR_txt_team_acro)).setText("GVSU");
        
        //establish GridView of players
        m_vTeamGrid = (GridView) findViewById(R.id.SCR_grid);
        layout = m_vTeamGrid.getLayoutParams();
        layout.width = gridWidth;
        layout.height = gridHeight;
        m_vTeamGrid.setColumnWidth(colWidth);
        m_vTeamGrid.setMinimumWidth(gridWidth);
        m_vTeamGrid.setAdapter(
        		new GridImageAdapter(this, getString(R.string.file_sil_blue), Global.SCR_GRID_WIDTH_PERCENT, Global.SCR_GRID_HEIGHT_PERCENT)); 	//TODO - determine team and color
        m_vTeamGrid.setClickable(false);
        m_vTeamGrid.setSelected(false);
        m_vTeamGrid.setFocusable(false);
        
        //setup add/remove player buttons
        m_bt_add_player = (Button) findViewById(R.id.SCR_bt_add_player);
        m_bt_remove_player = (Button) findViewById(R.id.SCR_bt_remove_player);
        m_bt_add_player.setOnClickListener(m_Listener);
        m_bt_remove_player.setOnClickListener(m_Listener);
    }

	@Override
	/** onBackPressed
	 * 	Called when Back soft-key button is pressed by user.  Throw up a dialog confirming exit
	 */
	public void onBackPressed(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setMessage("Are you sure you want to quit?  All current game content will be lost.");
		alert.setCancelable(true);
		alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		alert.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int id){
				SCRGameActivity.this.finish();
			}
		});
		AlertDialog dialog = alert.create();
		dialog.show();
	}
	
	/** getGameTimer
	 * 
	 * @return GameClock representing the game timer
	 */
	public GameClock getGameTimer() {
		return m_GameClock;
	}
	
	/** getShotClock
	 * 
	 * @return ShotClock object representing the shot clock
	 */
	public ShotClock getShotClock() {
		return m_ShotClock;
	}
	

	@Override
	/** onAddPlayerEvent
	 * 	Add a player to the Gridview of players
	 */
	protected void onAddPlayerEvent(){
		int maxPlayers = 0;
		if( isOvertime() )
			maxPlayers = Global.NUM_MAX_OVERTIME_PLAYERS;
		else
			maxPlayers = Global.NUM_MAX_STARTING_PLAYERS;
		
		//if the court is full, don't add a player
		if( m_nPlayersOnCourt + 1 > maxPlayers )
			Toast.makeText(SCRGameActivity.sInstance, "Max players on court", Toast.LENGTH_SHORT).show();
		//court is not full, add a player
		else{
			//TODO - restore team's colored image
			ImageView img = (ImageView)((BaseAdapter)m_vTeamGrid.getAdapter()).getItem(m_nPlayersOnCourt);
			int imgID = getResources().getIdentifier(getString(R.string.file_sil_blue), "drawable", getString(R.string.app_package));
			img.setImageResource(imgID);			
			m_nPlayersOnCourt++;
		}
	}

	@Override
	/** onRemovePlayerEvent
	 * 	remove player from the Gridview of players
	 */
	protected void onRemovePlayerEvent() {
		if(m_nPlayersOnCourt > 0){
			ImageView img = (ImageView)((BaseAdapter)m_vTeamGrid.getAdapter()).getItem(m_nPlayersOnCourt-1);
			int greyID = getResources().getIdentifier(getString(R.string.file_sil_grey), "drawable", getString(R.string.app_package));
			img.setImageResource(greyID);	
			m_nPlayersOnCourt--;
		}
		
		if( m_nPlayersOnCourt == 0 )
			onRoundCompleteEvent();
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
	
	@Override
	/** onPause
	 * 	TODO
	 */
	public void onPause(){
		super.onPause();
	}
	
	@Override
	/** onResume
	 * 	TODO
	 */
	public void onResume(){
		super.onResume();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
	/*	ButtonListener
	 * 	onClickListener for the elements of SCR GUI
	 */
	class ButtonListener implements OnClickListener{

		/** onClick
		 * 	Find the GUI element selected and initiate the associated event
		 */
		public void onClick(View view) {
			
			int id = view.getId();
			
			//RULEBOOK BUTTON
			if( id == findViewById(R.id.SCR_bt_rulebook).getId() )
				onRulebookPressed();
			
			//SETTINGS BUTTON
			else if( id == findViewById(R.id.SCR_bt_settings).getId() )
				onSettingsPressed();
			
			//START/RESET BUTTON
			else if(id == findViewById(R.id.SCR_bt_start_reset).getId())
				(SCRGameActivity.sInstance.getShotClock()).onResetStartRestart();
			
			//PAUSE/RESUME BUTTON
			else if(id == findViewById(R.id.SCR_bt_pause_sc).getId())
				(SCRGameActivity.sInstance.getShotClock()).onPauseResumeReset();
			
			//ADD PLAYER BUTTON
			else if( id == findViewById(R.id.SCR_bt_add_player).getId() )
				onAddPlayerEvent();
			
			//REMOVE PLAYER BUTTON
			else if( id == findViewById(R.id.SCR_bt_remove_player).getId() )
				onRemovePlayerEvent();
		}
    }
}