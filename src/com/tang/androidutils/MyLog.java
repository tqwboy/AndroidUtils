package com.tang.androidutils;

public class MyLog {
	public static final boolean DEBUG = true;
	public static final String TAG = "测试";
	
	public static void d(String tag, String msg){
		if(DEBUG)
			android.util.Log.d(TAG, "[" + tag + "]" + msg);
	}
	
	public static void v(String tag, String msg){
		if(DEBUG)
			android.util.Log.v(TAG, "[" + tag + "]" + msg);
	}
	
	public static void i(String tag, String msg){
		if(DEBUG)
			android.util.Log.i(TAG, "[" + tag + "]" + msg);
	}
	
	public static void e(String tag, String msg){
		if(DEBUG)
			android.util.Log.e(TAG, "[" + tag + "]" + msg);
	}
	
	public static void w(String tag, String msg){
		if(DEBUG)
			android.util.Log.w(TAG, "[" + tag + "]" + msg);
	}
}