package com.telpo.base.util;

/**
 * 单位:广东天波信息技术股份有限公司 创建人:luyq 功能：字符串处理组件 日期:2014-1-21
 */
public class StringUtil {

    /**
     * 清除StringBuffer字符串的内容
     *
     * @param sb 输入字符串
     * @return 无内容的StringBuffer字符串
     */
    public static StringBuffer clearSB(StringBuffer sb) {
        if (sb != null && sb.length() > 0) {
            sb.delete(0, sb.length());
        }
        return sb;
    }

    /**
     * 删除字符串的最后一个字符
     *
     * @param sb StringBuffer类型参数
     * @return 删除最后一个字符的字符串
     */
    public static StringBuffer delLastChar(StringBuffer sb) {
        if (sb != null && sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb;
    }

    /**
     * 把null字符串转换为空字符串
     *
     * @param strOrig 输入字符串
     * @return 非空的源字符串或空字符串
     */
    public static final String convertStringNull(String strOrig) {
        String strReturn = "";
        if (strOrig == null || strOrig.equals(null)) {
            strReturn = "";
        } else {
            strReturn = strOrig;
        }
        return strReturn;
    }

    /**
     * 分解以特定分隔符分隔多个同一类型信息的字符串为字符串数组
     *
     * @param strOrigin 原始字符串
     * @param separator 分隔符
     * @return 处理后的字符串数组
     */
    public static final String[] parserString(String strOrigin, String separator) {
        try {
            java.util.StringTokenizer st;
            String strItem;

            if (strOrigin == null) {
                return null;
            }
            st = new java.util.StringTokenizer(strOrigin, separator);
            String[] returnValue = new String[st.countTokens()];
            int index = 0;
            while (st.hasMoreTokens()) {
                strItem = (String) st.nextToken();
                if (strItem != null && strItem.trim().length() != 0) {
                    returnValue[index++] = strItem;
                }
            }
            return returnValue;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 转换成非空中文字符串，用于经网络传输后的中文字符串还原
     *
     * @param strOrigin 输入字符串
     * @return 转换后字符
     */
    public static String toChineseStrNoNull(String strOrigin) {
        if (strOrigin == null || strOrigin.equals(null)
                || "null".equalsIgnoreCase(strOrigin.trim())) {
            strOrigin = "";
        } else {
            strOrigin = strOrigin.trim();
        }

        try {
            strOrigin = new String(strOrigin.getBytes("ISO8859_1"), "GBK");
        } catch (Exception e) {
        }
        return strOrigin;
    }

    /**
     * 将给定字符串转换成ascii码字符串
     *
     * @param strOrigin 输入字符串
     * @return ascii字符串
     */
    public static String toAscii(String strOrigin) {
        return toStandardStr(strOrigin);
    }

    /**
     * 将中文字符串转换为ISO8859_1编码格式，并将空字符串转换为""
     *
     * @param strOrigin strOrigin 原始字符串（中文字符串）
     * @return 处理后的字符串
     */
    public static String toStandardStr(String strOrigin) {
        if (strOrigin == null || strOrigin.equals(null)) {
            strOrigin = "";
        } else {
            strOrigin = strOrigin.trim();
        }

        try {
            strOrigin = new String(strOrigin.getBytes("GBK"), "ISO8859_1");
        } catch (Exception e) {
        }
        return strOrigin;
    }

    /**
     * 将给定数组字符串转换成中文数组字符串
     *
     * @param strOrigin 输入字符串数组
     * @return 中文字符串数组
     */
    public static String[] toChineseStringArray(String[] strOrigin) {
        for (int i = 0; i < strOrigin.length; i++) {
            if (strOrigin[i] == null || strOrigin[i].equals(null)) {
                strOrigin[i] = "";
            } else {
                strOrigin[i] = strOrigin[i].trim();
            }

            try {
                strOrigin[i] = new String(strOrigin[i].getBytes("ISO8859_1"),
                        "GBK");
            } catch (Exception e) {
            }
        }
        return strOrigin;
    }

    /**
     * 将给定字符串按分隔符转换成数组
     *
     * @param s             输入字符串
     * @param separatorSign 字符串分割标签
     * @return 分隔符后的字符串数组
     */

    public static String[] split(String s, String separatorSign) {
        try {
            if (s == null)
                return null;
            int index = 0;
            java.util.Vector vec = new java.util.Vector();
            while (true) {
                index = s.indexOf(separatorSign, index);
                if (index < 0)
                    break;
                vec.addElement(new Integer(index++));
            }

            int size = vec.size();

            if (size <= 0) {
                String[] ret = new String[1];
                ret[0] = s;
                return ret;
            }

            String[] strarr = new String[size + 1];

            Integer[] indArr = new Integer[size];
            vec.copyInto(indArr);

            // sort the index array for getting the string.
            java.util.Arrays.sort(indArr);

            int ind = 0;
            int len = strarr.length;
            for (int j = 0; j < (len - 1); j++) {
                strarr[j] = s.substring(ind, indArr[j].intValue());
                ind = indArr[j].intValue() + 1;
            }
            if (len > 0)
                strarr[len - 1] = s.substring(ind);

            return strarr;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将空格串" "或空指针转换为html的空格编码
     *
     * @return 返回转换后的字符串
     */
    public static final String filterNullStringToHTMLSpace(String strOrigin) {
        String rets = "";
        if (strOrigin == null) {
            rets = "&nbsp;";
        } else if (strOrigin.length() == 0) {
            rets = "&nbsp;";
        } else {

            for (int i = 0; i < strOrigin.length(); i++) {
                if (strOrigin.charAt(i) == ' ') {
                    rets += "&nbsp;";

                } else {
                    rets += strOrigin.charAt(i);

                }
            }

        }
        return rets;
    }

    /**
     * 将数字0转换为""，并将空字符串转换为""
     *
     * @param strOrigin 原始字符串（中文字符串）
     * @return 转换后的字符串
     */
    public static String convertZeroToSpace(String strOrigin) {
        if (strOrigin == null || strOrigin.equals(null)
                || strOrigin.equals("0")) {
            strOrigin = "";
        } else {
            strOrigin = strOrigin.trim();
        }

        return strOrigin;
    }

    /**
     * 将数字0转换为""，并将空字符串转换为""
     *
     * @param origin 原始字符串（中文字符串）
     * @return 转换后的字符串
     */
    public static String convertZeroToSpace(int origin) {
        if (0 == origin) {
            return "";
        } else {
            return String.valueOf(origin);
        }
    }

    /**
     * 转变字符串数组到逗号分隔的字符串(每个字段不含单引号)，用于SQL语句拼装
     *
     * @param arr 输入字符串数组
     * @return 转换后的字符串
     */
    public static String convertArrToStr(String[] arr) {
        StringBuffer buf = new StringBuffer("");
        for (int i = 0; i < arr.length; i++) {
            buf.append(arr[i]).append(",");
        }
        buf.deleteCharAt(buf.length() - 1);

        return buf.toString();
    }

    /**
     * 转变字符串数组到逗号分隔的字符串(每个字段含单引号)，用于SQL语句拼装
     *
     * @param arr 输入字符串数组
     * @return 转换后的字符串
     */
    public static String convertArrToStrs(String[] arr) {
        StringBuffer buf = new StringBuffer("");
        for (int i = 0; i < arr.length; i++) {
            buf.append("'").append(arr[i]).append("',");
        }
        buf.deleteCharAt(buf.length() - 1);

        return buf.toString();
    }

    /**
     * 从 yyyy-MM-dd HH:mm:ss取日期
     *
     * @param sTime 日期格式字符串
     * @return 转换后的字符串
     */
    public static String getDateString(String sTime) {
        if (sTime == null || "".equals(sTime)) {
            return "";
        }
        sTime = sTime.trim();
        int pos = sTime.length() > 10 ? 10 : sTime.length();
        return sTime.substring(0, pos);
    }

    /**
     * 过滤掉字符串中的单引号
     *
     * @param instr 输入字符串
     * @return 转换后的字符串
     */
    public static String filterComma(String instr) {
        if (instr != null && instr.length() > 0) {
            StringBuffer buf = new StringBuffer(instr);
            int i = -1;
            i = buf.indexOf("'");
            while (i >= 0) {
                buf.deleteCharAt(i);
                i = buf.indexOf("'");
            }
            return buf.toString();
        } else
            return "";
    }

    /**
     * 过滤掉字符串中的回车符
     *
     * @param instr 输入字符串
     * @return 转换后的字符串
     */
    public static String filterEnter(String instr) {
        if (instr != null && instr.length() > 0) {
            StringBuffer buf = new StringBuffer(instr);
            int i = -1;
            i = buf.indexOf("\n");
            while (i >= 0) {
                buf.deleteCharAt(i);
                i = buf.indexOf("\n");
            }
            return buf.toString();
        } else
            return "";
    }

    /**
     * 截取输入小数，使精确到小数后两位
     *
     * @param inNum 输入字符串
     * @return 转换后的字符串
     */
    public static String interceptDecimalScaleTwo(String inNum)
            throws Exception {
        try {
            java.math.BigDecimal inval = new java.math.BigDecimal(inNum);
            double dval = inval.doubleValue();
            java.text.DecimalFormat myformat = new java.text.DecimalFormat("#0.00");
            String sval = myformat.format(dval); // 格式化数字，精确到小数点后两位
            return sval;
        } catch (NumberFormatException e) {
            throw new Exception("非法数字");
        }
    }

    /**
     * 截取输入小数，使精确到小数后4位
     *
     * @param inNum 输入字符串
     * @return 转换后的字符串
     */
    public static String interceptDecimalScale4(String inNum) throws Exception {
        try {
            java.math.BigDecimal inval = new java.math.BigDecimal(inNum);
            double dval = inval.doubleValue();
            java.text.DecimalFormat myformat = new java.text.DecimalFormat("#0.0000");
            String sval = myformat.format(dval); // 格式化数字，精确到小数点后两位
            return sval;
        } catch (NumberFormatException e) {
            throw new Exception("非法数字");
        }
    }

    /**
     * 截取输入小数，使精确到小数后两位
     *
     * @param inNum 输入字符串
     * @return 转换后的字符串
     */
    public static String string2formatNum(String inNum) throws Exception {
        if (inNum == null || "".equals(inNum.trim()))
            return "0.00";
        try {
            java.math.BigDecimal inval = new java.math.BigDecimal(inNum);
            double dval = inval.doubleValue();
            java.text.DecimalFormat myformat = new java.text.DecimalFormat("#,##0.00");
            String sval = myformat.format(dval); // 格式化数字，精确到小数点后两位
            return sval;
        } catch (NumberFormatException e) {
            throw new Exception("非法数字");
        }
    }

    /**
     * 将空字符串转换成“0”，在数字变量要给定缺省值0时可以调用
     *
     * @param srcStr 源字符串
     * @return 转换后的字符串
     */
    public static String formatStrNull2Zero(String srcStr) {
        if (srcStr == null || "".equals(srcStr.trim()))
            return "0";
        else
            return srcStr;
    }

    /**
     * 判断num是否为0，如果为0则返回“”，否则返回原值
     *
     * @param num 数字型字符串
     * @return 转换后的字符串
     */
    public String ZeroToemptyString(String num) {
        if ("0".equals(num) || "0.00".equals(num) || "0.0".equals(num)) {
            return "";
        }
        return num;
    }

    /**
     * 格式化日期字符串，如果是20060226，则输出2006.02.26
     *
     * @param datestr 日期格式字符串 格式为YYYMMDD
     * @return 转换后的字符串
     */
    public static String formatDateByDot(String datestr) {
        if (datestr != null) {
            if (datestr.length() == 6) {
                return datestr.substring(0, 4) + "." + datestr.substring(4);
            } else if (datestr.length() == 8) {
                return datestr.substring(0, 4) + "." + datestr.substring(4, 6)
                        + "." + datestr.substring(6);
            } else
                return datestr;
        } else
            return "";
    }

    /**
     * 去掉字符串中的所有空格
     *
     * @param s 源字符串
     * @return 转换后的字符串
     */
    public static String replaceSpace(String s) {
        if (s == null)
            return "";
        StringBuffer t = new StringBuffer("");
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c != 57344 && c != ' ') {
                t.append(s.charAt(i));
            }
        }
        return t.toString();
    }

    /**
     * 分割字符串
     *
     * @param tempaborts 源字符串
     * @param dot        点
     * @return 分割后的字符串数组
     */
    public static String[] splits(String tempaborts, String dot) {
        java.util.StringTokenizer st = new java.util.StringTokenizer(tempaborts, dot);
        int len = st.countTokens();
        String[] val = new String[len];
        int m = 0;
        // 分割
        while (st.hasMoreTokens()) {
            val[m] = st.nextToken();
            m++;
        }
        return val;
    }

    /**
     * 把字符串转换为n位，与Oracle数据库rpad方法一致
     *
     * @param str    源字符串
     * @param counts 位数
     * @return 转换后的字符串
     */
    public static String trunStrDigit(String str, int counts) {
        int iLength = str.length();
        if (iLength < counts) {
            StringBuffer strBuffer = new StringBuffer();
            strBuffer.append(str);
            for (int i = 0; i < counts - iLength; i++) {
                strBuffer.append(" ");
            }
            str = strBuffer.toString();
        }
        return str;
    }

    /**
     * 简单的把Object对象装换为String对象 如果输入参数为null则返回长度为0的字符串
     * 如果输入参数为String，返回则把该String的两端空 字符串去掉后的字符串
     */
    public static String o2s(Object o) {
        if (null == o) {
            return "";
        }
        if (o instanceof String) {
            return ((String) o).trim();
        }
        return o.toString().trim();
    }


    /**
     * 字符串右边补零
     *
     * @param str   待补零的字符串
     * @param lenth 补零后的字符串长度
     * @return
     * @throws
     */
    public static String strAutozeroAfter(String str, int lenth) {
        StringBuffer buff = new StringBuffer();
        String tempStr = str == null ? "" : str;
        try {
            if (str.getBytes().length < lenth) {
                for (int i = 0; i < lenth - str.getBytes().length; i++) {
                    buff.append("0");
                }
                tempStr = tempStr + buff.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return tempStr;
    }


    /**
     * 字符串左边补0
     *
     * @param code
     * @param len
     * @return
     */

    public static String strAutozeroBefore(String code, int len) {
        String strHao = code;
        while (strHao.length() < len) {
            strHao = "0" + strHao;
        }
        return strHao;
    }


    /**
     * 打印Map的内容
     *
     * @param map
     * @return
     */
    public static void printHashMapContent(java.util.HashMap map) {
        try {
            java.util.Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                java.util.Map.Entry e = (java.util.Map.Entry) iter.next();
                String key = (String) e.getKey();
                String value = o2s(e.getValue());
                //System.out.println(key + ":" + value);
                android.util.Log.w("map", key + ":" + value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //字符序列转换为16进制字符串

    /**
     * 例子 {0x31,0x32,0xab,0xcd}  >  “3132ABCD”
     *
     * @param
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return "";
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }
    //字符序列转换为16进制字符串
    /**
     * 例子 {0x31,0x32,0xab,0xcd}  >  “3132ABCD”
     * @param
     * @return
     */
    public static String bytesToHexString(byte[] src, int len) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return "";
        }
        char[] buffer = new char[2];
        for (int i = 0; i < len; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString().toUpperCase();
    }
    //字符序列转换为16进制字符串,大写

    /**
     * 例子 {0x31,0x32,0xab,0xcd}  >  “31 32 AB CD”
     *
     * @param
     * @return
     */
    public static String bytesToHexString_upcase(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");

        if (src == null || src.length <= 0) {
            return "";
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            stringBuilder.append(buffer);
            stringBuilder.append(" ");
        }

        return stringBuilder.toString().toUpperCase();
    }

    /**
     * 例子 “3132ABCD” > {0x30,0x31 ,0x30,0x32,0x41,0x42,0x43,0x44}
     *
     * @param
     * @return
     */
    public static byte[] StringToAsciiByte(String hex) {
        int len = hex.length();
        byte[] result = new byte[len];
        String indata = hex.toUpperCase();

        for (int i = 0; i < len; i++) {
            char c = indata.charAt(i);
            result[i] = (byte) c;
        }
        return result;
    }


    /**
     * 例子：“3132ABCD”》{0x31,0x32,0xab,0xcd}
     *
     * @param hex
     * @return
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toUpperCase().toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    /**
     * 例子：“3132ABCD”》{0x31,0x32,0xab,0xcd}
     *
     * @param hex0
     * @return
     */
    public static byte[] hexStringToByte0(String hex0) {
        StringBuffer temp;
        if (hex0.length() % 2 == 1) {
            temp = new StringBuffer(hex0);
            temp.append('0');
        } else {
            temp = new StringBuffer(hex0);
        }
        String hex = temp.toString();

        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toUpperCase().toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    public static String Int2HexString_upcase(int a) {
        StringBuffer result = new StringBuffer();
        result.append(Integer.toHexString(a));
        if (result.length() % 2 == 1) {
            result.insert(0, '0');
        }
        return result.toString().toUpperCase();
    }

    public static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    public static String Int2HexString(int a) {
        StringBuffer result = new StringBuffer();
        result.append(Integer.toHexString(a));
        if (result.length() % 2 == 1) {
            result.insert(0, '0');
        }
        return result.toString();
    }

    public static boolean isNumeric(String str) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[0-9]*");
        java.util.regex.Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static int differentDays(java.util.Date date1, java.util.Date date2) {
        java.util.Calendar cal1 = java.util.Calendar.getInstance();
        cal1.setTime(date1);
        java.util.Calendar cal2 = java.util.Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(java.util.Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(java.util.Calendar.DAY_OF_YEAR);
        int year1 = cal1.get(java.util.Calendar.YEAR);
        int year2 = cal2.get(java.util.Calendar.YEAR);
        if (year1 != year2) {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    timeDistance += 366;
                } else {
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1);
        } else {
            return day2 - day1;
        }
    }
}
