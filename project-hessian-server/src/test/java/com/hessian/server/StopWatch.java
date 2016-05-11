package com.hessian.server;

import lombok.Data;

/**
 * 压测实体类
 * 
 * @author Liubao
 * @2016年5月11日
 * 
 */
@Data
public class StopWatch {

    private long startTime;
    private long endTime;
    // 一次hessian请求耗时
    private long elapsedTime;
    // 成功失败
    private boolean status;

    public StopWatch() {
        this.startTime = 0L;
        this.endTime = 0L;
        this.elapsedTime = 0L;
        this.status = false;
    }
}