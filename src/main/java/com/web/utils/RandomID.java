package com.web.utils;

import java.util.Random;

public class RandomID {


    public static String genID(){
        int max = 20000;
        int min = 10;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return String.valueOf(s);
    }

    public static String genID2(){
        int max = 2000;
        int min = 100;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return String.valueOf(s);
    }

    public static String genIDWorker(){
        int max = 30000;
        int min = 20001;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return String.valueOf(s);
    }

}
