package net.dragonclaw.game.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

import net.dragonclaw.game.GameClient;
import net.dragonclaw.game.window.WindowManager;
import net.dragonclaw.math.Vector2i;

public class MouseMoveInput extends GLFWCursorPosCallback {

    private double mouseX;
    private double mouseY;
    private double mousePosX;
    private double mousePosY;
    private final GameClient game;
    private final WindowManager manager;

    public MouseMoveInput(GameClient game) {
        this.game = game;
        this.manager = game.getWindowManager();
    }

    @Override
    public void invoke(long window, double xpos, double ypos) {
        this.mousePosX = xpos;
        this.mousePosY = ypos;
        Vector2i size = manager.getWindowSize();
        float normX = (float) ((xpos - size.x() / 2.0) / size.x() * 2.0);
        float normY = (float) ((ypos - size.y() / 2.0) / size.y() * 2.0);
        mouseX = Math.max(-size.x() / 2.0f, Math.min(size.x() / 2.0f, normX));
        mouseY = Math.max(-size.y() / 2.0f, Math.min(size.y() / 2.0f, normY));
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public double getMousePosX() {
        return mousePosX;
    }

    public double getMousePosY() {
        return mousePosY;
    }

}
