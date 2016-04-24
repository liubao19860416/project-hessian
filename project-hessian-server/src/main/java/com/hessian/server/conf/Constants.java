package com.hessian.server.conf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * 基础常量配置类
 * 
 * @author Liubao
 * @2016年4月24日
 *
 */
public class Constants {
    
    public static void main(String[] args) {
        Enum<?> enum1 = getEnumByValue(UserCouponStatus.class, "已过期");
        System.out.println(enum1.toString());
        System.out.println(readValue("CALLBACKLISTENER.URL"));
    }
    
    private static final Logger logger = LoggerFactory.getLogger(Constants.class);

    private static Properties config = null;
    public static final int RETRYTIMES = 3;
    
    private Constants() {
    }

    static {
        logger.info("Params static load constant.properties");
        // 会重新加载spring框架
        ClassPathResource cr = new ClassPathResource("constant.properties");
        config = new Properties();
        try {
            config.load(cr.getInputStream());
        } catch (IOException e) {
            logger.error("资源文件不存在", e);
        }
    }

    /**
     * 根据key读取value
     */
    public static String readValue(String key) {
        try {
            String value = config.getProperty(key);
            return value;
        } catch (Exception e) {
            logger.error("ConfigInfoError", e);
            return null;
        }
    }
    
    public static final String CODE_SEPARATOR = ","; // 字段值分割符
    public static final String FULL_PAHT_CODE_SEPARATOR = "/"; // 字段值分割符
    public static final String NAME_SEPARATOR = "-"; // 字段值分割符

    public static final String CODE_WILDCARD = "*"; // 编号通配符
    public static final String HTML_WRAP_CHARACTER = "<br/>"; // HTML换行符

    public static final int PAGESIZE_MAX = 500;// 分页查询最大值
    public static final int PAGESIZE_DEFAULT = 10;// 分页查询最大值
    public static final int BATCH_SIZE = 100;// 批量插入最大值

    // boolean true/false 字符串表示
    public static final String BOOLEAN_TRUE_IN_STR = "1";
    public static final String BOOLEAN_FALSE_IN_STR = "0";
    
    public static final String BOOLEAN_TRUE_STR = "true";
    public static final String BOOLEAN_FALSE_STR = "false";

    public static final HashMap<Integer, String> weekMap = new HashMap<>();
    private static final Map<Class<? extends Enum<?>>, Enum<?>[]> mapEnumTypes = new HashMap<>();

    static {
        mapEnumTypes.put(OrderStatus.class,
                OrderStatus.class.getEnumConstants());
        mapEnumTypes.put(UserCouponStatus.class,
                UserCouponStatus.class.getEnumConstants());

        weekMap.put(0, "星期日");
        weekMap.put(1, "星期一");
        weekMap.put(2, "星期二");
        weekMap.put(3, "星期三");
        weekMap.put(4, "星期四");
        weekMap.put(5, "星期五");
        weekMap.put(6, "星期六");
    }



    // 用户保养券状态--1 已生成(未激活),2 已发放(已激活),3 已使用 ,4 已结算,5 已关联,6 已取消,7 已过期
    public static enum UserCouponStatus {
        GENERATED(1, "已生成"), 
        ISSUED(2, "已发放"), 
        USED(3, "已使用"), 
        SETTLED(4, "已结算"), 
        ASSOCIATED(5, "已关联"), 
        CANCELED(6, "已取消"), 
        EXPIRED(7, "已过期"),
        NA(0, "-"); // N/A 未知

        private int key;
        private String value;

        private UserCouponStatus(int key, String value) {
            this.key = key;
            this.value = value;
        }

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        /**
         * 返回对应的key的方法
         */
        public static int getKey(UserCouponStatus couponStatus) {
            int key = 0;
            UserCouponStatus[] values = UserCouponStatus.values();
            for (UserCouponStatus us : values) {
                if (couponStatus.getValue().equalsIgnoreCase(us.getValue())) {
                    key = us.getKey();
                }
            }
            return key;
        }

        @Override
        public String toString() {
            return getValue();
        }
    }

    public static enum OrderStatus {
        UNCONFIRMED(1, "未确认", 1), 
        CONFIRMED(2, "已确认", 2), 
        REFUSED(3, "已拒绝", 5), 
        FINISHED(9, "已完工", 3), 
        KMUPDATED(11, "已更新公里数", 4), 
        CANCELED(99, "已取消", 6), 
        COMMENTED(97, "已评论", 12), 
        EXPIRED(96, "已过期", 13), 
        VIEWREFUSED(95,"拒绝已查看", 14), 
        ERROR(404, "有错误", 404);
        
        private String key;
        private int code;
        private int sort;

        OrderStatus(int code, String key, int sort) {
            this.key = key;
            this.code = code;
            this.sort = sort;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return getKey();
        }

        public static OrderStatus getKey(int code) {
            OrderStatus[] os = OrderStatus.values();
            for (int i = 0; i < os.length; i++) {
                if (os[i].getCode() == code) {
                    return os[i];
                }
            }
            return OrderStatus.ERROR;
        }
    }

    public static Enum<?> getEnumByValue(Class<? extends Enum<?>> enumClass,
            String value) {

        if (enumClass == null || !enumClass.isEnum()) {
            throw new IllegalArgumentException("Argument enumClass is null!!");
        }

        if (value == null || "".equals(value)) {
            return null;
        }

        for (Class<? extends Enum<?>> eclass : mapEnumTypes.keySet()) {
            if (eclass.equals(enumClass)) {
                for (Enum<?> e : mapEnumTypes.get(eclass)) {
                    if (e.toString().equals(value)) {
                        return e;
                    }
                }
            }
        }
        throw new IllegalArgumentException("Enum class \""
                + enumClass.getName() + "\"" + " with value \"" + value + "\""
                + " is not supported!!");
    }

}
