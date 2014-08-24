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
import java.io.OutputStream;

import com.ncdadodgeball.ndropp.MainActivity;
import com.ncdadodgeball.util.Log;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class BluetoothThread implements Runnable{
	
	private boolean m_bSendData;	//true to send data, false to receive
	private BluetoothSocket m_BTServerSocket;
	private BluetoothDevice m_BTServerDevice;
	
	public BluetoothThread(boolean bSendData){
		m_bSendData = bSendData;
		m_BTServerSocket = null;
		m_BTServerDevice = null;
	}

	public void run() {
		BluetoothManager.eSocketType connectionType = BluetoothManager.instance().getConnectionType();
		if( m_bSendData ){
			//if this device is the server, send data to clients
			if( connectionType == BluetoothManager.eSocketType.SERVER )
				sendDataToClients();
			//if this device is the client, send data to only the server
			else if( connectionType == BluetoothManager.eSocketType.CLIENT )
				sendDataToServer();
		}
		else{
			//if this device is the server, receive data from all clients
			if( connectionType == BluetoothManager.eSocketType.SERVER )
				receiveDataFromClients();
			//if this device is a client, receive data from only the server
			else if( connectionType == BluetoothManager.eSocketType.CLIENT )
				receiveDataFromServer();
		}
	}
	
	
	private void sendDataToServer(){
		//we are a client and we only need to send data to the server
		if( m_BTServerSocket == null && m_BTServerDevice == null){
			for( BTDeviceDesc connection : BluetoothManager.instance().getConnectedDevices() ){
				if( connection.getSocketType() == BluetoothManager.eSocketType.CLIENT ){
					//we are connected to this device as a client which means it's the server
					m_BTServerSocket = connection.getSocket();
					m_BTServerDevice = connection.getDevice();
					break;
				}
			}
		}
		if( m_BTServerDevice == null || m_BTServerSocket == null ){
			//TODO
			throw new RuntimeException("ERR: no server found to send data to");
		}
		
		OutputStream ostream = null;
		try{
			ostream = m_BTServerSocket.getOutputStream();
		}
		catch(IOException ioe){}
		
		while( true ){
			if( !BluetoothManager.instance().isBluetoothEnabled() ){
				//TODO
				continue;
			}
			try{
				//do we have data to send to the server?
				// TODO -- send events to server (create DB of events)
				
				
				//send data to the server
				String message = "client's message to server";
				byte [] buffer = message.getBytes();
				ostream.write(buffer);
				Log.D("CLIENT SENT MESSAGE");
			}
			catch(IOException ioe){
				
			}
		}
	}
	
	private void receiveDataFromServer(){
		
		//we are the client and need to get data from the server
		if( m_BTServerSocket == null & m_BTServerDevice == null){
			for( BTDeviceDesc connection : BluetoothManager.instance().getConnectedDevices() ){
				if( connection.getSocketType() == BluetoothManager.eSocketType.CLIENT ){
					//we are connected to this device as a client which means it's the server
					m_BTServerSocket = connection.getSocket();
					m_BTServerDevice = connection.getDevice();
					break;
				}
			}
		}
		if( m_BTServerSocket == null || m_BTServerDevice == null ){
			//TODO
			throw new RuntimeException("ERR: no server found to receive data from");
		}
		
		InputStream input = null;
		try{
			input = m_BTServerSocket.getInputStream();
		}
		catch(IOException ioe){	}	//TODO
		
		while( true ){
			try{
				if( !BluetoothManager.instance().isBluetoothEnabled() ){
					//TODO
					continue;
				}

				//get data from the server
				if( input.available() > 0){
					byte [] buffer = new byte [input.available()];
					input.read(buffer);
					Log.D("CLIENT RECEIVED: " + new String(buffer));
				}
			}
			catch(IOException ioe){

			}
		}
	}
	
	private void sendDataToClients(){
		//we are the server and need to send data to all clients
		while( true ){
			if( !BluetoothManager.instance().isBluetoothEnabled() ){
				//TODO
				continue;
			}
			for( BTDeviceDesc connection : BluetoothManager.instance().getConnectedDevices() ){
				if( connection.getSocketType() == BluetoothManager.eSocketType.SERVER ){
					//we are connected to this device as the server which means this device is client
					OutputStream ostream = null;
					try{
						ostream = connection.getSocket().getOutputStream();
						String message = "Server's message to client";
						ostream.write(message.getBytes());
						Log.D("SERVER SENT MESSAGE TO CLIENT");
					}
					catch(IOException ioe){}
				}
			}		
		}
	}
	
	private void receiveDataFromClients(){
		while( true ){
			if( !BluetoothManager.instance().isBluetoothEnabled() ){
				//TODO
				continue;
			}
			
			//we are server and need to listen to clients
			for( BTDeviceDesc connection : BluetoothManager.instance().getConnectedDevices() ){
				if( connection.getSocketType() == BluetoothManager.eSocketType.SERVER ){
					//we are connected to this device as the server which means this device is client
					InputStream istream = null;
					try{
						istream = connection.getSocket().getInputStream();
						//get data from the client
						if( istream.available() > 0){
							byte [] buffer = new byte [istream.available()];
							istream.read(buffer);
							Log.D("SERVER RECEIVED: " + new String(buffer));
						}
					}
					catch(IOException ioe){}
				}
			}		
		}
	}
}
