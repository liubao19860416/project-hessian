package com.hessian;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 基础测试环境
 * 
 * @author Liubao
 * @2016年4月24日
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
        "classpath:remote-hessian.xml"
        })
public class BaseTest {

}
