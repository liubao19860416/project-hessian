package com.hessian.server.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hessian.server.dao.LotteryRecordDAO;
import com.hessian.server.dao.entity.LotteryRecord;
import com.hessian.server.service.LotteryRecordService;
import com.hessian.server.service.base.GenericServiceImpl;
import com.hessian.vo.LotteryRecordViewObject;

@Service
public class LotteryRecordServiceImpl extends
        GenericServiceImpl<LotteryRecordViewObject, LotteryRecord, LotteryRecordServiceImpl>
        implements LotteryRecordService {
    
    @SuppressWarnings("unused")
    private Logger logger = LoggerFactory.getLogger(LotteryRecordServiceImpl.class);
    
    @Resource
    private LotteryRecordDAO lotteryRecordDAO;
    
    public LotteryRecordServiceImpl() {
        super();
    }

    @Autowired
    public LotteryRecordServiceImpl(LotteryRecordDAO lotteryRecordDAO) {
        super(lotteryRecordDAO);
        this.lotteryRecordDAO=lotteryRecordDAO;
    }

}
