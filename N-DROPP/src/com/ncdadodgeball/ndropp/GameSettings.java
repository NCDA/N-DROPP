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

/*	GameSettings
 * 	Oject describing the entire set of settings for this user/device
 */
public class GameSettings {

	private boolean m_bMute;
	private boolean m_bShotClockCountDown;
	private boolean m_bShotClockAudio;
	private boolean m_bVibration;
	private boolean m_bHasHalftime;
	private int m_nTimeouts;
	private long m_nShotClockDuration;	//shot clock in milliseconds
	private long m_nGameClockDuration;	//game clock in milliseconds

	/** GameSettings
	 * 	Create default settings
	 */
	public GameSettings(){
		m_bMute = false;
		m_bShotClockCountDown = false;
		m_bShotClockAudio = false;
		m_bVibration = false;
		m_bHasHalftime = true;
		m_nTimeouts = 2;
		m_nShotClockDuration = Clock.SECOND * 15;		//shot clock 15 seconds
		m_nGameClockDuration = Clock.MINUTE * 25;		//game clock at 25 minutes
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
	public long getShotClock() {
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
	public long getGameClock() {
		return m_nGameClockDuration;
	}

	/** setGameClockDuration
	 * 
	 * @param duration : time (milliseconds) the game clock should be set to for a single half
	 */
	public void setGameClock(long duration) {
		m_nGameClockDuration = duration;
	}
}
