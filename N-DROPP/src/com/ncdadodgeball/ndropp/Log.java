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

/*	Log
 *		Used to send log messages to the device's logcat with the N-DROPP tag.
 */
public class Log {
	
	private static final String TAG_DEBUG = "N-DROPP D";
	private static final String TAG_ERROR = "N-DROPP E";
	
	public static void D(String debugMessage){
		android.util.Log.d(TAG_DEBUG, " ");
		android.util.Log.d(TAG_DEBUG, "+++++++++++++++++++++++++++++++++++++++++++++++++++");
		android.util.Log.d(TAG_DEBUG, " ");
		android.util.Log.d(TAG_DEBUG, "<<<<<<<<<  " + debugMessage + "  >>>>>>>>");
		android.util.Log.d(TAG_DEBUG, " ");
		android.util.Log.d(TAG_DEBUG, "+++++++++++++++++++++++++++++++++++++++++++++++++++");
		android.util.Log.d(TAG_DEBUG, " ");
	}
	
	public static void E(String errMessage){
		android.util.Log.e(TAG_ERROR, "--------  " + errMessage + "  --------" );
	}
	

}
