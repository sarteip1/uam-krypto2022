package pl.uam.krypto.crypto;

import java.security.Key;

public class BobKeys implements Key {

    private final byte[] key = {-25, 73, 41, 113, -88, -89, -84, -49, -59, -124, 82, 25, -112, -72, -127, 56};

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
