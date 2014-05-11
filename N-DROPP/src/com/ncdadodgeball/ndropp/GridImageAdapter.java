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

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/*	GridImageAdapter
 * 	ListAdapter that keeps track of the image elements in the gridview
 */
class GridImageAdapter extends BaseAdapter
{
	private Activity	mParentActivity;
	private ImageView 	images[];
	private String		strResource;
	private double		nScreenWPercent;	//percent of width of screen the entire grid should occupy
	private double		nScreenHPercent;	//percent of height of screen the entire grid should occupy
	
	/** GridImageAdapter
	 * 
	 * @param ctx : application context
	 * 
	 * Create an adapter list of 15 elements
	 */
	public GridImageAdapter(Activity parent, String resourceName, double widthPercent, double heightPercent){
		mParentActivity = parent;
		images = new ImageView[15];
		strResource = resourceName;
		nScreenWPercent = widthPercent;
		nScreenHPercent = heightPercent;
	}

	/** getCount
	 * 	return size of gridview
	 */
	public int getCount() {
		return images.length;
	}

	/** getItem
	 *  
	 *  @param index : array index of element
	 *  
	 *  @return ImageView of the associated element
	 */
	public ImageView getItem(int index) {
		return images[index];
	}

	/** getItemId
	 * 	
	 * 	@param index : array index of element in the gridview
	 * 
	 *	@return ImageView id of the assiciated ImageView
	 */
	public long getItemId(int index) {
		return images[index].getId();
	}

	/** getView
	 * 	
	 *	@param index : index of element
	 *	@param view : view to store at the specified index
	 *	@param parent : parent view
	 *
	 *	@return the created ImageView at the specific location
	 */
	public View getView(int index, View view, ViewGroup vParent) {
		
		ImageView imageView = images[index];
		
		if( imageView == null ){
			imageView = new ImageView(mParentActivity);
			int imgID = (mParentActivity.getResources().getIdentifier(strResource, "drawable", mParentActivity.getString(R.string.app_package) ) );
	        imageView.setImageResource(imgID);
	        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
	        Display display = mParentActivity.getWindowManager().getDefaultDisplay();
	        imageView.setLayoutParams(new GridView.LayoutParams(
	        		(int)(( display.getWidth() * nScreenWPercent) / 5 ), 
	        		(int)(( display.getHeight() * nScreenHPercent) / 3 )));
	        imageView.setClickable(false);
	        imageView.setFocusable(false);
	        imageView.setSelected(false);
	        images[index] = imageView;
		}
        return imageView;
	}
}