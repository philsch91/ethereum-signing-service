package at.schunker.mt.dto;

import java.util.Objects;

public class UserPrivateKey {

    private String privateKey;
    private String password;

    protected UserPrivateKey() {
        //
    }

    public UserPrivateKey(String privateKey, String password) {
        this.privateKey = privateKey;
        this.password = password;
    }

    public String getPrivateKey() {
        return this.privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPrivateKey)) return false;
        UserPrivateKey that = (UserPrivateKey) o;
        return Objects.equals(privateKey, that.privateKey) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(privateKey, password);
    }

    @Override
    public String toString() {
        return "UserPrivateKey{" +
                "privateKey='" + this.privateKey + '\'' +
                ", password='" + this.password + '\'' +
                '}';
    }
}
