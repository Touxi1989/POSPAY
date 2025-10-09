package com.telpo.pospay.main.util;

/**
 * 字符串工具集合
 *
 * @author LIMING
 */
public class DESClass {

	private static final String PASSWORD_CRYPT_KEY = "ORIFOUND";
	private final static String DES = "DES";

	/**
	 * 加密
	 *
	 * @param src
	 *            数据源
	 * @param key
	 *            密钥，长度必须是8的倍数
	 * @return 返回加密后的数据
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
		// DES算法要求有一个可信任的随机数源
		java.security.SecureRandom sr = new java.security.SecureRandom();
		// 从原始密匙数据创建DESKeySpec对象
		javax.crypto.spec.DESKeySpec dks = new javax.crypto.spec.DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成
		// 一个SecretKey对象
		javax.crypto.SecretKeyFactory keyFactory = javax.crypto.SecretKeyFactory.getInstance(DES);
		javax.crypto.SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成加密操作
		javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(DES);
		// 用密匙初始化Cipher对象
		cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, securekey, sr);
		// 现在，获取数据并加密
		// 正式执行加密操作
		return cipher.doFinal(src);
	}
	/**
	 * 解密
	 *
	 * @param src
	 *            数据源
	 * @param key
	 *            密钥，长度必须是8的倍数
	 * @return 返回解密后的原始数据
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
		// DES算法要求有一个可信任的随机数源
		java.security.SecureRandom sr = new java.security.SecureRandom();
		// 从原始密匙数据创建一个DESKeySpec对象
		javax.crypto.spec.DESKeySpec dks = new javax.crypto.spec.DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
		// 一个SecretKey对象
		javax.crypto.SecretKeyFactory keyFactory = javax.crypto.SecretKeyFactory.getInstance(DES);
		javax.crypto.SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成解密操作
		javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(DES);
		// 用密匙初始化Cipher对象
		cipher.init(javax.crypto.Cipher.DECRYPT_MODE, securekey, sr);
		// 现在，获取数据并解密
		// 正式执行解密操作
		return cipher.doFinal(src);
	}

	/**
	 * 密码解密
	 *
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public final static String decrypt(String data) {
		try {
			byte[] dd=decrypt(hex2byte(data.getBytes("Unicode")),
					PASSWORD_CRYPT_KEY.getBytes());
			return new String(dd);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 密码加密
	 *
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public final static String encrypt(String password) {
		try {

			byte[] aa=password.getBytes("Unicode");
			byte[] aa1=new byte[aa.length-2];
			for(int i=2;i<aa.length;i++)
				aa1[i-2]=aa[i];
			byte[] temp=encrypt(aa1, PASSWORD_CRYPT_KEY.getBytes());
			return byte2hex(temp);
		} catch (Exception e) {
		}
		return null;
	}

	/**

	 * 二行制转字符串

	 * @param b

	 * @return

	 */

	public static String byte2hex(byte[] b) {

		String hs = "";

		String stmp = "";

		for (int n = 0; n < b.length; n++) {

			stmp = (Integer.toHexString(b[n] & 0XFF));

			if (stmp.length() == 1)

				hs = hs + "0" + stmp;

			else

				hs = hs + stmp;

		}

		return hs.toUpperCase();

	}

	public static byte[] hex2byte(byte[] b) {

		if ((b.length % 2) != 0)

			throw new IllegalArgumentException("长度不是偶数");

		byte[] b2 = new byte[b.length / 2];

		for (int n = 0; n < b.length; n += 2) {

			String item = new String(b, n, 2);

			b2[n / 2] = (byte) Integer.parseInt(item, 16);

		}

		return b2;
	}
}
