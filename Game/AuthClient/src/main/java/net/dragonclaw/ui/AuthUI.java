package net.dragonclaw.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import net.dragonclaw.client.AuthClient;

public class AuthUI {

    private final AuthClient client;

    public AuthUI(AuthClient client) {
        this.client = client;
        JFrame frame = new JFrame("Authentication Service");
        frame.setLayout(new BorderLayout());
        frame.add(new AuthUIPanel(client), BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
