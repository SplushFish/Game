package net.dragonclaw;

import net.dragonclaw.server.AuthServer;

public class Main {

    public static void main(String[] args) {
        try {
            new AuthServer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
