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
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentLinkedQueue;
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
public class BluetoothManager implements Runnable {

	public enum eSocketType { SERVER, CLIENT };
	public static final String UUID_BT = "04032014-0006-0005-0001-000D0D8EBA11"; 
								/* <today-date>-<#MDCs>-<#Nationals>-<#Beast>-<dodgeball> */
	
	private ConcurrentLinkedQueue<Event> mSendEventQueue;
	private ConcurrentLinkedQueue<Event> mRecEventQueue;
	
//	private Activity				mParent;
	private BluetoothAdapter 		mBTAdapter;
	private BroadcastReceiver		mReceiver;
	private Set<BluetoothDevice>	mKnownDevices;
	private ArrayList<BTDeviceDesc>	mConnectedDevices;	//need to know all devices you're connected to, their names, their socket, their connection type, and what they are (HR, SCR, etc.)
	
	private eSocketType				mSocketType;		//connection type of this device. If this device is a client, this should be client
	
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
	
	private BluetoothManager(){
//		mParent = PreGameSetupActivity.sInstance;
		mBTAdapter = BluetoothAdapter.getDefaultAdapter();
		mSendEventQueue = new ConcurrentLinkedQueue<Event>();
		mRecEventQueue = new ConcurrentLinkedQueue<Event>();
		mSocketType = null;
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
	
//	public void setParentActivity(Activity parent){
//		mParent = parent;
//	}
	
	public boolean isBluetoothEnabled(){
		//bluetooth not supported
		if(mBTAdapter == null){
			mBTAdapter = BluetoothAdapter.getDefaultAdapter();
			if( mBTAdapter == null ){
//				Toast.makeText(mParent, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		
		if( !mBTAdapter.isEnabled() )
			mBTAdapter = BluetoothAdapter.getDefaultAdapter();
		
		//if bluetooth is not enabled
		if ( !mBTAdapter.isEnabled() ) {
//			Toast.makeText(mParent, "Please enable bluetooth", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		//if bluetooth is enabled, good
		else{
			//Toast.makeText(mParent, "Bluetooth enabled", Toast.LENGTH_SHORT).show();
			return true;
		}
	}

	public void destroy(){
		if(mBTAdapter.isDiscovering())
			mBTAdapter.cancelDiscovery();
		try{
			PreGameSetupActivity.sInstance.unregisterReceiver(mReceiver);
		}
		catch(Exception e){ }
	}

	
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
	
	public boolean isConnectedToOtherDevices(){
		return( isBluetoothEnabled() && mConnectedDevices != null && !mConnectedDevices.isEmpty() );
	}
	
	public void connect(BluetoothDevice device){
		if( mConnectedDevices == null )
			mConnectedDevices = new ArrayList<BTDeviceDesc>();
		BluetoothConnectionTask task = new BluetoothConnectionTask(
				PreGameSetupActivity.sInstance, mBTAdapter, mSocketType, mConnectedDevices);
		task.execute( new BluetoothDevice [] {device} );
	}
	
	public void connectCallback(boolean result){
		PreGameSetupActivity.sInstance.onBluetoothConnectCallback(result);
	}
	
	
	/*************************************************************************************
	 * 							THREAD HANDLING
	 * **********************************************************************************/
	public void initThread(){
		if( mBTSendThread == null && mBTRecThread == null){
			new Thread(this).start();
		}
	}
	
	public void run() {
		sendDataThread();
		receiveDataThread();
	}
	
	public void postEvent(Event e){
		if( e.getReceiver() == null /*|| e.getReceiver().getBTConnection() == null*/ )
			return;
		synchronized(mSendEventQueue){
			mSendEventQueue.add(e);
			mSendEventQueue.notify();
		}
	}
	
	public Event getSendEvent(){
		if( !mSendEventQueue.isEmpty() )
			return mSendEventQueue.poll();
		else
			return null;
	}
	
	// send data thread
	private void sendDataThread(){
		mBTSendThread = new Thread( new Runnable(){
			public void run(){
				synchronized(mSendEventQueue){
					boolean bProcessing = false;
					while( true ){
						try{
							//wait until we get data
							if( !bProcessing ){
								mSendEventQueue.wait();
								bProcessing = true;
								Event e = getSendEvent();

								//if spurious wake-up or bluetooth disabled, wait again
								if( e == null || !isBluetoothEnabled() ){
									bProcessing = false;
									continue;
								}

								//we have an event. sending to who?
								if( e.getSender() == GameSettings.STAFF.HR )
									sendDataToClient(e);
								else
									sendDataToServer(e);
								bProcessing = false;
							}
						}
						catch(InterruptedException ie){
							Log.D("SENDING THREAD ERR");
						}
					}
				}
			}
		});
		mBTSendThread.start();
	}
	
	private void receiveDataThread(){
		mBTRecThread = new Thread( new Runnable() {
			public void run(){
				while(true){
					try{
						boolean bDataReceived = false;
						for(BTDeviceDesc desc : getConnectedDevices() ){
							InputStream istream = desc.getSocket().getInputStream();
							if( istream.available() > 0 ){
								bDataReceived = true;
								ObjectInputStream oistream = new ObjectInputStream(istream);
								Object obj = oistream.readObject();
								Event e = null;
								if( obj instanceof Event )
									e = (Event)obj;
								if( e == null )
									Log.D("REC THREAD: DATA IS NOT EVENT");
								else{
									Log.D("REC THREAD: RECEIVED EVENT");
									EventHandler.instance().postEvent(e);
								}
								Thread.yield();
							}
						}
						if( !bDataReceived )
							Thread.sleep(50);	//sleep 50ms
					}
					catch(Exception e){
						Log.D("RECEIVING THREAD ERR");
						Log.E(e.getMessage());
					}
				}
			}
		});
		mBTRecThread.start();
	}
	
	private void sendDataToClient(Event e){
		//we are the server and need to send data to a client
		GameSettings.STAFF staff = e.getReceiver();
		BTDeviceDesc client = null;
		//TODO -- need to know which connections are who (change BTDeviceDesc?)
		for( BTDeviceDesc desc : getConnectedDevices() ){
			if( desc.getSocketType() == eSocketType.SERVER ){
				client = desc;
				break;
			}
		}
		
		OutputStream ostream = null;
		try{
			ostream = client.getSocket().getOutputStream();
//			String message = "Server's message to client";
//			ostream.write(message.getBytes());
			ObjectOutputStream oostream = new ObjectOutputStream(ostream);
			oostream.writeObject(e);
			oostream.flush();
			Log.D("SERVER SENT EVENT TO CLIENT");
		}
		catch(IOException ioe){
			Log.D("SERVER FAILED TO SEND EVENT");
			Log.E(ioe.getMessage());
		}
	}
	
	
	private void sendDataToServer(Event e){
	
	}
}













//TODO - make this work - this will continually check to make sure bluetooth is enabled
////do this here or in run() ?
//private void createBTSafeguard(){
//	Thread tBTSafeguard = new Thread(new Runnable(){
//		public void run(){
//			if( !isBluetoothEnabled() )
//				throw new RuntimeException("ERROR: lost Bluetooth connection");
//		}
//	});
//	tBTSafeguard.setDaemon(true);
//	tBTSafeguard.run();
//}
	
//private BluetoothManager(Activity parent, eSocketType connectionType){
//	mParent = parent;
//	mBTAdapter = BluetoothAdapter.getDefaultAdapter();
//	mSocketType = connectionType;
//	mServerSocket = null;
//}

///** connectAsServer
// * @param client : client Bluetooth device to connect to as a server device 
// * @return BluetoothSocket connection between this device and the given device
// * 
// * 	Connect to the given client with this device acting as the server
// */
//private BluetoothSocket connectAsServer(BluetoothDevice client){		
//	try {
//		if( mServerSocket == null ){
//			mServerSocket = mBTAdapter.listenUsingRfcommWithServiceRecord(
//					mParent.getString(R.string.app_name), UUID.fromString(UUID_BT));
//		}
//		
//		BluetoothSocket connection = mServerSocket.accept(30000);		//timeout in milliseconds
//		mServerSocket.close();
//		mConnectedDevices.add(new BTDeviceDesc(client, connection, eSocketType.CLIENT));
//		return connection;
//	} catch (IOException e) {
//		return null;
//	}
//}

///** connectAsClient
// * @param server : server Bluetooth device to connect to as a client
// * @return BluetoothSocket connection between this device and the given device
// * 
// * Connect to the given server device with this device acting as a client
// */
//private BluetoothSocket connectAsClient(BluetoothDevice server){
//	try{
//		BluetoothSocket socket = server.createRfcommSocketToServiceRecord(UUID.fromString(UUID_BT));
//		socket.connect();
//		mConnectedDevices.add(new BTDeviceDesc(server, socket, eSocketType.SERVER));
//		return socket;
//	} catch (IOException e) {
//		return null;
//	}
//}

//@Override
//protected void onActivityResult(int requestCode, int resultCode, Intent data){
//	if(requestCode == BLUETOOTH_ENABLE_REQUEST){
////		mbRequestBT = false;
//		if(resultCode == RESULT_OK){
//			if( !isBluetoothEnabled() ){
//				throw new RuntimeException("ERROR: still couldn't enable bluetooth");
//			}
//			Toast.makeText(MainActivity.sInstance, "bluetooth enabled", Toast.LENGTH_SHORT).show();	
//		}
//		else if(resultCode == RESULT_CANCELED){
//			Toast.makeText(MainActivity.sInstance, "cancelled", Toast.LENGTH_SHORT).show();
//		}
//	}
//	super.onActivityResult(requestCode, resultCode, data);
//}

//public boolean isRequestingBT(){
//return mbRequestBT;
//}

//private void discoverDevices(){
//if(mBTAdapter.isDiscovering())
//	mBTAdapter.cancelDiscovery();
//mBTAdapter.startDiscovery();
//
//AlertDialog.Builder builder = new AlertDialog.Builder(mParent);
//ListView list = new ListView(mParent);
//builder.setView(list);
//mArrayAdapter = new ArrayAdapter<String>(mParent, list.getId());
//// Create a BroadcastReceiver for ACTION_FOUND
//mReceiver = new BroadcastReceiver() {
//	public void onReceive(Context context, Intent intent) {
//		String action = intent.getAction();
//		if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//			mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
//			Log.D("device: " + device.getName());
//		}
//	}
//};
//list.setAdapter(mArrayAdapter);
//Dialog dialog = builder.create();
//dialog.setCanceledOnTouchOutside(false);
//// Register the BroadcastReceiver
//IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
//
//
////get paired devices
////		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
////		// If there are paired devices
////		if (pairedDevices.size() > 0) {
////			// Loop through paired devices
////			for (BluetoothDevice device : pairedDevices) {
////				// Add the name and address to an array adapter to show in a ListView
////				mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
////			}
////		}
//}

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