package com.tang.androidutils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class NetTools {
    public static final int	NETTYPE_WIFI	= 0x0001;
	public static final int	NETTYPE_2G		= 0x0002;
	public static final int	NETTYPE_3G		= 0x0003;

	/**
	 * 获取当前网络类型
	 * 
	 * @return 0：没有网络; 1：WIFI网络; 2：2G网络; 3：3G网络
	 */
    public static int getNetworkType( Context context ) {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService( Context.CONNECTIVITY_SERVICE );
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		
		if (networkInfo == null)
			return netType;
		
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			int nSubType = networkInfo.getSubtype();
			if (nSubType == TelephonyManager.NETWORK_TYPE_1xRTT
					|| nSubType == TelephonyManager.NETWORK_TYPE_CDMA
					|| nSubType == TelephonyManager.NETWORK_TYPE_EDGE
					|| nSubType == TelephonyManager.NETWORK_TYPE_GPRS
					|| nSubType == TelephonyManager.NETWORK_TYPE_UNKNOWN
					|| nSubType == TelephonyManager.NETWORK_TYPE_IDEN) {
				netType = NETTYPE_2G;
			}
			else if (nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
					|| nSubType == TelephonyManager.NETWORK_TYPE_EVDO_A
					|| nSubType == TelephonyManager.NETWORK_TYPE_EVDO_B
					|| nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
					|| nSubType == TelephonyManager.NETWORK_TYPE_HSPA
					|| nSubType == TelephonyManager.NETWORK_TYPE_HSPAP
					|| nSubType == TelephonyManager.NETWORK_TYPE_HSUPA
					|| nSubType == TelephonyManager.NETWORK_TYPE_EHRPD
					|| nSubType == TelephonyManager.NETWORK_TYPE_UMTS
					|| nSubType == TelephonyManager.NETWORK_TYPE_LTE) {
				netType = NETTYPE_3G;
			}
			else {
				netType = NETTYPE_2G;
			}

		}
		else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		
		return netType;
	}
    
    /**
	 * 返回Wifi是否启用
	 * 
	 * @param context上下文
	 * @return Wifi网络可用则返回true，否则返回false
	 */
	public static boolean isWIFIActivate(Context context) {
		return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
				.isWifiEnabled();
	}
    
    /**
	 * 返回网络是否可用
	 * 
	 * @param context 上下文
	 * @return 网络可用则返回true，否则返回false
	 */
	public static boolean isNetworkConnected( Context context ) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService( Context.CONNECTIVITY_SERVICE );
		NetworkInfo info = cm.getActiveNetworkInfo();
		return info != null && info.isConnectedOrConnecting();
	}

    /**
     * 获取物理地址
     */
    public String getMacAddress(WifiManager wifiManager) {
    	WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    	return (wifiInfo == null) ? "NULL" : wifiInfo.getMacAddress();
    }

    /**
     * 获取BSSID
     */
    public String getBSSID(WifiManager wifiManager) {
    	WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    	return (wifiInfo == null) ? "NULL" : wifiInfo.getBSSID();
    }

    /**
     * 获ip地址
     */
    public String getIpAddress(WifiManager wifiManager) {
    	WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    	
		if (wifiInfo == null) {
		    return String.valueOf(0);
		} 
		else {
		    int ip = wifiInfo.getIpAddress();
		    return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "."
			    + ((ip >> 16) & 0xFF) + "." + (ip >> 24 & 0xFF);
		}
    }

    /**
     * 获取ip地址
     */
    public int getIp(Context context) {
    	WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    	WifiInfo wifiInfo = null==wifiManager ? null:wifiManager.getConnectionInfo();
    	
  		if (wifiInfo == null)
  		    return 0;
  		else
  		    return wifiInfo.getIpAddress();
    }
    /** 
     * 得到连接的ID 
     */
    public int getNetWordId(WifiManager wifiManager) {
    	WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    	return (wifiInfo == null) ? 0 : wifiInfo.getNetworkId();
    }

    /**
     * 得到WifiInfo的所有信息
     */
    public String getWifiInfo(WifiManager wifiManager) {
    	WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    	return (wifiInfo == null) ? "NULL" : wifiInfo.toString();
    }

    /**
     * 添加一个网络并连接
     * 
     * @param configuration
     */
    public void addNetWork(WifiManager wifiManager, WifiConfiguration configuration) {
    	int wcgId = wifiManager.addNetwork(configuration);
    	wifiManager.enableNetwork(wcgId, true);
    }

    /**
     * 断开指定ID的网络
     */
    public void disConnectionWifi(WifiManager wifiManager, int netId) {
    	wifiManager.disableNetwork(netId);
    	wifiManager.disconnect();
    }

    /**
     * 判断wifi是否打开
     */
    public boolean isWifiEnabled(Context context) {
    	WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    	return wifiManager.isWifiEnabled();
    }

    /**
     * 跳转到wifi设置界面
     */
    public static void gotoWifiSettings(Context context) {
		if (android.os.Build.VERSION.SDK_INT > 10) {
		    // 3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
		    // context.startActivity(new
		    // Intent(android.provider.Settings.ACTION_SETTINGS));
		    context.startActivity(new Intent(
			    android.provider.Settings.ACTION_WIFI_SETTINGS));
		} 
		else {
		    context.startActivity(new Intent(
			    android.provider.Settings.ACTION_WIFI_SETTINGS));
		}
    }
    
    /**
     * 获掩码地址
     * 
     * @return
     */
    public int getNetmask(Context context) {
    	WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    	
		if (wifiManager == null) 
		    return 0xffffffff;
		else 
		    return wifiManager.getDhcpInfo().netmask;
    }
    
    /**
     * 获网关地址
     * 
     * @return
     */
    public int getGateway(Context context) {
    	WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    	
		if (wifiManager == null)
		    return 0;
		else
		    return wifiManager.getDhcpInfo().gateway;
    }
    
    /**
     * 是否为同一个ip地址段
     */
    public boolean isHostIp(String strIp, Context context) {
    	if ( TextUtils.isEmpty( strIp ) )
    		return false;
    	
    	long[] ip = new long[4];
    	
	    //先找到IP地址字符串中.的位置
	    int position1 = strIp.indexOf(".");
	    int position2 = strIp.indexOf(".", position1 + 1);
	    int position3 = strIp.indexOf(".", position2 + 1);
	    
	    //将每个.之间的字符串转换成整型
	     ip[0] = Long.parseLong(strIp.substring(0, position1));
	     ip[1] = Long.parseLong(strIp.substring(position1+1, position2));
	     ip[2] = Long.parseLong(strIp.substring(position2+1, position3));
	     ip[3] = Long.parseLong(strIp.substring(position3+1));
	     
	    long host = (ip[0]) + (ip[1] << 8) + (ip[2] << 16) + (ip[3] << 24);
	    long netmask = getNetmask(context);
	    long local = getIp(context);
	    
	    if ((host & netmask) == (local & netmask))
	    	return true;

	    return false;
    }
}