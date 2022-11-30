package pl.uam.krypto.client;

import lombok.extern.log4j.Log4j2;
import pl.uam.krypto.dto.Agreement;
import pl.uam.krypto.dto.MasterKeyRequest;
import pl.uam.krypto.dto.MasterKeyResponse;
import pl.uam.krypto.dto.RawBytes;
import pl.uam.krypto.crypto.AliceKeys;
import pl.uam.krypto.crypto.BobKeys;
import pl.uam.krypto.services.Communitation;
import pl.uam.krypto.crypto.CipherUtils;
import pl.uam.krypto.crypto.KeyManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;

@Log4j2
public class KeyDistributionCenter {

    public final static int PORT = 6002;

    private static final AliceKeys aliceKeys = new AliceKeys();
    private static final BobKeys bobKeys = new BobKeys();

    private static ServerSocket socket = null;
    private static Socket aliceSocket = null;

    private static final Communitation communitation = new Communitation(null, PORT);

    public static void main(String[] args) {

        log.info("Start working");
        try {
            log.info("KeyDistributionCentre is running on port {}", communitation.getPort());
            socket = new ServerSocket(communitation.getPort());

            aliceSocket = socket.accept();

            communitation.setInSock(new ObjectInputStream(aliceSocket.getInputStream()));
            communitation.setOutSock(new ObjectOutputStream(aliceSocket.getOutputStream()));

            MasterKeyRequest masterKeyRequest = getMasterKey(communitation.getInSock());
            Key masterKey = KeyManager.generateKey();

            Agreement agreement = new Agreement(Alice.ID, masterKey);
            byte[] encryptedAgreement = CipherUtils.encrypt(agreement, bobKeys);

            MasterKeyResponse response =
                    new MasterKeyResponse(masterKeyRequest.getNonce(), masterKey, Bob.ID, encryptedAgreement);

            byte[] encResponse = CipherUtils.encrypt(response, aliceKeys);

            communitation.getOutSock().writeObject(new RawBytes(encResponse));
            closeSockets();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.error(e.getMessage());
        }
    }

    private static MasterKeyRequest getMasterKey(ObjectInputStream in) throws IOException, ClassNotFoundException {
        return (MasterKeyRequest) in.readObject();
    }

    private static void closeSockets() throws IOException {
        socket.close();
        communitation.getInSock().close();
        communitation.getOutSock().close();
        aliceSocket.close();
        log.info("Closed connections");
    }
}
