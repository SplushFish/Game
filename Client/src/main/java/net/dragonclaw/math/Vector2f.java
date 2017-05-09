package net.dragonclaw.math;

/**
 * Represents a 3D vector (immutable).
 */
public class Vector2f {

    // Origin and axis vectors.
    public final static Vector2f ZERO = new Vector2f(0, 0);
    public final static Vector2f RIGHT = new Vector2f(1, 0);
    public final static Vector2f FORWARD = new Vector2f(0, 1);
    public final static Vector2f BACK = new Vector2f(0, -1);
    public final static Vector2f LEFT = new Vector2f(-1, 0);

    // Components.
    protected float x, y;

    /**
     * Construct from components.
     */
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get X component.
     */
    public float x() {
        return x;
    }

    /**
     * Get Y component.
     */
    public float y() {
        return y;
    }

    /**
     * Euclidian length squared.
     */
    public float lengthSqr() {
        return x * x + y * y;
    }

    /**
     * Euclidian length.
     */
    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Normalized vector, which is not defined for
     * a vector of zero length.
     */
    public Vector2f normalized() {
        float length = length();

        return new Vector2f(x / length, y / length);
    }

    /**
     * Dot product with the given vector.
     */
    public float dot(Vector2f that) {
        return this.x * that.x + this.y * that.y;
    }

    /**
     * Add with the given vector.
     */
    public Vector2f add(Vector2f that) {
        return new Vector2f(this.x + that.x, this.y + that.y);
    }

    /**
     * Subtract by the given vector.
     */
    public Vector2f subtract(Vector2f that) {
        return new Vector2f(this.x - that.x, this.y - that.y);
    }

    /**
     * Multiply with the given scalar.
     */
    public Vector2f scale(float scalar) {
        return new Vector2f(scalar * this.x, scalar * this.y);
    }

    /**
     * String representation.
     */
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

}
