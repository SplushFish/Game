package net.dragonclaw.init;

import net.dragonclaw.game.GameClient;
import net.dragonclaw.user.UserProfile;

public final class Init {

    public static void main(String[] args) throws InterruptedException {
        // LWJGLNativesLoader.load();
        // AuthClient client = new AuthClient();
        // client.open();
        // client.waitForLogin();
        // client.close();
        // new GameClient(new UserProfile(client.getLoginInfo().asInfo()));
        new GameClient(new UserProfile());
    }

}
