package net.dragonclaw.game;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL11.*;

import net.dragonclaw.game.input.InputManager;
import net.dragonclaw.game.window.WindowManager;
import net.dragonclaw.math.Vector;
import net.dragonclaw.math.Vector2d;
import net.dragonclaw.math.Vector2i;
import net.dragonclaw.user.UserProfile;

public class GameClient {

    /** Instance of the camera. */
    private final Camera camera = new Camera();

    String title = "Game";

    private float vDist;

    private final InputManager inputManager;
    private final WindowManager windowManager;
    private final long window;
    private final UserProfile user;

    public GameClient(UserProfile user) {
        if (user == null) {
            System.exit(0);
        }
        this.user = user;

        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        windowManager = new WindowManager(this, title);
        window = windowManager.create(800, 600);

        inputManager = new InputManager(this, window);
        inputManager.init();
        windowManager.init();

        GL.createCapabilities();
        glfwSwapInterval(1);
        windowManager.setVisable(true);
        glfwSetCursor(window, glfwCreateStandardCursor(GLFW_CROSSHAIR_CURSOR));
        while (!glfwWindowShouldClose(window)) {
            loop();
        }
    }

    private void loop() {
        glClearColor(0f, 0f, 0f, 0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glColor3f(0f, 0f, 0f);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glfwSwapBuffers(window);
        inputManager.update();
        if (inputManager.isButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            Vector2d pos = inputManager.getMousePos();
            System.out.println("you bloody clicker ... -.-. Trying to click on position: " + pos);
        }
        Vector2i size = windowManager.getWindowSize();
        glViewport(0, 0, size.x(), size.y());
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glPerspective(45, (float) size.x() / (float) size.y(), 0.1f * vDist, 10f * vDist);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        camera.update(0, 0, 10);
        glLookAt(camera.eye, camera.center, camera.up);
    }

    public WindowManager getWindowManager() {
        return windowManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    private static final float[] IDENTITY_MATRIX = new float[] {1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};

    private static final FloatBuffer matrix = BufferUtils.createFloatBuffer(16);

    public static void glPerspective(float fovy, float aspect, float zNear, float zFar) {
        float sine, cotangent, deltaZ;
        float radians = (float) (fovy / 2 * Math.PI / 180);

        deltaZ = zFar - zNear;
        sine = (float) Math.sin(radians);

        if ((deltaZ == 0) || (sine == 0) || (aspect == 0)) {
            return;
        }

        cotangent = (float) Math.cos(radians) / sine;

        int oldPos = matrix.position();
        matrix.put(IDENTITY_MATRIX);
        matrix.position(oldPos);

        matrix.put(0 * 4 + 0, cotangent / aspect);
        matrix.put(1 * 4 + 1, cotangent);
        matrix.put(2 * 4 + 2, -(zFar + zNear) / deltaZ);
        matrix.put(2 * 4 + 3, -1);
        matrix.put(3 * 4 + 2, -2 * zNear * zFar / deltaZ);
        matrix.put(3 * 4 + 3, 0);

        glMultMatrixf(matrix);
    }

    public static void glLookAt(Vector eye, Vector center, Vector up) {
        Vector forward = center.subtract(eye).normalized();
        Vector side = forward.cross(up).normalized();
        up = side.cross(forward);

        int oldPos = matrix.position();
        matrix.put(IDENTITY_MATRIX);
        matrix.position(oldPos);

        matrix.put(0 * 4 + 0, side.x());
        matrix.put(1 * 4 + 0, side.y());
        matrix.put(2 * 4 + 0, side.z());

        matrix.put(0 * 4 + 1, up.x());
        matrix.put(1 * 4 + 1, up.y());
        matrix.put(2 * 4 + 1, up.z());

        matrix.put(0 * 4 + 2, -forward.x());
        matrix.put(1 * 4 + 2, -forward.y());
        matrix.put(2 * 4 + 2, -forward.z());

        glMultMatrixf(matrix);
        glTranslatef(-eye.x(), -eye.y(), -eye.z());
    }

    /**
     * Draws the x-axis (red), y-axis (green), z-axis (blue),
     * and origin (yellow).
     */
    public void drawAxisFrame() {
        // draw the x-axis
        glPushMatrix();
        glColor3f(1, 0, 0); // x-axis (red)
        glRotatef(-90, 0, 1, 0);
        drawArrow();
        glPopMatrix();

        // draw the y-axis
        glPushMatrix();
        glColor3f(0, 1, 0); // y-axis (green)
        glRotatef(-90, 1, 0, 0);
        drawArrow();
        glPopMatrix();

        // draw the z-axis
        glPushMatrix();
        glColor3f(0, 0, 1); // z-axis (blue)
        drawArrow();
        glPopMatrix();
    }

    /**
     * Draws a single arrow
     */
    public void drawArrow() {
        // draw the body of the arrow
        // glutSolidCylinder(0.05, 0.75, 5, 1);
        // move 3/4 up to draw the arrow at the right position
        glTranslatef(0, 0, 0.75f);
        // draw the arrow head of the arrow
        // glutSolidCone(0.075, 0.25, 5, 1);
    }

}
