package pl.uam.krypto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class MasterKeyRequest implements Serializable {

    private byte[] nonce;
}
