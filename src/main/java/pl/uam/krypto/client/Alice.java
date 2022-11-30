package pl.uam.krypto.client;

import org.apache.commons.lang3.SerializationUtils;
import pl.uam.krypto.dto.MasterKeyRequest;
import pl.uam.krypto.dto.MasterKeyResponse;
import pl.uam.krypto.dto.Message;
import pl.uam.krypto.dto.RawBytes;
import pl.uam.krypto.crypto.AliceKeys;
import pl.uam.krypto.services.Communitation;
import pl.uam.krypto.services.MessageServices;
import pl.uam.krypto.crypto.CipherUtils;
import pl.uam.krypto.crypto.NumberUtils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.security.Key;
import java.util.Arrays;

public class Alice {

    private static final Communitation keyDistributionCenterCom = new Communitation("localhost", KeyDistributionCenter.PORT);
    private static final Communitation bobCom = new Communitation("localhost", Bob.PORT);

    public static void main(String[] args) {
        byte[] RaNonce = NumberUtils.getNonce();

        try {
            Socket keyDistributionCenterSocket = new Socket(keyDistributionCenterCom.getAddress(), keyDistributionCenterCom.getPort());
            System.out.println("Alice working on " + keyDistributionCenterCom.getPort());

            keyDistributionCenterCom.setOutSock(new ObjectOutputStream(keyDistributionCenterSocket.getOutputStream()));
            keyDistributionCenterCom.setInSock(new ObjectInputStream(keyDistributionCenterSocket.getInputStream()));

            AliceKeys aliceKeys = new AliceKeys();

            MasterKeyRequest mKReq = new MasterKeyRequest(RaNonce);
            keyDistributionCenterCom.getOutSock().writeObject(mKReq);
            keyDistributionCenterCom.getOutSock().flush();

            RawBytes raw = (RawBytes) keyDistributionCenterCom.getInSock().readObject();
            byte[] decrypted = CipherUtils.decrypt(raw.getRawData(), aliceKeys);
            MasterKeyResponse kdcResponse = SerializationUtils.deserialize(decrypted);

            if (!Bob.ID.equals(kdcResponse.getClientId())) {
                keyDistributionCenterCom.getOutSock().close();
                keyDistributionCenterCom.getInSock().close();
                keyDistributionCenterSocket.close();
                System.exit(0);
            }

            if (!Arrays.equals(RaNonce, kdcResponse.getNonce())) {
                keyDistributionCenterCom.getOutSock().close();
                keyDistributionCenterCom.getInSock().close();
                keyDistributionCenterSocket.close();
                System.exit(0);
            }

            Key masterKey = kdcResponse.getKey();
            RawBytes encryptedMKtoBob = new RawBytes(kdcResponse.getEncryptedAgreement());

            keyDistributionCenterCom.getOutSock().close();
            keyDistributionCenterCom.getInSock().close();
            keyDistributionCenterSocket.close();

            Socket bobSocket = new Socket(bobCom.getAddress(), bobCom.getPort());
            System.out.println("Alice wait for Bob on : " + bobCom.getAddress() + ":" + bobCom.getPort());

            bobCom.setOutSock(new ObjectOutputStream(bobSocket.getOutputStream()));
            bobCom.setInSock(new ObjectInputStream(bobSocket.getInputStream()));

            bobCom.getOutSock().writeObject(encryptedMKtoBob);
            bobCom.getOutSock().flush();

            RawBytes challenge = (RawBytes) bobCom.getInSock().readObject();
            BigInteger RbNonce = new BigInteger(CipherUtils.decrypt(challenge.getRawData(), masterKey));

            BigInteger response = RbNonce.subtract(BigInteger.valueOf(1));
            RawBytes data = new RawBytes(CipherUtils.encrypt(response.toByteArray(), masterKey));
            bobCom.getOutSock().writeObject(data);

            while (true) {
                MessageServices.messageService(masterKey, bobCom.getOutSock());
                Message answer = (Message) bobCom.getInSock().readObject();
                Key bobSessionKey =
                        SerializationUtils.deserialize(CipherUtils.decrypt(answer.getEncSessionKey(), masterKey));
                String ansString = new String(CipherUtils.decrypt(answer.getEncMessage(), bobSessionKey));
                System.out.println("Response: " + ansString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
