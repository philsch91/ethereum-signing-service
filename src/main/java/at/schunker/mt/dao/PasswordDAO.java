package at.schunker.mt.dao;

public interface PasswordDAO {
    public abstract boolean savePassword(String address, String password);
    public abstract String getPassword(String address);
}
