package net.dragonclaw.game.window;

import org.lwjgl.glfw.GLFWWindowPosCallback;

import net.dragonclaw.math.Vector2i;

public class WindowMoveListener extends GLFWWindowPosCallback {

    private int windowX;
    private int windowY;

    @Override
    public void invoke(long window, int xpos, int ypos) {
        windowX = xpos;
        windowY = ypos;
    }

    public Vector2i getWindowPos() {
        return new Vector2i(windowX, windowY);
    }

}
