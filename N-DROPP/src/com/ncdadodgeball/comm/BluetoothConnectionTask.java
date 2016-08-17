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

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ncdadodgeball.comm.BluetoothManager.eSocketType;
import com.ncdadodgeball.ndropp.R;

public class BluetoothConnectionTask extends AsyncTask<BluetoothDevice,Void,Integer>{

	public static final int STATUS_OK		= 0;
	public static final int STATUS_CANCEL 	= 1;
	
	public static final int ERR_TIMEOUT			= -1;
	public static final int ERR_COMM_CONN		= -2;
	public static final int ERR_SERVER_SOCKET	= -3;
	public static final int ERR_FAILED_CONN		= -4;
	
	private final int BT_TIMEOUT = 6000;		// timeout in milliseconds
	
	
	private Activity				mParentActivity;
	private BluetoothDevice	[]		mBTDevices;
	private eSocketType				mConnectionType;
	private BluetoothAdapter 		mBTAdapter;
	private ArrayList<BTDeviceDesc>	mConnectedDevices;
	private ProgressDialog			mDialog;
	private BluetoothSocket 		mConnectionSocket;
	
	public BluetoothConnectionTask(Activity parent, BluetoothAdapter adapter, 
			eSocketType connectionType, ArrayList<BTDeviceDesc> connectedDevices){
		mParentActivity = parent;
		mConnectionType = connectionType;
		mBTAdapter = adapter;
		mConnectedDevices = connectedDevices;
	}
	
	@Override
	/**	onPreExecute
	 * 	
	 */
	protected void onPreExecute(){
		super.onPreExecute();
		mDialog = new ProgressDialog(mParentActivity);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setTitle( mParentActivity.getString(R.string.app_name ) );
		mDialog.setMessage( "Attempting to connect..." );
		mDialog.show();
	}
	
	
	@Override
	protected Integer doInBackground(BluetoothDevice... params) {
		mBTDevices = params;
		if( mConnectionType == eSocketType.SERVER )
			return connectAsServer();
		else if( mConnectionType == eSocketType.CLIENT )
			return connectAsClient();
		else
			return null;		//TODO
	}
	
	@Override
	/**	onPostExecute
	 * 	This is executed only when 'doInBackground' is completed. This function is bypassed if 
	 * the async task is cancelled
	 */
	protected void onPostExecute(Integer result){
		super.onPostExecute(result);
		
		switch(result){
			case(STATUS_CANCEL):
				onCancelled(result);
				break;
			
			case(ERR_TIMEOUT):
				Toast.makeText(mParentActivity, "Connection timeout", Toast.LENGTH_SHORT).show();
				break;
				
			case(STATUS_OK):
				mConnectedDevices.add(new BTDeviceDesc(mBTDevices[0], mConnectionSocket, mConnectionType));
				break;
		}
		mDialog.dismiss();
		boolean bStatus = ( result == STATUS_OK );
		BluetoothManager.instance().connectCallback(bStatus);
	}
	
	@Override
	/**	onCancelled
	 * 	cleans up resources if this task is halted prematurely
	 */
	protected void onCancelled(Integer result){
		//if this task was cancelled, do not set a new view
		super.onCancelled(result);
//		Toast.makeText(mParentActivity, "Cancelled connection", Toast.LENGTH_SHORT).show();
		boolean bStatus = ( result == STATUS_OK );
		BluetoothManager.instance().connectCallback(bStatus);
	}
	
	private Integer connectAsServer(){
		String strAppName = mParentActivity.getString(R.string.app_name);
		BluetoothServerSocket serverSocket = null;
		
		// create comm connection
		try{
			serverSocket = mBTAdapter.listenUsingRfcommWithServiceRecord(
					strAppName, UUID.fromString(BluetoothManager.UUID_BT));
		}
		catch(IOException ioe){
			return ERR_COMM_CONN;
		}
		
		// accept socket connection
		try{
			mConnectionSocket = serverSocket.accept(BT_TIMEOUT);
		}
		catch(IOException ioe){
			return ERR_TIMEOUT;
		}
	
		//close server socket
		try{
			serverSocket.close();
		}
		catch(IOException ioe){
			return ERR_SERVER_SOCKET;
		}
	
		return STATUS_OK;
	}
	
	private Integer connectAsClient(){
		
		// create comm socket
		try{
			mConnectionSocket = mBTDevices[0].createRfcommSocketToServiceRecord(
					UUID.fromString(BluetoothManager.UUID_BT));
		}
		catch(IOException ioe){
			return ERR_COMM_CONN;
		}
		
		// attempt connection
		try{
			mConnectionSocket.connect();
		}
		catch(IOException ioe){
			return ERR_FAILED_CONN;
		}
		
		return STATUS_OK;
	}
}