package at.schunker.mt.util;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;

import java.math.BigInteger;

public class CredentialUtils {

    public static ECKeyPair createKeyPair(String privateKey) {
        BigInteger privateKeyBigInt = new BigInteger(privateKey, 16);
        ECKeyPair keyPair = ECKeyPair.create(privateKeyBigInt);
        return keyPair;
    }

    /**
     * Deprecated: Use Credentials.create(String privateKey) instead
     * @param privateKey
     * @return Credentials
     */
    @Deprecated
    public static Credentials createCredentials(String privateKey) {
        BigInteger privateKeyBigInt = new BigInteger(privateKey, 16);
        ECKeyPair keyPair = ECKeyPair.create(privateKeyBigInt);
        return Credentials.create(keyPair);
    }

    public static String getPublicKeyInHex(String privateKeyInHex) {
        BigInteger privateKey = new BigInteger(privateKeyInHex, 16);
        ECKeyPair keyPair = ECKeyPair.create(privateKey);
        BigInteger publicKey = keyPair.getPublicKey();
        String publicKeyInHex = publicKey.toString(16);
        return publicKeyInHex;
    }

    public static String getPublicKeyInCredentials(Credentials credentials) {
        ECKeyPair keyPair = credentials.getEcKeyPair();
        String publicKey = keyPair.getPublicKey().toString(16);
        return publicKey;
    }

    public static String getPrivateKeyInCredentials(Credentials credentials) {
        ECKeyPair keyPair = credentials.getEcKeyPair();
        String privateKey = keyPair.getPrivateKey().toString(16);
        return privateKey;
    }

    public static String getAddressInCredentials(Credentials credentials) {
        return credentials.getAddress();
    }
}
