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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.ncdadodgeball.ndropp.R;

/*	MainActivity
 * 	Class to set up the start of the application and introduce the main menu
 */
public class MainActivity extends Activity {
    
	ButtonListener btListener;
	Button btStart;
	Button btHistory;
	Button btInfo;
	Button btSettings;
	public static MainActivity sInstance;
	
    @Override
    /** onCreate
     * 	Set up the Main Menu interface
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        sInstance = this;
        
        //grab buttons and set listener
        btStart 	= (Button)findViewById(R.id.MAIN_btNewGame);
        btHistory 	= (Button)findViewById(R.id.MAIN_btHistory);
        btInfo		= (Button)findViewById(R.id.MAIN_btInfo);
        btSettings	= (Button)findViewById(R.id.MAIN_btSettings);
        btStart.setVisibility(View.VISIBLE);
        btHistory.setVisibility(View.VISIBLE);
        btInfo.setVisibility(View.VISIBLE);
        btSettings.setVisibility(View.VISIBLE);
        btListener = new ButtonListener();
        btStart.setOnClickListener(btListener);
        btHistory.setOnClickListener(btListener);
        btInfo.setOnClickListener(btListener);
        btSettings.setOnClickListener(btListener);
        
        GameSettings.loadSettings(this);
        
    }
    
    /** launchSCRActivity
     * 	Fires an intent to start the Shot Clock Referee Activity in front of this Activity
     */
    private void launchSCRActivity(){
    	Intent intent = new Intent(MainActivity.sInstance, SCRGameActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
    }
    
    /** launchHRActivity
     * 	Fires an intent to start the Head-Referee Activity in front of this Activity
     */
    private void launchHRActivity(){
    	Intent intent = new Intent(MainActivity.sInstance, HRGameActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
    }

    /*	ButtonListener
     * 	onClickListener for all GUI elements in this Activity
     */
    class ButtonListener implements OnClickListener{
    	public ButtonListener(){ super(); }

    	/** onClick
    	 * 	determine which GUI element was selected and initiate the appropriate event
    	 */
		public void onClick(View view) {
			//NEW GAME
			if(view.getId() == MainActivity.sInstance.btStart.getId()){
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.sInstance);
				builder.setTitle("New Game");
				builder.setMessage("Select shot clock ref or head ref view");
				builder.setCancelable(true);
				
				//cancel
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int id){
						dialog.cancel();
					}					
				});
				
				//shot clock referee option
				builder.setPositiveButton("Shot Clock Referee", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Log.D("SCR selected");
						MainActivity.sInstance.launchSCRActivity();
					}
				});
				
				//head referee option
				builder.setNeutralButton("Head Referee", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Log.D("HR selected");
						MainActivity.sInstance.launchHRActivity();
					}
				});
				
				//show the dialog
				AlertDialog dialog = builder.create();
				dialog.show();
			}
			
			//INFO
			if(view.getId() == MainActivity.sInstance.btInfo.getId()){
		
				//Rulebook - download/view
				if(DownloadManager.DownloadRulebook()){
					File fRulebook = new File(AppGlobals.EXTERNAL_DIR + "/" + AppGlobals.RULEBOOK_FILE);
					Uri path = Uri.fromFile(fRulebook);
	                Intent pdfViewIntent = new Intent(Intent.ACTION_VIEW);
	                pdfViewIntent.setDataAndType(path, "application/pdf");
	                pdfViewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	
	                try {
	                    startActivity(pdfViewIntent);
	                } 
	                catch (ActivityNotFoundException e) {
	                	Log.D("ERROR: No application to view PDF.");
	                    Toast.makeText(MainActivity.sInstance, "Error: No application exists on this device to view PDF", Toast.LENGTH_LONG).show();
	                }
				}
			}
		}    	
    }

	
//	 @Override
//    public void onPause(){
//    	super.onPause();
//    }
//    
//    @Override
//    public void onResume(){
//    	super.onResume();
//    }
//    
//    @Override
//    public void onDestroy(){
//    	super.onDestroy();
//    }
//    
//    @Override
//    public void onBackPressed(){
//    	this.finish();
//    }
}