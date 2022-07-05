package com.seu.seuauth.util;

import java.util.HashMap;
import java.util.List;

public class CookieUtil {

    public static HashMap<String,String> cookie2Map(List<String> cookieList){
        HashMap<String,String> hashMap = new HashMap<>();
        if(cookieList!=null){
            for(String cookieit :cookieList){
                int equalpos = cookieit.indexOf("=");
                int endpos = cookieit.length();
                if(cookieit.contains(";")){
                    endpos = cookieit.indexOf(";");
                }
                String cookiename = cookieit.substring(0,equalpos);
                String cookievalue = cookieit.substring(equalpos+1,endpos);
                hashMap.put(cookiename,cookievalue);
            }
        }
        return hashMap;
    }
    public static String getCookieStr(HashMap<String,String> cookieMap){
        StringBuilder stringBuilder = new StringBuilder();
        for(String key :cookieMap.keySet()){
            stringBuilder.append(key).append("=").append(cookieMap.get(key)).append(";");
        }
        return stringBuilder.toString();
    }

}
