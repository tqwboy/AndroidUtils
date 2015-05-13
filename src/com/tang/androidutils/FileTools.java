package com.tang.androidutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.text.TextUtils;

public class FileTools {
	public static final String TAG = "FileTools";

	/**
	 * 从路径中获取文件名
	 */
	public static String getFileNameFromPath(String path) {
		String fileName = null;

		String[] strTemp = path.split(File.separator);
		if (TextUtils.isEmpty(strTemp[strTemp.length - 1]) && strTemp.length > 2)
			fileName = strTemp[strTemp.length - 2];
		else
			fileName = strTemp[strTemp.length - 1];

		return fileName;
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param 文件路径
	 */
	public static boolean fileExists(String path) {
		File file = new File(path);
		boolean result = false;
		if (file.exists())
			result = true;

		return result;
	}

	/**
	 * 变更文件名
	 * 
	 * @param oldName 原来的名字（需要包含路径）
	 * @param newName 新的名字（需要包含路径）
	 * @return true修改成功，false修改失败
	 */
	public static boolean renameFile(String oldName, String newName) {
		boolean result = false;
		File file = new File(oldName);
		result = file.renameTo(new File(newName));
		return result;
	}

	/**
	 * 复制文件
	 * 
	 * @param 原路径
	 * @param 新路径
	 */
	public static synchronized boolean copyFile(String oldPath, String newPath) {
		FileInputStream fis = null;
		FileOutputStream fos = null;

		try {
			fis = new FileInputStream(oldPath);
			fos = new FileOutputStream(new File(newPath));
			byte[] buffer = new byte[2048];
			int len;
			while ((len = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			return true;
		}
		catch (FileNotFoundException ex) {
			MyLog.e(TAG, "copyFile,Source File not found:" + oldPath);
		}
		catch (IOException ex) {
			MyLog.e(TAG, "copyFile error:\n" + ex.toString());
		}
		finally {
			try {
				if (fis != null)
					fis.close();
				if (fos != null)
					fos.close();
			}
			catch (IOException ex) {
				MyLog.e(TAG, "copyFile stream close error:\n" + ex.toString());
			}
		}

		return false;
	}

	/**
	 * 创建文件夹
	 */
	public static boolean mkDir(String path) {
		File file = new File(path);
		return file.mkdir();
	}

	/**
	 * 删除全部文件
	 * 
	 * @param 文件路径(末尾不要有"/")
	 */
	public static void deleteAllFile(String path) {
		deleteAllFile(path, null);
	}

	/**
	 * 删除全部文件
	 * 
	 * @param 文件路径(末尾不要有"/")
	 * @param 文件路径下，不删除的文件或文件夹
	 * @param
	 */
	public static void deleteAllFile(String path, String args) {
		try {
			File f = new File(path);
			if (!f.exists())
				return;

			else if (null == args || !args.equals(path + File.separator)) {
				if (!f.isDirectory()) {
					deleteFile(path);
				}
				else {
					String[] tempList = f.list();
					for (int i = 0; i < tempList.length; i++) {
						String nowPath = path + File.separator + tempList[i];
						deleteAllFile(nowPath, args);
					}

					f.delete();
				}
			}
		}
		catch (Exception e) {
			MyLog.e(TAG, e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 删除指定文件
	 * 
	 * @param fileName
	 */
	public static boolean deleteFile(String filePath) {
		boolean delectResult = false;

		if (!TextUtils.isEmpty(filePath)) {
			try {
				File f = new File(filePath);
				if (f.exists() && f.isFile())
					delectResult = f.delete();
			}
			catch (Exception e) {
				// TODO
				MyLog.e(TAG, e.toString());
			}
		}

		return delectResult;
	}

	/**
	 * 复制资源文件
	 * 
	 * @param context App上下文
	 * @param fileName 文件名
	 * @param newPath 存放路径
	 */
	public static boolean copyAssetsFile(Context context, String fileName, String newPath) {
		InputStream is = null;
		FileOutputStream fos = null;

		try {
			is = context.getClass().getResourceAsStream("/assets/" + fileName);
			fos = new FileOutputStream(newPath);
			byte[] buffer = new byte[2048];
			int len;
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			return true;
		}
		catch (FileNotFoundException ex) {
			MyLog.e(TAG, "copyFile,Source File not found:" + fileName);
		}
		catch (IOException ex) {
			MyLog.e(TAG, "copyFile error:\n" + ex.toString());
		}
		finally {
			try {
				if (is != null)
					is.close();
			}
			catch (IOException ex) {
				MyLog.e(TAG, "copyFile stream close error:\n" + ex.toString());
			}

			try {
				if (fos != null)
					fos.close();
			}
			catch (IOException ex) {
				MyLog.e(TAG, "copyFile stream close error:\n" + ex.toString());
			}
		}

		return false;
	}
}