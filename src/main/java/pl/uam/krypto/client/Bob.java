package pl.uam.krypto.client;

import org.apache.commons.lang3.SerializationUtils;
import pl.uam.krypto.dto.Agreement;
import pl.uam.krypto.dto.Message;
import pl.uam.krypto.dto.RawBytes;
import pl.uam.krypto.crypto.BobKeys;
import pl.uam.krypto.services.Communitation;
import pl.uam.krypto.services.MessageServices;
import pl.uam.krypto.crypto.CipherUtils;
import pl.uam.krypto.crypto.NumberUtils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;

public class Bob {

    public static final int PORT = 6001;
    public final static String ID = "Bob";

    private static final Communitation bobCommunitation = new Communitation(null, PORT);

    public static void main(String[] args)
    {
        try
        {
            BobKeys bobKeys = new BobKeys();

            ServerSocket socket = new ServerSocket(bobCommunitation.getPort(), 50, InetAddress.getByName("127.0.1.1"));
            System.out.println("Bob working on "+ bobCommunitation.getPort());

            Socket alice = socket.accept();
            System.out.println(alice.getRemoteSocketAddress());

            bobCommunitation.setInSock(new ObjectInputStream(alice.getInputStream()));
            bobCommunitation.setOutSock(new ObjectOutputStream(alice.getOutputStream()));

            RawBytes encryptedAgreement = (RawBytes) bobCommunitation.getInSock().readObject();
            Agreement mkAgreement = SerializationUtils
                    .deserialize(CipherUtils.decrypt(encryptedAgreement.getRawData(), bobKeys));

            Key masterKey = mkAgreement.getKey();

            byte[] Rb = NumberUtils.getNonce();
            bobCommunitation.getOutSock().writeObject(new RawBytes(CipherUtils.encrypt(Rb, masterKey)));
            bobCommunitation.getOutSock().flush();

            BigInteger bigNonce = new BigInteger(Rb);
            BigInteger challengeCheck = bigNonce.subtract(new BigInteger(String.valueOf(1)));

            RawBytes encryptAnswer = (RawBytes) bobCommunitation.getInSock().readObject();
            BigInteger answer = new BigInteger(CipherUtils.decrypt(encryptAnswer.getRawData(), masterKey));

            if(answer.equals(challengeCheck))
            {
                while (true)
                {
                    Message ansMsg = (Message) bobCommunitation.getInSock().readObject();
                    Key bobSessionKey =
                            SerializationUtils.deserialize(CipherUtils.decrypt(ansMsg.getEncSessionKey(), masterKey));
                    String ansString = new String(CipherUtils.decrypt(ansMsg.getEncMessage(), bobSessionKey));
                    System.out.println("Response: "+ansString);
                    MessageServices.messageService(masterKey, bobCommunitation.getOutSock());
                }
            }
            else
            {
                System.out.println("Something goes wrong");
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



    }


}
