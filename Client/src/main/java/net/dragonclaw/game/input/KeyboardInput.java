package net.dragonclaw.game.input;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyboardInput extends GLFWKeyCallback {

    private boolean[] keyDown = new boolean[GLFW_KEY_LAST];

    public KeyboardInput() {}

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_UNKNOWN) 
            return;
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
            glfwSetWindowShouldClose(window, true);
        }
        if (action == GLFW_PRESS || action == GLFW_REPEAT) {
            keyDown[key] = true;
        } else {
            keyDown[key] = false;
        }
    }
    
    public boolean isKeyDown(int key){
        if(key < 0 || key >= GLFW_KEY_LAST){
            return false;
        }
        return keyDown[key];
    }

}
