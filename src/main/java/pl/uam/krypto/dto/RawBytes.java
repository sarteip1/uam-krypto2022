package pl.uam.krypto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class RawBytes implements Serializable {

    private byte[] rawData;

}
