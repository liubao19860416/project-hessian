package com.hessian.client;

import java.net.MalformedURLException;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.caucho.hessian.client.HessianProxyFactory;
import com.hessian.api.LotteryRecordHessianService;
import com.hessian.vo.LotteryRecordViewObject;

public class LotteryRecordHessianServiceClient {

    public static void main(String[] args) throws MalformedURLException {
        String url = "http://localhost:8080/project-hessian-server/remote/getAllLotteryRecord";
        HessianProxyFactory hessianProxyFactory = new HessianProxyFactory();
        LotteryRecordHessianService proxyHessianService = (LotteryRecordHessianService) hessianProxyFactory
                .create(LotteryRecordHessianService.class, url);
        List<LotteryRecordViewObject> resultList = proxyHessianService.getAllByHessian();
        System.out.println(JSON.toJSONString(resultList));
        Long countByHessian = proxyHessianService.getCountByHessian();
        System.out.println(JSON.toJSONString(countByHessian));
    }

}
