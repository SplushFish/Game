package net.dragonclaw.init;

import net.dragonclaw.client.AuthClient;
import net.dragonclaw.game.GameClient;
import net.dragonclaw.user.UserProfile;

public final class Init {

    public static void main(String[] args) throws InterruptedException {
        LWJGLNativesLoader.load();
        AuthClient client = new AuthClient();
        client.open();
        client.waitForLogin();
        client.close();
        GameClient game = new GameClient(new UserProfile(client.getLoginInfo().asInfo()));
        client = new AuthClient();
        client.open();
        client.sendLogoutRequest(game.user.getAccountName());
        client.waitForLogout();
        client.close();

    }

}
