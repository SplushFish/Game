package net.dragonclaw.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class AuthUIImagePanel extends JPanel {

    private Image bgImage;
    private Image header;

    public AuthUIImagePanel(String image) {
        try {
            bgImage = ImageIO.read(new File(image));
            header = ImageIO.read(new File("images/header.png"));
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
