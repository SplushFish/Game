package net.dragonclaw.game;

import net.dragonclaw.user.UserProfile;

public class GameClient {

    public final UserProfile user;

    public GameClient(UserProfile user) {
        if (user == null) {
            System.exit(0);
        }
        this.user = user;
    }

}
