package com.telpo.base.util;


public class RSAUtils {

    public static final String RSA = "RSA";// 非对称加密密钥算法
    public static final String ECB_PADDING = "RSA/ECB/PKCS1Padding";//加密填充方式
    //public static final String ECB_PADDING = "RSA/ECB/OAEPWithSHA256AndMGF1Padding";//加密填充方式
    /**
     * RSA算法规定：待加密的字节数不能超过密钥的长度值除以8再减去11。
     * 而加密后得到密文的字节数，正好是密钥的长度值除以 8。
     */
    private static int KEYSIZE = 2048;// 密钥位数
    private static int RESERVE_BYTES = 11;
    private static int DECRYPT_BLOCK = KEYSIZE / 8;
    private static int ENCRYPT_BLOCK = DECRYPT_BLOCK - RESERVE_BYTES;

    /**
     * 随机生成RSA密钥对
     *
     * @param keysize 密钥长度，范围：512-2048,一般2048
     */
    public static java.security.KeyPair generateKeyPair(int keysize) {
        try {
            java.security.KeyPairGenerator kpg = java.security.KeyPairGenerator.getInstance(RSA);
            kpg.initialize(keysize);
            return kpg.genKeyPair();
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }



    /**
     * 用公钥对字符串进行加密
     *
     * @param data 原文
     */
    public static byte[] encryptWithPublicKey(byte[] data, byte[] key)
            throws Exception {
        javax.crypto.Cipher cp = javax.crypto.Cipher.getInstance(ECB_PADDING);
        cp.init(javax.crypto.Cipher.ENCRYPT_MODE, getPublicKey(key));
        return cp.doFinal(data);
    }

    /**
     * 公钥解密
     *
     * @param data 待解密数据
     * @param key  密钥
     */
    public static byte[] decryptWithPublicKey(byte[] data, byte[] key)
            throws Exception {
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(ECB_PADDING);
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, getPublicKey(key));
        return cipher.doFinal(data);
    }

    /**
     * 私钥加密
     *
     * @param data 待加密数据
     * @param key  密钥
     */
    public static byte[] encryptWithPrivateKey(byte[] data, byte[] key)
            throws Exception {
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(ECB_PADDING);
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, getPrivateKey(key));
        return cipher.doFinal(data);
    }


    /**
     * 私钥解密
     *
     * @param data 待解密数据
     * @param key  密钥
     */
    public static byte[] decryptWithPrivateKey(byte[] data, byte[] key)
            throws Exception {
        javax.crypto.Cipher cp = javax.crypto.Cipher.getInstance(ECB_PADDING);
//            Cipher cp = Cipher.getInstance("RSA/NONE/OAEPWITHSHA256AndMGF1Padding");
        cp.init(javax.crypto.Cipher.DECRYPT_MODE, getPrivateKey(key));
        byte[] arr = cp.doFinal(data);
        return arr;
    }


    /**
     * 使用模和指数生成RSA公钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus  模
     * @param exponent 指数
     * @return
     */
    public static java.security.interfaces.RSAPublicKey getPublicKey(String modulus, String exponent) {
        try {
            java.math.BigInteger b1 = new java.math.BigInteger(DataProcessUtil.hexStringToByte(modulus));
            java.math.BigInteger b2 = new java.math.BigInteger(DataProcessUtil.hexStringToByte(exponent));
            java.security.KeyFactory keyFactory;
            keyFactory = java.security.KeyFactory.getInstance(RSA);     //适配Android P及以后版本，否则报错NoSuchAlgorithmException

            java.security.spec.RSAPublicKeySpec keySpec = new java.security.spec.RSAPublicKeySpec(b1, b2);
            return (java.security.interfaces.RSAPublicKey) keyFactory.generatePublic(new java.security.spec.X509EncodedKeySpec(keyFactory.generatePublic(keySpec).getEncoded()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 使用模和指数生成RSA私钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus  模
     * @param exponent 指数
     * @return
     */
    public static java.security.interfaces.RSAPrivateKey getPrivateKey(String modulus, String exponent) {
        try {
            java.math.BigInteger b1 = new java.math.BigInteger(DataProcessUtil.hexStringToByte(modulus));
            java.math.BigInteger b2 = new java.math.BigInteger(DataProcessUtil.hexStringToByte(exponent));
            java.security.KeyFactory keyFactory;
            keyFactory = java.security.KeyFactory.getInstance(RSA);     //适配Android P及以后版本，否则报错NoSuchAlgorithmException

            java.security.spec.RSAPrivateKeySpec keySpec = new java.security.spec.RSAPrivateKeySpec(b1, b2);
            return (java.security.interfaces.RSAPrivateKey) keyFactory.generatePrivate(new java.security.spec.PKCS8EncodedKeySpec(keyFactory.generatePrivate(keySpec).getEncoded()));
//
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static java.security.PrivateKey getPrivateKeyFromPKCS8(String algorithm, java.io.InputStream ins) throws Exception {
        if (ins != null && !isEmpty(algorithm)) {
            java.security.KeyFactory keyFactory= java.security.KeyFactory.getInstance(algorithm);

            byte[] encodedKey = readText(ins).getBytes();
            encodedKey = java.util.Base64.getDecoder().decode(encodedKey);
            return keyFactory.generatePrivate(new java.security.spec.PKCS8EncodedKeySpec(encodedKey));
        } else {
            return null;
        }
    }

    public static java.security.PublicKey getPublicKeyFromPKCS8(String algorithm, java.io.InputStream ins) throws Exception {
        if (ins != null && !isEmpty(algorithm)) {
            java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance(algorithm);

            byte[] encodedKey = readText(ins).getBytes();
            encodedKey = java.util.Base64.getDecoder().decode(encodedKey);
            return keyFactory.generatePublic(new java.security.spec.X509EncodedKeySpec(encodedKey));
        } else {
            return null;
        }
    }

    public static java.security.PublicKey getPublicKey(byte[] key) throws Exception {
        java.security.spec.X509EncodedKeySpec keySpec = new java.security.spec.X509EncodedKeySpec(key);
        java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance(RSA);
        return keyFactory.generatePublic(keySpec);
    }

    public static java.security.PrivateKey getPrivateKey(byte[] key) throws Exception {
        java.security.spec.PKCS8EncodedKeySpec keySpec = new java.security.spec.PKCS8EncodedKeySpec(key);
        java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance(RSA);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 分块加密
     *
     * @param data
     * @param key
     */
    public static byte[] encryptWithPublicKeyBlock(byte[] data, byte[] key) throws Exception {
        int blockCount = (data.length / ENCRYPT_BLOCK);

        if ((data.length % ENCRYPT_BLOCK) != 0) {
            blockCount += 1;
        }

        java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream(blockCount * ENCRYPT_BLOCK);
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(ECB_PADDING);
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, getPublicKey(key));

        for (int offset = 0; offset < data.length; offset += ENCRYPT_BLOCK) {
            int inputLen = (data.length - offset);
            if (inputLen > ENCRYPT_BLOCK) {
                inputLen = ENCRYPT_BLOCK;
            }
            byte[] encryptedBlock = cipher.doFinal(data, offset, inputLen);
            bos.write(encryptedBlock);
        }

        bos.close();
        return bos.toByteArray();
    }

    /**
     * 分块加密
     *
     * @param data
     * @param key
     */
    public static byte[] encryptWithPrivateKeyBlock(byte[] data, byte[] key) throws Exception {
        int blockCount = (data.length / ENCRYPT_BLOCK);

        if ((data.length % ENCRYPT_BLOCK) != 0) {
            blockCount += 1;
        }
        java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream(blockCount * ENCRYPT_BLOCK);
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(ECB_PADDING);
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, getPrivateKey(key));

        for (int offset = 0; offset < data.length; offset += ENCRYPT_BLOCK) {
            int inputLen = (data.length - offset);
            if (inputLen > ENCRYPT_BLOCK) {
                inputLen = ENCRYPT_BLOCK;
            }
            byte[] encryptedBlock = cipher.doFinal(data, offset, inputLen);
            bos.write(encryptedBlock);
        }

        bos.close();
        return bos.toByteArray();
    }

    /**
     * 分块解密
     *
     * @param data
     * @param key
     */
    public static byte[] decryptWithPublicKeyBlock(byte[] data, byte[] key) throws Exception {
        int blockCount = (data.length / DECRYPT_BLOCK);
        if ((data.length % DECRYPT_BLOCK) != 0) {
            blockCount += 1;
        }
        java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream(blockCount * DECRYPT_BLOCK);
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(ECB_PADDING);
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, getPublicKey(key));
        for (int offset = 0; offset < data.length; offset += DECRYPT_BLOCK) {
            int inputLen = (data.length - offset);
            if (inputLen > DECRYPT_BLOCK) {
                inputLen = DECRYPT_BLOCK;
            }
            byte[] decryptedBlock = cipher.doFinal(data, offset, inputLen);
            bos.write(decryptedBlock);
        }

        bos.close();
        return bos.toByteArray();
    }

    /**
     * 分块解密
     *
     * @param data
     * @param key
     */
    public static byte[] decryptWithPrivateKeyBlock(byte[] data, byte[] key) throws Exception {

        int blockCount = (data.length / DECRYPT_BLOCK);
        if ((data.length % DECRYPT_BLOCK) != 0) {
            blockCount += 1;
        }
        java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream(blockCount * DECRYPT_BLOCK);
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(ECB_PADDING);
//            Cipher cipher = Cipher.getInstance("RSA/NONE/OAEPWITHSHA256AndMGF1Padding","BC");
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, getPrivateKey(key));
        for (int offset = 0; offset < data.length; offset += DECRYPT_BLOCK) {
            int inputLen = (data.length - offset);

            if (inputLen > DECRYPT_BLOCK) {
                inputLen = DECRYPT_BLOCK;
            }
            byte[] decryptedBlock = cipher.doFinal(data, offset, inputLen);
            bos.write(decryptedBlock);
        }

        bos.close();
        return bos.toByteArray();
    }

    public static boolean isEmpty(String value) {
        int strLen;
        if (value != null && (strLen = value.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(value.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }


    public static String readText(java.io.InputStream in) throws java.io.IOException {
        return readText(in, (String) null, -1);
    }

    public static String readText(java.io.InputStream in, String encoding) throws java.io.IOException {
        return readText(in, encoding, -1);
    }

    public static String readText(java.io.InputStream in, String encoding, int bufferSize) throws java.io.IOException {
        java.io.InputStreamReader reader = encoding == null ? new java.io.InputStreamReader(in) : new java.io.InputStreamReader(in, encoding);
        return readText(reader, bufferSize);
    }

    public static String readText(java.io.Reader reader) throws java.io.IOException {
        return readText(reader, -1);
    }

    public static String readText(java.io.Reader reader, int bufferSize) throws java.io.IOException {
        java.io.StringWriter writer = new java.io.StringWriter();
        io((java.io.Reader) reader, (java.io.Writer) writer, bufferSize);
        return writer.toString();
    }


    public static void io(java.io.Reader in, java.io.Writer out) throws java.io.IOException {
        io((java.io.Reader) in, (java.io.Writer) out, -1);
    }

    public static void io(java.io.Reader in, java.io.Writer out, int bufferSize) throws java.io.IOException {
        if (bufferSize == -1) {
            bufferSize = 4096;
        }

        char[] buffer = new char[bufferSize];

        int amount;
        while ((amount = in.read(buffer)) >= 0) {
            out.write(buffer, 0, amount);
        }

    }
}

