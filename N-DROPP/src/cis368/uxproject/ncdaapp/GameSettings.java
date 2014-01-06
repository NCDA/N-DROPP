package cis368.uxproject.ncdaapp;

public class GameSettings {

	boolean m_bVolume;
	boolean m_bShotClkCountUp;
	boolean m_bShotClkAudio;
	boolean m_bVibration;
	boolean m_bHomeTeamLeft;
	int m_iTimeOuts;
	long m_lShotClock;	//shot clock in milliseconds
	long m_lGameClock;	//game clock in milliseconds

	public GameSettings(){
		m_bVolume = true;
		m_bShotClkCountUp = true;
		m_bShotClkAudio = false;
		m_bVibration = false;
		m_bHomeTeamLeft = true;
		m_iTimeOuts = 2;
		m_lShotClock = 15000;		//shot clock 15 seconds
		m_lGameClock = 1500000;		//game clock at 25 minutes
	}
	
	
	public boolean isM_bVolume() {
		return m_bVolume;
	}



	public void setM_bVolume(boolean m_bVolume) {
		this.m_bVolume = m_bVolume;
	}



	public boolean isM_bShotClkCountUp() {
		return m_bShotClkCountUp;
	}



	public void setM_bShotClkCountUp(boolean m_bShotClkCountUp) {
		this.m_bShotClkCountUp = m_bShotClkCountUp;
	}



	public boolean isM_bShotClkAudio() {
		return m_bShotClkAudio;
	}



	public void setM_bShotClkAudio(boolean m_bShotClkAudio) {
		this.m_bShotClkAudio = m_bShotClkAudio;
	}



	public boolean isM_bVibration() {
		return m_bVibration;
	}



	public void setM_bVibration(boolean m_bVibration) {
		this.m_bVibration = m_bVibration;
	}



	public boolean isM_bHomeTeamLeft() {
		return m_bHomeTeamLeft;
	}



	public void setM_bHomeTeamLeft(boolean homeTeamLeft) {
		m_bHomeTeamLeft = homeTeamLeft;
	}



	public int getM_iTimeOuts() {
		return m_iTimeOuts;
	}



	public void setM_iTimeOuts(int timeOuts) {
		m_iTimeOuts = timeOuts;
	}
	
	
	public long getShotClock() {
		return m_lShotClock;
	}


	public void setShotClock(long milliseconds) {
		m_lShotClock = milliseconds;
	}


	public long getGameClock() {
		return m_lGameClock;
	}


	public void setGameClock(long milliseconds) {
		m_lGameClock = milliseconds;
	}

}
