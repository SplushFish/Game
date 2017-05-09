package net.dragonclaw.game;

import net.dragonclaw.math.Vector;

/**
 * Implementation of a camera with a position and orientation.
 */
public class Camera {

    /** The position of the camera. */
    public Vector eye = new Vector(3f, 6f, 5f);

    /** The point to which the camera is looking. */
    public Vector center = Vector.O;

    /** The up vector. */
    public Vector up = Vector.Z;

    /** The smoothness of the camera */
    private final float smoothness = 100;

    /**
     * Updates the camera viewpoint and direction based on the
     * selected camera mode.
     */
    public void update(float theta, float phi, float vDist) {
        float sinTheta = (float) Math.sin(theta); // the sin of the angle theta
        float cosTheta = (float) Math.cos(theta); // the cos of the angle theta

        phi = (float) (Math.PI / 2 - phi); // the inverse of the angle phi
        float sinPhi = (float) Math.sin(phi); // the sin of the angle phi
        float cosPhi = (float) Math.cos(phi); // the cos of the angle phi

        float x = vDist * cosTheta * sinPhi; // x-coordinate of the eye position
        float y = vDist * sinTheta * sinPhi; // y-coordinate of the eye position
        float z = vDist * cosPhi; // z-coordinate of the eye position
        Vector V = new Vector(x, y, z); // the view vector

        Vector newCenter = Vector.Z; // the new center
        Vector centerDir = newCenter.subtract(center); // the vector between old and new center
        center = lerp(center, newCenter, centerDir.length() / smoothness); // interpolate over time to the new center

        Vector newEye = V.add(Vector.Z); // the new eye pos
        Vector eyeDir = newEye.subtract(eye); // the vector between old en new eye pos
        eye = lerp(eye, newEye, eyeDir.length() / smoothness); // interpolate over time to the new eye pos
    }


    /**
     * Computes eye, center, and up, based on the first person mode.
     * The camera should view from the perspective of the robot.
     */
    /*
     * private void setFirstPersonMode(GlobalState gs, Robot focus) {
     * Vector newCenter = focus.direction.normalized().scale(20).add(focus.position); // the new center
     * Vector centerDir = newCenter.subtract(center); // the vector between old and new center
     * center = lerp(center, newCenter, centerDir.length() / smoothness);// interpolate over time to the new center
     * 
     * Vector newEye = focus.eyePosition; // the new eye pos
     * Vector eyeDir = newEye.subtract(eye); // the vector between old en new eye pos
     * eye = lerp(eye, newEye, eyeDir.length() / smoothness);// interpolate over time to the new eye pos
     * 
     * double newVDist = 55; // the new Vdist such that we see everything properly in firstperson view
     * double VDistDiffrence = Math.abs(55 - gs.vDist); // the diffrence between the new and old vDist
     * gs.vDist = (float) lerp(gs.vDist, newVDist, VDistDiffrence / smoothness); // interpolates over time to the new
     * // vDist
     * }
     */
    /**
     * linearly interpolates between 2 vectors
     */
    private Vector lerp(Vector o, Vector n, float factor) {
        if (factor > 1) { // make sure the factor stays between 0-1
            factor = 1;
        }
        return o.scale(1 - factor).add(n.scale(factor)); // interpolate between the vectors
    }

    /**
     * linearly interpolates between 2 doubles
     */
    private double lerp(double o, double n, double factor) {
        if (factor > 1) { // make sure the factor stays between 0-1
            factor = 1;
        }
        return o * (1 - factor) + n * factor; // interpolate between the doubles
    }


}
