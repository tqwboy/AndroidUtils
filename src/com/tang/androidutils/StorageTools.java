package com.tang.androidutils;

import java.io.File;

import android.annotation.SuppressLint;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

public class StorageTools {
	/**
	 * 判断SD Card是否插入
	 * 
	 * @return true or false
	 */
	public static boolean isExistSdCard() {
		boolean sdCardExit = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);

		return sdCardExit;
	}

	/**
	 * 获取SD卡根目录路径
	 * @return String SD卡根目录路径(结尾带"/")
	 */
	public static String getSdCardPath() {
		return Environment.getExternalStorageDirectory().toString()
				+ File.separator;
	}
	
	/**
	 * 获取手机内部剩余存储空间
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static long getAvailableInternalDiskSize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		
		long availableSize = 0L;
		
		if( VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2 ) {
			availableSize = stat.getAvailableBytes();
		}
		else {
			int blockSize = stat.getBlockSize();
			int availableBlocks = stat.getAvailableBlocks();
			
			availableSize = blockSize * availableBlocks;
		}
		
		return availableSize;
	}

	/**
	 * 获取手机内部总的存储空间
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static long getTotalInternalDiskSize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());

		long diskTotalSize = 0L;
		
		if( VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2 ) {
			diskTotalSize = stat.getTotalBytes();
		}
		else {
			int blockSize = stat.getBlockSize();
			int availableBlocks = stat.getBlockCount();
			
			diskTotalSize = blockSize * availableBlocks;
		}
		
		return diskTotalSize;
	}

	/**
	 * 获取Sdcard剩余存储空间
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static long getAvailableSdcardSize() {
		long availableSize = 0L;
		
		if (isExistSdCard()) {
			StatFs stat = new StatFs(getSdCardPath());

			if( VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2 ) {
				availableSize = stat.getAvailableBytes();
			}
			else {
				int blockSize = stat.getBlockSize();
				int availableBlocks = stat.getAvailableBlocks();
				
				availableSize = blockSize * availableBlocks;
			}
		} 
		
		return availableSize;
	}

	/**
	 * 获取Sdcard的总存储空间
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static long getTotalSdcardSize() {
		long diskTotalSize = 0L;
		
		if (isExistSdCard()) {
			StatFs stat = new StatFs(getSdCardPath());
			
			if( VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2 ) {
				diskTotalSize = stat.getTotalBytes();
			}
			else {
				int blockSize = stat.getBlockSize();
				int availableBlocks = stat.getBlockCount();
				
				diskTotalSize = blockSize * availableBlocks;
			}
		} 
		
		return diskTotalSize;
	}
	
	/**
	 * 计算对应目录的可用空间
	 * 
	 * @return 剩余空间大小（单位：兆字节）
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static double freeSpaceOnDir(File file) {
		StatFs stat = null;

		if (file.exists() && file.isDirectory())
			stat = new StatFs(file.getPath());
		else
			return 0;

		double sdFreeMB = 0.0;
		if (null != stat) {
			if (VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1)
				sdFreeMB = (stat.getAvailableBlocksLong() * stat
						.getBlockSizeLong());
			else
				sdFreeMB = (stat.getAvailableBlocks() * stat.getBlockSize());
		}

		return sdFreeMB;
	}
	
	/**
	 * 计算对应目录的可用空间
	 * 
	 * @return 剩余空间大小（单位：字节）
	 */
	public static double freeSpaceOnDir(String path) {
		return freeSpaceOnDir(new File(path));
	}
}