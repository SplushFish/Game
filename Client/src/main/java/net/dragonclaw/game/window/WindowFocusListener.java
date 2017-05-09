package net.dragonclaw.game.window;

import org.lwjgl.glfw.GLFWWindowFocusCallback;

public class WindowFocusListener extends GLFWWindowFocusCallback {

    private boolean focus;

    @Override
    public void invoke(long window, boolean focused) {
        focus = focused;
    }
    
    public boolean hasFocus(){
        return focus;
    }

}
