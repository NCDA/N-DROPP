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
package com.ncdadodgeball.comm;

import java.io.Serializable;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class BTDeviceDesc implements Serializable{
	private static final long serialVersionUID = 0xD0D83BA11L;
	private BluetoothDevice					mDevice;
	private BluetoothSocket 				mSocket;
	private BluetoothManager.eSocketType 	mSocketType;
	
	/**
	 * 
	 * @param device
	 * @param socket
	 * @param type : connection type of this device. Client if this device is trying to connect to server
	 */
	public BTDeviceDesc(BluetoothDevice device, BluetoothSocket socket, BluetoothManager.eSocketType type){
		mDevice = device;
		mSocket = socket;
		mSocketType = type;
	}

	public BluetoothDevice getDevice() {
		return mDevice;
	}

	public BluetoothSocket getSocket() {
		return mSocket;
	}

	public BluetoothManager.eSocketType getSocketType() {
		return mSocketType;
	}
	//use appglobals settings to find who we're connected to (HR, SCR -- home or away, etc).
}