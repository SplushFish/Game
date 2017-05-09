package net.dragonclaw.game.input;

import org.lwjgl.glfw.GLFWWindowFocusCallback;

public class WindowFocusInput extends GLFWWindowFocusCallback {

    private boolean focus;

    @Override
    public void invoke(long window, boolean focused) {
        focus = focused;
    }
    
    public boolean hasFocus(){
        return focus;
    }

}
