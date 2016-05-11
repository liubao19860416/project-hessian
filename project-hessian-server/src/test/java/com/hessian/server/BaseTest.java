package com.hessian.server;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 加载运行环境
 * 
 * @author Liubao
 * @2016年5月11日
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration( locations = { 
        "classpath:remote-hessian.xml"
})
public class BaseTest {
    
    @Before
    public void init() throws Exception {
    }

    @After
    public void down() throws Exception {
    }

}
