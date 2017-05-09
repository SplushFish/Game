package net.dragonclaw.game;

import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL11.*;

import net.dragonclaw.math.Vector;
import net.dragonclaw.user.UserProfile;

public class GameClient {

    /** Instance of the camera. */
    private final Camera camera = new Camera();

    boolean resizable = true;
    boolean visible = true;

    int width = 1024;
    int height = 768;

    String title = "Game";

    private float vDist;

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

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, visible ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);

        window = glfwCreateWindow(width, height, title, NULL, NULL);

        if (window == NULL) {
            throw new RuntimeException("Failed to create window");
        }

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
        } // the stack frame is popped automatically

        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glfwSwapInterval(1);
        glfwShowWindow(window);
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        while (!glfwWindowShouldClose(window)) {
            loop();
        }
    }

    private void loop() {
        glClearColor(1f, 1f, 1f, 0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glColor3f(0f, 0f, 0f);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glfwSwapBuffers(window);
        glfwPollEvents();
        glViewport(0, 0, width, height);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glPerspective(45, (float) width / (float) height, 0.1f * vDist, 10f * vDist);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        camera.update(0, 0, 10);
        glLookAt(camera.eye, camera.center, camera.up);
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
        glutSolidCylinder(0.05, 0.75, 5, 1);
        // move 3/4 up to draw the arrow at the right position
        glTranslatef(0, 0, 0.75f);
        // draw the arrow head of the arrow
        glutSolidCone(0.075, 0.25, 5, 1);
    }

}
