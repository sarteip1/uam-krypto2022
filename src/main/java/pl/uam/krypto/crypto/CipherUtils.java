package pl.uam.krypto.crypto;


import org.apache.commons.lang3.SerializationUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;


public class CipherUtils {

    private static final String algorithm = "AES";
    private static Cipher cipher = null;

    static {
        try
        {
            cipher = Cipher.getInstance(algorithm);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException e)
        {
            e.printStackTrace();
        }
    }

    public static byte[] encrypt(String input, Key key)
            throws InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException
    {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] inputBytes = input.getBytes();
        return cipher.doFinal(inputBytes);
    }

    public static byte[] encrypt(byte[] input, Key key)
            throws InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException
    {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(input);
    }

    public static byte[] encrypt(Serializable input, Key key)
            throws InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException
    {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] inputBytes = SerializationUtils.serialize(input);
        return cipher.doFinal(inputBytes);
    }

    public static byte[] decrypt(byte[] encryptionBytes, Key key)
            throws InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException
    {
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(encryptionBytes);
    }

}
