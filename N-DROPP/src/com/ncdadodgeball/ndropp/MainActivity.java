/**************************************************************************************************
 * 
 * _____   __     _______________________________________ 
 * ___  | / /     ___  __ \__  __ \_  __ \__  __ \__  __ \
 * __   |/ /________  / / /_  /_/ /  / / /_  /_/ /_  /_/ /
 * _  /|  /_/_____/  /_/ /_  _, _// /_/ /_  ____/_  ____/		  ASCII ART:
 * /_/ |_/        /_____/ /_/ |_| \____/ /_/     /_/      		  http://patorjk.com/software/taag/
 * 
 * 
 * National Collegiate Dodgeball Association (NCDA)
 * NCDA - Dodgeball Referee Officiating Application
 * http://www.ncdadodgeball.com
 * Copyright 2014. All Rights Reserved.
 *************************************************************************************************/

package com.ncdadodgeball.ndropp;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import com.ncdadodgeball.ndropp.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public static final String PACKAGE = "cis368.uxproject.ncdaapp";
	public static final String EXTERNAL_DIR = "/mnt/sdcard/Android/data/";
	public static final String RULEBOOK_FILE = "NCDA_rulebook.pdf";
	public static final String RULEBOOK_URL = "http://www.ncdadodgeball.com/rulebook/ncda-rules.pdf";
	public static final int BUFFER = 51200;		//50K
    
	ButtonListener btListener;
	Button btStart;
	Button btHistory;
	Button btInfo;
	Button btSettings;
	public static MainActivity sInstance;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        sInstance = this;
        
        //grab buttons and set listener
        btStart 	= (Button)findViewById(R.id.btNewGame);
        btHistory 	= (Button)findViewById(R.id.btHistory);
        btInfo		= (Button)findViewById(R.id.btInfo);
        btSettings	= (Button)findViewById(R.id.btSettings);
        btStart.setVisibility(View.VISIBLE);
        btHistory.setVisibility(View.VISIBLE);
        btInfo.setVisibility(View.VISIBLE);
        btSettings.setVisibility(View.VISIBLE);
        btListener = new ButtonListener();
        btStart.setOnClickListener(btListener);
        btHistory.setOnClickListener(btListener);
        btInfo.setOnClickListener(btListener);
        btSettings.setOnClickListener(btListener);
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    }
    
    public ButtonListener getBtListener() {
		return btListener;
	}

	public Button getBtStart() {
		return btStart;
	}

	public Button getBtHistory() {
		return btHistory;
	}

	public Button getBtInfo() {
		return btInfo;
	}

	public Button getBtSettings() {
		return btSettings;
	}

	public static MainActivity getsInstance() {
		return sInstance;
	}

    class ButtonListener implements OnClickListener{
    	
    	public ButtonListener sInstance;
    	boolean bTryAgain;
    	public boolean bdialog;
    	
    	public ButtonListener(){
    		super();
    		sInstance = this;
    		bTryAgain = true;
    		bdialog = false;
    	}
    	
    	public void setTryAgainOptionSelected(boolean b){
    		bTryAgain = b;
    	}

		public void onClick(View view) {
			
			//NEW GAME
			if(view.getId() == MainActivity.sInstance.getBtStart().getId()){
				Intent intent = new Intent(MainActivity.sInstance, GameActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
			
			//INFO
			if(view.getId() == MainActivity.sInstance.getBtInfo().getId()){
				
				File fPackageDir = new File(EXTERNAL_DIR + PACKAGE);
				if(!fPackageDir.exists())
					fPackageDir.mkdirs();
				fPackageDir = null;
								
				File fRulebook = new File(EXTERNAL_DIR + PACKAGE + "/" + RULEBOOK_FILE);
				
				//Download the rulebook if it's not on the device
				if(!fRulebook.exists()){
					URL url = null;
					HttpURLConnection socket= null;
					boolean bTryAgain = true;
					
					while(bTryAgain){
						while(bdialog);
						try {
							//HttpURLConnection object provides poor data communication. Wi-fi==corrupt file.  Data==perfect file.
							url = new URL(RULEBOOK_URL);
							socket = (HttpURLConnection) url.openConnection();
							socket.setInstanceFollowRedirects(true);
							socket.setDoInput(true);
							socket.connect();
							InputStream istream = new BufferedInputStream(socket.getInputStream());
							OutputStream ostream = new FileOutputStream(fRulebook.getAbsolutePath(), false);
							byte [] input = new byte[BUFFER];
							int numBytes = istream.read(input, 0, BUFFER); 
							while(numBytes != -1){
								ostream.write(input, 0, numBytes);
								numBytes = istream.read(input, 0, BUFFER); 
							}
							
							bTryAgain = false;
							istream.close();
							ostream.close();
						}
						//catch no internet connection. need better way to get user input
						catch (UnknownHostException uhe){
							System.exit(0);
//							AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.sInstance);
//							alert.setMessage("There was an error connecting to the internet. Make sure to enable internet connection on your device.");
//							alert.setCancelable(true);
//							alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog, int id) {
//									dialog.cancel();
////									sInstance.setTryAgainOptionSelected(false);
//								}
//							});
//							alert.setPositiveButton("Try Again", new DialogInterface.OnClickListener(){
//								public void onClick(DialogInterface dialog, int id){
//									dialog.cancel();
////									sInstance.setTryAgainOptionSelected(true);
//								}
//							});
//							AlertDialog dialog = alert.create();
//							dialog.show();
//							bdialog = true;
////							while(dialog.isShowing());
						}
						catch (Exception e) {
							throw new RuntimeException("An error occured. Sorry :( ");
						}
						finally{
							socket.disconnect();
						}
					}
				}
				
				
				//user could have downloaded the file or quit. if downloaded, display file
				if(fRulebook.exists()){
					Uri path = Uri.fromFile(fRulebook);
	                Intent pdfViewIntent = new Intent(Intent.ACTION_VIEW);
	                pdfViewIntent.setDataAndType(path, "application/pdf");
	                pdfViewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	
	                try {
	                    startActivity(pdfViewIntent);
	                } 
	                catch (ActivityNotFoundException e) {
	                    Toast.makeText(MainActivity.sInstance, "Error: No application exists on this device to view PDF", Toast.LENGTH_LONG).show();
	                }
				}
			}
		}    	
    }
}