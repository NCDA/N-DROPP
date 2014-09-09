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
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ncdadodgeball.comm.DownloadManager;
import com.ncdadodgeball.util.GameSettings;
import com.ncdadodgeball.util.Log;

/*	MainActivity
 * 	Class to set up the start of the application and introduce the main menu
 */
public class MainActivity extends Activity implements View.OnClickListener {//DialogInterface.OnCancelListener, DialogInterface.OnClickListener {
    
	private Button btStart;
	private Button btHistory;
	private Button btInfo;
	private Button btSettings;
	
	public static MainActivity sInstance;
	
	
    @Override
    /** onCreate
     * 	Set up the Main Menu interface
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sInstance = this;
        
        //grab buttons and set listener
        btStart 	= (Button)findViewById(R.id.MAIN_bt_new_game);
        btHistory 	= (Button)findViewById(R.id.MAIN_bt_history);
        btInfo		= (Button)findViewById(R.id.MAIN_bt_info);
        btSettings	= (Button)findViewById(R.id.MAIN_bt_settings);
        btStart.setVisibility(View.VISIBLE);
        btHistory.setVisibility(View.VISIBLE);
        btInfo.setVisibility(View.VISIBLE);
        btSettings.setVisibility(View.VISIBLE);
        btStart.setOnClickListener(this);
        btHistory.setOnClickListener(this);
        btInfo.setOnClickListener(this);
        btSettings.setOnClickListener(this);
    }
    
    /** launchSCRActivity
     * 	Fires an intent to start the Shot Clock Referee Activity in front of this Activity
     */
    private void launchSCRActivity(){
    	Intent intent = new Intent(this, SCRGameActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
    }
    
    /** launchHRActivity
     * 	Fires an intent to start the Head-Referee Activity in front of this Activity
     */
    private void launchHRActivity(){
    	Intent intent = new Intent(this, HRGameActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
    }
    
    private void launchPreGameSetupActivity(){
    	Intent intent = new Intent(this, PreGameSetupActivity.class);
    	intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    	startActivity(intent);
    }


/*
    //TODO -- make this into its own pretty dialog class or something
    //TODO - also, based on selection, have pre-game settings show up too
    private void createNewGameDialog(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.sInstance);
		builder.setTitle("New Game");
		builder.setMessage("Select shot clock ref or head ref view");
		builder.setCancelable(true);
		
		//cancel
		builder.setNegativeButton("Cancel", this);
		
		//shot clock referee option
		builder.setPositiveButton("Shot Clock Referee", this);
		
		//head referee option
		builder.setNeutralButton("Head Referee", this);
		
		//show the dialog
		mDialog = builder.create();
		mDialog.show();
    }
    

	public void onClick(DialogInterface dialog, int button) {
		switch(button){
			case(DialogInterface.BUTTON_POSITIVE):
				Log.D("SCR selected");
				mSettings.setStaffType(Global.STAFF.SCR);
				launchSCRActivity();
//				initBluetooth(BluetoothManager.eSocketType.CLIENT);
				
				// TODO -- team selection
//				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.sInstance);
//				builder.setTitle("Team select");
//				builder.setCancelable(false);
//				builder.setNegativeButton("Home", new DialogInterface.OnClickListener(){
//					public void onClick(DialogInterface dialog, int which) {
//						mSettings.setTeam(AppGlobals.TEAM.HOME);
//						MainActivity.sInstance.launchSCRActivity();
//					}
//				});
//				builder.setPositiveButton("Away", new DialogInterface.OnClickListener() {
//					
//					public void onClick(DialogInterface dialog, int which) {
//						mSettings.setTeam(AppGlobals.TEAM.AWAY);
//						MainActivity.sInstance.launchSCRActivity();
//					}
//				});
//				
//				Dialog teamDialog = builder.create();
//				teamDialog.setCanceledOnTouchOutside(false);
//				teamDialog.show();
				break;
			case(DialogInterface.BUTTON_NEGATIVE):
				mDialog.cancel();
				break;
			case(DialogInterface.BUTTON_NEUTRAL):
				Log.D("HR selected");
				mSettings.setStaffType( Global.STAFF.HR );
				launchHRActivity();
//				initBluetooth(BluetoothManager.eSocketType.SERVER);
				break;
		}
	}

    
	public void onCancel(DialogInterface dialog) {
		dialog.cancel();
	}
*/
 
	public void onClick(View view) {
		//NEW GAME
		if(view.getId() == MainActivity.sInstance.btStart.getId()){
//			ViewPropertyAnimator anim = ((LinearLayout)findViewById(R.id.MAIN_layout_buttons)).animate();
//			anim.alpha(0f);
//			anim.setDuration(2000);
//			anim.setListener(null);
//			createNewGameDialog();
			launchPreGameSetupActivity();
		}
		
		//INFO
		if(view.getId() == MainActivity.sInstance.btInfo.getId()){
	
			//Rulebook - download/view
			if(DownloadManager.DownloadRulebook()){
				File fRulebook = new File(Global.getExternalDir(MainActivity.sInstance) + "/" + getString(R.string.file_rulebook));
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
    
//    private void initBluetooth(BluetoothManager.eSocketType socktype){
//		BluetoothManager btm = new BluetoothManager(MainActivity.sInstance, socktype);
//		mSettings.setBTM(btm);
//    }

	
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
//    	if(mSettings.getBTM() != null){
//    		mSettings.getBTM().destroy();
//    		mSettings.setBTM(null);
//    	}
//    	super.onDestroy();
//    }
    
//    
//    @Override
//    public void onBackPressed(){
//    	this.finish();
//    }
}