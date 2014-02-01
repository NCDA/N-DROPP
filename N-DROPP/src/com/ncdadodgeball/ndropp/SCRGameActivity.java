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
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SCRGameActivity extends  GameActivity{
	
	public static SCRGameActivity	sInstance;
	ButtonListener	m_Listener;
	GameSettings 	m_Settings;
	ShotClock		m_ShotClock;
	Clock			m_GameClock;
	Button			m_btStartReset;
	Button			m_btPauseResume;
	Button			m_btAddPlayer;
	Button			m_btRemovePlayer;
	GridView		m_vTeamGrid;
	int				m_nPlayersOnCourt;
	
	public SCRGameActivity(){
		m_Settings = new GameSettings();
		m_Listener = new ButtonListener();
		m_ShotClock = null;
		m_btStartReset = null;
		m_btPauseResume = null;
		m_nPlayersOnCourt = AppGlobals.NUM_MAX_STARTING_PLAYERS;
	}
	
	@Override
	 public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shot_clock_ref);
        
        sInstance = this;
        
        //set up shot clock
        TextView clockText = (TextView)findViewById(R.id.txtShotClock);
        m_btStartReset 	= (Button)findViewById(R.id.btStartReset);
        m_btPauseResume	= (Button)findViewById(R.id.btPauseShotClock);
        m_ShotClock = new ShotClock(m_btStartReset, m_btPauseResume, clockText, 15000, Clock.CENTISEC, false);
        m_btStartReset.setOnClickListener(m_Listener);
        m_btPauseResume.setOnClickListener(m_Listener);
        m_btPauseResume.setClickable(false);
        m_btPauseResume.setBackgroundColor(Color.GRAY);
    	clockText.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);		//change shot clock font
    	clockText.setTextSize(36);
        
        //set up GridView dimensions
        int screenW = getWindowManager().getDefaultDisplay().getWidth();
        int screenH = getWindowManager().getDefaultDisplay().getHeight();
        int colWidth = (int)(( screenW * AppGlobals.SCR_GRID_WIDTH_PERCENT) / 5 );
        int rowHeight = (int)(( screenH * AppGlobals.SCR_GRID_HEIGHT_PERCENT) / 3 );
        int gridWidth = colWidth*5;
        int gridHeight = rowHeight*3;
        
        //grab correct team logo
        ImageView vLogo = (ImageView) findViewById(R.id.imgTeamLogo);
        Resources res = getResources();
        int imgID = res.getIdentifier("logo_gvsu", "drawable", AppGlobals.PACKAGE);		//TODO
        LayoutParams layout = vLogo.getLayoutParams();
        layout.width = (int)(screenW * AppGlobals.SCR_LOGO_WIDTH_PERCENT);
        layout.height = (int) (layout.width * AppGlobals.LOGO_ASPECT_RATIO);
        vLogo.setBackgroundDrawable(res.getDrawable(imgID));
        
        //establish GridView of players
        m_vTeamGrid = (GridView) findViewById(R.id.gridTeam);
        layout = m_vTeamGrid.getLayoutParams();
        layout.width = gridWidth;
        layout.height = gridHeight;
        m_vTeamGrid.setColumnWidth(colWidth);
        m_vTeamGrid.setMinimumWidth(gridWidth);
        m_vTeamGrid.setAdapter(new GridImageAdapter(this)); 
        m_vTeamGrid.setClickable(false);
        m_vTeamGrid.setSelected(false);
        m_vTeamGrid.setFocusable(false);
        
        //setup add/remove player buttons
        m_btAddPlayer = (Button) findViewById(R.id.btAddPlayer);
        m_btRemovePlayer = (Button) findViewById(R.id.btRemovePlayer);
        m_btAddPlayer.setOnClickListener(m_Listener);
        m_btRemovePlayer.setOnClickListener(m_Listener);
        
        //set up game clock - put game clock midway between shot clock and bottom of screen
    	int bottom = findViewById(R.id.hLinearShotClock).getBottom();
    	findViewById(R.id.txtSCRGameClock).getLayoutParams().height = screenH-bottom;
    	TextView gameClockText = (TextView) findViewById(R.id.txtSCRGameClock);
    	gameClockText.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
    	clockText.setTextSize(28);
    }

	@Override
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
	
	public GameSettings getSettings() {
		return m_Settings;
	}
	public Clock getGameTimer() {
		return m_GameClock;
	}
	public ShotClock getShotClock() {
		return m_ShotClock;
	}
	public void setShotClock(ShotClock shotClock) {
		m_ShotClock = shotClock;
	}

	@Override
	protected void onAddPlayerEvent(){
		int maxPlayers = 0;
		if( isOvertime() )
			maxPlayers = AppGlobals.NUM_MAX_OVERTIME_PLAYERS;
		else
			maxPlayers = AppGlobals.NUM_MAX_STARTING_PLAYERS;
		
		//if the court is full, don't add a player
		if( m_nPlayersOnCourt + 1 > maxPlayers )
			Toast.makeText(SCRGameActivity.sInstance, "Max players on court", Toast.LENGTH_SHORT).show();
		//court is not full, add a player
		else{
			BaseAdapter adapter = (BaseAdapter)SCRGameActivity.sInstance.m_vTeamGrid.getAdapter();
			((ImageView)adapter.getItem(m_nPlayersOnCourt)).setVisibility(View.VISIBLE);	//TODO - restore colored image
			m_nPlayersOnCourt++;
		}
		
		if(m_nPlayersOnCourt == 1)
			SCRGameActivity.sInstance.m_vTeamGrid.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onRemovePlayerEvent() {
		if(m_nPlayersOnCourt > 0){
			BaseAdapter adapter = (BaseAdapter)SCRGameActivity.sInstance.m_vTeamGrid.getAdapter();
			((ImageView)adapter.getItem(m_nPlayersOnCourt - 1)).setVisibility(View.INVISIBLE);	//TODO - grey out image
			m_nPlayersOnCourt--;
		}
		
		if( m_nPlayersOnCourt == 0 ){
			SCRGameActivity.sInstance.m_vTeamGrid.setVisibility(View.INVISIBLE);		//for some reason, GridView NEEDS to be showing something so the last player never gets hidden.  Instead, we'll just hid the whole view
			onRoundCompleteEvent();
		}
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
	
	class ButtonListener implements OnClickListener{

		public void onClick(View view) {
			
			int id = view.getId();
			
			//START/RESET BUTTON
			if(id == findViewById(R.id.btStartReset).getId())
				(SCRGameActivity.sInstance.getShotClock()).ieResetStartRestart();
			
			//PAUSE/RESUME BUTTON
			else if(id == findViewById(R.id.btPauseShotClock).getId())
				(SCRGameActivity.sInstance.getShotClock()).iePauseResumeReset();
			
			//ADD PLAYER BUTTON
			else if( id == findViewById(R.id.btAddPlayer).getId() )
				onAddPlayerEvent();
			
			//REMOVE PLAYER BUTTON
			else if( id == findViewById(R.id.btRemovePlayer).getId() )
				onRemovePlayerEvent();
		}
    }
}

class GridImageAdapter extends BaseAdapter
{
	private Context context;
	ImageView images[];
	
	public GridImageAdapter(Context ctx){
		context=ctx;
		images = new ImageView[15];
	}

	public int getCount() {
		return images.length;
	}

	public ImageView getItem(int index) {
		return images[index];
	}

	public long getItemId(int index) {
		return images[index].getId();
	}

	public View getView(int index, View view, ViewGroup parent) {
		ImageView imageView = new ImageView(context);
		int imgID = SCRGameActivity.sInstance.getResources().getIdentifier(AppGlobals.SIL_BLUE, "drawable", AppGlobals.PACKAGE);	//TODO
        imageView.setImageResource(imgID);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Display display = SCRGameActivity.sInstance.getWindowManager().getDefaultDisplay();
        imageView.setLayoutParams(new GridView.LayoutParams(
        		(int)(( display.getWidth() * AppGlobals.SCR_GRID_WIDTH_PERCENT) / 5 ), 
        		(int)(( display.getHeight() * AppGlobals.SCR_GRID_HEIGHT_PERCENT) / 3 )));
        imageView.setClickable(false);
        imageView.setFocusable(false);
        imageView.setSelected(false);
        images[index] = imageView;
        return imageView;
	}
}
