package com.codecool.test.login;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {

    public String hashPassword (String passwordToHash) {

        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        messageDigest.update(passwordToHash.getBytes());
        byte[] bytes = messageDigest.digest();

        StringBuilder stringBuilder = new StringBuilder();

        for(int i=0; i< bytes.length ;i++)
        {
            stringBuilder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        return stringBuilder.toString();
    }
}
