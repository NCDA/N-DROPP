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

import java.io.File;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

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
	
	/** onRulebookPressed
	 * 	calls the DownloadManager to check to see if the rulebook exists. If the rulebook exists,
	 * 	it is opened. Otherwise, error flag.
	 */
	protected void onRulebookPressed(){
		//Rulebook - download/view
		if(DownloadManager.DownloadRulebook()){
			File fRulebook = new File(AppGlobals.EXTERNAL_DIR + AppGlobals.PACKAGE + "/" + AppGlobals.RULEBOOK_FILE);
			Uri path = Uri.fromFile(fRulebook);
            Intent pdfViewIntent = new Intent(Intent.ACTION_VIEW);
            pdfViewIntent.setDataAndType(path, "application/pdf");
            pdfViewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(pdfViewIntent);
            } 
            catch (ActivityNotFoundException e) {
            	Log.D("ERROR: No application to view PDF.");
                Toast.makeText(MainActivity.sInstance, "Error: No application exists on this device to view PDF", Toast.LENGTH_LONG).show();
            }
		}
	}
	
	/** openSettings
	 * 	//TODO
	 */
	protected void onSettingsPressed(){

	}
	
	/** onPenaltyPressed
	 * 	TODO 
	 */
	protected void onPenaltyPressed(){
		
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
