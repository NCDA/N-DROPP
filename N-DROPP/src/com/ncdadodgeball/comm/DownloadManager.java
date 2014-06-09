package com.ncdadodgeball.comm;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import com.ncdadodgeball.ndropp.Global;
import com.ncdadodgeball.ndropp.MainActivity;
import com.ncdadodgeball.ndropp.R;
import com.ncdadodgeball.ndropp.R.string;
import com.ncdadodgeball.util.Log;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


/*	DownloadManager
 * 	Class meant to handle all download processes within the application.
 */
public class DownloadManager
{
//	/**	establishDirectory
//	 * 	Creates this application's personal directory on the device's "external" (sdcard)
//	 * 	storage device if it currently does not exist.  If it does exist, function returns.
//	 */
//	private static void establishDirectory(){
//		//create directory if not already created
//		File fPackageDir = new File(AppGlobals.EXTERNAL_DIR);
//		if(!fPackageDir.exists())
//			fPackageDir.mkdirs();
//		fPackageDir = null;
//	}
	
	/**	DownloadRulebook
	 * 	
	 * 	@return true if the rulebook exists in the application's external directory
	 * 
	 * 	Static function that checks to see if the rulebook exists in the application's external
	 * 	directory.  If it doesn't exist, the rulebook is downloaded from the NCDA website.
	 */
	public static boolean DownloadRulebook(){
//		establishDirectory(context);
		Activity main = MainActivity.sInstance;
		File fRulebook = new File(
				Global.getExternalDir(main) + "/" + main.getString(R.string.file_rulebook));
		
		//Download the rulebook if it's not on the device
		if(!fRulebook.exists()){
			DownloadRulebookTask task = new DownloadRulebookTask(fRulebook);
			Thread taskThread = new Thread(task);
			taskThread.start();
			try {
				taskThread.join();
				Log.D("Thread joined");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return fRulebook.exists();
		}
		else
			return true;	//rulebook exists, return true
	}
	
	static class DownloadRulebookTask implements Runnable
	{
		
		File fRulebook;
		
		public DownloadRulebookTask(File file){
			fRulebook = file;
		}

		public void run(){
			Log.D("Downloading Rulebook...");
			//Toast.makeText(context, "Downloading Rulebook...", Toast.LENGTH_SHORT).show();
			URL url = null;
			HttpURLConnection socket= null;
			boolean bTryAgain = true;
			
			while(bTryAgain){
				try {
					url = new URL(MainActivity.sInstance.getString(R.string.url_rulebook));
					socket = (HttpURLConnection) url.openConnection();
					socket.setInstanceFollowRedirects(true);
					socket.setDoInput(true);
					socket.connect();
					InputStream istream = new BufferedInputStream(socket.getInputStream(), 8*1024);
					OutputStream ostream = new FileOutputStream(fRulebook.getAbsolutePath(), false);
					byte [] input = new byte[Global.NUM_DL_BUFF_SIZE];
					int numBytes = istream.read(input, 0, Global.NUM_DL_BUFF_SIZE); 
					while(numBytes != -1){
						ostream.write(input, 0, numBytes);
						numBytes = istream.read(input, 0, Global.NUM_DL_BUFF_SIZE); 
					}
					
					bTryAgain = false;
					istream.close();
					ostream.close();
					Log.D("Download complete.");
					//Toast.makeText(MainActivity.sInstance, "Download complete.", Toast.LENGTH_SHORT).show();
				}
				//catch no internet connection. need better way to get user input
				catch (UnknownHostException uhe){
					/* TODO: launch dialog: try again, cancel */
					System.exit(0);
				}
				catch (Exception e) {
					/* TODO: launch dialog: try again, cancel */
					throw new RuntimeException(e.getMessage());
				}
				finally{
					socket.disconnect();
				}
			}
		}
	}
}
