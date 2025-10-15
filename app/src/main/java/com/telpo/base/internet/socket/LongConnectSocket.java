package com.telpo.base.internet.socket;

import com.ipp.ssl.SSLContextCreator;

public class LongConnectSocket {

    public static java.net.Socket getSocketInstance(String ip, int port) throws java.net.UnknownHostException, Exception {

        java.net.Socket instance = new java.net.Socket();
        java.net.SocketAddress socAddress = new java.net.InetSocketAddress(ip, port);
        instance.connect(socAddress, 60000);// 设置socket的超时时间
        instance.setSoTimeout(60000);// 设置read的超时时间

        return instance;
    }

    public static java.net.Socket get30SecSocketInstance(String ip, int port) throws java.net.UnknownHostException, Exception {

        java.net.Socket instance = new java.net.Socket();
        java.net.SocketAddress socAddress = new java.net.InetSocketAddress(ip, port);
        instance.connect(socAddress, 30000);// 设置socket的超时时间
        instance.setSoTimeout(30000);// 设置read的超时时间

        return instance;
    }

    public static javax.net.ssl.SSLSocket getSSLSocketInstance(android.content.Context context, String ip, int port) throws java.net.UnknownHostException, Exception {

        javax.net.ssl.SSLSocket s = (javax.net.ssl.SSLSocket) getSSLContext(context).getSocketFactory().createSocket(ip, port);
        return s;
    }

    private static javax.net.ssl.SSLContext getSSLContext(android.content.Context context) throws Exception {
//        String keyPwd = "B8083DFC633584AD";
//        String ca = "cert/ca.cer";
//        String cert = "cert/862900000000001_00011124.crt";
//        String keyPem = "cert/862900000000001_00011124.key";

        String keyPwd = com.telpo.base.util.SharedPreferencesUtil.getString(context, "keyPwd", "");
        android.util.Log.i("xuxl", "密码是：" + keyPwd);
        String Merch_Pos_No = com.telpo.base.util.SharedPreferencesUtil.getString(context, "Merch_Pos_No", "");
        String ca = "/sdcard/ca.cer";
        String cert = "/sdcard/" + Merch_Pos_No + ".crt";
        String keyPem = "/sdcard/" + Merch_Pos_No + ".key";

        int platform = 0; // 0:android 1:sun
        byte[] caCertByte = toByteArray(new java.io.FileInputStream(ca));
        byte[] personalByte = toByteArray(new java.io.FileInputStream(cert));
        byte[] keyPemByte = toByteArray(new java.io.FileInputStream(keyPem));

        // create SSLContext.
        javax.net.ssl.SSLContext ctx = SSLContextCreator.create(platform, caCertByte, personalByte, keyPemByte, keyPwd);

//        // test socket.
//        Socket socket = (SSLSocket) ctx.getSocketFactory().createSocket("test.ippit.cn", 10101);
//        System.out.println(socket);
//        socket.getOutputStream().write(1);
        return ctx;
    }

    private static long copyLarge(java.io.InputStream input, java.io.OutputStream output) throws java.io.IOException {
        byte[] buffer = new byte[1024];
        long count = 0;
        int n = 0;

        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }

        return count;
    }

    private static byte[] toByteArray(java.io.InputStream input) throws java.io.IOException {
        java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
        copyLarge(input, output);

        return output.toByteArray();
    }

}



