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
    public void createWallet(String password, Credentials credentials) throws CipherException {
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
        logger.info("createWallet {}", address);
        Optional<WalletFile> optWalletFile = this.walletRepository.findByAddress(address);

        if (optWalletFile.isPresent()) {
            logger.info("{} already saved", address);
            logger.info("{} already saved", publicKey);
            return;
        }

        //WalletUtils.generateNewWalletFile()
        ECKeyPair keyPair = credentials.getEcKeyPair();
        WalletFile walletFile = Wallet.createStandard(password, keyPair);

        this.walletRepository.save(walletFile);
        //WalletFile.Crypto crypto = walletFile.getCrypto();
        //WalletUtils.loadJsonCredentials(password, json);
        //WalletUtils.loadCredentials(password, json);
    }

    /**
     * consider Exception
     * @param password
     * @param publicKey
     * @return
     */
    @Override
    public Credentials loadCredentials(String password, String publicKey) {
        return null;
    }
}