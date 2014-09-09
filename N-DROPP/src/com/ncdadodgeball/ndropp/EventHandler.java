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

import android.app.Activity;
import android.os.Looper;

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
		//what is event
		if( e.getType() == Event.TYPE.GC_PAUSE_RESUME ){
			onGameClockStartPauseResume(e);
		}
	}
	
	public void onGameStarted(){
		
	}
	
	
	private void onGameClockStartPauseResume(Event e){
		//update model
		GameModel.instance().getGameClock().onStartPauseResume();
		
		//TODO -- record in database
		
		// do we need to tell anyone about this event
		if( BluetoothManager.instance().isConnectedToOtherDevices() ){
			if( GameModel.instance().getCurrentStaffMember() == GameSettings.STAFF.HR ){				
				//grab data from model and update event
				e.setStringValue(GameModel.instance().getGameClock().getMinutesTimeString());
				
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
	
}