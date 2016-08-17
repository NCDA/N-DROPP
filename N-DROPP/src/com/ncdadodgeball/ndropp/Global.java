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

import com.ncdadodgeball.comm.BluetoothManager;
import com.ncdadodgeball.util.GameSettings;

import android.app.Activity;

/*	AppGlobals
 * 	Class specifically for the purpose of declaring Application-wide global variables and constants
 */
public class Global {
	
	//INTEGER CONSTANTS
	public static final int	NUM_DL_BUFF_SIZE 			= 51200;		//download buff size (50K)
	public static final int NUM_MAX_STARTING_PLAYERS	= 15;
	public static final int NUM_MAX_OVERTIME_PLAYERS	= 6;
	
	//DOUBLE CONSTANTS
	public static final double LOGO_ASPECT_RATIO		= 0.60;
	public static final double NAV_IMG_HEIGHT_PERCENT	= 0.15;		//navigation images height percentage
	public static final double SCR_GRID_WIDTH_PERCENT	= 0.45;
	public static final double SCR_GRID_HEIGHT_PERCENT	= 0.25;
	public static final double SCR_LOGO_WIDTH_PERCENT 	= 0.40;
	public static final double HR_GRID_WIDTH_PERCENT	= 0.33;
	public static final double HR_GRID_HEIGHT_PERCENT	= 0.20;
	public static final double HR_LOGO_WIDTH_PERCENT	= 0.40;
	
	public static String getExternalDir(Activity parent){
		return parent.getExternalFilesDir(null).getAbsolutePath();
	}
	
	public static String getInternalDir(Activity parent){
		return parent.getFilesDir().getAbsolutePath();
	}
}
