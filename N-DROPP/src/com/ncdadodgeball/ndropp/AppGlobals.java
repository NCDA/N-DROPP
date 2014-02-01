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

/*	AppGlobals
 * 	Class specifically for the purpose of declaring Application-wide global variables and constants
 */
public class AppGlobals {
	
	//STRING CONSTANTS
	public static final String PACKAGE 			= "com.ncdadodgeball.ndropp";
	public static final String EXTERNAL_DIR 	= "/mnt/sdcard/Android/data/";
	public static final String RULEBOOK_FILE 	= "NCDA_rulebook.pdf";
	public static final String NCDA_URL 		= "http://www.ncdadodgeball.com";
	public static final String RULEBOOK_URL 	= "http://www.ncdadodgeball.com/rulebook/ncda-rules.pdf";
	
	public static final String SIL_BLACK	= "silhouette_black";
	public static final String SIL_BLUE		= "silhouette_blue";
	public static final String SIL_BROWN	= "silhouette_brown";
	public static final String SIL_GREEN	= "silhouette_green";
	public static final String SIL_GREY 	= "silhouette_grey";
	public static final String SIL_LIME 	= "silhouette_lime";
	public static final String SIL_PURPLE 	= "silhouette_purple";
	public static final String SIL_ORANGE 	= "silhouette_orange";
	public static final String SIL_RED 		= "silhouette_red";
	public static final String SIL_SKY_BLUE = "silhouette_sky_blue";
	public static final String SIL_WHITE 	= "silhouette_white";
	public static final String SIL_YELLOW	= "silhouette_yellow";
	
	
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
	
	//GLOBAL OBJECTS
	public static GameSettings gGameSettings = null;
}
