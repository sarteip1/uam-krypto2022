package pl.uam.krypto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.security.Key;

@Data
@AllArgsConstructor
public class Agreement implements Serializable {

    private Key key;

}
