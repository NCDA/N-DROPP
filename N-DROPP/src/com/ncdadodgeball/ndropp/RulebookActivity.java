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
import java.net.MalformedURLException;
import java.net.URL;

import com.ncdadodgeball.ndropp.R;


import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class RulebookActivity extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		File rootFilesDir = getFilesDir();
		Log.e("NCDA App directory internal directory: " + rootFilesDir.getAbsolutePath(), "");
		
		File rulebook = new File(rootFilesDir.getAbsolutePath() + "/rulebook.pdf");
		
		//DOWNLOAD RULEBOOK PDF -- WORKS!! :)
		URL url = null;
		HttpURLConnection socket= null;
		try {
			url = new URL("http://www.ncdadodgeball.com/rulebook/ncda-rules.pdf");
			socket = (HttpURLConnection) url.openConnection();
			socket.setInstanceFollowRedirects(true);
			socket.setDoInput(true);
			socket.connect();
			InputStream istream = new BufferedInputStream(socket.getInputStream());
			OutputStream ostream = new FileOutputStream(rulebook);
			byte [] input = new byte[1024];
			while(istream.read(input, 0, 1024) != -1){
				ostream.write(input);
			}
		}
		catch (Exception e) {
			Log.d("NCDA App", e.getMessage());
			e.printStackTrace();
		}
		finally{
			socket.disconnect();
		}
		
		
		//COPY DOWNLOADED PDF FROM PRIVATE MEMORY TO PUBLIC MEMORY --> NEEDED FOR TESTING. CONFIRMED --> IT WORKS !! :)
		File publicRulebook = new File((Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).getAbsolutePath() + "/rulebook.pdf");
		
		try{
			InputStream istream = new FileInputStream(rulebook.getAbsolutePath());
			OutputStream ostream = new FileOutputStream(publicRulebook);
			byte [] input = new byte[1024];
			
			while(istream.read(input, 0, 1024) != -1){
				ostream.write(input);
			}
			ostream.close();
			istream.close();
		}
		catch(Exception e){
			Log.d("NCDA app", e.getMessage());
		}
		
		setContentView(R.layout.rulebook);		//still no rulebook yet. to come, to come.

	}
}
