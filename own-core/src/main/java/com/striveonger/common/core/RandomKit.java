package com.striveonger.common.core;

import java.util.Random;

/**
 * @author Mr.Lee
 * @since 2024-11-04 10:01
 */
public class RandomKit {

    public static String randomString(int length) {
        char[] chars = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L','M', 'N', 'O', 'P', 'Q', 'R','S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(chars.length);
            sb.append(chars[number]);
        }
        return sb.toString();
    }

    public static int randomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

}
