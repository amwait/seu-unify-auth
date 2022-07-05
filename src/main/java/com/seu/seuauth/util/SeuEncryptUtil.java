package com.seu.seuauth.util;

public class SeuEncryptUtil {
    private static final String _chars = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678";
    private static final int  _chars_len = _chars.length();
    public static String encryptAES(String data,String pwdDefaultEncryptSalt) {
        return _gas(_rds(64) + data, pwdDefaultEncryptSalt, _rds(16));
    }
    private static String _gas(String data,String key0,String iv0) {
        return AESUtil.encrypt(data,key0,iv0);
    }
    private static String _rds(int len) {
        StringBuilder retStr = new StringBuilder();
        for (int i = 0; i < len; i++) {
            retStr.append(_chars.charAt((int) Math.floor(Math.random() * _chars_len)));
        }
        return retStr.toString();
    }
}
