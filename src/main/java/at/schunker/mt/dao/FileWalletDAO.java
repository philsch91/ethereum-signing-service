package at.schunker.mt.dao;

import at.schunker.mt.util.CredentialUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class FileWalletDAO implements CredentialsDAO {

    private static final Logger logger = LoggerFactory.getLogger(FileWalletDAO.class);
    private String directoryPath;

    public FileWalletDAO(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    private String generateWallet(String password) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, CipherException, IOException {
        String walletFileName = WalletUtils.generateNewWalletFile(password, new File(this.directoryPath));
        return walletFileName;
    }

    private String generateWallet(String password, String privateKey) throws CipherException, IOException {
        ECKeyPair keyPair = CredentialUtils.createKeyPair(privateKey);
        String walletFileName = WalletUtils.generateWalletFile(password, keyPair, new File(this.directoryPath), false);
        return walletFileName;
    }

    /** CredentialsDAO */

    @Override
    public void createWallet(String password, Credentials credentials) throws CipherException, IOException {
        String publicKey = CredentialUtils.getPublicKeyInCredentials(credentials);

        String fileName = this.directoryPath + "/" + publicKey + ".json";
        File newWalletFile = new File(fileName);

        logger.info("newWalletFile.getName(): " + newWalletFile.getName());
        logger.info("newWalletFile.getPath(): " + newWalletFile.getAbsolutePath());

        if (newWalletFile.exists()) {
            return;
        }

        ECKeyPair keyPair = credentials.getEcKeyPair();
        String walletFileName = WalletUtils.generateWalletFile(password, keyPair, new File(this.directoryPath), false);
        walletFileName = this.directoryPath + "/" + walletFileName;
        logger.info("walletFileName: " + walletFileName);

        Path walletPath = Paths.get(walletFileName);
        logger.info("walletPath: " + walletPath.toString());
        //Path walletPathFileName = walletPath.getFileName();

        //File walletFile = walletPath.toFile();
        //boolean res = walletFile.renameTo(newWalletFile);
        //logger.info("res: " + res);

        //String pubKeyFileName = publicKey + ".json";
        String pubKeyFileName = newWalletFile.getName();
        logger.info("pubKeyFileName: " + pubKeyFileName);
        //Files.move(walletPath, walletPath.resolveSibling(pubKeyFileName));

        Path newWalletPath = Files.move(walletPath, newWalletFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        //Path newWalletPath = Files.copy(walletPath, newWalletFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        logger.info("newWalletPath: " + newWalletPath.toString());
    }

    @Override
    public Credentials loadCredentials(String password, String address) {
        return null;
    }
}
