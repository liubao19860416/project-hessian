package com.hessian.server;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hessian.api.LotteryRecordHessianService;
import com.hessian.vo.LotteryRecordViewObject;

/**
 * HessianServerTest测试类
 * 
 * @author LiuBao
 * @version 1.0.0
 * @Date 2016年3月24日
 * 
 */
public class HessianServerTest extends BaseTest {

    @Autowired
    private LotteryRecordHessianService lotteryRecordHessianService;

    @Test
    public void testGetByHessian() throws Exception {
        Long count = lotteryRecordHessianService.getCountByHessian();
        Assert.assertTrue(count>=0);
        List<LotteryRecordViewObject> result = lotteryRecordHessianService.getAllByHessian();
        Assert.assertTrue(result!=null);
    }
    
    
}