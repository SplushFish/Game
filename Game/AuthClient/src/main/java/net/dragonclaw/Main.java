package net.dragonclaw;

import net.dragonclaw.client.AuthClient;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        AuthClient c = new AuthClient().open();
        c.sendRegisterRequest("lucdon112", "Admin123", "lucdon@gmail.com");
        c.sendLoginRequest("lucdon112", "Admin123");
        c.sendLogoutRequest("lucdon112");
    }

}
