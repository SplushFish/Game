package net.dragonclaw.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class AuthUIImagePanel extends JPanel {

    private Image bgImage;
    private Image header;

    public AuthUIImagePanel(String image) {
        try {
            InputStream in = getClass().getClassLoader().getResourceAsStream(image);
            bgImage = ImageIO.read(in);
            in = getClass().getClassLoader().getResourceAsStream("images/header.png");
            header = ImageIO.read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImage, 0, 0, 800, 600, null);
        g.drawImage(header, 200, 50, 400, 100, null);
    }

}
