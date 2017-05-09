package net.dragonclaw.game.window;

import org.lwjgl.glfw.GLFWWindowSizeCallback;

public class WindowResizeListener extends GLFWWindowSizeCallback {

    private final WindowManager manager;

    public WindowResizeListener(WindowManager manager) {
        this.manager = manager;
    }

    @Override
    public void invoke(long window, int width, int height) {
        if (width > 0 && height > 0 && (manager.width != width || manager.height != height)) {
            manager.width = width;
            manager.height = height;
        }
    }
}
