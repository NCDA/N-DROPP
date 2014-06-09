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

package com.ncdadodgeball.util;

import com.ncdadodgeball.ndropp.Global;
import com.ncdadodgeball.ndropp.MainActivity;
import com.ncdadodgeball.ndropp.R;
import com.ncdadodgeball.ndropp.Global.*;
import com.ncdadodgeball.ndropp.R.string;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/*	GameSettings
 * 	Oject describing the entire set of settings for this user/device
 */
public class GameSettings implements Serializable {

	private static final long serialVersionUID = 0L;
	
	private boolean m_bMute;
	private boolean m_bShotClockCountDown;
	private boolean m_bShotClockAudio;
	private boolean m_bVibration;
	private boolean m_bHasHalftime;
	
	private int m_nTimeouts;
	
	private long m_nShotClockDuration;	//shot clock in milliseconds
	private long m_nGameClockDuration;	//game clock in milliseconds
	
	private STAFF	m_eStaffType;
	private TEAM	m_eTeam;
	
//	private BluetoothManager m_BTM;

	/** GameSettings
	 * 	Create default settings
	 */
	public GameSettings(){
		resetToDefaults();
	}
	
	public void resetToDefaults(){
		m_bMute = false;
		m_bShotClockCountDown = false;
		m_bShotClockAudio = false;
		m_bVibration = false;
		m_bHasHalftime = true;
		m_nTimeouts = 2;
		m_nShotClockDuration = Clock.SECOND * 15;		//shot clock 15 seconds
		m_nGameClockDuration = Clock.MINUTE * 25;		//game clock at 25 minutes
		
		m_eStaffType = null;
		m_eTeam = null;
	}
	
	
	/**	loadSettings
	 * 
	 * @param ctx - application context
	 * 
	 * Load an existing "game_settings" file from the application storage or create a new
	 * settings object if it doesn't exist/is corrupt.  The GameSettings object is loaded
	 * into AppGlobals.mGameSettings.
	 */
	public static GameSettings loadSettings(Activity parent){
		//load settings from file
		Activity main = MainActivity.sInstance;
        File fSettings = new File( Global.getInternalDir(main) + "/" + main.getString(R.string.file_settings) );
        boolean bLoaded = false;
        if(fSettings.exists()){
        	try{
        		Global.gGameSettings = GameSettings.readSettings(new ObjectInputStream( new FileInputStream(fSettings)));
        		bLoaded = true;
        		Log.D("Settings read from app data");
        		return Global.gGameSettings;
        	}
        	catch(Exception e){
        		Toast.makeText(parent, "Settings data is corrupt. Resetting to defaults", Toast.LENGTH_LONG).show();
        	}
        }
        
        //if we didn't load settings (corrupt or doesn't exist), create new settings
        if(!bLoaded){
        	Global.gGameSettings = new GameSettings();
        	try{
        		GameSettings.writeSettings(Global.gGameSettings, new ObjectOutputStream(new FileOutputStream(fSettings)));
        		return Global.gGameSettings;
        	}
        	catch(Exception e){
        		throw new RuntimeException(e.getMessage());
        	}
        }
        return null;
	}
	
	/** isMute
	 * 
	 * @return true if application is muted (no audio should be playing)
	 */
	public boolean isMute() {
		return m_bMute;
	}

	/** setIsMute
	 * 
	 * @param mute : true if the application should be muted (no audio)
	 */
	public void setMute(boolean mute) {
		m_bMute = mute;
	}

	/** isShotClockCountDown
	 * 
	 * @return true if the shotclock is set to countdown mode
	 */
	public boolean isShotClockCountDown() {
		return m_bShotClockCountDown;
	}

	/** setShotClockCountMode ( countDown )
	 * 
	 * @param countDown : true if the shot clock should count down
	 */
	public void setShotClockCountMode(boolean countDown) {
		m_bShotClockCountDown = countDown;
	}
	
	/** isHalftimeEnabled
	 * 
	 * @return true if the game has halftime
	 */
	public boolean isHalftimeEnabled(){
		return m_bHasHalftime;
	}
	
	/** setHalftimeEnabled
	 * 
	 * @param enable : true if the game should have a halftime (2 distinct halves)
	 */
	public void setHalftimeEnabled(boolean enable){
		m_bHasHalftime = enable;
	}

	/** isShotClockAudioEnabled()
	 * 
	 * @return true if the shot clock is currently set to audible
	 */
	public boolean isM_bShotClkAudio() {
		return m_bShotClockAudio;
	}

	/** setShotClockAudioEnabled( enable )
	 * 
	 * @param enable : true if the shot clock should count audably
	 */
	public void setM_bShotClkAudio(boolean enable) {
		m_bShotClockAudio = enable;
	}

	/** isVibrationEnabled
	 * 
	 * @return true if vibration is enabled
	 */
	public boolean isM_bVibration() {
		return m_bVibration;
	}

	/** setVibrationEnabled (enable)
	 * 
	 * @param enable : true if vibration should be enabled throughout the application
	 */
	public void setM_bVibration(boolean enable) {
		m_bVibration = enable;
	}

	/** getTotalTimeouts
	 * 
	 * @return the total number of timeouts a team should receive per half
	 */
	public int getTotalTimeouts() {
		return m_nTimeouts;
	}

	/** setTotalTimeouts (timeouts)
	 * 
	 * @param timeouts : number of timeouts a team should receive per half
	 */
	public void setM_iTimeOuts(int timeouts) {
		m_nTimeouts = timeouts;
	}
	
	/** getShotClockDuration
	 * 
	 * @return time (milliseconds) of the duration of the shotclock
	 */
	public long getShotClockDuration() {
		return m_nShotClockDuration;
	}

	/** setShotClockDuration
	 * 
	 * @param duration : time in milliseconds the shotclock should be set to
	 */
	public void setShotClock(long duration) {
		m_nShotClockDuration = duration;
	}

	/** getGameClockDuration
	 * 
	 * @return time (milliseconds) of the GameClock's total set duration
	 */
	public long getGameClockDuration() {
		return m_nGameClockDuration;
	}

	/** setGameClockDuration
	 * 
	 * @param duration : time (milliseconds) the game clock should be set to for a single half
	 */
	public void setGameClock(long duration) {
		m_nGameClockDuration = duration;
	}
	
	
	public void setStaffType( Global.STAFF type ){
		assert ( type != null );
		m_eStaffType = type;
	}
	
	public Global.STAFF getStaffType(){
		return m_eStaffType;
	}
	
	public void setTeam( Global.TEAM team ){
		m_eTeam = team;
	}
	
	public Global.TEAM getTeam(){
		return m_eTeam;
	}
	
//	public void setBTM( BluetoothManager btm ){
//		m_BTM = btm;
//	}
//	
//	public BluetoothManager getBTM(){
//		return m_BTM;
//	}
	
	/** writeSettings
	 * 
	 * @param out
	 * @throws IOException
	 */
	public static void writeSettings(GameSettings settings, ObjectOutputStream out) throws IOException {
		     out.writeObject(settings);
	}  

	/** readSettings
	 * 
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static GameSettings readSettings(ObjectInputStream in) throws IOException, ClassNotFoundException {
		return (GameSettings) in.readObject();
	}
}
