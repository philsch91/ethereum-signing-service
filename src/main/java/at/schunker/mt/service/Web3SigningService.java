package at.schunker.mt.service;

import at.schunker.mt.dao.CredentialsDAO;
import at.schunker.mt.dao.DBWalletDAO;
import at.schunker.mt.dao.PasswordDAO;
import at.schunker.mt.exception.AuthenticationForbiddenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.tx.ChainIdLong;
import org.web3j.tx.RawTransactionManager;
import org.web3j.utils.Numeric;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

@Service
public class Web3SigningService {
    private static final Logger log = LoggerFactory.getLogger(Web3SigningService.class);
    @Autowired
    public PasswordDAO passwordDAO;
    public CredentialsDAO credentialsDAO;

    public Web3SigningService(DBWalletDAO walletDAO) {
        this.credentialsDAO = walletDAO;
    }

    /**
     * replaces RawTransactionManager(Web3j web3j, Credentials credentials).sendTransaction()
     * send signed transaction in client: new Web3j().ethSendRawTransaction(String signedHexTransaction).send()
     * @param address an address of the account to be used for signing the rawTransaction
     * @param rawTransaction a RawTransaction instance to be signed
     *                       RawTransaction.createTransaction(
     *                          nonce
     *                          DefaultGasProvider.GAS_PRICE,
     *                          DefaultGasProvicer.GAS_LIMIT,
     *                          toAddress,
     *                          value,
     *                          txData)
     *
     * @return
     * @throws Exception
     */
    public String signTransaction(String address, RawTransaction rawTransaction) throws AuthenticationForbiddenException {
        String password = this.passwordDAO.getPassword(address);
        if (password == null) {
            return null;
        }

        Credentials credentials = this.credentialsDAO.loadCredentials(address, password);
        if (credentials == null) {
            throw new AuthenticationForbiddenException();
        }

        //byte[] signedTransaction = TransactionEncoder.signMessage(rawTransaction, ChainIdLong.NONE, credentials)
        byte[] signedTransaction = TransactionEncoder.signMessage(rawTransaction, credentials);
        //String transaction = new String(signedTransaction, StandardCharsets.UTF_8);
        String transaction = Numeric.toHexString(signedTransaction);

        return transaction;
    }
}
