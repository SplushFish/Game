package net.dragonclaw.game.input;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.system.Callback;

import static org.lwjgl.glfw.GLFW.*;

import net.dragonclaw.game.GameClient;
import net.dragonclaw.math.Vector2d;

public class InputManager {

    private final long window;
    private final GameClient game;
    private final KeyboardInput keyboard;
    private final MouseClickInput mouseClick;
    private final MouseMoveInput mouseMove;
    private final MouseScrollInput mouseScroll;
    private final List<Callback> callbacks = new ArrayList<Callback>();


    public InputManager(GameClient game, long window) {
        this.game = game;
        this.window = window;
        keyboard = new KeyboardInput();
        mouseClick = new MouseClickInput();
        mouseMove = new MouseMoveInput(game);
        mouseScroll = new MouseScrollInput();
    }

    public void init() {
        callbacks.add(glfwSetKeyCallback(window, keyboard));
        callbacks.add(glfwSetCursorPosCallback(window, mouseMove));
        callbacks.add(glfwSetMouseButtonCallback(window, mouseClick));
        callbacks.add(glfwSetScrollCallback(window, mouseScroll));
    }

    public void update() {
        glfwPollEvents();
    }

    public void dispose() {
        for (Callback cb : callbacks) {
            cb.free();
        }
    }

    public boolean isKeyDown(int key) {
        return keyboard.isKeyDown(key);
    }

    public boolean isButtonDown(int button) {
        return mouseClick.isMouseButtonDown(button);
    }

    public Vector2d getMouseMove() {
        return new Vector2d(mouseMove.getMouseX(), mouseMove.getMouseY());
    }

    public Vector2d getMousePos() {
        return new Vector2d(mouseMove.getMousePosX(), mouseMove.getMousePosY());
    }

    public Vector2d getMouseScroll() {
        return new Vector2d(mouseScroll.getScrollX(), mouseScroll.getScrollY());
    }

}
