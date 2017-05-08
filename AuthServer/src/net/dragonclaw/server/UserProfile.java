package net.dragonclaw.server;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

import net.dragonclaw.sqlite.SQLDataType;
import net.dragonclaw.sqlite.SQLField;

public class UserProfile extends SQLDataType {

    @SQLField(dataType = "INTEGER", notNull = true, primaryKey = true, autoIncrement = true, unique = false)
    public Integer id = -1;
    @SQLField(dataType = "TEXT", notNull = true, unique = true)
    public String accountName;
    @SQLField(dataType = "TEXT", notNull = true, unique = true)
    public String emailAdress;
    @SQLField(dataType = "TEXT", notNull = true, unique = true)
    public String uuid;
    @SQLField(dataType = "TEXT", notNull = true)
    public String password;
    @SQLField(dataType = "TEXT", notNull = true)
    public String lastLogin;
    @SQLField(dataType = "TEXT", notNull = true)
    public String creationDate;
    @SQLField(dataType = "BOOLEAN", notNull = true)
    public Boolean newAccount;

    public UserProfile() {}

    public UserProfile(String name, String password, String email) {
        this.accountName = name;
        this.password = password;
        this.emailAdress = email;
        this.uuid = UUID.randomUUID().toString();
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        this.lastLogin = now.toString();
        this.creationDate = now.toString();
        this.newAccount = true;
    }

    @Override
    public boolean isValid() {
        return id != -1;
    }
}
