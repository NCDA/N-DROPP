package com.ncdadodgeball.ndropp;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import android.widget.Toast;


public class DownloadManager
{
	private static void establishDirectory(){
		//create directory if not already created
		File fPackageDir = new File(AppGlobals.EXTERNAL_DIR + AppGlobals.PACKAGE);
		if(!fPackageDir.exists())
			fPackageDir.mkdirs();
		fPackageDir = null;
	}
	
	public static boolean DownloadRulebook(){
		establishDirectory();
		File fRulebook = new File(AppGlobals.EXTERNAL_DIR + AppGlobals.PACKAGE + "/" + AppGlobals.RULEBOOK_FILE);
		
		//Download the rulebook if it's not on the device
		if(!fRulebook.exists()){
			Log.D("Downloading Rulebook...");
			Toast.makeText(MainActivity.sInstance, "Downloading Rulebook...", Toast.LENGTH_SHORT);
			URL url = null;
			HttpURLConnection socket= null;
			boolean bTryAgain = true;
			
			while(bTryAgain){
				try {
					url = new URL(AppGlobals.RULEBOOK_URL);
					socket = (HttpURLConnection) url.openConnection();
					socket.setInstanceFollowRedirects(true);
					socket.setDoInput(true);
					socket.connect();
					InputStream istream = new BufferedInputStream(socket.getInputStream(), 8*1024);
					OutputStream ostream = new FileOutputStream(fRulebook.getAbsolutePath(), false);
					byte [] input = new byte[AppGlobals.NUM_DL_BUFF_SIZE];
					int numBytes = istream.read(input, 0, AppGlobals.NUM_DL_BUFF_SIZE); 
					while(numBytes != -1){
						ostream.write(input, 0, numBytes);
						numBytes = istream.read(input, 0, AppGlobals.NUM_DL_BUFF_SIZE); 
					}
					
					bTryAgain = false;
					istream.close();
					ostream.close();
					Log.D("Download complete.");
					Toast.makeText(MainActivity.sInstance, "Download complete.", Toast.LENGTH_SHORT);
				}
				//catch no internet connection. need better way to get user input
				catch (UnknownHostException uhe){
					/* TODO: launch dialog: try again, cancel */
					System.exit(0);
				}
				catch (Exception e) {
					/* TODO: launch dialog: try again, cancel */
					throw new RuntimeException("An error occured. Sorry :( ");
				}
				finally{
					socket.disconnect();
				}
			}
			
			return fRulebook.exists();	
		}
		else
			return true;	//rulebook exists, return true
	}
}
