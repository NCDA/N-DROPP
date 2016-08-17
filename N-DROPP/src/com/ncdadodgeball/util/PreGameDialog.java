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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;

public class PreGameDialog
{
	public static final int SCR_DIALOG = 0;
	public static final int HR_DIALOG = 1;
	
	private Activity parent;
	private int dialogType;
	private AlertDialog.Builder builder;
	
	public PreGameDialog(Activity activity, int dialogType){
		this.parent = activity;
		this.dialogType = dialogType;
		
		builder = new AlertDialog.Builder(activity);
		
		if(dialogType == SCR_DIALOG)
			createSCRDialog(builder);
		else
			createHRDialog(builder);
	}
	
	private void createSCRDialog(AlertDialog.Builder builder){
		
	}
	
	private void createHRDialog(AlertDialog.Builder builder){
		
	}
	
	public void show(){
		
	}
}
