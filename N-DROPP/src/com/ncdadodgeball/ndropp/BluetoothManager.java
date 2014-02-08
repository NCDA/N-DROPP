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

import java.util.Set;

import android.app.Activity;
import android.bluetooth.*;
import android.content.Intent;

/*	BluetoothManager
 * 
 * 	http://developer.android.com/guide/topics/connectivity/bluetooth.html
 */
public class BluetoothManager extends Activity implements Runnable{

	BluetoothAdapter 		mAdapter;
	BluetoothDevice			mThisDevice;
	Set<BluetoothDevice>	mAttachedDevices;
	
	public static short BLUETOOTH_ENABLE_REQUEST = 100;
	
	public BluetoothManager(){
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		
		// TODO
		if(mAdapter == null){
			throw new RuntimeException("Bluetooth not supported by this device");
		}
		
		// check if bluetooth is enabled
		if (!mAdapter.isEnabled()) {
		    Intent iEnableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(iEnableBT, BLUETOOTH_ENABLE_REQUEST);
		}
		
		// TODO
		if(!mAdapter.isEnabled()){
			throw new RuntimeException("ERROR: still couldn't enable bluetooth");
		}
		
		
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	if(requestCode == BLUETOOTH_ENABLE_REQUEST){
    		if(resultCode == RESULT_OK){
    			
    		}
    	}
    }
	
	public void run() {
		
	}

}
