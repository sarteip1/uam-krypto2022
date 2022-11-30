package pl.uam.krypto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.security.Key;

@Data
@AllArgsConstructor
public class MasterKeyResponse implements Serializable {

    private byte[] nonce;
    private Key key;
    private String B;
    private byte[] encryptedAgreement;

}
