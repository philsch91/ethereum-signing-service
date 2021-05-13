package at.schunker.mt.dao;

import at.schunker.mt.data.WalletRepository;
import at.schunker.mt.util.CredentialUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.crypto.*;

import java.util.Optional;

@Component
public class DBWalletDAO implements CredentialsDAO {

    private static final Logger logger = LoggerFactory.getLogger(DBWalletDAO.class);
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    public void setWalletRepository(WalletRepository walletRepository) {
        logger.info("setWalletRepository");
        this.walletRepository = walletRepository;
    }

    @Override
    public void createWallet(Credentials credentials, String password) throws CipherException {
        if (this.walletRepository == null) {
            logger.info("walletRepository is null");
            return;
        }

        String publicKey = CredentialUtils.getPublicKeyInCredentials(credentials);
        //String walletId = "04a133e2-e296-483a-ae58-19da443ea966";
        //Optional<WalletFile> optWalletFile = this.walletRepository.findById(walletId);
        String address = credentials.getAddress();
        //address = Long.toHexString(Long.parseLong(address, 16));  // NumberFormatException
        if (address.startsWith("0x")) {
            address = address.substring(2);
        }

        logger.info("create wallet for address {}", address);

        Optional<WalletFile> optionalWalletFile = this.walletRepository.findByAddress(address);
        if (optionalWalletFile.isPresent()) {
            logger.info("{} already saved", address);
            logger.info("{} already saved", publicKey);
            return;
        }

        //WalletUtils.generateNewWalletFile()
        ECKeyPair keyPair = credentials.getEcKeyPair();
        WalletFile walletFile = Wallet.createStandard(password, keyPair);

        this.walletRepository.save(walletFile);
    }

    /**
     * consider Exception
     * @param password
     * @param address
     * @return
     */
    @Override
    public Credentials loadCredentials(String address, String password) {
        if (this.walletRepository == null) {
            logger.info("walletRepository is null");
            return null;
        }

        if (address.startsWith("0x")) {
            address = address.substring(2);
        }

        WalletFile walletFile = this.getWalletFile(address);
        if (walletFile == null) {
            logger.info("no wallet found for address {}", address);
            return null;
        }

        //WalletFile.Crypto crypto = walletFile.getCrypto();
        //WalletUtils.loadJsonCredentials(password, json);
        //WalletUtils.loadCredentials(password, json);

        ECKeyPair ecKeyPair = null;

        try {
            ecKeyPair = Wallet.decrypt(password, walletFile);
        } catch (CipherException ex) {
            return null;
        }

        Credentials credentials = Credentials.create(ecKeyPair);
        return credentials;
    }

    @Override
    public boolean deleteCredentials(String address, String password) {
        if (address.startsWith("0x")) {
            address = address.substring(2);
        }

        Credentials credentials = this.loadCredentials(address, password);
        if (credentials == null) {
            return false;
        }
        /*
        WalletFile walletFile = this.getWalletFile(address);
        if (walletFile == null) {
            return false;
        }

        this.walletRepository.delete(walletFile);
        */
        boolean deleteResult = this.walletRepository.deleteByAddress(address);
        return deleteResult;
    }

    private WalletFile getWalletFile(String address) {
        Optional<WalletFile> optionalWalletFile = this.walletRepository.findByAddress(address);

        if (!optionalWalletFile.isPresent()) {
            return null;
        }

        WalletFile walletFile = optionalWalletFile.get();
        return walletFile;
    }
}
