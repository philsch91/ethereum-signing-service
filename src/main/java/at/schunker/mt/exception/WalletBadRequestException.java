package at.schunker.mt.exception;

public class WalletBadRequestException extends BadRequestException {
    public WalletBadRequestException(){
        super("Incorrect wallet data");
    }
}
