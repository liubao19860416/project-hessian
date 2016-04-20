package com.hessian.server.dao.impl;

import org.springframework.stereotype.Repository;

import com.hessian.server.dao.LotteryRecordDAO;
import com.hessian.server.dao.base.GenericDAOBatisImpl;
import com.hessian.server.dao.entity.LotteryRecord;

@Repository
public class LotteryRecordDAOImpl extends
        GenericDAOBatisImpl<LotteryRecord, LotteryRecordDAOImpl> implements
        LotteryRecordDAO {
}
