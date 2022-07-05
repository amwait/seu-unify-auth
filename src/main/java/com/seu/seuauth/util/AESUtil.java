package com.seu.seuauth.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;


public class AESUtil {
    private static final String CHARSET_NAME = "UTF-8";
    private static final String AES_NAME = "AES";
    public static final String ALGORITHM = "AES/CBC/PKCS7Padding";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String encrypt(String content, String key, String iv) {
        byte[] result = null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET_NAME), AES_NAME);
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, paramSpec);
            result = cipher.doFinal(content.getBytes(CHARSET_NAME));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return Base64.getEncoder().encodeToString(result);
    }

    public static String decrypt(String content, String key) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET_NAME), AES_NAME);
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(new byte[16]);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(content)), CHARSET_NAME);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return "";
    }
}