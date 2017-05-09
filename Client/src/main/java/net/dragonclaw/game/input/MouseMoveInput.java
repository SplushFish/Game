package net.dragonclaw.game.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

import net.dragonclaw.game.GameClient;

public class MouseMoveInput extends GLFWCursorPosCallback {

    private double mouseX;
    private double mouseY;
    private double mousePosX;
    private double mousePosY;
    private final GameClient game;

    public MouseMoveInput(GameClient game) {
        this.game = game;
    }

    @Override
    public void invoke(long window, double xpos, double ypos) {
        this.mousePosX = xpos;
        this.mousePosY = ypos;
        // float normX = (float) ((xpos - game.width / 2.0) / game.width * 2.0);
        // float normY = (float) ((ypos - game.height / 2.0) / game.height * 2.0);
        // mouseX = Math.max(-game.width / 2.0f, Math.min(width / 2.0f, normX));
        // mouseY = Math.max(-game.height / 2.0f, Math.min(height / 2.0f, normY));
    }
    
    public double getMouseX(){
        return mouseX;
    }
    
    public double getMouseY(){
        return mouseY;
    }
    
    public double getMousePosX(){
        return mousePosX;
    }
    
    public double getMousePosY(){
        return mousePosY;
    }

}
