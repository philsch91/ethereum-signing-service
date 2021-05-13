package at.schunker.mt.dao;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;

import java.io.IOException;

public interface CredentialsDAO {
    public abstract void createWallet(Credentials credentials, String password) throws CipherException, IOException;
    public abstract Credentials loadCredentials(String address, String password);
    public abstract boolean deleteCredentials(String address, String password);
}
