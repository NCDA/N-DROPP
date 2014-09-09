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

import com.ncdadodgeball.ndropp.GameActivity;
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

	//Global constants
//	public static enum TEAM { NONE, HOME, AWAY };
	public static enum STAFF { NONE, HR, HOME_SCR, AWAY_SCR, HYBRID_HR };		//TODO -- assistant ref, commentator, etc.
		
	//Class member vars
	private static GameSettings sInstance = null;
	private static final long serialVersionUID = 0xD0D83BA11L;
	
	private boolean m_bMute;
	private boolean m_bShotClockCountDown;
	private boolean m_bShotClockAudio;
	private boolean m_bVibration;
	private boolean m_bHasHalftime;
	private int m_nTimeouts;
	private long m_nShotClockDuration;	//shot clock in milliseconds
	private long m_nGameClockDuration;	//game clock in milliseconds
	private STAFF	m_eStaffType;
//	private TEAM	m_eTeam;
	

	/** GameSettings
	 * 	Create default settings
	 */
	private GameSettings(){
		resetToDefaults();
	}
	
	public static GameSettings instance(){
		if( sInstance == null )
			sInstance = new GameSettings();
		return sInstance;
	}
	
	public void init(GameActivity activity){
		if( activity != null )
			loadSettings(activity);
	}
	
	public void resetToDefaults(){
		m_bMute = false;
		m_bShotClockCountDown = false;
		m_bShotClockAudio = false;
		m_bVibration = false;
		m_bHasHalftime = true;
		m_nTimeouts = 2;
		m_nShotClockDuration = Clock.SECOND * 15;		//shot clock 15 seconds
		m_nGameClockDuration = Clock.MINUTE * 50;		//game clock at 50 minutes
		
		m_eStaffType = STAFF.NONE;
//		m_eTeam = TEAM.NONE;
	}
	
	
	/**	loadSettings
	 * 
	 * @param ctx - application context
	 * 
	 * Load an existing "game_settings" file from the application storage or create a new
	 * settings object if it doesn't exist/is corrupt.  The GameSettings object is loaded
	 * into AppGlobals.mGameSettings.
	 */
	public boolean loadSettings(Activity parent){
		//load settings from file
        File fSettings = new File( Global.getInternalDir(parent) + "/" + parent.getString(R.string.file_settings) );
        if(fSettings.exists()){
        	try{
        		readSettings(new ObjectInputStream( new FileInputStream(fSettings)));
        		Log.D("Settings read from app data");
        		return true;
        	}
        	catch(Exception e){
        		Toast.makeText(parent, "Settings data is corrupt. Resetting to defaults", Toast.LENGTH_LONG).show();
        	}
        }
        return false;
	}
	
	
	public boolean saveSettings(Activity parent){
		//delete existing file
        File fSettings = new File( Global.getInternalDir(parent) + "/" + parent.getString(R.string.file_settings) );
        if(fSettings.exists()){
        	try{
        		writeSettings(sInstance, new ObjectOutputStream( new FileOutputStream(fSettings)));
        		Log.D("Settings saved to app data");
        		return true;
        	}
        	catch(Exception e){
        		Toast.makeText(parent, "Could not save settings", Toast.LENGTH_LONG).show();
        	}
        }
        return false;
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
	
	
	public void setStaffType( GameSettings.STAFF type ){
		assert ( type != null );
		m_eStaffType = type;
	}
	
	public GameSettings.STAFF getStaffType(){
		return m_eStaffType;
	}
	
//	public void setTeam( GameSettings.TEAM team ){
//		m_eTeam = team;
//	}
//	
//	public GameSettings.TEAM getTeam(){
//		return m_eTeam;
//	}
	
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
	private void writeSettings(GameSettings settings, ObjectOutputStream out) throws IOException {
		out.writeObject(settings);
	}  

	/** readSettings
	 * 
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readSettings(ObjectInputStream in) throws IOException, ClassNotFoundException {
		sInstance = (GameSettings) in.readObject();
	}
}
