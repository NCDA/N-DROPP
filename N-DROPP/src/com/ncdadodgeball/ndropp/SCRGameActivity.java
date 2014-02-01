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

/*	SCRGameActivity
 *	Shotclock Referee Game Activity sets up the Shot Clock referee's interface and all associated functions
 */
public class SCRGameActivity extends  GameActivity{
	
	public static SCRGameActivity	sInstance;
	
	//class member variables
	private ButtonListener	m_Listener;
	private GameSettings 	m_Settings;
	private ShotClock		m_ShotClock;
	private GameClock			m_GameClock;
	private Button			m_btStartReset;
	private Button			m_btPauseResume;
	private Button			m_btAddPlayer;
	private Button			m_btRemovePlayer;
	private GridView		m_vTeamGrid;
	private int				m_nPlayersOnCourt;
	
	/** SCRGameActivity -- CONSTRUCTOR
	 *	Set up game
	 */
	public SCRGameActivity(){
		m_Settings = new GameSettings();
		m_Listener = new ButtonListener();
		m_ShotClock = null;
		m_btStartReset = null;
		m_btPauseResume = null;
		m_nPlayersOnCourt = AppGlobals.NUM_MAX_STARTING_PLAYERS;
	}
	
	@Override
	/** onCreate
	 * 	set up SCR UI and features
	 */
	 public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shot_clock_ref);
        
        sInstance = this;
        
        //set up shot clock
        TextView clockText = (TextView)findViewById(R.id.txtShotClock);
        m_btStartReset 	= (Button)findViewById(R.id.btStartReset);
        m_btPauseResume	= (Button)findViewById(R.id.btPauseShotClock);
        m_ShotClock = new ShotClock(m_btStartReset, m_btPauseResume, clockText, 15*Clock.SECOND, false);
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
	/** onRemovePlayerEvent
	 * 	remove player from the Gridview of players
	 */
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
			
			//START/RESET BUTTON
			if(id == findViewById(R.id.btStartReset).getId())
				(SCRGameActivity.sInstance.getShotClock()).onResetStartRestart();
			
			//PAUSE/RESUME BUTTON
			else if(id == findViewById(R.id.btPauseShotClock).getId())
				(SCRGameActivity.sInstance.getShotClock()).onPauseResumeReset();
			
			//ADD PLAYER BUTTON
			else if( id == findViewById(R.id.btAddPlayer).getId() )
				onAddPlayerEvent();
			
			//REMOVE PLAYER BUTTON
			else if( id == findViewById(R.id.btRemovePlayer).getId() )
				onRemovePlayerEvent();
		}
    }
}

/*	GridImageAdapter
 * 	ListAdapter that keeps track of the image elements in the gridview
 */
class GridImageAdapter extends BaseAdapter
{
	private Context context;
	ImageView images[];
	
	/** GridImageAdapter
	 * 
	 * @param ctx : application context
	 * 
	 * Create an adapter list of 15 elements
	 */
	public GridImageAdapter(Context ctx){
		context=ctx;
		images = new ImageView[15];
	}

	/** getCount
	 * 	return size of gridview
	 */
	public int getCount() {
		return images.length;
	}

	/** getItem
	 *  
	 *  @param index : array index of element
	 *  
	 *  @return ImageView of the associated element
	 */
	public ImageView getItem(int index) {
		return images[index];
	}

	/** getItemId
	 * 	
	 * 	@param index : array index of element in the gridview
	 * 
	 *	@return ImageView id of the assiciated ImageView
	 */
	public long getItemId(int index) {
		return images[index].getId();
	}

	/** getView
	 * 	
	 *	@param index : index of element
	 *	@param view : view to store at the specified index
	 *	@param parent : parent view
	 *
	 *	@return the created ImageView at the specific location
	 */
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
