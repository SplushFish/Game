package net.dragonclaw.resources;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Resource {

    private final InputStream stream;
    private final String path;
    private boolean valid;

    public Resource(String path) {
        this.stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            valid = false;
        }
        this.path = path;
    }
    
    public boolean isValid() {
        return valid;
    }
    
    public File asFile() {
        if (!valid) {
            System.err.println("Trying to read an invalid file!");
            return null;
        }
        return new File(path);
    }

    public Image asImage() {
        if (!valid) {
            System.err.println("Trying to read an invalid file!");
            return null;
        }
        try {
            return ImageIO.read(stream);
        } catch (IOException e) {
            System.err.println("The file: " + path + " cannot be read as an image!");
            return null;
        }
    }

    public Font asFont(boolean isTrueType) {
        if (!valid) {
            System.err.println("Trying to read an invalid file!");
            return null;
        }
        try {
            return Font.createFont(isTrueType ? Font.TRUETYPE_FONT : Font.TYPE1_FONT, stream);
        } catch (FontFormatException | IOException e) {
            System.err.println("The file: " + path + " cannot be read as a font!");
            return null;
        }
    }
}

