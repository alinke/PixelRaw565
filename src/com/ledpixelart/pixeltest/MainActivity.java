package com.ledpixelart.pixeltest;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


import ioio.lib.api.AnalogInput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ParserError")
public class MainActivity extends IOIOActivity   {
	
	//This sample code simply writes to the LED matrix but in code as opposed by loading a PNG or raw 565 file
	
	private static ioio.lib.api.RgbLedMatrix matrix_;
	private static ioio.lib.api.RgbLedMatrix.Matrix KIND;  //have to do it this way because there is a matrix library conflict
	private AnalogInput prox_;
    private static final String LOG_TAG = "PixelTest";	  	
  	private static short[] frame_ = new short[512];
  	private static byte[] BitmapBytes;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.main);
		KIND = ioio.lib.api.RgbLedMatrix.Matrix.SEEEDSTUDIO_32x32; //v2		
	    frame_ = new short [KIND.width * KIND.height];
		BitmapBytes = new byte[KIND.width * KIND.height *2]; //512 * 2 = 1024 or 1024 * 2 = 2048			 
	    loadRGB565(); 
        
	}
	
	
	private void loadRGB565() {

   		
   		for (int i = 0; i < frame_.length; i++) {
   		
   			frame_[i] = (short) (((short) 0x00000000 & 0xFF) | (((short) (short) 0x00000000 & 0xFF) << 8));  //all black
   			//frame_[i] = (short) (((short) 0xFFF5FFB0 & 0xFF) | (((short) (short) 0xFFF5FFB0 & 0xFF) << 8));  //pink
   			//frame_[i] = (short) (((short) 0xFFFFFFFF & 0xFF) | (((short) (short) 0xFFFFFFFF & 0xFF) << 8));  //all white
   		}
   }
   
   
    class IOIOThread extends BaseIOIOLooper {
  		//private ioio.lib.api.RgbLedMatrix matrix_;
    	public AnalogInput prox_;

  		@Override
  		protected void setup() throws ConnectionLostException {
  			matrix_ = ioio_.openRgbLedMatrix(KIND);
  			prox_ = ioio_.openAnalogInput(32);	//if you comment out this line, no artifacts appear
  		   //loadRGB565(); //this function loads a raw RGB565 image to the matrix
  		   matrix_.frame(frame_); //write select pic to the frame since we didn't start the timer
  		}

  		//@Override
  	//	public void loop() throws ConnectionLostException {
  		
  			//try {
  				
  	  			//proxValue = prox_.read();
  	  			//proxValue = proxValue * 1000;	
  	  			//int proxInt = (int)proxValue;
  	  			
  	  			//if (showProx_ == true) {
  	  				//setText(Integer.toString(proxInt));
  	  			//}
  	  			
  	  			
	  	  	//if ((proxOn_ == true) && (Playing == 1 ) && (proxValue >= proximityThresholdLower_) && (proxValue <= proximityThresholdUpper_) && (proxTriggeredFlag == 0)) { //we're in range so let's trigger
	  	  		//		proxTriggeredFlag = 1;
	  	  		//		proxInteractive(); //let's now play the triggered prox animation
	  	  	//}
  	  		
	  	  			
  	  			//if (proxTriggeredFlag == 0) { //if we're in range
	  				//proxTriggeredFlag = 1;
	  				//loadProxImage2();
	  			//}
  	  			
  	  			
  					
  				//	Thread.sleep(10);
  				//} catch (InterruptedException e) {
  				//	ioio_.disconnect();
  			//	}
  	//	}	//
  		
  		@Override
		public void disconnected() {   			
  			Log.i(LOG_TAG, "IOIO disconnected");						
	  		//showToast("Bluetooth Disconnected");
		}

		@Override
		public void incompatible() {  //if the wrong firmware is there	
			showToast("Incompatbile firmware!");
			showToast("This app won't work until you flash the IOIO with the correct firmware!");
			showToast("You can use the IOIO Manager Android app to flash the correct firmware");
			Log.e(LOG_TAG, "Incompatbile firmware!");
		}
  		
  		}

  	@Override
  	protected IOIOLooper createIOIOLooper() {
  		return new IOIOThread();
  	}
    
    private  void showToast(final String msg) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast toast = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG);
                toast.show();
			}
		});
	}  
    
    private void showToastShort(final String msg) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast toast = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT);
                toast.show();
			}
		});
	} 
    
   
	
}