package com.hessian.server.dao.entity.base;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;


@Alias( "baseEntity" )
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 8100192126279967648L;
    
    private Integer id;                 // 表关键字
    private String code;                // 业务编码（关键字）
    private String description;         // 描述
    private Timestamp createdDatetime;  // 记录创建日期
    private Timestamp updatedDatetime;  // 记录最近修改日期
    private boolean actived = true;     // 记录是否启用、激活或有效（默认为true）
    private boolean deleted = false;    // 记录是否已被删除（默认为false）
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public final String getCode() {
        return code;
    }

    public final void setCode(String code) {
        this.code = code;
    }

    public final boolean isDeleted() {
        return deleted;
    }

    public final void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public final Timestamp getCreatedDatetime() {
        return createdDatetime;
    }
    
    public final void setCreatedDatetime(Timestamp createdDatetime) {
        this.createdDatetime = createdDatetime;
    }
    
    public final Timestamp getUpdatedDatetime() {
        return updatedDatetime;
    }
    
    public final void setUpdatedDatetime(Timestamp updatedDatetime) {
        this.updatedDatetime = updatedDatetime;
    }

    public /*final*/ String getDescription() {
        return description;
    }

    public /*final*/ void setDescription(String description) {
        this.description = description;
    }

    public final boolean isActived() {
        return actived;
    }

    public final void setActived(boolean actived) {
        this.actived = actived;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
