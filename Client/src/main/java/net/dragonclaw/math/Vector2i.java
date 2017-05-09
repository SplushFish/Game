package net.dragonclaw.math;

/**
 * Represents a 2D vector (immutable).
 */
public class Vector2i {

    // Origin and axis vectors.
    public final static Vector2i ZERO = new Vector2i(0, 0);
    public final static Vector2i RIGHT = new Vector2i(1, 0);
    public final static Vector2i FORWARD = new Vector2i(0, 1);
    public final static Vector2i BACK = new Vector2i(0, -1);
    public final static Vector2i LEFT = new Vector2i(-1, 0);

    // Components.
    protected int x, y;

    /**
     * Construct from components.
     */
    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get X component.
     */
    public int x() {
        return x;
    }

    /**
     * Get Y component.
     */
    public int y() {
        return y;
    }

    /**
     * Euclidian length squared.
     */
    public int lengthSqr() {
        return x * x + y * y;
    }

    /**
     * Euclidian length.
     */
    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Normalized vector, which is not defined for
     * a vector of zero length.
     */
    public Vector2d normalized() {
        double length = length();

        return new Vector2d(((double) x) / length, ((double) y) / length);
    }

    /**
     * Dot product with the given vector.
     */
    public int dot(Vector2i that) {
        return this.x * that.x + this.y * that.y;
    }

    /**
     * Add with the given vector.
     */
    public Vector2i add(Vector2i that) {
        return new Vector2i(this.x + that.x, this.y + that.y);
    }

    /**
     * Subtract by the given vector.
     */
    public Vector2i subtract(Vector2i that) {
        return new Vector2i(this.x - that.x, this.y - that.y);
    }

    /**
     * Multiply with the given scalar.
     */
    public Vector2i scale(int scalar) {
        return new Vector2i(scalar * this.x, scalar * this.y);
    }

    /**
     * String representation.
     */
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

}
