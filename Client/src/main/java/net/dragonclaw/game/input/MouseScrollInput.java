package net.dragonclaw.game.input;

import org.lwjgl.glfw.GLFWScrollCallback;

public class MouseScrollInput extends GLFWScrollCallback {

    private double scrollX;
    private double scrollY;

    @Override
    public void invoke(long window, double xoffset, double yoffset) {
        scrollX = xoffset;
        scrollY = yoffset;
    }

    public double getScrollX() {
        return scrollX;
    }

    public double getScrollY() {
        return scrollY;
    }

}
