package org.sbd;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataBase {
    private String dbFile;
    private static final String SECRET_KEY = "mySecretKey12345";
    private Map<String, String> data;

    DataBase(String fileName) {
        dbFile = fileName;
        this.data = new HashMap<>();
        File myObj = new File("db.db");
        try {
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            Error er = new Error(e);
            er.initException();
        }
    }

    public void readData() {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            try (ObjectInputStream ois = new ObjectInputStream(new CipherInputStream(new FileInputStream(dbFile), cipher))) {
                data = (HashMap<String, String>) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException | NoSuchPaddingException |
                 InvalidKeyException e) {
            Error er = new Error(e);
            er.initException();
        }
    }

    public void writeData() {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            try (ObjectOutputStream oos = new ObjectOutputStream(new CipherOutputStream(new FileOutputStream(dbFile), cipher))) {
                oos.writeObject(data);
            }
        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            Error er = new Error(e);
            er.initException();
        }
    }

    public void put(String key, String value) {
        data.put(key, value);
    }
    public String get(String key) {
        return data.get(key);
    }
}

