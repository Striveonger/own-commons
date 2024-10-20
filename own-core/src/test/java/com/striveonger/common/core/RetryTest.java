package com.striveonger.common.core;

import com.striveonger.common.core.exception.CustomException;
import org.junit.Test;

import java.util.Arrays;

public class RetryTest {

    @Test
    public void test() {
        final int[] x = {0};
        int[] execute = Retry.execute(() -> {
            x[0]++;
            if (x[0] < 5) {
                throw new CustomException("la la la~");
            }
            return x;
        }, 11, null);
        System.out.println(Arrays.toString(execute));
    }
}