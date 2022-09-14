package com.ntg.sadmin.utils;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.ntg.sadmin.common.NTGMessageOperation;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ntg.sadmin.constants.ConfigurationConstant;
import com.ntg.sadmin.data.service.ConfigurationEntityService;


@Component
public class EncryptionUtils {


    private final ConfigurationEntityService configurationEntityService;

    @Autowired
    public EncryptionUtils(ConfigurationEntityService configurationEntityService) {
        this.configurationEntityService = configurationEntityService;
    }

    public static char[] Enc = {'N', 'T', 'G'};

    /**
     * @param S
     * @return
     */
    public static String Enc(String S) {

        if (S == null) {
            return "";
        }
        int j = 0;
        char[] C = S.toCharArray();
        for (int i = 0; i < C.length; i++) {
            C[i] ^= Enc[j];
            j++;
            if (j > 2) {
                j = 0;
            }
        }
        return String.valueOf(C);

    }


    public static String encrypt(String value, String encryptionKey, String method) throws Exception {
        if (Utils.isEmptyString(value) || Utils.isEmptyString(encryptionKey) || Utils.isEmptyString(method)) {
            return "";
        } else {
            if (method.toLowerCase().trim().equals("blowfish")) {
                return encryptBlowfish(value, encryptionKey);
            } else if (method.toLowerCase().trim().equals("aes")) {
                return encryptAES(value, encryptionKey);
            } else {
                throw new Exception();
            }
        }
    }

    private static String encryptBlowfish(String value, String encryptionKey) throws Exception {
        byte[] keyData = (encryptionKey).getBytes("UTF8");
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, "Blowfish");
        Cipher cipher = Cipher.getInstance("BLOWFISH/CBC/PKCS5Padding");
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0};
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivspec);
        byte[] hasil = cipher.doFinal(value.getBytes("UTF8"));
        return bytesToHex(hasil);
    }

    private static String encryptAES(String value, String encryptionKey) throws Exception {
        byte[] keyData = (encryptionKey).getBytes("UTF8");
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(encryptionKey.toCharArray(), keyData, 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secretKeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        IvParameterSpec ivspec = new IvParameterSpec(keyData);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivspec);
        byte[] hasil = cipher.doFinal(value.getBytes("UTF8"));
        return Base64.encodeBase64String(hasil);
    }

    private static String bytesToHex(byte[] data) {
        if (data == null) {
            return null;
        } else {
            int len = data.length;
            String str = "";
            for (int i = 0; i < len; i++) {
                if ((data[i] & 0xFF) < 16)
                    str = str + "0" + Integer.toHexString(data[i] & 0xFF);
                else
                    str = str + Integer.toHexString(data[i] & 0xFF);
            }
            return str.toUpperCase();
        }
    }

    public static String decrypt(String value, String encryptionKey, String method) throws Exception {
        if (Utils.isEmptyString(value) || Utils.isEmptyString(encryptionKey) || Utils.isEmptyString(method)) {
            return "";
        } else {
            if (method.toLowerCase().trim().equals("blowfish")) {
                return decryptBlowfish(value, encryptionKey);
            } else if (method.toLowerCase().trim().equals("aes")) {
                return decryptAES(value, encryptionKey);
            } else {
                throw new Exception();
            }
        }
    }

    public static String decryptBlowfish(String value, String encryptionKey) throws Exception {
        byte[] keyData = (encryptionKey).getBytes("UTF8");
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, "Blowfish");
        Cipher cipher = Cipher.getInstance("BLOWFISH/CBC/PKCS5Padding");
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0};
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivspec);
        byte[] hasil = cipher.doFinal(hexToBytes(value));
        return new String(hasil);
    }

    private static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }

    }

    public static String decryptAES(String value, String encryptionKey) throws Exception {
        byte[] keyData = (encryptionKey).getBytes("UTF8");
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(encryptionKey.toCharArray(), keyData, 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secretKeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        IvParameterSpec ivspec = new IvParameterSpec(keyData);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivspec);
        byte[] hasil = cipher.doFinal(Base64.decodeBase64(value));
        return new String(hasil);
    }

    // public static void main(String[] args) throws Exception {
    // System.err
    // .println(encrypt("1/4/2017", "030385_" + "NTG1026_E", "aes"));
    // System.err
    // .println(encrypt("600000", "030385_" + "NTG1026_E", "aes"));
    // System.err
    // .println(decrypt(encrypt("1/4/2017", "030385_" + "NTG1026_E", "aes"),
    // "030385_" + "NTG1026_E", "aes"));
    // //
    // System.err.println(decrypt(encrypt("602030937292706","357504056149388"),"357504056149388"));//8246208C1A0EED4D58D609CDC1E9435D
    // }

    /**
     * <p>
     * This function used to Encrypt password
     * </p>
     *
     * @param pass
     * @since 2019
     */
    public String encryptPassword(String pass) {
        try {
            String method;
            String encryptionKey;

            encryptionKey = configurationEntityService.getByKey(ConfigurationConstant.PASS_ENC_KEY);//.getValue();
            method = configurationEntityService.getByKey(ConfigurationConstant.PASS_ENC_METHOD);//.getValue();
            System.out.println("Encryption Key: " + encryptionKey + ", Method: " + method);
            return EncryptionUtils.encrypt(pass, "030385_" + encryptionKey, method);
        } catch (Exception e) {
            NTGMessageOperation.PrintErrorTrace(e);
        }
        return "";
    }

}
