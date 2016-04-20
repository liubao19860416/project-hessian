package com.hessian.server.utils;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean2;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.AbstractConverter;
import org.apache.commons.beanutils.converters.StringConverter;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.hessian.server.conf.Constants;
import com.hessian.server.dao.entity.base.BaseEntity;
import com.hessian.utils.DatetimeUtils;
import com.hessian.utils.MyStringUtils;
import com.hessian.vo.base.BaseViewObject;

/**
 * Grape DAO 工具类
 * 
 * 
 */
public final class GrapeDAOUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(GrapeDAOUtils.class);
    
    private final static ObjectMapper jacksonMapper = new ObjectMapper();

    private GrapeDAOUtils() {
    }

    static {
        BeanUtilsBean.setInstance(new BeanUtilsBean(new CustomizedConvertUtilsBean()));
    }

    public static <V extends BaseViewObject<V>, E extends BaseEntity> void transToViewObject(
            Class<V> destClassType,List<V> viewObjects, List<E> entities) throws InstantiationException, IllegalAccessException {
        if(viewObjects==null||entities==null){
            throw new IllegalArgumentException("Argument viewObjects or entities is empty!!");
        }
        for (BaseEntity entity : entities) {
            V viewObject=destClassType.newInstance();
            //这样不可以!
            //V viewObject=new BaseViewObject<V>().getViewObjectClass().newInstance();
            GrapeDAOUtils.copyProperties(viewObject, entity);
            viewObjects.add(viewObject);
        }
    }
    
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void copyProperties(Object dest, Object orig) {
        try {
            BeanUtilsBean.getInstance().copyProperties(dest, orig);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public static <E extends BaseEntity> void populate(E bean,
            Map<String, Object> properties) throws IllegalAccessException,
            InvocationTargetException {
        BeanUtilsBean.getInstance().populate(bean, properties);
    }

    public static <E extends BaseEntity> int getPeriodValue(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Argument entity is null!!");
        }
        PropertyUtilsBean propertyUtils = BeanUtilsBean.getInstance().getPropertyUtils();
        try {
            return (int) propertyUtils.getProperty(entity, "periodValue");
        } catch (Exception ex) {
            logger.error("GrapeDAOUtils.getPeriodValue exception:", ex);
        }
        throw new IllegalArgumentException(
                "Cannot get period value from entity: "+ entity.getClass().getName());
    }

    public static <E extends BaseEntity> Map<String, List<?>> describe(E entity)
            throws IllegalAccessException, InvocationTargetException,NoSuchMethodException {
        Map<String, List<?>> mapList = new HashMap<>();
        List<String> fields = new ArrayList<>();
        mapList.put("fields", fields);
        List<Object> values = new ArrayList<>();
        mapList.put("values", values);
        PropertyUtilsBean propertyUtils = BeanUtilsBean.getInstance().getPropertyUtils();
        Map<String, Object> map = propertyUtils.describe(entity);
        if (map.size() > 0) {
            for (String key : map.keySet()) {
                // ignore the Class and id property
                if ("class".equalsIgnoreCase(key) || "id".equalsIgnoreCase(key)) {
                    continue; 
                }
                Class<?> pClazz = propertyUtils.getPropertyType(entity, key);
                //当属性为数组,Class类或者实体类BaseEntity或者集合等时候,进行跳过
                if (pClazz.isInterface() || pClazz.isArray()
                        || Collection.class.isAssignableFrom(pClazz)
                        || BaseEntity.class.isAssignableFrom(pClazz)) {
                    continue;
                }

                Object value = map.get(key);
                if (pClazz.isEnum() && (value != null)) {
                    value = value.toString();
                }
                fields.add(key);
                values.add(value);
            }
        }
        return mapList;
    }

    /**
     * 自定义注册类型转换器 class CustomizedConvertUtilsBean
     */
    private static class CustomizedConvertUtilsBean extends ConvertUtilsBean2 {
        private static final CustomizedStringConverter STRING_CONVERTER = new CustomizedStringConverter();
        private static final EnumConverter ENUM_CONVERTER = new EnumConverter();
        private static final DatetimeConverter DATE_CONVERTER = new DatetimeConverter();
//        private static final BooleanConverter BOOLEAN_CONVERTER = new BooleanConverter();

        @Override
        @SuppressWarnings("rawtypes")
        public Converter lookup(Class pClazz) {
            if (String.class.equals(pClazz)) {
                return STRING_CONVERTER;
            } else if (pClazz.isEnum()) {
                return ENUM_CONVERTER;
            } else if (Date.class.isAssignableFrom(pClazz)) {
                return DATE_CONVERTER;
            } else {
                return super.lookup(pClazz);
            }

        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static class CustomizedStringConverter extends AbstractConverter {
        StringConverter strConverter = new StringConverter();
        @Override
        protected String convertToString(final Object pValue) throws Throwable {
            if (pValue != null) {
                if (Timestamp.class.isAssignableFrom(pValue.getClass())) {
                    if (DatetimeUtils.isTimestampZero((Timestamp) pValue)) {
                        return "";
                    }
                    return DatetimeUtils.formatTimestamp((Timestamp) pValue);
                } else if (Date.class.isAssignableFrom(pValue.getClass())) {
                    if (DatetimeUtils.isDatetimeZero((Date) pValue)) {
                        return "";
                    }
                    return DatetimeUtils.formatDate((Date) pValue);
                } else if (Boolean.class.equals(pValue.getClass())) {
                    return Boolean.TRUE.equals(pValue) ? Constants.BOOLEAN_TRUE_IN_STR
                            : Constants.BOOLEAN_FALSE_IN_STR;
                } else {
                    return pValue.toString();
                }
            }
            return null;
        }

        @Override
        protected Class getDefaultType() {
            return String.class;
        }

        @Override
        protected Object convertToType(Class type, Object value)
                throws Throwable {
            if (value == null) {
                return Date.class.isAssignableFrom(type) ? (Timestamp.class
                        .isAssignableFrom(type) ? DatetimeUtils.TIMESTAMP_ZERO
                        : DatetimeUtils.DATETIME_ZERO) : null;
            }
            if (type.isEnum()) {
                return Constants.getEnumByValue(type, value.toString());
            }
            return strConverter.convert(type, value);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static class EnumConverter extends AbstractConverter {
        @Override
        protected String convertToString(final Object pValue) throws Throwable {
            return ((Enum<?>) pValue).toString();
        }

        @Override
        protected Class getDefaultType() {
            return null;
        }

        @Override
        protected Object convertToType(Class type, Object value)
                throws Throwable {
            if (value == null) {
                return null;
            }
            return Constants.getEnumByValue(type, value.toString());
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static class DatetimeConverter extends AbstractConverter {
        @Override
        protected String convertToString(final Object pValue) throws Throwable {
            if (DatetimeUtils.isDatetimeZero((Date) pValue)) {
                return "";
            }
            return DatetimeUtils.formatDate((Date) pValue);
        }

        @Override
        protected Class getDefaultType() {
            return null;
        }

        @Override
        protected Object convertToType(Class type, Object value) throws Throwable {
            if (value == null || "".equals(value)) {
                return DatetimeUtils.TIMESTAMP_ZERO;
            }
            return DatetimeUtils.parseTimestamp((String) value);
        }
    }
    
    public static <T> T fromJson( Class<T> clazz, String jsonStr ) {
        T jsonObj = null;
        if( jsonStr != null && ! "".equals( jsonStr ) ) {
            try {
                jsonObj = jacksonMapper.readValue( jsonStr, clazz );
            } catch ( Exception ex ) {
                throw new RuntimeException( ex );
            }
        }
        return jsonObj;
    }
    
    public static String toJsonString( Object obj ) {
        if( obj != null ) {
            try {
                return jacksonMapper.writeValueAsString( obj );
            } catch ( Exception ex ) {
                throw new RuntimeException( ex );
            }
        }
        return null;
    }
    
    //不允许更新为空值
    public static <T> void fromObjectToMapNotNull( T object, Map<String,Object> paramMap ) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if(paramMap==null){
            paramMap=new HashMap<String, Object>();
        }
        PropertyUtilsBean propertyUtils = BeanUtilsBean.getInstance().getPropertyUtils();
        Map<String, Object> map = propertyUtils.describe(object);
        if (map.size() > 0) {
            for (String key : map.keySet()) {
                Class<?> pClazz = propertyUtils.getPropertyType(object, key);
                //当属性为数组,Class类或者实体类BaseEntity或者集合等时候,进行跳过
                if ("class".equalsIgnoreCase( key )
                        ||pClazz.isInterface() || pClazz.isArray()
                        || Collection.class.isAssignableFrom(pClazz)
                        || BaseEntity.class.isAssignableFrom(pClazz)) {
                    continue;
                }

                Object value = map.get(key);
                if (pClazz.isEnum() && (value != null)) {
                    value = value.toString();
                }
                if(StringUtils.isNotBlank(MyStringUtils.getStringValue(value))){
                    paramMap.put(key, value);
                }
            }
        }
    }
    
    //允许更新为空值
    public static <T> void fromObjectToMapAllowsNull( T object, Map<String,Object> paramMap ) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if(paramMap==null){
            paramMap=new HashMap<String, Object>();
        }
        PropertyUtilsBean propertyUtils = BeanUtilsBean.getInstance().getPropertyUtils();
        Map<String, Object> map = propertyUtils.describe(object);
        if (map.size() > 0) {
            for (String key : map.keySet()) {
                Class<?> pClazz = propertyUtils.getPropertyType(object, key);
                //当属性为数组,Class类或者实体类BaseEntity或者集合等时候,进行跳过
                if ("class".equalsIgnoreCase( key )
                        ||pClazz.isInterface() || pClazz.isArray()
                        || Collection.class.isAssignableFrom(pClazz)
                        || BaseEntity.class.isAssignableFrom(pClazz)) {
                    continue;
                }
                
                Object value = map.get(key);
                if (pClazz.isEnum() && (value != null)) {
                    value = value.toString();
                }
                paramMap.put(key, value);
            }
        }
    }
    
    /**
     * 将两个对象进行属性拷贝
     */
    public static <T> List<T> fromListMapToListT( Class<T> classType, List<Map<String,Object>> orginal ) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String text=JSON.toJSONString(orginal);
        //该方法在迭代的时候,需要类型强制转换,否则会报错,所以选第一种方法即可;
        //List<T> dest2 = JSONObject.parseObject(text, List.class);
        List<T> dest = JSON.parseArray(text, classType);
        return dest;
    }


}
