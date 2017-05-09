package net.dragonclaw.game.window;

import org.lwjgl.glfw.GLFWFramebufferSizeCallback;

import net.dragonclaw.math.Vector2i;

public class FrameBufferResizeListener extends GLFWFramebufferSizeCallback {

    private int fbWidth;
    private int fbHeight;

    @Override
    public void invoke(long window, int width, int height) {
        if (width > 0 && height > 0 && (fbWidth != width || fbHeight != height)) {
            this.fbHeight = height;
            this.fbWidth = width;
        }
    }

    public Vector2i getFrameBufferSize() {
        return new Vector2i(fbWidth, fbHeight);
    }

}
