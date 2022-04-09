package com.baby.wind.office.assistant.util;

public class VC {

    public static boolean isTrue(Boolean val){
        return val != null && val;
    }

    public static boolean notTrue(Boolean val){
        return !isTrue(val);
    }
}
