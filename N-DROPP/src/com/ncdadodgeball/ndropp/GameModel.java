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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.ncdadodgeball.util.GameClock;
import com.ncdadodgeball.util.GameSettings;
import com.ncdadodgeball.util.ShotClock;

/* GameModel
 * 		This class keeps track of the game as a whole
 */
public class GameModel 
{
	private static GameModel sInstance = null;
	
	private static GameClock 	m_cGameClock;
	private static ShotClock 	m_cHomeSC;
	private static ShotClock 	m_cAwaySC;
	
	private GameSettings.STAFF		m_eStaffMember;
	
	private int m_nHomePlayers;
	private int m_nAwayPlayers;
	private int m_nHomeTimeouts;
	private int m_nAwayTimeouts;
	
	private GridView m_vHomeTeamGrid;
	private GridView m_vAwayTeamGrid;
	
	private GameModel()
	{
		m_nHomePlayers = m_nAwayPlayers = GameSettings.instance().getMaxPlayers();
		m_nHomeTimeouts = m_nAwayTimeouts = GameSettings.instance().getTotalTimeouts();
		m_vHomeTeamGrid = null;
		m_vAwayTeamGrid = null;
	}
	
	public static GameModel instance(){
		if( sInstance == null )
			sInstance = new GameModel();
		return sInstance;
	}
	
	public void init(){
		GameSettings settings = GameSettings.instance();
		
		//GAME CLOCK
		if( settings.isHalftimeEnabled() )
			m_cGameClock = new GameClock(settings.getGameClockDuration()/2);
		else
			m_cGameClock = new GameClock(settings.getGameClockDuration());
		
		//who are we?
		m_eStaffMember = settings.getStaffType();
		
		// HOME SHOT CLOCK
		// if we're the shot clock ref, use our settings
		if( m_eStaffMember == GameSettings.STAFF.HOME_SCR ){
			m_cHomeSC = new ShotClock( settings.getShotClockDuration(), settings.isShotClockCountDown() );
		}
		//we're not the home shot clock ref, use other settings?
		else{
			//TODO - use settings of the person who is the home SCR
			m_cHomeSC = new ShotClock( settings.getGameClockDuration(), settings.isShotClockCountDown() );
		}
		
		// AWAY SHOT CLOCK
		// if we're the shot clock ref, use our settings
		if( m_eStaffMember == GameSettings.STAFF.AWAY_SCR ){
			m_cAwaySC = new ShotClock( settings.getShotClockDuration(), settings.isShotClockCountDown() );
		}
		//we're not the home shot clock ref, use other settings?
		else{
			//TODO - use settings of the person who is the home SCR
			m_cAwaySC = new ShotClock( settings.getGameClockDuration(), settings.isShotClockCountDown() );
			
			//TODO - set m_sAwaySCR, if any
		}
	}
	
	public void setHomeTeamGridView(GridView v){
		m_vHomeTeamGrid = v;
	}
	
	public void setAwayTeamGridView(GridView v){
		m_vAwayTeamGrid = v;
	}
	
	public void addHomePlayer(){
		if( m_nHomePlayers == 0 )
			return;
		
		if( m_vHomeTeamGrid != null ){
			GameActivity.currentActivity().runOnUiThread(
					new GridViewUpdate(m_vHomeTeamGrid, m_nHomePlayers, 
							GameActivity.currentActivity().getString(R.string.file_sil_blue) ) );
		}
		
		m_nHomePlayers++;
		if( m_nHomePlayers > GameSettings.instance().getMaxPlayers() ){
			m_nHomePlayers--;
			return;
		}
	}
	
	public void addAwayPlayer(){
		if( m_nAwayPlayers == 0 )
			return;
		
		if( m_vAwayTeamGrid != null ){
			GameActivity.currentActivity().runOnUiThread(
					new GridViewUpdate(m_vAwayTeamGrid, m_nAwayPlayers, 
							GameActivity.currentActivity().getString(R.string.file_sil_green) ) );
		}
		
		m_nAwayPlayers++;
		if( m_nAwayPlayers > GameSettings.instance().getMaxPlayers() ){
			m_nAwayPlayers--;
			return;
		}
	}
	
	public boolean removeHomePlayer(){
		if( m_nHomePlayers <= 0 )
			return true;
		
		m_nHomePlayers--;
		
		if( m_vHomeTeamGrid != null ){
			GameActivity.currentActivity().runOnUiThread(
					new GridViewUpdate(m_vHomeTeamGrid, m_nHomePlayers, 
							GameActivity.currentActivity().getString(R.string.file_sil_grey) ) );
		}
		
		if( m_nHomePlayers == 0)
			return true;			//return true if team eliminated
		return false;
	}
	
	public boolean removeAwayPlayer(){
		if( m_nHomePlayers <= 0 )
			return true;
		
		m_nAwayPlayers--;
		
		if( m_vAwayTeamGrid != null ){
			GameActivity.currentActivity().runOnUiThread(
					new GridViewUpdate(m_vAwayTeamGrid, m_nAwayPlayers, 
							GameActivity.currentActivity().getString(R.string.file_sil_grey) ) );
		}
		
		if( m_nAwayPlayers == 0)
			return true;			//return true if team eliminated
		return false;
	}
	
	public void spendHomeTimeout(){
		if( m_nHomeTimeouts == 0 )
			throw new RuntimeException("Home team doesn't have any timeouts to spend!");
		--m_nHomeTimeouts;
	}
	
	public void spendAwayTimeout(){
		if( m_nAwayTimeouts == 0 )
			throw new RuntimeException("Away team doesn't have any timeouts to spend");
		--m_nAwayTimeouts;
	}
	
	public GameClock getGameClock(){
		return m_cGameClock;
	}
	
	public ShotClock getHomeShotClock(){
		return m_cHomeSC;
	}

	public ShotClock getAwayShotClock(){
		return m_cAwaySC;
	}
	
	public GameSettings.STAFF getCurrentStaffMember(){
		return m_eStaffMember;
	}
	
	class GridViewUpdate implements Runnable{
		GridView m_vGrid;
		int m_nIndex;
		String m_strImgFile;
		public GridViewUpdate(GridView v, int index, String file){
			m_vGrid = v;
			m_nIndex = index;
			m_strImgFile = file;
		}
		public void run() {
			//TODO -- get team color
			if( m_nIndex >= m_vGrid.getAdapter().getCount() || m_nIndex < 0 )
				return;
			ImageView img = (ImageView)((BaseAdapter)m_vGrid.getAdapter()).getItem(m_nIndex);
			Activity activity = GameActivity.currentActivity();
			int imgID = activity.getResources().getIdentifier(
					m_strImgFile, "drawable", activity.getString(R.string.app_package));
			img.setImageResource(imgID);
		}
	}
}