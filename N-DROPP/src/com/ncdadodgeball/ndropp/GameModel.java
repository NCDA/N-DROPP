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
	
	private GameModel()
	{
		
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
}