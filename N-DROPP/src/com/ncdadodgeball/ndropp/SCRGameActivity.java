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

import com.ncdadodgeball.ndropp.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class SCRGameActivity extends Activity{
	
	public static SCRGameActivity	sInstance;
	ButtonListener	m_Listener;
	GameSettings 	m_Settings;
	ShotClock		m_ShotClock;
	Clock			m_GameClock;
	Button			m_btStartReset;
	Button			m_btPauseResume;
	
//	public GameActivity(GameSettings settings){
//		m_Settings = settings;
//		m_ClockText = (TextView)findViewById(R.id.txtClock);
//		m_ShotClock = new Clock(m_ClockText, true, 15000);
//		m_Button = null;
//	}
	
	public SCRGameActivity(){
		m_Settings = new GameSettings();
		m_Listener = new ButtonListener();
		m_ShotClock = null;
		m_btStartReset = null;
		m_btPauseResume = null;
	}
	
	@Override
	 public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shot_clock_ref);
        sInstance = this;
        TextView clockText = (TextView)findViewById(R.id.txtShotClock);
        m_btStartReset 	= (Button)findViewById(R.id.btStartReset);
        m_btPauseResume	= (Button)findViewById(R.id.btPauseShotClock);
        
        m_ShotClock = new ShotClock(m_btStartReset, m_btPauseResume, clockText, 15000, Clock.CENTISEC, false);
        m_btStartReset.setOnClickListener(m_Listener);
        m_btPauseResume.setOnClickListener(m_Listener);
        m_btPauseResume.setClickable(false);
        m_btPauseResume.setBackgroundColor(Color.GRAY);
        
        //create gidview of players
        GridView teamGrid = (GridView) findViewById(R.id.gridTeam);
        teamGrid.setHorizontalSpacing((int)((getWindowManager().getDefaultDisplay().getWidth()*0.7)/5)/5);
        teamGrid.setColumnWidth(25);
        teamGrid.setMinimumWidth(100);
        teamGrid.setMinimumHeight(150);
        //teamGrid.setBackgroundColor(Color.WHITE);
        teamGrid.setAdapter(new GridImageAdapter(this));   
        
        //change shot clock font
    	clockText.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
    	clockText.setTextSize(32);
    	
    }

	@Override
	public void onBackPressed(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setMessage("Are you sure you want to quit?  All current game content will be lost.");
		alert.setCancelable(true);
		alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		alert.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int id){
				SCRGameActivity.this.finish();
			}
		});
		AlertDialog dialog = alert.create();
		dialog.show();
	}
	
	public GameSettings getSettings() {
		return m_Settings;
	}
	public Clock getGameTimer() {
		return m_GameClock;
	}
	public ShotClock getShotClock() {
		return m_ShotClock;
	}
	public void setShotClock(ShotClock shotClock) {
		m_ShotClock = shotClock;
	}
	
	class ButtonListener implements OnClickListener{

		public void onClick(View view) {
			
			//START/RESET BUTTON
			if(view.getId() == findViewById(R.id.btStartReset).getId())
				(SCRGameActivity.sInstance.getShotClock()).ieResetStartRestart();
			
			//PAUSE/RESUME BUTTON
			else if(view.getId() == findViewById(R.id.btPauseShotClock).getId())
				(SCRGameActivity.sInstance.getShotClock()).iePauseResumeReset();
		}
    }
}

class GridImageAdapter extends BaseAdapter
{
	private Context context;
	View images[];
	
	public GridImageAdapter(Context ctx){
		context=ctx;
		images = new View[15];
	}

	public int getCount() {
		return images.length;
	}

	public Object getItem(int index) {
		return images[index];
	}

	public long getItemId(int index) {
		return images[index].getId();
	}

	public View getView(int index, View view, ViewGroup parent) {
		ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.silhouette_blue);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Display display = SCRGameActivity.sInstance.getWindowManager().getDefaultDisplay();
        imageView.setLayoutParams(new GridView.LayoutParams((int)((display.getWidth()*0.7)/5), (int)((display.getHeight()*.25)/3)));
        //imageView.setLayoutParams(new GridView.LayoutParams(34, 76));
        imageView.setPadding(2, 2, 2, 2);
        return imageView;
	}
	
}
