package com.hessian.api;

import java.util.List;

import com.hessian.vo.LotteryRecordViewObject;

/**
 * Hessian对应的rpc接口
 * 
 * @author Liubao
 * @2016年4月19日
 * 
 */
public interface LotteryRecordHessianService {
    
    public abstract List<LotteryRecordViewObject> getAllByHessian();
    
    public abstract Long getCountByHessian();

}
