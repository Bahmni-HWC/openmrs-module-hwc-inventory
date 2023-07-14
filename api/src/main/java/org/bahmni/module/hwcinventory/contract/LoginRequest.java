package org.bahmni.module.hwcinventory.contract;

public class LoginRequest {
    private String userName;
    private String password;
    private String salt;
    private String source;

    public LoginRequest(String userName, String password, String salt, String source) {
        this.userName = userName;
        this.password = password;
        this.salt = salt;
        this.source = source;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
