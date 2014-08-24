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
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import com.ncdadodgeball.ndropp.*;
import com.ncdadodgeball.util.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/*	BluetoothManager
 * 
 * 	http://developer.android.com/guide/topics/connectivity/bluetooth.html
 */
public class BluetoothManager{

	public enum eSocketType { SERVER, CLIENT };
	public static final String UUID_BT = "04032014-0006-0005-0001-000D0D8EBA11"; /* <today-date>-<#MDCs>-<#Nationals>-<#Beast>-<dodgeball> */
	
	private Activity				mParent;
	private BluetoothAdapter 		mBTAdapter;
	private BroadcastReceiver		mReceiver;
	private BluetoothServerSocket	mServerSocket;
	private Set<BluetoothDevice>	mKnownDevices;
	private ArrayList<BTDeviceDesc>	mConnectedDevices;	//need to know all devices you're connected to, their names, their socket, their connection type, and what they are (HR, SCR, etc.)
	
	private eSocketType				mSocketType;		//connection type of this device. If this device is a client, this should be client
	
//	private boolean					mbRequestBT;
	private ArrayAdapter<String>	mArrayAdapter;
	private Thread					mBTSendThread;
	private Thread					mBTRecThread;


	public static short BLUETOOTH_ENABLE_REQUEST = 100;

	private static BluetoothManager sInstance = null;
	
	public static BluetoothManager instance(){
		if( sInstance == null ){
			sInstance = new BluetoothManager();
		}
		return sInstance;
	}
	
	public BluetoothManager(){
		mParent = PreGameSetupActivity.sInstance;
		mBTAdapter = BluetoothAdapter.getDefaultAdapter();
		mSocketType = null;
		mServerSocket = null;
		mKnownDevices = null;
		mConnectedDevices = null;
		mBTSendThread = mBTRecThread = null;
	}
	
	public void setConnectionType(eSocketType connectionType){
		mSocketType = connectionType;
	}
	
	public eSocketType getConnectionType(){
		return mSocketType;
	}
	
	public void setParentActivity(Activity parent){
		mParent = parent;
	}
	
//	private BluetoothManager(Activity parent, eSocketType connectionType){
//		mParent = parent;
//		mBTAdapter = BluetoothAdapter.getDefaultAdapter();
//		mSocketType = connectionType;
//		mServerSocket = null;
//	}
	
//	/** connectAsServer
//	 * @param client : client Bluetooth device to connect to as a server device 
//	 * @return BluetoothSocket connection between this device and the given device
//	 * 
//	 * 	Connect to the given client with this device acting as the server
//	 */
//	private BluetoothSocket connectAsServer(BluetoothDevice client){		
//		try {
//			if( mServerSocket == null ){
//				mServerSocket = mBTAdapter.listenUsingRfcommWithServiceRecord(
//						mParent.getString(R.string.app_name), UUID.fromString(UUID_BT));
//			}
//			
//			BluetoothSocket connection = mServerSocket.accept(30000);		//timeout in milliseconds
//			mServerSocket.close();
//			mConnectedDevices.add(new BTDeviceDesc(client, connection, eSocketType.CLIENT));
//			return connection;
//		} catch (IOException e) {
//			return null;
//		}
//	}
	
//	/** connectAsClient
//	 * @param server : server Bluetooth device to connect to as a client
//	 * @return BluetoothSocket connection between this device and the given device
//	 * 
//	 * Connect to the given server device with this device acting as a client
//	 */
//	private BluetoothSocket connectAsClient(BluetoothDevice server){
//		try{
//			BluetoothSocket socket = server.createRfcommSocketToServiceRecord(UUID.fromString(UUID_BT));
//			socket.connect();
//			mConnectedDevices.add(new BTDeviceDesc(server, socket, eSocketType.SERVER));
//			return socket;
//		} catch (IOException e) {
//			return null;
//		}
//	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data){
//		if(requestCode == BLUETOOTH_ENABLE_REQUEST){
////			mbRequestBT = false;
//			if(resultCode == RESULT_OK){
//				if( !isBluetoothEnabled() ){
//					throw new RuntimeException("ERROR: still couldn't enable bluetooth");
//				}
//				Toast.makeText(MainActivity.sInstance, "bluetooth enabled", Toast.LENGTH_SHORT).show();	
//			}
//			else if(resultCode == RESULT_CANCELED){
//				Toast.makeText(MainActivity.sInstance, "cancelled", Toast.LENGTH_SHORT).show();
//			}
//		}
//		super.onActivityResult(requestCode, resultCode, data);
//	}
	
	public void initThread(){
		if( mBTSendThread == null && mBTRecThread == null){
			mBTSendThread = new Thread( new BluetoothThread(true) );
			mBTSendThread.start();
			mBTRecThread = new Thread( new BluetoothThread(false) );
			mBTRecThread.start();
		}
	}

//	public boolean isRequestingBT(){
//		return mbRequestBT;
//	}

	public boolean isBluetoothEnabled(){
		//bluetooth not supported
		if(mBTAdapter == null){
			mBTAdapter = BluetoothAdapter.getDefaultAdapter();
			if( mBTAdapter == null ){
				Toast.makeText(mParent, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		
		if( !mBTAdapter.isEnabled() )
			mBTAdapter = BluetoothAdapter.getDefaultAdapter();
		
		//if bluetooth is not enabled
		if ( !mBTAdapter.isEnabled() ) {
			Toast.makeText(mParent, "Please enable bluetooth", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		//if bluetooth is enabled, good
		else{
			//Toast.makeText(mParent, "Bluetooth enabled", Toast.LENGTH_SHORT).show();
			return true;
		}
	}

	//TODO - make this work - this will continually check to make sure bluetooth is enabled
	//do this here or in run() ?
	private void createBTSafeguard(){
		Thread tBTSafeguard = new Thread(new Runnable(){
			public void run(){
				if( !isBluetoothEnabled() )
					throw new RuntimeException("ERROR: lost Bluetooth connection");
			}
		});
		tBTSafeguard.setDaemon(true);
		tBTSafeguard.run();
	}


	public void destroy(){
		if(mBTAdapter.isDiscovering())
			mBTAdapter.cancelDiscovery();
		try{
			mParent.unregisterReceiver(mReceiver);
		}
		catch(Exception e){ }
	}

//	private void discoverDevices(){
//		if(mBTAdapter.isDiscovering())
//			mBTAdapter.cancelDiscovery();
//		mBTAdapter.startDiscovery();
//
//		AlertDialog.Builder builder = new AlertDialog.Builder(mParent);
//		ListView list = new ListView(mParent);
//		builder.setView(list);
//		mArrayAdapter = new ArrayAdapter<String>(mParent, list.getId());
//		// Create a BroadcastReceiver for ACTION_FOUND
//		mReceiver = new BroadcastReceiver() {
//			public void onReceive(Context context, Intent intent) {
//				String action = intent.getAction();
//				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//					mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
//					Log.D("device: " + device.getName());
//				}
//			}
//		};
//		list.setAdapter(mArrayAdapter);
//		Dialog dialog = builder.create();
//		dialog.setCanceledOnTouchOutside(false);
//		// Register the BroadcastReceiver
//		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//		registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
//
//
//		//get paired devices
//		//		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//		//		// If there are paired devices
//		//		if (pairedDevices.size() > 0) {
//		//			// Loop through paired devices
//		//			for (BluetoothDevice device : pairedDevices) {
//		//				// Add the name and address to an array adapter to show in a ListView
//		//				mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
//		//			}
//		//		}
//	}
	
	/*
		if(mBTAdapter.isEnabled()){
			//TODO -- createBTSafeguard();

			//find devices this phone is paired to
			getPairedDevices();

			//set up connection
			if( mSocketType == eSocketType.SERVER)
				mBTSocket = createServer();
			else
				mBTSocket = createClient();
		}
	*/
	
	/*
	 * return set of devices that are known to this device. they are not necessarily available at the moment
	 */
	public Set<BluetoothDevice> getKnownDevices(){
		if(mKnownDevices != null)
			return mKnownDevices;
		
		mKnownDevices = mBTAdapter.getBondedDevices();
		if( mKnownDevices.size() == 0 )
			//TODO: discover devices
			return null;
		else
			return mKnownDevices;
	}
	
	//TODO
	// return set of devices connected to this device via bluetooth currently
	public ArrayList<BTDeviceDesc> getConnectedDevices(){
		return mConnectedDevices;
	}
	
	public void connect(BluetoothDevice device){
		if( mConnectedDevices == null )
			mConnectedDevices = new ArrayList<BTDeviceDesc>();
		BluetoothConnectionTask task = new BluetoothConnectionTask(
				mParent, mBTAdapter, mSocketType, mConnectedDevices);
		task.execute( new BluetoothDevice [] {device} );
	}
	
	public void connectCallback(boolean result){
		if( mParent instanceof PreGameSetupActivity ){
			((PreGameSetupActivity) mParent).onBluetoothConnectCallback(result);
		}
	}
}