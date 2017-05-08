package net.dragonclaw.user;

import java.time.ZonedDateTime;

public class UserProfile {

    private int id;
    private String accountName;
    private String emailAdress;
    private String uuid;
    private ZonedDateTime lastLogin;
    private ZonedDateTime lastLogout;
    private ZonedDateTime creationDate;
    private boolean newAccount;

    public UserProfile(String[] info) {
        id = Integer.parseInt(info[0]);
        accountName = info[1];
        uuid = info[2];
        emailAdress = info[3];
        creationDate = ZonedDateTime.parse(info[4]);
        lastLogin = ZonedDateTime.parse(info[5]);
        lastLogout = ZonedDateTime.parse(info[6]);
        newAccount = Boolean.parseBoolean(info[7]);
    }

    public int getId() {
        return id;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getEmailAdress() {
        return emailAdress;
    }

    public String getUUID() {
        return uuid;
    }

    public ZonedDateTime getLastLogin() {
        return lastLogin;
    }

    public ZonedDateTime getLastLogout() {
        return lastLogout;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public boolean isNewAccount() {
        return newAccount;
    }

}
