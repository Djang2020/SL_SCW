package com.atguigu.crowd.test;

import ch.qos.logback.core.CoreConstants;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.util.CrowdUtil;
import org.junit.Test;

/**
 * @author Django
 * @date 2022-02-20 11:04
 * @DESC:
 */
public class StringTest {
    /**
     * 加密算法测试
     */
    @Test
    public void testMd5(){
        String source = "123456";
        String encoded = CrowdUtil.md5(source);
        System.out.println("encoded = " + encoded);
    }
}
