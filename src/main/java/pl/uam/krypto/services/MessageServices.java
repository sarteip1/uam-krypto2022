package pl.uam.krypto.services;

import pl.uam.krypto.dto.Message;
import pl.uam.krypto.crypto.CipherUtils;
import pl.uam.krypto.crypto.KeyManager;

import java.io.ObjectOutputStream;
import java.security.Key;
import java.util.Scanner;

public class MessageServices {

    private static final Scanner scanner = new Scanner(System.in);

    public static void messageService(Key masterKey, ObjectOutputStream outSock) throws Exception {
        System.out.print("Message: ");
        String msg = scanner.next();

        Key sessionKey = KeyManager.generateKey();

        byte[] encKey = CipherUtils.encrypt(sessionKey, masterKey);
        byte[] encMessage = CipherUtils.encrypt(msg, sessionKey);

        Message message = new Message(encKey, encMessage);

        outSock.writeObject(message);

        System.out.println("Message sent");
        System.out.println("Waiting for response");
    }
}
