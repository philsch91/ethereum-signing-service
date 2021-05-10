package at.schunker.mt.dto;

import java.util.Objects;

public class User implements Cloneable {

    private String address;
    private String password;

    protected User(){}

    public User(String address){
        this.address = address;
    }

    public User(String address, String password) {
        this.address = address;
        this.password = password;
    }

    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
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
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(this.address, user.address) &&
                Objects.equals(this.password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, password);
    }

    @Override
    public String toString() {
        return String.format("User[address='%s', password='%s']", address, password);
    }

    // Cloneable

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
