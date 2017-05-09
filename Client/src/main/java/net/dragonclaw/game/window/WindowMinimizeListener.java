package net.dragonclaw.game.window;

import org.lwjgl.glfw.GLFWWindowIconifyCallback;

public class WindowMinimizeListener extends GLFWWindowIconifyCallback {

    private boolean minimized;

    @Override
    public void invoke(long window, boolean minimized) {
        this.minimized = minimized;
    }

    public boolean isMinimized() {
        return minimized;
    }

}
