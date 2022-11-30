package pl.uam.krypto.crypto;

import java.security.Key;

public class AliceKeys implements Key {

    private final byte[] key = {-25, 38, 122, 6, -101, 74, -2, 42, -41, 54, 43, 102, -113, 3, -34, -43};

    @Override
    public String getAlgorithm() {
        return "AES";
    }

    @Override
    public String getFormat() {
        return "RAW";
    }

    @Override
    public byte[] getEncoded() {
        return key;
    }
}
