package com.telpo.pospay.main.util;

public class TripleDesClass {
	
	/**
	 * 3DES加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 */
    public static String encrypt(String data, String key) {
    	if(key.length() == 16) {
    		key += key.substring(0, 8);
    	}
    	byte[] d = hexToByteArray(data);
    	byte[] k = hexToByteArray(key);
    	
    	javax.crypto.SecretKey sk = new javax.crypto.spec.SecretKeySpec(k, "DESede");
    	try {
    		javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DESede/ECB/NoPadding");
    		cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, sk);
			byte[] enc = cipher.doFinal(d);
			return toHexString(enc);
		} catch (Exception e) {
			e.printStackTrace();
		} 
    	
    	return null;
    }
    
	/**
	 * 3DES解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 */
    public static String decrypt(String data, String key) {
    	if(key.length() == 16) {
    		key += key.substring(0, 8);
    	}
    	byte[] d = hexToByteArray(data);
    	byte[] k = hexToByteArray(key);
    	
    	javax.crypto.SecretKey sk = new javax.crypto.spec.SecretKeySpec(k, "DESede");
    	try {
    		javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DESede/ECB/NoPadding");
    		cipher.init(javax.crypto.Cipher.DECRYPT_MODE, sk);
			byte[] enc = cipher.doFinal(d);
			return toHexString(enc);
		} catch (Exception e) {
			e.printStackTrace();
		} 
    	
    	return null;
    }
    
    
    private static byte[] hexToByteArray(String s) {
		if(s == null) {
			s = "";
		}
		java.io.ByteArrayOutputStream bout = new java.io.ByteArrayOutputStream();
		for(int i = 0; i < s.length() - 1; i += 2) {
			String data = s.substring(i, i + 2);
			bout.write(Integer.parseInt(data, 16));
		}
		return bout.toByteArray();
	}
    
    private static String toHexString(byte[] b) {
		if(b == null) {
			return "null";
		}
		String result = "";
		for (int i=0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xFF) + 0x100, 16).substring( 1 );
		}
		return result;
	}
    
}
