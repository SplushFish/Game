package net.dragonclaw.ui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.dragonclaw.client.AuthClient;

public class AuthUIPanel extends JPanel {

    private final AuthClient client;
    private Image bgImage;
    private JPanel mainPanel = new JPanel();
    private JPanel registerPanel = new JPanel();
    private JPanel loginPanel = new JPanel();

    public AuthUIPanel(AuthClient client) {
        super(new BorderLayout());
        this.client = client;
        bgImage = Toolkit.getDefaultToolkit().createImage(new byte[] {0, 0, 0});
        setupUI();
        setupLoginPanel();
        setupRegisterPanel();
    }

    public void setupUI() {
        mainPanel.setLayout(new GridBagLayout());
        JButton login = new JButton("Login");
        login.addActionListener(e -> {
            swapToScreen(loginPanel, BorderLayout.NORTH);
        });
        JButton register = new JButton("Register");
        register.addActionListener(e -> {
            swapToScreen(registerPanel, BorderLayout.CENTER);
        });
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 150;
        c.ipady = 10;
        c.insets = new Insets(0, 30, 0, 30);
        mainPanel.add(login, c);
        c.gridx = 1;
        mainPanel.add(register, c);
        add(mainPanel, BorderLayout.CENTER);
    }

    public void setupLoginPanel() {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        loginPanel.setLayout(new GridBagLayout());
        JButton back = new JButton("Back");
        back.addActionListener(e -> {
            swapToScreen(mainPanel, BorderLayout.CENTER);
        });
        JTextField username = new JTextField();
        JPasswordField pass = new JPasswordField();
        loginPanel.add(new JLabel("username: "), c);
        loginPanel.add(username, c);
        loginPanel.add(new JLabel("password: "), c);
        loginPanel.add(pass, c);
        loginPanel.add(back, c);
    }

    public void setupRegisterPanel() {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        registerPanel.setLayout(new GridBagLayout());
        JButton back = new JButton("Back");
        back.addActionListener(e -> {
            swapToScreen(mainPanel, BorderLayout.CENTER);
        });
        JButton register = new JButton("Register");
        JTextField username = new JTextField();
        JTextField email = new JTextField();
        JPasswordField pass = new JPasswordField();
        JPasswordField repass = new JPasswordField();
        register.addActionListener(e -> {
            if (new String(pass.getPassword()).equals(new String(repass.getPassword()))) {
                client.sendRegisterRequest(username.getText(), new String(pass.getPassword()), email.getText());
            } else {
                JOptionPane.showMessageDialog(this, "passwords do not match!", "Register Request", JOptionPane.ERROR_MESSAGE);
            }
        });
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 150;
        c.ipady = 5;
        c.insets = new Insets(0, 0, 10, 0);
        registerPanel.add(new JLabel("username: "), c);
        c.gridx = 1;
        c.gridy = 0;
        c.ipadx = 300;
        registerPanel.add(username, c);
        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 150;
        registerPanel.add(new JLabel("email: "), c);
        c.gridx = 1;
        c.gridy = 1;
        c.ipadx = 300;
        registerPanel.add(email, c);
        c.gridx = 0;
        c.gridy = 2;
        c.ipadx = 150;
        registerPanel.add(new JLabel("password: "), c);
        c.gridx = 1;
        c.gridy = 2;
        c.ipadx = 300;
        registerPanel.add(pass, c);
        c.gridx = 0;
        c.gridy = 3;
        c.ipadx = 150;
        registerPanel.add(new JLabel("re-password: "), c);
        c.gridx = 1;
        c.gridy = 3;
        c.ipadx = 300;
        registerPanel.add(repass, c);
        c.gridx = 0;
        c.gridy = 4;
        c.ipadx = 150;
        c.gridwidth = 2;
        c.insets = new Insets(0, 0, 0, 0);
        registerPanel.add(register, c);
        c.gridy = 5;
        registerPanel.add(back, c);
    }

    public void swapToScreen(JPanel panel, String layout) {
        removeAll();
        add(panel, layout);
        validate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImage, 0, 0, null);
    }

}
