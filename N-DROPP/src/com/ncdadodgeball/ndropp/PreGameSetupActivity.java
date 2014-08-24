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
import java.util.TreeMap;

import com.ncdadodgeball.comm.BluetoothManager;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PreGameSetupActivity extends Activity implements OnClickListener {

	public static PreGameSetupActivity sInstance;
	
	public enum eMenuState { START, SELECT_REF_NEW, SELECT_REF_JOIN, LIST_DEVICES, SELECT_BROADCAST }; 
	
	private Button			mBtBack, mBtNeutral, mBtContinue;
	private RelativeLayout 	mSubview1, mSubview2;
	private TextView		mStateDescription;
	private eMenuState		mState;
	private TreeMap<Integer,BluetoothDevice>	mDevices;
	
    @Override
    /** onCreate
     * 	Set up the Main Menu interface
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregame);
        sInstance = this;
        
        mStateDescription = (TextView) findViewById(R.id.PREGAME_txt_state_desc);
        
        mBtBack = (Button) findViewById(R.id.PREGAME_bt_back);
        mBtNeutral = (Button) findViewById(R.id.PREGAME_bt_neutral);
        mBtContinue = (Button) findViewById(R.id.PREGAME_bt_continue);
        
        mSubview1 = (RelativeLayout)findViewById(R.id.PREGAME_layout_subview1);
        mSubview2 = (RelativeLayout)findViewById(R.id.PREGAME_layout_subview2);
        
        loadStartState();
    }
    
    /*==========================================================================
     * 									STATES
     *=========================================================================*/
    private void loadStartState(){
    	mState = eMenuState.START;
    	mStateDescription.setText("Create your own game or join and existing game");
    	
    	if( !(mSubview1.isShown() || mSubview2.isShown()) ){
    		View.inflate(this, R.layout.pregame_start, mSubview1);
    	}
    	else{
	    	RelativeLayout hidden = getHiddenPane();
	    	View.inflate(this, R.layout.pregame_start, hidden);
	    	showHiddenPane();
    	}
    	hideAllNavButtons();
    }
    
    private void loadNewGameState(){
    	mState = eMenuState.SELECT_REF_NEW;
    	mStateDescription.setText("Select your referee type");
    	View.inflate(this, R.layout.pregame_new, getHiddenPane());
    	showHiddenPane();
    	showBackButton();
    	hideNeutralButton();
    	hideContinueButton();
    }
    
    //displayed when joining a game or broadcasting as the bluetooth server
    private void loadConnectionState(BluetoothManager.eSocketType connectionType){
    	BluetoothManager.instance().setParentActivity(this);
    	BluetoothManager.instance().setConnectionType(connectionType);
    	mState = eMenuState.LIST_DEVICES;
    	mStateDescription.setText("Select a device to connect to");
    	
    	//if bluetooth is on, get paired devices
    	if(BluetoothManager.instance().isBluetoothEnabled()){
    		Set<BluetoothDevice> devices = BluetoothManager.instance().getKnownDevices();
    		
    		// TODO : if no devices found
    		
    		RelativeLayout pane = getHiddenPane();
    		View.inflate(this, R.layout.pregame_connect, pane);
    		
    		LinearLayout layout = (LinearLayout) pane.findViewById(R.id.PREGAME_layout_connect_options);
    		mDevices = new TreeMap<Integer,BluetoothDevice>();
    		for( BluetoothDevice device : devices ){
    			Button b = new Button(this);
    			b.setText(device.getName());
    			b.setOnClickListener(this);
    			mDevices.put(b.hashCode(), device);
    			layout.addView(b);
    		}
    		showHiddenPane();

    		showNeutralButton();
    	}
    	else{
    		hideNeutralButton();
    	}
    	
    	showBackButton();
    	hideContinueButton();
    }
    
    private void loadBroadcastState(){
    	mState = eMenuState.SELECT_BROADCAST;
    	mStateDescription.setText("Create your own local game or broadcast a game via bluetooth for others to join");
		View.inflate(this, R.layout.pregame_broadcast, getHiddenPane());
		showHiddenPane();
		
		showBackButton();
		hideNeutralButton();
		hideContinueButton();
    }
    
    private void loadJoinAsState(){
    	mState = eMenuState.SELECT_REF_JOIN;
    	mStateDescription.setText("Select your referee type");
    	View.inflate(this, R.layout.pregame_join_as, getHiddenPane());
    	showHiddenPane();
    	
    	showBackButton();
    	hideNeutralButton();
    	hideContinueButton();
    }
    
    
    /*==========================================================================
     * 								DISPLAY CHANGES
     *=========================================================================*/
    private RelativeLayout getHiddenPane(){
    	if(!mSubview1.isShown() && mSubview2.isShown())
    		return mSubview1;
    	else if( !mSubview2.isShown() && mSubview1.isShown())
    		return mSubview2;
    	else
    		throw new RuntimeException();
    }
    
    private RelativeLayout getVisiblePane(){
    	if(!mSubview1.isShown() && mSubview2.isShown())
    		return mSubview2;
    	else if( !mSubview2.isShown() && mSubview1.isShown())
    		return mSubview1;
    	else
    		throw new RuntimeException();
    }
    
    private void showHiddenPane(){
    	RelativeLayout hidden = getHiddenPane();
    	RelativeLayout visible = getVisiblePane();
    	
    	visible.animate().alpha(0f).setListener(new MyAnimatorListener(visible)).start();
    	hidden.setAlpha(0f);
    	hidden.setVisibility(View.VISIBLE);
    	hidden.animate().alpha(1f).setListener(null).start();
    }
    
    private void showBackButton(){
    	mBtBack.setVisibility(View.VISIBLE);
    	mBtBack.setClickable(true);
    }
    
    private void hideBackButton(){
    	mBtBack.setVisibility(View.INVISIBLE);
    	mBtBack.setClickable(false);
    }
    
    private void showNeutralButton(){
    	mBtNeutral.setVisibility(View.VISIBLE);
    	mBtNeutral.setClickable(true);
    }
    
    private void hideNeutralButton(){
    	mBtNeutral.setVisibility(View.INVISIBLE);
    	mBtNeutral.setClickable(false);
    }

    private void showContinueButton(){
    	mBtContinue.setVisibility(View.VISIBLE);
    	mBtContinue.setClickable(true);
    }
    
    private void hideContinueButton(){
    	mBtContinue.setVisibility(View.INVISIBLE);
    	mBtContinue.setClickable(true);
    }
    
    private void hideAllNavButtons(){
    	hideBackButton();
    	hideNeutralButton();
    	hideContinueButton();
    }
    
    private void showAllNavButtons(){
    	showBackButton();
    	showNeutralButton();
    	showContinueButton();
    }
    
    /*==========================================================================
     * 								INPUT EVENTS
     *=========================================================================*/
    @Override
    public void onBackPressed(){
    	if( mState == eMenuState.START )
    		super.onBackPressed();
    	else
    		onBackSelected(mBtBack);
    }
    
    public void onBackSelected(View view){
    	if( mState == eMenuState.START )
    		throw new IllegalStateException();

    	else if( mState == eMenuState.SELECT_REF_NEW || mState == eMenuState.SELECT_REF_JOIN)
    		loadStartState();
    	
    	else if( mState == eMenuState.LIST_DEVICES){
    		BluetoothManager.eSocketType type = BluetoothManager.instance().getConnectionType();
    		if( type == null || type == BluetoothManager.eSocketType.CLIENT )
    			loadStartState();
    		else if ( type == BluetoothManager.eSocketType.SERVER)
    			loadBroadcastState();
    	}
    	
    	else if( mState == eMenuState.SELECT_BROADCAST )
    		loadNewGameState();
    }
    
    public void onNeutralSelected(View view){
    	//TODO : search for BT devices to connect to
    }
    
    public void onContinueSelected(View view){
    	if( mState != eMenuState.LIST_DEVICES && BluetoothManager.instance().getConnectionType() != BluetoothManager.eSocketType.SERVER )
    		throw new IllegalStateException();
    	
    	//TODO check to make sure we're connected to devices
    	
    	launchHRActivity(view);
    }
    
    public void onNewGameSelected(View view){
//    	View.inflate(this, R.layout.pregame_new, getHiddenPane() );
//    	showHiddenPane();
    	loadNewGameState();
    }
    
    public void onJoinGameSelected(View view){
    	if( BluetoothManager.instance().isBluetoothEnabled() )
    		loadConnectionState(BluetoothManager.eSocketType.CLIENT);
    }
    
    public void onHeadRefSelected(View view){
    	loadBroadcastState();
    }
    
    public void onBluetoothSelected(View view){
    	if( BluetoothManager.instance().isBluetoothEnabled() )
    		loadConnectionState(BluetoothManager.eSocketType.SERVER);
    }   
    
    
    
    /** launchSCRActivity
     * 	Fires an intent to start the Shot Clock Referee Activity in front of this Activity
     */
    public void launchSCRActivity(View v){
    	Intent intent = new Intent(this, SCRGameActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
    }
    
    /** launchHRActivity
     * 	Fires an intent to start the Head-Referee Activity in front of this Activity
     */
    public void launchHRActivity(View v){
    	Intent intent = new Intent(this, HRGameActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
    }
    
    /**
     *
     */
    public void launchHybridRefActivity(View v){
    	Intent intent = new Intent(this, HybridRefGameActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
    }
    
    
    /* listeners are only set for the dynamic buttons (connect to diff devices)
     * 
     */
	public void onClick(View v) {
		if( mDevices.isEmpty()|| mState != eMenuState.LIST_DEVICES )
			return;
		
		BluetoothDevice device = mDevices.get(v.hashCode());
		BluetoothManager.instance().connect(device);
	}
	
	public void onBluetoothConnectCallback(boolean bConnected){
		if( !bConnected ){
			Toast.makeText(this, "Couldn't connect!", Toast.LENGTH_SHORT).show();
			return;
		}

		Toast.makeText(this, "connected!", Toast.LENGTH_SHORT).show();
		if( BluetoothManager.instance().getConnectionType() == BluetoothManager.eSocketType.CLIENT){
			loadJoinAsState();
		}
		else if( BluetoothManager.instance().getConnectionType() == BluetoothManager.eSocketType.SERVER){
			//TODO -- indicate the selected device is connected (highlight button)
			mBtContinue.setVisibility(View.VISIBLE);
		};
	}
}





class MyAnimatorListener implements AnimatorListener{
	private RelativeLayout view;
	public MyAnimatorListener(RelativeLayout v){
		view = v;
	}
	public void onAnimationEnd(Animator arg0) {
		view.setVisibility(View.GONE);
		view.removeAllViews();
	}
	public void onAnimationCancel(Animator arg0) {}
	public void onAnimationRepeat(Animator arg0) {}
	public void onAnimationStart(Animator arg0) {}
}