package net.dragonclaw.game.window;

import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.Callback;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.GLFW.*;

import net.dragonclaw.game.GameClient;
import net.dragonclaw.math.Vector2i;

public class WindowManager {

    private String title;

    protected int width;
    protected int height;

    private boolean isCreated = false;
    private long window = -1;
    private GameClient game;

    private WindowCloseListener windowClosed;
    private WindowFocusListener windowFocused;
    private WindowMinimizeListener windowMinimized;
    private WindowMoveListener windowMoved;
    private WindowRefreshListener windowRefreshed;
    private WindowResizeListener windowResized;
    private FrameBufferResizeListener frameBufferResized;
    private final List<Callback> callbacks = new ArrayList<Callback>();

    public WindowManager(GameClient game, String title) {
        this.game = game;
        this.title = title;
        windowClosed = new WindowCloseListener();
        windowFocused = new WindowFocusListener();
        windowMinimized = new WindowMinimizeListener();
        windowMoved = new WindowMoveListener();
        windowRefreshed = new WindowRefreshListener();
        windowResized = new WindowResizeListener(this);
        frameBufferResized = new FrameBufferResizeListener();
    }

    public void setResizable(boolean resizable) {
        if (!isCreated) {
            glfwWindowHint(GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);
        }
    }

    public void setVisable(boolean visible) {
        if (!isCreated) {
            glfwWindowHint(GLFW_VISIBLE, visible ? GLFW_TRUE : GLFW_FALSE);
        } else {
            if (visible) {
                glfwShowWindow(window);
            } else {
                glfwHideWindow(window);
            }
        }
    }

    public long create(int width, int height) {
        this.width = width;
        this.height = height;
        glfwDefaultWindowHints();

        window = glfwCreateWindow(width, height, title, NULL, NULL);

        if (window == NULL) {
            throw new RuntimeException("Failed to create window");
        }

        
        isCreated = true;
        return window;
    }

    public void init() {
        callbacks.add(glfwSetWindowCloseCallback(window, windowClosed));
        callbacks.add(glfwSetWindowFocusCallback(window, windowFocused));
        callbacks.add(glfwSetWindowIconifyCallback(window, windowMinimized));
        callbacks.add(glfwSetWindowPosCallback(window, windowMoved));
        callbacks.add(glfwSetWindowRefreshCallback(window, windowRefreshed));
        callbacks.add(glfwSetWindowSizeCallback(window, windowResized));
        callbacks.add(glfwSetFramebufferSizeCallback(window, frameBufferResized));
        centerWindow();
        glfwMakeContextCurrent(window);
    }

    private void centerWindow() {
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
        }
    }

    public void dispose() {
        for (Callback cb : callbacks) {
            cb.free();
        }
    }

    public Vector2i getWindowSize() {
        return new Vector2i(width, height);
    }

    public boolean hasFocus() {
        return windowFocused.hasFocus();
    }

    public boolean isMinimized() {
        return windowMinimized.isMinimized();
    }

    public Vector2i getWindowPos() {
        return windowMoved.getWindowPos();
    }

    public Vector2i getFrameBufferSize() {
        return frameBufferResized.getFrameBufferSize();
    }
}
