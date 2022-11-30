package pl.uam.krypto.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class NumberUtils {

    public static byte[] getNonce() {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

            byte[] seed = new byte[128];
            random.nextBytes(seed);
            return seed;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

}
