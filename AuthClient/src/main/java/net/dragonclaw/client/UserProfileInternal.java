package net.dragonclaw.client;

public class UserProfileInternal {

    public int id;
    public String accountName;
    public String emailAdress;
    public String uuid;
    public String lastLogin;
    public String creationDate;
    public boolean newAccount;
    private String[] info;

    public UserProfileInternal(String[] info) {
        this.info = info;
        id = Integer.parseInt(info[0]);
        accountName = info[1];
        uuid = info[2];
        emailAdress = info[3];
        creationDate = info[4];
        lastLogin = info[5];
        newAccount = Boolean.parseBoolean(info[6]);

    }

    public String[] asInfo() {
        return info;
    }
}
