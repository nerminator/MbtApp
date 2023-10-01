package com.daimler.biziz.android.utils;

import java.util.ArrayList;

public class BottomSheelHelper {

    public static ArrayList<String> createYearList(ArrayList<Integer> intList) {
        ArrayList<String> result = new ArrayList<String>();
        for (Integer i : intList) {
            result.add(String.valueOf(i));
        }
        return result;
    }

    public static String[] getDisplayValues(ArrayList<String> list) {
        return list.toArray(new String[0]);
    }
}
