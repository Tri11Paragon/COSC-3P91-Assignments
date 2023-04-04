package ca.cosc3p91.a4.util;

import java.util.Random;

public class Util {

    public static int randomInt(int min, int max, Random random) {
        return random.nextInt(max - min) + min;
    }

    public static int clamp(int min, int max, int i){
        return Math.min(Math.max(i, min), max);
    }

}
