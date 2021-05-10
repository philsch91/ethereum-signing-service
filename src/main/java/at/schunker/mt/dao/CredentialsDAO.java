package at.schunker.mt.dao;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;

import java.io.IOException;

public interface CredentialsDAO {
    public abstract void createWallet(String password, Credentials credentials) throws CipherException, IOException;
    public abstract Credentials loadCredentials(String password, String address);
}
