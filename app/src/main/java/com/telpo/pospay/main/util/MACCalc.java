package com.telpo.pospay.main.util;


public class MACCalc {


    public static String Mac_Calc(byte[] MACKEY, byte[] data) {

        int group = (data.length + (8 - 1)) / 8;
        // 偏移量
        int offset = 0;
        // 输入计算数据
        byte[] edata = new byte[8];
        for (int i = 0; i < group; i++) {
            byte[] temp = new byte[8];
            if (i != group - 1) {
                System.arraycopy(data, offset, temp, 0, 8);
                offset += 8;
            } else {// 只有最后一组数据才进行填充0x00
                System.arraycopy(data, offset, temp, 0, data.length - offset);
            }

            if (i != 0) {// 只有第一次不做异或
                temp = XOR(edata, temp);
            }
            System.arraycopy(temp, 0, edata, 0, 8);
        }
        // 将异或运算后的最后得到8个字节（RESULT BLOCK）转换成16 个HEXDECIMAL
        String Data = com.telpo.base.util.DataProcessUtil.bytesToHexString(edata);
        byte[] tmp = new byte[16];
        tmp = Data.getBytes();
        // 取前8 个字节用MAK加密
        byte[] tmp1 = new byte[8];
        System.arraycopy(tmp, 0, tmp1, 0, 8);
        byte[] tmp2 = null;
        try {
            tmp2 = com.telpo.pospay.main.util.DESClass.encrypt(tmp1, MACKEY);
            // tmp2 = desedeEn(MACKEY,tmp1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 将加密后的结果与后8 个字节异或
        System.arraycopy(tmp, 8, tmp1, 0, 8);
        byte[] tmp3 = XOR(tmp2, tmp1);

        // 用异或的结果 再进行一次单倍长密钥算法运算
        try {
            tmp2 = com.telpo.pospay.main.util.DESClass.encrypt(tmp3, MACKEY);
            // tmp2 = desedeEn(MACKEY,tmp3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String str1 = com.telpo.base.util.DataProcessUtil.bytesToHexString(tmp2);

        String Mac = str1.substring(0, 8);

        // System.out.println(java.util.Arrays.toString(byte2Int(edata)));
        // System.out.println(Hex.encodeHexString(edata));
        return Mac;
    }

    public static byte[] XOR(byte[] edata, byte[] temp) {
        byte[] result = new byte[8];
        for (int i = 0, j = result.length; i < j; i++) {
            result[i] = (byte) (edata[i] ^ temp[i]);
        }
        return result;
    }

    public static int[] byte2Int(byte[] data) {
        int[] result = new int[data.length];

        for (int i = 0; i < data.length; i++) {
            if (data[i] < 0) {
                result[i] = data[i] + 256;
            } else {
                result[i] = data[i];
            }
        }

        return result;
    }

    public static byte[] desedeEn(byte[] key, byte[] data) {
        byte[] result = null;
        try {
            javax.crypto.SecretKey secretKey = getSecretKeySpec(key);
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DES/CBC/NoPadding", "BC");
            // Cipher cipher = Cipher.getInstance("DES");

            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, secretKey, new javax.crypto.spec.IvParameterSpec(
                    new byte[8]));
            // 初始化向量为0,即异或不改变原始数据

            result = cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static javax.crypto.SecretKey getSecretKeySpec(byte[] keyB)
            throws java.security.NoSuchAlgorithmException, java.security.spec.InvalidKeySpecException {
        javax.crypto.SecretKeyFactory secretKeyFactory = javax.crypto.SecretKeyFactory.getInstance("Des");
        javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(keyB, "Des");
        return secretKeyFactory.generateSecret(secretKeySpec);
    }

    /**
     * 银联的MAC算法，ECB算法
     * @param data
     * @return
     */
    public static byte[] Credit_MacGenerate(byte[] data) {
        byte[] mac = new byte[8];
        byte[] temp = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        if (data == null) {
            return null;
        }
        int len = data.length;
        if (len < 1) {
            return null;
        }
        int j = (len % 8 != 0) ? (len / 8 + 1) : len / 8;
        byte[] d = new byte[j * 8];
        System.arraycopy(data, 0, d, 0, len);

        for (int i = 0; i < j; i++) {
            for (int k = 0; k < 8; k++)
                temp[k] ^= d[i * 8 + k];
        }
        com.telpo.base.util.MLog.i("temp:" + com.telpo.base.util.StringUtil.bytesToHexString(temp));

        String indata = com.telpo.base.util.StringUtil.bytesToHexString(temp);
        byte[] indata2 = com.telpo.base.util.StringUtil.StringToAsciiByte(indata);

//        int result = PinpadService.TP_PinpadGetMac(GlobalParams.currMacKeyIndex, indata2, mac, PinpadService.MAC_X99);

        String t = com.telpo.base.util.StringUtil.bytesToHexString(mac);
        byte[] mac2 = com.telpo.base.util.StringUtil.StringToAsciiByte(t);
        System.arraycopy(mac2, 0, mac, 0, 8);
        return mac;
    }

    /**
     * 中行的算法
     * 用CBC算法计算MAC
     * 使用双倍长密钥来计算时，要把密钥分成左边16位和右边16位
     * 例如密钥为“B92AB90440F8549B6EC7FEC475FE7558”
     * 则要分成
     * 密钥A：“B92AB90440F8549B”
     * 和
     * 密钥B：“6EC7FEC475FE7558”
     * @param inData  用于计算MAC的原始数据
     * @param leftKeyIndex    左边16位密钥的索引，即“B92AB90440F8549B”的索引
     * @param rightKeyIndex   右边16位密钥的索引,即“6EC7FEC475FE7558”的索引
     * @return  MAC结果
     */
    public static byte[] getMAC_CBC(byte[] inData, int leftKeyIndex, int rightKeyIndex){
        int result = 0;
        byte[] outdata = new byte[8];
        byte[] tmpData1 = new byte[8];
        byte[] tmpData2 = new byte[8];

//        result = PinpadService.TP_PinpadGetMac(leftKeyIndex, inData, tmpData1, PinpadService.MAC_X99);
//        Log("GET MAC_X99: result[" + result + "]; inData[" + com.telpo.base.util.StringUtil.bytesToHexString(inData).toUpperCase() + "]; outData[" + com.telpo.base.util.StringUtil.bytesToHexString(tmpData1).toUpperCase() + "]");
//
//        result = PinpadService.TP_DesByKeyIndex(rightKeyIndex, tmpData1, tmpData2, PinpadService.PIN_DES_DECRYPT);
//        Log("GET DES:     result[" + result + "]; inData[" + com.telpo.base.util.StringUtil.bytesToHexString(tmpData1).toUpperCase() + "]; outData[" + com.telpo.base.util.StringUtil.bytesToHexString(tmpData2).toUpperCase() + "]");
//
//        result = PinpadService.TP_DesByKeyIndex(leftKeyIndex, tmpData2, outdata, PinpadService.PIN_DES_ENCRYPT);
//        Log("GET DES:     result[" + result + "]; inData[" + com.telpo.base.util.StringUtil.bytesToHexString(tmpData2).toUpperCase() + "]; outData[" + com.telpo.base.util.StringUtil.bytesToHexString(outdata).toUpperCase() + "]");

        return outdata;
    }
}