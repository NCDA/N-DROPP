///**************************************************************************************************
// * _____   __     _______________________________________ 
// * ___  | / /     ___  __ \__  __ \_  __ \__  __ \__  __ \
// * __   |/ /________  / / /_  /_/ /  / / /_  /_/ /_  /_/ /
// * _  /|  /_/_____/  /_/ /_  _, _// /_/ /_  ____/_  ____/
// * /_/ |_/        /_____/ /_/ |_| \____/ /_/     /_/
// * 
// * National Collegiate Dodgeball Association (NCDA)
// * NCDA - Dodgeball Referee Officiating Application
// * http://www.ncdadodgeball.com
// * Copyright 2014. All Rights Reserved.
// *************************************************************************************************/
//package com.ncdadodgeball.ndropp;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Set;
//import java.util.UUID;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.bluetooth.*;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.Toast;
//
///*	BluetoothManager
// * 
// * 	http://developer.android.com/guide/topics/connectivity/bluetooth.html
// */
//public class BluetoothManager extends Activity implements Runnable{
//
//	public enum eSocketType { SERVER, CLIENT };
//	
//	private Activity				mParent;
//	private BluetoothAdapter 		mBTAdapter;
//	private BluetoothDevice			mThisDevice;
//	private BroadcastReceiver		mReceiver;
//	private Set<BluetoothDevice>	mKnownDevices;
//	private BluetoothDevice			mHostDevice;
//	private ArrayList<BTDeviceDesc>	mConnectedDevices;	//need to know all devices you're connected to, their names, their socket, their connection type, and what they are (HR, SCR, etc.)
//	
//	private BluetoothSocket			mBTSocket;
//	private eSocketType				mSocketType;
//	
////	private boolean					mbRequestBT;
//	private ArrayAdapter<String>	mArrayAdapter;
//
//
//	public static short BLUETOOTH_ENABLE_REQUEST = 100;
//
//	
//	public BluetoothManager(Activity parent, eSocketType connectionType){
//		mParent = parent;
//		mBTAdapter = BluetoothAdapter.getDefaultAdapter();
//		mBTSocket = null;
//		mSocketType = connectionType;
//
//		// TODO - BT not supported
//		if(mBTAdapter == null){
//			throw new RuntimeException("Bluetooth not supported by this device");
//		}
//
//		// check if bluetooth is enabled
//		LEFT OFF HERE!!! I GOT BLUETOOTH TO WORK (CONNECT DEVICES). NOW I JUST WANT TO CLEAN
//		THINGS UP.  FIRST, WITH THE BT REQUEST. THIS IS AN ASYNCHRONOUS CALL AND IN ORDER TO
//		GET THE RESULT, WE NEED TO BE AN ACTIVITY. THIS DOESN'T WORK OUT. WE DON'T WANT THE
//		MAIN ACTIVITY TO GET THE RESULT, WE WANT THIS TO GET THE RESULT. I looked into
//		Services but they cant get results either.  This may have to be its own activity.
//		if (!mBTAdapter.isEnabled()) {
//			Intent iEnableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//			mParent.startActivityForResult(iEnableBT, BLUETOOTH_ENABLE_REQUEST);
////			mbRequestBT = true;
//		}
//		else
////			mbRequestBT = false;
//			Toast.makeText(mParent, "null", Toast.LENGTH_LONG);
//
////		while(mbRequestBT);
//		
//		if(mBTAdapter.isEnabled()){
//			//TODO -- createBTSafeguard();
//			
//			//find devices this phone is paired to
//			getPairedDevices();
//			
//			//set up connection
//			if( mSocketType == eSocketType.SERVER)
//				mBTSocket = createServer();
//			else
//				mBTSocket = createClient();
//		}
//	}
//	
//	private BluetoothSocket createServer(){		
//		try {
//			BluetoothServerSocket serverSock = mBTAdapter.listenUsingRfcommWithServiceRecord(
//					Global.APP_TITLE, UUID.fromString(Global.BT_UUID));
//			BluetoothSocket connection = serverSock.accept(30000);		//timeout in milliseconds
//			serverSock.close();
//			return connection;
//		} catch (IOException e) {
//			e.printStackTrace();
//			throw new RuntimeException(e.getMessage());
//		}
//	}
//	
//	private BluetoothSocket createClient(){
//		BluetoothDevice server = null;
//		try {
//			for( BluetoothDevice device : mKnownDevices ){
//				server = device;
//				break;
//			}
//			BluetoothSocket socket = server.createRfcommSocketToServiceRecord(UUID.fromString(Global.BT_UUID));
//			socket.connect();
//			return socket;
//		
//		} catch (IOException e) {
//			e.printStackTrace();
//			throw new RuntimeException(e.getMessage());
//		}
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data){
//		if(requestCode == BLUETOOTH_ENABLE_REQUEST){
////			mbRequestBT = false;
//			if(resultCode == RESULT_OK){
//				if(!mBTAdapter.isEnabled()){
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
//
//	//TODO -- do the BT safeguard here?
//	public void run() {
//
//	}
//
////	public boolean isRequestingBT(){
////		return mbRequestBT;
////	}
//
//	public boolean isConnected(){
//		return mBTAdapter.isEnabled();
//	}
//
//	//TODO - make this work - this will continually check to make sure bluetooth is enabled
//	//do this here or in run() ?
//	private void createBTSafeguard(){
//		Thread tBTSafeguard = new Thread(new Runnable(){
//			public void run(){
//				if(!mBTAdapter.isEnabled())
//					throw new RuntimeException("ERROR: lost Bluetooth connection");
//			}
//		});
//		tBTSafeguard.setDaemon(true);
//		tBTSafeguard.run();
//	}
//
//
//	public void destroy(){
//		if(mBTAdapter.isDiscovering())
//			mBTAdapter.cancelDiscovery();
//		try{
//			mParent.unregisterReceiver(mReceiver);
//		}
//		catch(Exception e){ }
//	}
//
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
//	
//	private void getPairedDevices(){
//		mKnownDevices = mBTAdapter.getBondedDevices();
//		// If there are paired devices
//		if (mKnownDevices.size() > 0) {
//		    // Loop through paired devices
//			String devices = "";
//		    for (BluetoothDevice device : mKnownDevices) {
//		        devices += (device.getName() + "\n" + device.getAddress());
//		    }
//		    AlertDialog.Builder builder = new AlertDialog.Builder(mParent);
//		    builder.setTitle("connected devices");
//		    builder.setMessage(devices);
//		    builder.create().show();
//		}
//	}
//}
//
//
//class BTDeviceDesc{
//	private BluetoothDevice					mDevice;
//	private BluetoothSocket 				mSocket;
//	private BluetoothManager.eSocketType 	mSocketType;
//	
//	public BTDeviceDesc(BluetoothDevice device, BluetoothSocket socket, BluetoothManager.eSocketType type){
//		mDevice = device;
//		mSocket = socket;
//		mSocketType = type;
//	}
//
//	public BluetoothDevice getDevice() {
//		return mDevice;
//	}
//
//	public BluetoothSocket getSocket() {
//		return mSocket;
//	}
//
//	public BluetoothManager.eSocketType getSocketType() {
//		return mSocketType;
//	}
//	//use appglobals settings to find who we're connected to (HR, SCR -- home or away, etc).
//}
