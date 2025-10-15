package com.telpo.base.util;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

/**
 * Created by bruce on 2017/12/25.
 */
public class SignUtils {

    /**
     * 构造支付订单参数信息
     *
     * @param map 支付订单参数
     * @return
     */
    public static String buildOrderParam(java.util.Map<String, String> map) {
        java.util.List<String> keys = new java.util.ArrayList<String>(map.keySet());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, true));
            sb.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, true));

        return sb.toString();
    }

    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(java.net.URLEncoder.encode(value, "UTF-8"));
            } catch (java.io.UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

    public static String getSignContent(java.util.Map<String, String> sortedParams) {
        StringBuffer content = new StringBuffer();
        java.util.ArrayList keys = new java.util.ArrayList(sortedParams.keySet());
        java.util.Collections.sort(keys);
        int index = 0;

        for (int i = 0; i < keys.size(); ++i) {
            String key = (String) keys.get(i);
            String value = (String) sortedParams.get(key);
            if (areNotEmpty(new String[]{key, value})) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                ++index;
            }
        }

        return content.toString();
    }

    public static String rsaSign(String content, String privateKey, String charset, String signType) throws Exception {
        if ("RSA".equals(signType)) {
            return rsaSign(content, privateKey, charset);
        } else if ("RSA2".equals(signType)) {
            return rsa256Sign(content, privateKey, charset);
        } else {
            throw new Exception("Sign Type is Not Support : signType=" + signType);
        }
    }

    public static String rsa256Sign(String content, String privateKey, String charset) throws Exception {
        try {
            java.security.PrivateKey e = getPrivateKeyFromPKCS8("RSA", new java.io.ByteArrayInputStream(privateKey.getBytes()));
            java.security.Signature signature = java.security.Signature.getInstance("SHA256WithRSA");
            signature.initSign(e);
            if (isEmpty(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }

            byte[] signed = signature.sign();
            String result = java.util.Base64.getEncoder().encodeToString(signed);
            System.out.println("sign=" + result);
            return result;
        } catch (Exception var6) {
            throw new Exception("RSAcontent = " + content + "; charset = " + charset, var6);
        }
    }

    public static byte[] rsa256Sign(byte[] content, java.security.PrivateKey privateKey) throws Exception {
        try {
            java.security.PrivateKey e = privateKey;
            java.security.Signature signature = java.security.Signature.getInstance("SHA256WithRSA");
            signature.initSign(e);
            signature.update(content);
            byte[] signed = signature.sign();
            return signed;
        } catch (Exception var6) {
            var6.printStackTrace();
            throw new Exception(var6);
        }
    }


    public static boolean rsa256Verify(byte[] content, byte[] sign, java.security.PublicKey publickey) {
        try {
            java.security.PublicKey e = publickey;
            java.security.Signature signature = java.security.Signature.getInstance("SHA256WithRSA");
            signature.initVerify(e);
            signature.update(content);

            boolean verify = signature.verify(sign);

            return verify;
        } catch (Exception var6) {
            var6.printStackTrace();
            return false;

        }
    }

    public static boolean rsaVerify(byte[] content, byte[] sign, java.security.PublicKey publickey) {
        try {
            java.security.PublicKey e = publickey;
            java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");

            signature.initVerify(e);
            signature.update(content);
            boolean verify = signature.verify(sign);

            return verify;
        } catch (Exception var6) {
            var6.printStackTrace();
            return false;

        }
    }


    public static String rsaSign(String content, String privateKey, String charset) throws Exception {
        try {
            java.security.PrivateKey e = getPrivateKeyFromPKCS8("RSA", new java.io.ByteArrayInputStream(privateKey.getBytes()));
            java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");
            signature.initSign(e);
            if (isEmpty(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }

            byte[] signed = signature.sign();
            return java.util.Base64.getEncoder().encodeToString(signed);
        } catch (java.security.spec.InvalidKeySpecException var6) {
            throw new Exception("RSA私钥格式不正确，请检查是否正确配置了PKCS8格式的私钥", var6);
        } catch (Exception var7) {
            throw new Exception("RSAcontent = " + content + "; charset = " + charset, var7);
        }
    }

    public static java.security.PrivateKey getPrivateKeyFromPKCS8(String algorithm, java.io.InputStream ins) throws Exception {
        if (ins != null && !isEmpty(algorithm)) {
            java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance(algorithm);     //适配Android P及以后版本，否则报错NoSuchAlgorithmException

            byte[] encodedKey = readText(ins).getBytes();
            encodedKey = java.util.Base64.getDecoder().decode(encodedKey);
            return keyFactory.generatePrivate(new java.security.spec.PKCS8EncodedKeySpec(encodedKey));
        } else {
            return null;
        }
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

    public static boolean areNotEmpty(String... values) {
        boolean result = true;
        if (values != null && values.length != 0) {
            String[] var2 = values;
            int var3 = values.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                String value = var2[var4];
                result &= !isEmpty(value);
            }
        } else {
            result = false;
        }

        return result;
    }

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    public static void io(java.io.InputStream in, java.io.OutputStream out) throws java.io.IOException {
        io((java.io.InputStream) in, (java.io.OutputStream) out, -1);
    }

    public static void io(java.io.InputStream in, java.io.OutputStream out, int bufferSize) throws java.io.IOException {
        if (bufferSize == -1) {
            bufferSize = 8192;
        }

        byte[] buffer = new byte[bufferSize];

        int amount;
        while ((amount = in.read(buffer)) >= 0) {
            out.write(buffer, 0, amount);
        }

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

    public static java.io.OutputStream synchronizedOutputStream(java.io.OutputStream out) {
        return new com.telpo.base.util.SignUtils.SynchronizedOutputStream(out);
    }

    public static java.io.OutputStream synchronizedOutputStream(java.io.OutputStream out, Object lock) {
        return new com.telpo.base.util.SignUtils.SynchronizedOutputStream(out, lock);
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

    private static class SynchronizedOutputStream extends java.io.OutputStream {
        private java.io.OutputStream out;
        private Object lock;

        SynchronizedOutputStream(java.io.OutputStream out) {
            this(out, out);
        }

        SynchronizedOutputStream(java.io.OutputStream out, Object lock) {
            this.out = out;
            this.lock = lock;
        }

        public void write(int datum) throws java.io.IOException {
            Object var2 = this.lock;
            synchronized (this.lock) {
                this.out.write(datum);
            }
        }

        public void write(byte[] data) throws java.io.IOException {
            Object var2 = this.lock;
            synchronized (this.lock) {
                this.out.write(data);
            }
        }

        public void write(byte[] data, int offset, int length) throws java.io.IOException {
            Object var4 = this.lock;
            synchronized (this.lock) {
                this.out.write(data, offset, length);
            }
        }

        public void flush() throws java.io.IOException {
            Object var1 = this.lock;
            synchronized (this.lock) {
                this.out.flush();
            }
        }

        public void close() throws java.io.IOException {
            Object var1 = this.lock;
            synchronized (this.lock) {
                this.out.close();
            }
        }
    }

}
