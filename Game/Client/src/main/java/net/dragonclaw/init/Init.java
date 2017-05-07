package net.dragonclaw.init;

import java.util.Arrays;

import net.dragonclaw.client.AuthClient;
import net.dragonclaw.user.UserProfile;

public final class Init {

    public static void main(String[] args) throws InterruptedException {
        LWJGLNativesLoader.load();
        AuthClient client = new AuthClient();
        client.open();
        while (!client.isDone()) {
        }
        client.close();
        UserProfile profile = new UserProfile(client.getLoginInfo().asInfo());
    }

}
