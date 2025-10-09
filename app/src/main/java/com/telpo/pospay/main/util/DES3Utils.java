package com.telpo.pospay.main.util;

import java.io.UnsupportedEncodingException;

/**
 * Created by liangjz on 2016/10/20.
 */
public class DES3Utils {
    // 定义加密算法，DESede即3DES
    //private static final String Algorithm = "DESede";
    /*
    AES/CBC/NoPadding (128)
    AES/CBC/PKCS5Padding (128)
    AES/ECB/NoPadding (128)
    AES/ECB/PKCS5Padding (128)
    DES/CBC/NoPadding (56)
    DES/CBC/PKCS5Padding (56)
    DES/ECB/NoPadding (56)
    DES/ECB/PKCS5Padding (56)

    （DESede实际上是3-DES）
    DESede/CBC/NoPadding (168)
    DESede/CBC/PKCS5Padding (168)
    DESede/ECB/NoPadding (168)
    DESede/ECB/PKCS5Padding (168)
    */
    private static final String Algorithm = "DESede/ECB/NoPadding";
    // 加密密钥
    private static final String PASSWORD_CRYPT_KEY = "zhaokaiqiang1992";

    /**
     * 加密方法
     *
     * @param src
     *            源数据的字节数组
     * @return
     */
    public static byte[] encryptMode(byte[] src) {
        try {
            // 生成密钥
            javax.crypto.SecretKey deskey = new javax.crypto.spec.SecretKeySpec(
                    build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);
            // 实例化Cipher
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(Algorithm);
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, deskey);
            return cipher.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 加密方法
     *
     * @param src
     *            源数据的字节数组
     * @return
     */
    public static byte[] encryptMode(byte[] src, byte[] key) {
        try {
            // 生成密钥
            javax.crypto.SecretKey deskey = new javax.crypto.spec.SecretKeySpec(
                    key, Algorithm);
            // 实例化Cipher
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(Algorithm);
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, deskey);
            return cipher.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 解密函数
     *
     * @param src
     *            密文的字节数组
     * @return
     */
    public static byte[] decryptMode(byte[] src) {
        try {
            javax.crypto.SecretKey deskey = new javax.crypto.spec.SecretKeySpec(
                    build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);
            javax.crypto.Cipher c1 = javax.crypto.Cipher.getInstance(Algorithm);
            c1.init(javax.crypto.Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 解密函数
     *
     * @param src
     *            密文的字节数组
     * @return
     */
    public static byte[] decryptMode(byte[] src, byte[] key) {
        try {
            javax.crypto.SecretKey deskey = new javax.crypto.spec.SecretKeySpec(
                    key, Algorithm);
            javax.crypto.Cipher c1 = javax.crypto.Cipher.getInstance(Algorithm);
            c1.init(javax.crypto.Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 根据字符串生成密钥24位的字节数组
     *
     * @param keyStr
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] build3DesKey(String keyStr)
            throws java.io.UnsupportedEncodingException {
        byte[] key = new byte[24];
        byte[] temp = keyStr.getBytes("UTF-8");

        if (key.length > temp.length) {
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }
}
