package com.tang.androidutils;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class SystemTools {
	/**
	 * 扫描指定的多媒体文件
	 * @param context App上下文
	 * @param filePath 要扫描的路径
	 * @param fileType 要扫描的文件类型
	 */
	public static void ScanningMediaFile(Context context, String filePath, String fileType) {
		String typeKey = null;
		String dataKey = null;
		Uri scanUri = null;

		String[] typeStrs = fileType.split("/");

		if (typeStrs[0].equals("image")) {
			typeKey = MediaColumns.MIME_TYPE;
			dataKey = MediaColumns.DATA;
			scanUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		}
		else if (typeStrs[0].equals("video")) {
			typeKey = MediaColumns.MIME_TYPE;
			dataKey = MediaColumns.DATA;
			scanUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
		}
		else if (typeStrs[0].equals("audio")) {
			typeKey = MediaColumns.MIME_TYPE;
			dataKey = MediaColumns.DATA;
			scanUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		}

		if (null != scanUri) {
			ContentValues values = new ContentValues(2);
			values.put(typeKey, fileType);
			values.put(dataKey, filePath);

			context.getContentResolver().insert(scanUri, values);
			context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri
					.parse("file://" + filePath)));
		}
		else if (StorageTools.isExistSdCard()) {
			ScanningExternal(context, Environment.getExternalStorageDirectory());
		}
	}

	/** 扫描存储卡目录 */
	public static void ScanningExternal(Context context, String scanPath) {
		ScanningExternal(context, new File(scanPath));
	}

	public static void ScanningExternal(Context context, File file) {
		Uri uri = Uri.parse("file://" + file);
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, uri));
	}

	/** 判断程序是否运行与后台 */
	public static boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND)
					return true;
				else
					return false;
			}
		}
		return false;
	}

	/** 是否进入到安装界面 */
	public static boolean isInstallAPK(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = activityManager.getRunningTasks(1).get(0).topActivity;

		if (cn.getPackageName().equals("com.android.packageinstaller")) {
			return true;
		}
		return false;
	}

	/** 是否插入sim卡 */
	public static boolean isSim(Context context) {
		TelephonyManager mgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		if (TelephonyManager.SIM_STATE_READY == mgr.getSimState())
			return true;
		else
			return false;
	}

	/** 根据手机的分辨率从 dp 的单位 转成为 px(像素) */
	public static int dip2px(Context context, float dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/** 根据手机的分辨率从 px(像素) 的单位 转成为 dp */
	public static int px2dip(Context context, float pxValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/** 获取屏幕宽度 */
	public static int getScreenWidth(Resources res) {
		DisplayMetrics displayMetrics = res.getDisplayMetrics();
		return displayMetrics.widthPixels;
	}

	/** 获取屏幕宽度 */
	public static int getScreenWidth(Context context) {
		return getScreenWidth(context.getResources());
	}

	/** 获取屏幕高度 */
	public static int getScreenHeight(Resources res) {
		DisplayMetrics displayMetrics = res.getDisplayMetrics();
		return displayMetrics.heightPixels;
	}

	/** 获取屏幕高度 */
	public static int getScreenHeight(Context context) {
		return getScreenHeight(context.getResources());
	}

	/**
	 * 通过反射获取资源id
	 * 
	 * @param rClass 资源类别
	 * @param name 资源名称
	 * @return
	 */
	public static int getRId(Class<?> rClass, String name) {
		int id = 0;
		try {
			id = rClass.getField(name).getInt(rClass);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	/** 隐藏键盘 */
	public static void hideSoftInput(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	
	/**
	 * 打开相机拍照，并返回照片数据（在Activity中的onActivityResult获取到数据）
	 * 
	 * @param activity App的Activity
	 * @param requestCode 请求编号
	 */
	public static void openCamera(Activity activity, int requestCode) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		activity.startActivityForResult(intent, requestCode);
	}
	
	/**
	 * 打开系统切割图片程序，切割方式为按照宽高切割
	 * 
	 * @param activity App的Activity
	 * @param uri 图片文件保存路径Uri
	 * @param requestCode 请求编号
	 * @param outputX 宽度
	 * @param outputY 高度
	 */
	public static void startPhotoZoomByWh(Activity activity, Uri uri, int requestCode,
			int outputX, int outputY) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("return-data", true);
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 打开系统切割图片程序，切割方式为按照设定比例切割
	 * 
	 * @param activity App的Activity
	 * @param uri 图片文件保存路径Uri
	 * @param requestCode 请求编号
	 * @param aspectX 宽度比例
	 * @param aspectY 高度比例
	 */
	public static void startPhotoZoomByAspect(Activity activity, Uri uri,
			int requestCode, int aspectX, int aspectY) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", aspectX);
		intent.putExtra("aspectY", aspectY);
		intent.putExtra("return-data", true);
		activity.startActivityForResult(intent, requestCode);
	}
}