package com.hessian.client;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.hessian.BaseTest;
import com.hessian.api.LotteryRecordHessianService;
import com.hessian.vo.LotteryRecordViewObject;

public class LotteryRecordHessianServiceClient2 extends BaseTest{
    
    @Autowired
    private LotteryRecordHessianService lotteryRecordHessianService ;

    @Test
    public void testGetAllByHessian() throws Exception {
        List<LotteryRecordViewObject> resultList = lotteryRecordHessianService.getAllByHessian();
        System.out.println(JSON.toJSONString(resultList));
    }
    
    @Test
    public void testGetCountByHessian() throws Exception {
        Long count = lotteryRecordHessianService.getCountByHessian();
        System.out.println(JSON.toJSONString(count));
    }

    
}
