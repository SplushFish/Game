package net.dragonclaw.game.input;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LAST;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseClickInput extends GLFWMouseButtonCallback {

    private boolean[] mouseDown = new boolean[GLFW_MOUSE_BUTTON_LAST];

    @Override
    public void invoke(long window, int button, int action, int mods) {
        if (button >= 0) {
            if (action == GLFW_PRESS) {
                mouseDown[button] = true;
            } else if (action == GLFW_RELEASE) {
                mouseDown[button] = false;
            }
        }
    }

    public boolean isMouseButtonDown(int button) {
        if (button < 0 || button >= GLFW_MOUSE_BUTTON_LAST) {
            return false;
        }
        return mouseDown[button];
    }
}
