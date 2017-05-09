package net.dragonclaw.math;

/**
 * Represents a 2D vector (immutable).
 */
public class Vector2d {

    // Origin and axis vectors.
    public final static Vector2d ZERO = new Vector2d(0, 0);
    public final static Vector2d RIGHT = new Vector2d(1, 0);
    public final static Vector2d FORWARD = new Vector2d(0, 1);
    public final static Vector2d BACK = new Vector2d(0, -1);
    public final static Vector2d LEFT = new Vector2d(-1, 0);

    // Components.
    protected double x, y;

    /**
     * Construct from components.
     */
    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get X component.
     */
    public double x() {
        return x;
    }

    /**
     * Get Y component.
     */
    public double y() {
        return y;
    }

    /**
     * Euclidian length squared.
     */
    public double lengthSqr() {
        return x * x + y * y;
    }

    /**
     * Euclidian length.
     */
    public double length() {
        return (double) Math.sqrt(x * x + y * y);
    }

    /**
     * Normalized vector, which is not defined for
     * a vector of zero length.
     */
    public Vector2d normalized() {
        double length = length();

        return new Vector2d(x / length, y / length);
    }

    /**
     * Dot product with the given vector.
     */
    public double dot(Vector2d that) {
        return this.x * that.x + this.y * that.y;
    }

    /**
     * Add with the given vector.
     */
    public Vector2d add(Vector2d that) {
        return new Vector2d(this.x + that.x, this.y + that.y);
    }

    /**
     * Subtract by the given vector.
     */
    public Vector2d subtract(Vector2d that) {
        return new Vector2d(this.x - that.x, this.y - that.y);
    }

    /**
     * Multiply with the given scalar.
     */
    public Vector2d scale(double scalar) {
        return new Vector2d(scalar * this.x, scalar * this.y);
    }

    /**
     * String representation.
     */
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

}
