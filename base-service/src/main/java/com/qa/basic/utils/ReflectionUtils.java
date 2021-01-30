package com.qa.basic.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by chasen on 2021/1/30.
 */
public class ReflectionUtils {
    public ReflectionUtils() {
    }

    public static Method getCurrentMethod(Class... paramTypes) {
        StackTraceElement[] yste = Thread.currentThread().getStackTrace();
        if(yste.length < 2) {
            return null;
        } else {
            String str = "";

            for(int i = 0; i < yste.length; ++i) {
                if(yste[i].getMethodName().equals("getCurrentMethod")) {
                    Class cC = null;

                    try {
                        cC = Class.forName(yste[i + 1].getClassName());
                    } catch (ClassNotFoundException var9) {
                        var9.printStackTrace();
                    }

                    Method[] ym = cC.getMethods();
                    str = yste[i + 1].toString();
                    str = str.substring(0, str.lastIndexOf(40));
                    String[] tmp = str.split("\\.");
                    String mname = tmp[tmp.length - 1];

                    for(int j = 0; j < ym.length; ++j) {
                        if(mname.equals(ym[j].getName()) && Arrays.equals(ym[j].getParameterTypes(), varsToArray(paramTypes))) {
                            return ym[j];
                        }
                    }
                }
            }

            return null;
        }
    }

    private static <T> T[] varsToArray(T... paramsTypes) {
        ArrayList ls = new ArrayList();

        for(int i = 0; i < paramsTypes.length; ++i) {
            ls.add(paramsTypes[i]);
        }

        return (T[]) ls.toArray();
    }
}
