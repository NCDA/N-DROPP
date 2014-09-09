package com.ncdadodgeball.util;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

/*
   * Copyright (C) 2008 The Android Open Source Project
   *
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   *      http://www.apache.org/licenses/LICENSE-2.0
   *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
 
//Schedule a countdown until a time in the future, with regular notifications on intervals along the way. Example of showing a 30 second countdown in a text field:
// new CountdownTimer(30000, 1000) {
//
//     public void onTick(long millisUntilFinished) {
//         mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
//     }
//
//     public void onFinish() {
//         mTextField.setText("done!");
//     }
//  }.start();
 
//The calls to onTick(long) are synchronized to this object so that one call to onTick(long) won't ever occur before the previous callback is complete. This is only relevant when the implementation of onTick(long) takes an amount of time to execute that is significant compared to the countdown interval.
 
 public abstract class CustomCountdownTimer {
    
//Millis since epoch when alarm should stop.
 
     private final long mMillisInFuture;

    
//The interval in millis that the user receives callbacks
 
     private final long mCountdownInterval;
 
     private long mStopTimeInFuture;
     
     private boolean mbCancelled;
    
//Parameters:
//millisInFuture The number of millis in the future from the call to start() until the countdown is done and onFinish() is called.
//countDownInterval The interval along the way to receive onTick(long) callbacks.
 
     public CustomCountdownTimer(long millisInFuture, long countDownInterval) {
         mMillisInFuture = millisInFuture;
         mCountdownInterval = countDownInterval;
         mbCancelled = false;
     }
    
//Cancel the countdown.
 
     public final void cancel() {
         mHandler.removeMessages(MSG);
    	 mbCancelled = true;
     }

    
//Start the countdown.
 
     public synchronized final CustomCountdownTimer start() {
         if (mMillisInFuture <= 0) {
             onFinish();
             return this;
         }
         mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
         Message msg = mHandler.obtainMessage(MSG);
         boolean bstatus = mHandler.sendMessage(msg);
//         mHandler.sendMessage(mHandler.obtainMessage(MSG));
         return this;
     }


    
//Callback fired on regular interval.
//Parameters:
//millisUntilFinished The amount of time until finished.
 
     public abstract void onTick(long millisUntilFinished);

    
//Callback fired when the time is up.

    public abstract void onFinish();


    private static final int MSG = 1;


    // handles counting down
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            synchronized (CustomCountdownTimer.this) {
//            	if( mbCancelled )
//            		mHandler.getLooper().quit();
            	
                final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();

                if (millisLeft <= 0) {
                    onFinish();
                } else if (millisLeft < mCountdownInterval) {
                    // no tick, just delay until done
                    sendMessageDelayed(obtainMessage(MSG), millisLeft);
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    onTick(millisLeft);

                    // take into account user's onTick taking time to execute
                    long delay = lastTickStart + mCountdownInterval - SystemClock.elapsedRealtime();

                    // special case: user's onTick took more than interval to
                    // complete, skip to next interval
                    while (delay < 0) delay += mCountdownInterval;

                    sendMessageDelayed(obtainMessage(MSG), delay);
                }
            }
        }
    };
}


























































///**************************************************************************************************
// * _____   __     _______________________________________ 
// * ___  | / /     ___  __ \__  __ \_  __ \__  __ \__  __ \
// * __   |/ /________  / / /_  /_/ /  / / /_  /_/ /_  /_/ /
// * _  /|  /_/_____/  /_/ /_  _, _// /_/ /_  ____/_  ____/
// * /_/ |_/        /_____/ /_/ |_| \____/ /_/     /_/
// * 
// * National Collegiate Dodgeball Association (NCDA)
// * NCDA - Dodgeball Referee Officiating Application
// * http://www.ncdadodgeball.com
// * Copyright 2014. All Rights Reserved.
// *************************************************************************************************/
//package com.ncdadodgeball.util;
//
//import java.util.Date;
//
//import android.os.Handler;
//
//public abstract class CustomCountdownTimer {
//	
//	private long m_lDuration;
//	private long m_lTick;
//	private Handler m_handler;
//	private long m_lStart;
//	private TickRunnable m_tickRun;
//	
//	
//	public CustomCountdownTimer(long duration, long tick) {
//		m_lDuration = duration;
//		m_lTick = tick;
//		m_tickRun = null;
//	}
//	
//	public void start(){
//		m_handler = new Handler();
//		m_tickRun = new TickRunnable(this);
//		m_lStart = new Date().getTime();
//		boolean success = m_handler.postDelayed(m_tickRun, m_lTick);
//		if ( !success )
//			Log.E("NO SUCCESS");
//	}
//	
//	public void cancel(){
//		m_handler.removeCallbacks(m_tickRun);
//		m_handler = null;
//	}
//	
//	class TickRunnable implements Runnable
//	{
//		private CustomCountdownTimer m_timer;
//		public TickRunnable(CustomCountdownTimer timer){
//			m_timer = timer;
//		}
//
//		public void run() {
//			long lTimeRemaining = m_lDuration - (new Date().getTime()-m_lStart);
//			if( lTimeRemaining <= 0 )
//				m_timer.onFinish();
//			else{
//				m_timer.onTick( lTimeRemaining );
//				m_handler.postDelayed(m_tickRun, m_lTick);
//			}
//		}
//	}
//	
//	public abstract void onTick(long tte);
//	public abstract void onFinish();
//}
