package pl.uam.krypto.crypto;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

public class KeyManager{

    private static final String ALGORITHM = "AES";

    public static Key generateKey() throws Exception{
        SecretKey key =  KeyGenerator.getInstance(ALGORITHM).generateKey();
        return new SecretKeySpec(key.getEncoded(), 0, 16, ALGORITHM);
    }

}
