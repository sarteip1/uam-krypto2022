package pl.uam.krypto.services;

import lombok.Data;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@Data
public class Communitation {

    private String address;
    private int port;
    private ObjectOutputStream outSock;
    private ObjectInputStream inSock;

    public Communitation(String address, int port) {
        this.address = address;
        this.port = port;
        this.outSock = null;
        this.inSock = null;
    }
}
