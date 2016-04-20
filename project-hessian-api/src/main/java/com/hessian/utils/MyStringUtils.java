package com.hessian.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

public class MyStringUtils {
    
    private MyStringUtils() {
        super();
    }
    
    private static AtomicInteger uniqueId = new AtomicInteger(0);
    
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(genericCode("a"));
        }
    }
    
    /**
     * 
     * 功能描述: <br>
     * 通用生成业务主键方法
     * @param businessType 业务标志
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String  genericCode(String businessType){
        StringBuffer code = new StringBuffer();
        String date = DatetimeUtils.formatDate(new Date(), "yyyyMMddhhmmss");
        code.append(businessType);
        code.append(date);
        code.append(uniqueId.incrementAndGet());
        return code.toString();
    }

    public static boolean hasNull(Object... params) {
        for (int i = 0; i < params.length; i++) {
            if (params[i] == null) {
                return true;
            }
        }
        return false;
    }
    
    /** 
     * 判断字符串是否为空或空字符串 
     * @param str 原字符串 
     * @return 
     */  
    public static boolean isEmpty(String str) {  
        return str == null || "".equals(str);  
    }  
    
     /** 
     * 全角转半角: 
     * @param fullStr 
     * @return 
     */  
    public static final String full2Half(String fullStr) {  
        if(isEmpty(fullStr)){  
            return fullStr;  
        }  
        char[] c = fullStr.toCharArray();  
        for (int i = 0; i < c.length; i++) {  
            if (c[i] >= 65281 && c[i] <= 65374) {  
                c[i] = (char) (c[i] - 65248);  
            } else if (c[i] == 12288) { // 空格  
                c[i] = (char) 32;  
            }  
        }  
        return new String(c);  
    }
    
    /** 
     * 半角转全角 
     * @param halfStr 
     * @return 
     */  
    public static final String half2Full(String halfStr) {  
        if(isEmpty(halfStr)){  
            return halfStr;  
        }  
        char[] c = halfStr.toCharArray();  
        for (int i = 0; i < c.length; i++) {  
            if (c[i] == 32) {  
                c[i] = (char) 12288;  
            } else if (c[i] < 127) {  
                c[i] = (char) (c[i] + 65248);  
            }  
        }  
        return new String(c);  
    } 
    
    /**
     * 字符串转义
     */
    public static String escape(String string) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = string.length(); i < length; i++) {
            char c = string.charAt(i);
            switch (c) {
            case '&':
                sb.append("&amp;");
                break;
            case '<':
                sb.append("&lt;");
                break;
            case '>':
                sb.append("&gt;");
                break;
            case '"':
                sb.append("&quot;");
                break;
            case '\'':
                sb.append("&apos;");
                break;
            default:
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    /**
     * 转换html相关标签
     * @param html
     * @return
     */
    public static String trimHtml2Txt(String html) {
        if (null != StringUtils.trimToNull(html)) {
            html = html.replaceAll("\\<head>[\\s\\S]*?</head>(?i)", "");// 去掉head
            html = html.replaceAll("\\<!--[\\s\\S]*?-->", "");// 去掉注释
            html = html.replaceAll("\\<![\\s\\S]*?>", "");
            html = html.replaceAll("\\<style[^>]*>[\\s\\S]*?</style>(?i)", "");// 去掉样式
            html = html.replaceAll("\\<script[^>]*>[\\s\\S]*?</script>(?i)", "");// 去掉js
            html = html.replaceAll("\\<w:[^>]+>[\\s\\S]*?</w:[^>]+>(?i)", "");// 去掉word标签
            html = html.replaceAll("\\<xml>[\\s\\S]*?</xml>(?i)", "");
            html = html.replaceAll("\\<html[^>]*>|<body[^>]*>|</html>|</body>(?i)", "");
            html = html.replaceAll("\\\r\n|\n|\r", " ");// 去掉换行
            html = html.replaceAll("\\<br[^>]*>(?i)", "\n");
            html = html.replaceAll("\\</p>(?i)", "\n");
            html = html.replaceAll("\\<[^>]+>", "");
            html = html.replaceAll("\\ ", " ");
//            html = html.replaceAll("&nbsp;", " ");
//            html = html.replaceAll("&gt;", ">");
//            html = html.replaceAll("&lt;", "<");
//            html = html.replaceAll("&quot;", "\"");
//            html = html.replaceAll("&copy;", "?");
//            html = html.replaceAll("&reg;", "?");
//            html = html.replaceAll("&amp;", "&");
            return html.trim();
        }else{
            return "";
        }
    }

    /**
     * 判断对象是否为空，不为空则返回字符串，否则返回空，用于从Map中取值转化成字符串
     */
    public static String getStringValue(Object obj) {
        if (obj != null) {
            return obj.toString();
        } else {
            return "";
        }
    }

    /**
     * 判断对象是否为空，不为空则返回字符串，否则返回空，用于从Map中取值转化成字符串
     */
    public static float getFloatValue(Object obj) {
        if (obj != null) {
            BigDecimal bd = new BigDecimal(obj.toString());
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            return bd.floatValue();
        } else {
            return 0;
        }
    }

    /**
     * 判断对象是否为空，不为空则返回字符串，否则返回空，用于从Map中取值转化成字符串
     */
    public static int getIntValue(Object obj) {
        if (obj != null) {
            return Integer.valueOf(obj.toString());
        } else {
            return 0;
        }
    }

    /**
     * 判断对象是否为空，不为空则返回字符串，否则返回空，用于从Map中取值转化成字符串
     */
    public static Long getLongValue(Object obj) {
        if (obj != null) {
            return Long.valueOf(obj.toString());
        } else {
            return 0L;
        }
    }

    /**
     * 用于从map中获取折扣值
     */
    public static float getDiscount(Object obj) {
        return getFloatValue(obj) == 0 ? 1 : getFloatValue(obj);
    }

    /**
     * 将任意数字对象转成两位小数的字符串
     */
    public static String formatNumber(Number number, int digits) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(digits);
        numberFormat.setMinimumFractionDigits(digits);
        return StringUtils.replace(numberFormat.format(number), ",", "");
    }

    /**
     * 将任意数字乴组成的字符串转成两位小数的字符串
     */
    public static String formatNumberFromStr(String numberStr, int digits) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(digits);
        numberFormat.setMinimumFractionDigits(digits);
        return StringUtils.replace(numberFormat.format(numberStr), ",", "");
    }

    /**
     * 新增一个object是否为空判断
     */
    public static boolean isEmpty(Object o) {
        return o == null || "".equals(o);
    }

}
