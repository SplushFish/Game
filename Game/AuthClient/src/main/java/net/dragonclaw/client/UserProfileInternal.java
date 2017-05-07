package net.dragonclaw.client;

import java.util.Arrays;

public class UserProfileInternal {

    public int id;
    public String accountName;
    public String emailAdress;
    public String uuid;
    public String lastLogin;
    public String lastLogout;
    public String creationDate;
    public boolean newAccount;
    private String[] info;

    public UserProfileInternal(String[] info) {
        System.out.println(Arrays.toString(info));
        id = Integer.parseInt(info[0]);
        accountName = info[1];
        uuid = info[2];
        emailAdress = info[3];
        creationDate = info[4];
        lastLogin = info[5];
        lastLogout = info[6];
        newAccount = Boolean.parseBoolean(info[7]);

    }

    public String[] asInfo() {
        return info;
    }
}
