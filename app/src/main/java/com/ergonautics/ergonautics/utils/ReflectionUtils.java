package com.ergonautics.ergonautics.utils;

import java.lang.reflect.Method;
import java.util.Locale;

/**
 * Created by patrickgrayson on 8/29/16.
 */
public class ReflectionUtils {

    public static String toCammelCase(String with_underscores){
        String result = "";
        try {
            result += with_underscores.charAt(0);
            result = result.toUpperCase(Locale.US); //first character should be uppercase
            for (int i = 1; i < with_underscores.length(); i++) {
                char c = with_underscores.charAt(i);
                if (c == '_') {
                    //We should skip the underscore and capitalize the next letter
                    i++;
                    String toAdd = with_underscores.charAt(i) + "";
                    toAdd = toAdd.toUpperCase();
                    result += toAdd;
                } else {
                    result += c;
                }
            }
        } catch (IndexOutOfBoundsException ex) {
            throw new IllegalArgumentException("Invalid underscore text format");
        }

        return result;
    }

    public static Method getSetter(String propertyName, Class target) throws NoSuchMethodException{
        Method setter = null;
        String setterName = "set" + toCammelCase(propertyName);
        for(Method m: target.getMethods()){
            if(m.getName().equals(setterName)){
                setter = m;
                //Found our setter, should be only one
                break;
            }
        }
        if(setter == null) {
            throw  new NoSuchMethodException();
        }
        return  setter;

    }

    public static Method getGetter(String propertyName, Class target) throws NoSuchMethodException {
        Method getter = null;
        String getterName = "get" + toCammelCase(propertyName);
        for(Method m: target.getMethods()){
            if(m.getName().equals(getterName)){
                getter = m;
                //Found our setter, should be only one
                break;
            }
        }
        if(getter == null) {
            throw  new NoSuchMethodException();
        }
        return  getter;
    }
}
