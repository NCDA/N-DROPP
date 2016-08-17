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

import java.util.concurrent.ConcurrentLinkedQueue;

import com.ncdadodgeball.comm.BluetoothManager;
import com.ncdadodgeball.util.Event;
import com.ncdadodgeball.util.GameClock;
import com.ncdadodgeball.util.GameSettings;
import com.ncdadodgeball.util.Log;


/**
 * 	Given a single event, create all subevents from that event and handle
 * 	all of them. For example, the event to Start the game should handle the
 * 	sub-events of starting the game clock and starting both shot clocks.
 * 
 * 	CREATING EVENT:
 * 1.	Understand event
 * 2.	Notify the model of the event
 * 3.	Understand the model and log the event (database)
 * 3.	If connected to other devices, send event to correct people
 * 
 * RECEIVING EVENT
 * 1.	Gather event info.
 * 2.	Update model
 * 3.	Does this event trigger other events?
 * 
 */
public class EventHandler implements Runnable
{
	private static EventHandler sInstance = null;
	
	private ConcurrentLinkedQueue<Event> m_qEvents = new ConcurrentLinkedQueue<Event>();
	
	private EventHandler(){
		
	}
	
	public static EventHandler instance(){
		if( sInstance == null )
			sInstance = new EventHandler();
		return sInstance;
	}
	
	public void init(){
		new Thread(sInstance).start();
	}
	
	public void run(){
//		Looper.prepare();
		while(true){
			boolean bProcessing = false;
			synchronized(m_qEvents){
				try {
					if( !bProcessing ){
						m_qEvents.wait();
						bProcessing = true;
						Event e = m_qEvents.poll();
						
						if( e == null ){
							bProcessing = false;
							continue;
						}
						processEvent(e);
						bProcessing = false;
					}
				} catch (InterruptedException e) {
					Log.D("EVENT QUEUE ERR");
					Log.E(e.getMessage());
				}
			}
		}
	}
	
	public synchronized void postEvent(Event e){
		synchronized(m_qEvents){
			m_qEvents.add(e);
			m_qEvents.notify();
		}
	}
	
	private void processEvent(Event e){
		
		switch ( e.getType() ){
			case GC_PAUSE_RESUME:
				onGameClockStartPauseResume(e);
				break;
				
			case GC_EXPIRED:
				onGameClockExpired(e);
				break;
				
			case SC_PAUSE_RESUME_RESET:
				onShotClockPauseResumeReset(e);
				break;
				
			case SC_START_RESTART_RESET:
				onShotClockStartRestartReset(e);
				break;
				
			case SC_EXPIRED:
				onShotClockExpired(e);
				break;
				
			case GAME_START:
				onGameStarted(e);
				break;
				
			case GAME_PENALTY:
				onPenalty(e);
				break;
				
			case GAME_MATCH_COMPLETE:
				onMatchCompleted(e);
				break;
				
			case PLAYER_ADD:
				onAddPlayer(e);
				break;
				
			case PLAYER_REMOVE:
				onRemovePlayer(e);
				break;
				
			case TEAM_TIMEOUT:
				onTimeoutCalled(e);
				break;
				
			case OPEN_SETTINGS:
				launchSettingsMenu();
				break;
				
			case OPEN_RULEBOOK:
				launchRulebook();
				break;
				
			case NONE:
			default:
		}
	}
	
	public void onGameStarted(Event e){
		
	}
	
	
	private void onGameClockStartPauseResume(Event e){
		//update model
		GameModel.instance().getGameClock().onStartPauseResume();
		
		//if stopped game clock, update shot clocks
		if( !GameModel.instance().getGameClock().isRunning() ){
			GameModel.instance().getAwayShotClock().pauseClock();
			GameModel.instance().getHomeShotClock().pauseClock();
		}
		
		//did we throw this event?
		boolean bInitiate = e.getSender() == GameModel.instance().getCurrentStaffMember();
		
		//if we received this message from another device, sync the time
		if( !bInitiate ){
			if( GameModel.instance().getGameClock().isRunning() )
				GameModel.instance().getGameClock().setTime(e.getLongValue());
		}

		// do we need to tell anyone about this event
		else if( BluetoothManager.instance().isConnectedToOtherDevices() ){
			if( GameModel.instance().getCurrentStaffMember() == GameSettings.STAFF.HR ){				
				//grab data from model and update event
				e.setLongValue(GameModel.instance().getGameClock().getTime());
				
				//send 1 event to Away-SCR
				//TODO
				//e.setReceiver(GameSettings.STAFF.AWAY_SCR);
				//BluetoothManager.instance().postEvent(e);
				
				//send 1 event to Home-SCR
				e.setReceiver(GameSettings.STAFF.HOME_SCR);
				BluetoothManager.instance().postEvent(e);
			}
		}
	}
	
	private void onGameClockExpired(Event e){
		
	}
	
	private void onShotClockPauseResumeReset(Event e){
		//update model
		if( e.getSender() == GameSettings.STAFF.AWAY_SCR )
			GameModel.instance().getAwayShotClock().onPauseResumeReset();
		else if( e.getSender() == GameSettings.STAFF.HOME_SCR )
			GameModel.instance().getHomeShotClock().onPauseResumeReset();
				
		//did we throw this event?
		boolean bInitiate = e.getSender() == GameModel.instance().getCurrentStaffMember();
		
		// do we need to tell anyone about this event?
		if( BluetoothManager.instance().isConnectedToOtherDevices() ){
			
			// TODO -- if we're head ref, send to the opposing SCR
			if( !bInitiate && GameModel.instance().getCurrentStaffMember() == GameSettings.STAFF.HR){
				
			}
			// if we're the initiator of the event, send to HR
			else if( bInitiate ){	
				long value = 0;
				if( e.getSender() == GameSettings.STAFF.AWAY_SCR )
					value = GameModel.instance().getAwayShotClock().getTime();
				else if( e.getSender() == GameSettings.STAFF.HOME_SCR )
					value = GameModel.instance().getHomeShotClock().getTime();
				
				//grab data from model and update event
				e.setLongValue(value);

				//send event to HR
				e.setReceiver(GameSettings.STAFF.HR);
				BluetoothManager.instance().postEvent(e);
			}
		}
	}
	
	private void onShotClockStartRestartReset(Event e){
		//don't start the clock if the game clock hasn't started
		if( GameModel.instance().getGameClock().getCurrentState() == GameClock.ClockState.PausedTop &&
				!BluetoothManager.instance().isConnectedToOtherDevices() )
			return;
		
		//update model
		if( e.getSender() == GameSettings.STAFF.AWAY_SCR )
			GameModel.instance().getAwayShotClock().onResetStartRestart();
		else if( e.getSender() == GameSettings.STAFF.HOME_SCR )
			GameModel.instance().getHomeShotClock().onResetStartRestart();

		//did we throw this event?
		boolean bInitiate = e.getSender() == GameModel.instance().getCurrentStaffMember();
		
		// do we need to tell anyone about this event?
		if( BluetoothManager.instance().isConnectedToOtherDevices() ){

			// TODO -- if we're head ref, send to the opposing SCR
			if( !bInitiate && GameModel.instance().getCurrentStaffMember() == GameSettings.STAFF.HR){
				
			}
			// if we're the initiator of the event, send to HR
			else if( bInitiate ){	
				long value = 0;
				if( e.getSender() == GameSettings.STAFF.AWAY_SCR )
					value = GameModel.instance().getAwayShotClock().getTime();
				else if( e.getSender() == GameSettings.STAFF.HOME_SCR )
					value = GameModel.instance().getHomeShotClock().getTime();

				//grab data from model and update event
				e.setLongValue(value);

				//send event to HR
				e.setReceiver(GameSettings.STAFF.HR);
				BluetoothManager.instance().postEvent(e);
			}
		}
		
		//if we threw the event and we're not connected to anyone, we have local game
		// so we need to start the game clock if it hasn't started yet
		else if( bInitiate ){
			if( GameModel.instance().getGameClock().getCurrentState() == GameClock.ClockState.PausedTop )
				GameModel.instance().getGameClock().onStartPauseResume();
		}
	}
	
	private void onShotClockExpired(Event e){
		//stop all clocks
	}
	
	private void onAddPlayer(Event e){
		//update model
		if( e.getSender() == GameSettings.STAFF.HOME_SCR )
			GameModel.instance().addHomePlayer();
		else if( e.getSender() == GameSettings.STAFF.AWAY_SCR )
			GameModel.instance().addAwayPlayer();
		else
			return;	//TODO -- error
		
		boolean bInitiate = e.getSender() == GameModel.instance().getCurrentStaffMember();
		
		//do we need to tell anyone about this event?
		if( bInitiate && BluetoothManager.instance().isConnectedToOtherDevices() ){
			//send data to HR
			e.setReceiver(GameSettings.STAFF.HR);
			BluetoothManager.instance().postEvent(e);
		}
	}
	
	private void onRemovePlayer(Event e){
		boolean bEliminated = false;
		
		//update model
		if( e.getSender() == GameSettings.STAFF.HOME_SCR )
			bEliminated = GameModel.instance().removeHomePlayer();
		else if( e.getSender() == GameSettings.STAFF.AWAY_SCR )
			bEliminated = GameModel.instance().removeAwayPlayer();
		else
			return; //TODO -- error
		
		if( bEliminated ){
			postEvent( new Event( 
								Event.TYPE.GAME_MATCH_COMPLETE, 
								GameModel.instance().getCurrentStaffMember(), 
								GameSettings.STAFF.NONE, 
								null )
					);
		}
		
		boolean bInitiate = e.getSender() == GameModel.instance().getCurrentStaffMember();
		
		//do we need to tell anyone about this event?
		if( bInitiate && BluetoothManager.instance().isConnectedToOtherDevices() ){
			//send data to HR
			e.setReceiver(GameSettings.STAFF.HR);
			BluetoothManager.instance().postEvent(e);
		}
	}
	
	private void onTimeoutCalled(Event e){
		//stop all clocks
		//adjust team's timeout count if they have timeouts left
	}
	
	private void onMatchCompleted(Event e){
		//stop all clocks (timeout)
		//set back shot clocks
		//give a team a point
		//reset players on court
	}
	
	private void onPenalty(Event e){
		//stop all clocks (timeout)
		//display card
	}
	
	private void launchSettingsMenu(){
		//only launch if game clock is stopped
	}
	
	private void launchRulebook(){
		//only launch if game clock is stopped
	}
}