package com.defi.util.random;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class RandomUtil {
    private static final Random random = new Random();

    public static int nextInt(int bound) {
        return random.nextInt(bound);
    }

    public static int nextInt(int low, int height) {
        return random.nextInt(height - low) + low;
    }

    public static int randomIndexByRate(int[] rate, int total){
        int random = nextInt(total);
        for (int i = 0; i < rate.length; i++) {
            if (random < rate[i]) {
                return i;
            }
            random -= rate[i];
        }
        return -1;
    }

    public static boolean nextBoolean(double rate) {
        return random.nextDouble() < rate;
    }
}
