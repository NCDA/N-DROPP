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

public abstract class GameActivity extends Activity
{
//	private boolean m_bHasHalftime;					//can be acheived with GameSettings object in AppGlobals
	private boolean m_bIsHalftime, m_bIsOvertime;
	
	public GameActivity(){
//		m_bHasHalftime = true;
		m_bIsHalftime = m_bIsOvertime = false;
	}
	
	public GameActivity(boolean hasHalftime){
//		m_bHasHalftime = hasHalftime;
		m_bIsHalftime = m_bIsOvertime = false;
	}
	
//	protected void setHasHalftime(boolean hasHalftime){
//		m_bHasHalftime = hasHalftime;
//	}
	
	protected void setIsHalftime(boolean isHalftime){
		m_bIsHalftime = isHalftime;
	}
	
	protected void setIsOvertime(boolean isOvertime){
		m_bIsOvertime = isOvertime;
	}
	
//	protected boolean hasHalftime(){
//		return m_bHasHalftime;
//	}
	
	protected boolean isHalftime(){
		return m_bIsHalftime;
	}
	
	protected boolean isOvertime(){
		return m_bIsOvertime;
	}
	
	abstract protected void onAddPlayerEvent();
	abstract protected void onRemovePlayerEvent();
	abstract protected void onTimeoutEvent();
	abstract protected void onOvertimeEvent();
	abstract protected void onHalftimeEvent();
	abstract protected void onPenaltyEvent();
	abstract protected void onRoundCompleteEvent();
	abstract protected void onGameOverEvent();
}
