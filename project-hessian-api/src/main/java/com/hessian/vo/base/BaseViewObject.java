package com.hessian.vo.base;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;



/**
 * 视图类基类
 */
public class BaseViewObject<V extends BaseViewObject<V>> implements Serializable{
//public class BaseViewObject implements Serializable {


    private static final long serialVersionUID = 8950660914544659827L;
    
    private Class<V> viewObjectClass;
    
    private Integer id;
    private String code;
    private String description; // 描述
    private String createdDatetime; // 记录创建日期
    private String updatedDatetime; // 记录最近修改日期
    private String actived = "1"; // 记录是否启用、激活或有效（默认为true）
    private String deleted = "0"; // 记录是否已被删除（默认为false）
    
    public Class<V> getViewObjectClass() {
        return viewObjectClass;
    }

    public void setViewObjectClass(Class<V> viewObjectClass) {
        this.viewObjectClass = viewObjectClass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(String createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public String getUpdatedDatetime() {
        return updatedDatetime;
    }

    public void setUpdatedDatetime(String updatedDatetime) {
        this.updatedDatetime = updatedDatetime;
    }

    public String getActived() {
        return actived;
    }

    public void setActived(String actived) {
        this.actived = actived;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @SuppressWarnings("unchecked")
    public BaseViewObject() {
        Type type = this.getClass().getGenericSuperclass();
        if( type != null && type instanceof ParameterizedType ) {
            viewObjectClass = ( Class<V> )( ( ParameterizedType )type )
                    .getActualTypeArguments()[0];
        }else {
            throw new IllegalArgumentException( 
                    "Type argument <V> is empty!!" );
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals( Object object ) {
        boolean b = false;
        if( object == null ) {
            b = false;
        }else if( object == this ) {
            b = true;
        }else if( object.getClass() == this.viewObjectClass ) {
            b = equals( ( V )object );
        }
        return b;
    }
    
    public boolean equals( V object ) {
        return super.equals( object );
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
