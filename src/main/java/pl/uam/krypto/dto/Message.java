package pl.uam.krypto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Message implements Serializable {

    private byte[] encSessionKey;
    private byte[] encMessage;

}
