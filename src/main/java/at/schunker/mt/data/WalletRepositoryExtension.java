package at.schunker.mt.data;

import org.web3j.crypto.WalletFile;

import java.util.Optional;

public interface WalletRepositoryExtension {
    public abstract Optional<WalletFile> findByAddress(String address);
    public abstract boolean deleteByAddress(String address);
}
