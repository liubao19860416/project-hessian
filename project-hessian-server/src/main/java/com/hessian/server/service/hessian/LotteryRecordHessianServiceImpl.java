package com.hessian.server.service.hessian;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hessian.api.LotteryRecordHessianService;
import com.hessian.server.service.LotteryRecordService;
import com.hessian.vo.LotteryRecordViewObject;

/**
 * Hessian对应的rpc接口实现
 * 
 * @author Liubao
 * @2016年4月19日
 * 
 */
@Service
public class LotteryRecordHessianServiceImpl implements
        LotteryRecordHessianService {
    
    @Autowired
    private LotteryRecordService lotteryRecordService ;

    @Override
    public List<LotteryRecordViewObject> getAllByHessian() {
        //List<LotteryRecordViewObject> list=new ArrayList<LotteryRecordViewObject>();
        //list.add(new LotteryRecordViewObject("100", 1, 2, 3, 4, 5, 6, 7));
        //return list;
        return lotteryRecordService.getAll(true);
    }

    @Override
    public Long getCountByHessian() {
        return lotteryRecordService.getAllCount();
    }

}
