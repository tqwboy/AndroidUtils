package com.tang.androidutils.encry;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.tang.androidutils.MyLog;

/**
 * SHA加密
 * 
 * @author Hohenheim
 */
public class SHA {
	public String getSha256Str( String strSrc ){
		byte[] encryBytes = encry( strSrc, true );
		return bytes2Hex( encryBytes );
	}
	public byte[] getSha256Bytes( String strSrc ){
		return encry( strSrc, true );
	}
	
	public String sha1( String strSrc ){
		byte[] encryBytes = encry( strSrc, false );
		return bytes2Hex( encryBytes );
	}
	
	private byte[] encry( String strSrc, boolean is256 ){
        String encName = "SHA-1";
        byte[] encryBytes = null;

        try {
            if (is256) 
                encName = "SHA-256";

            byte[] bt = strSrc.getBytes();
            MessageDigest md = MessageDigest.getInstance(encName);
            md.update(bt);
            encryBytes = md.digest();
        } 
        catch (NoSuchAlgorithmException e) {
            //TODO
        	MyLog.e( "SHA", e.toString() );
        }
        
        return encryBytes;
	}
	
	private String bytes2Hex(byte[] bts) {
        StringBuilder des = new StringBuilder();

        for (int i = 0; i < bts.length; i++) {
        	if ((bts[i] & 0xff) < 0x10)  
        		des.append("0");    
            des.append(Long.toString(bts[i] & 0xff, 16));  
        }
        
        return des.toString();
    }
}
