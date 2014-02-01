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

/*	GameActivity
 * 	Base class Activity for all other GameActivity objects. This class establishes
 * 	game context and requires derived classes to override several in-game events.
 */
public abstract class GameActivity extends Activity
{
//	private boolean m_bHasHalftime;					//can be acheived with GameSettings object in AppGlobals
	private boolean m_bIsHalftime, m_bIsOvertime;
	
	/**	GameActivity -- CONSTRUCTOR
	 * 
	 */
	public GameActivity(){
//		m_bHasHalftime = true;
		m_bIsHalftime = m_bIsOvertime = false;
	}
	
//	protected void setHasHalftime(boolean hasHalftime){
//		m_bHasHalftime = hasHalftime;
//	}
	
	/** setIsHalftime
	 * 
	 * @param isHalftime : true if it is currently halftime
	 */
	protected void setIsHalftime(boolean isHalftime){
		m_bIsHalftime = isHalftime;
	}
	
	/** setIsOvertime
	 * 
	 * @param isOvertime : true if it is currently overtime
	 */
	protected void setIsOvertime(boolean isOvertime){
		m_bIsOvertime = isOvertime;
	}
	
	/** isHalftime
	 * 
	 * @return true if it is currently halftime
	 */
	protected boolean isHalftime(){
		return m_bIsHalftime;
	}
	
	/** isOvertime
	 * 
	 * @return true if it is currently overtime
	 */
	protected boolean isOvertime(){
		return m_bIsOvertime;
	}
	
	//In-game event overrides
	abstract protected void onAddPlayerEvent();
	abstract protected void onRemovePlayerEvent();
	abstract protected void onTimeoutEvent();
	abstract protected void onOvertimeEvent();
	abstract protected void onHalftimeEvent();
	abstract protected void onPenaltyEvent();
	abstract protected void onRoundCompleteEvent();
	abstract protected void onGameOverEvent();
}
