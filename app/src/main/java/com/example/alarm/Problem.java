package com.example.alarm;

import java.util.Random;

public class Problem {

    private float result;
    private final Random random = new Random();

    int getRandom(int min, int max) {
        return (int) (Math.random() * (max - min)) + min;
    }

    public float getResult() {
        return result;
    }
    public float getNotResult() {
        return result + getRandom(-9, -2);
    }

    public String getProblem() {
        int a = getRandom(-50, 50);
        int b = getRandom(-50, 50);
        String sign;
        switch (getRandom(0, 2)){
            case 1:
                sign = getRandomSign_sqr();
                break;
            case 0:
                sign = getRandomSign();
                break;
            default:
                return random.nextBoolean() ? getRandomSign() : getRandomSign_sqr();
        }
        switch (sign) {
            case "+":
                result = a + b;
                break;
            case "-":
                result = a - b;
                break;
            case "/":
                if (b==0){
                    if(random.nextBoolean())
                        b = getRandom(1, 50);
                    else b = getRandom(-50, -1);
                }
                result = (float) a / b;
                break;
            case "*":
                result = a * b;
                break;
        }
        return a + " "+ sign +" "+ b + " =";
    }
    private String getRandomSign() {
        return random.nextBoolean() ? "-" : "+";
    }
    private String getRandomSign_sqr() {
        return random.nextBoolean()? "/" : "*";

    }
}



