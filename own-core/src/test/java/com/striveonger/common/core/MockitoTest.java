package com.striveonger.common.core;

import com.striveonger.common.core.result.Result;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @since 2024-09-05 11:33
 */
@RunWith(MockitoJUnitRunner.class)
// @SpringBootTest
public class MockitoTest {

    private static final Logger log = LoggerFactory.getLogger(MockitoTest.class);

    @Mock
    private Result result;

    @InjectMocks
    private Zliv liv;


    @Before
    public void setup() {
        Mockito.doReturn(201).when(result).getState();
        Mockito.doReturn("Piling").when(result).getCode();
        Mockito.doReturn("Mockito Piling").when(result).getMessage();
    }

    @Test
    public void test() {
        // 会将: result 自动注入到 liv 中
        // 从hashcode, 可以看出, 是同一个对象
        log.info("result hashcode: {}", result.hashCode());
        log.info("liv result hashcode: {}", liv.getResult().hashCode());
        log.info("result: {}", Jackson.toJSONString(liv.getResult()));
    }

    public static class Zliv {

        private Result result;

        public Result getResult() {
            return result;
        }

        public void setResult(Result result) {
            this.result = result;
        }
    }

}
