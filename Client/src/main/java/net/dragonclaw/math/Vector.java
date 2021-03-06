package net.dragonclaw.math;

/**
 * Represents a 3D vector (immutable).
 */
public class Vector {

    // Origin and axis vectors.
    public final static Vector O = new Vector(0, 0, 0);
    public final static Vector X = new Vector(1, 0, 0);
    public final static Vector Y = new Vector(0, 1, 0);
    public final static Vector Z = new Vector(0, 0, 1);

    // Components.
    protected float x, y, z;

    /**
     * Construct from components.
     */
    public Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
     * Get Z component.
     */
    public float z() {
        return z;
    }

    /**
     * Euclidian length squared.
     */
    public float lengthSqr() {
        return x * x + y * y + z * z;
    }

    /**
     * Euclidian length.
     */
    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Normalized vector, which is not defined for
     * a vector of zero length.
     */
    public Vector normalized() {
        float length = length();

        return new Vector(x / length, y / length, z / length);
    }

    /**
     * Dot product with the given vector.
     */
    public float dot(Vector that) {
        return this.x * that.x + this.y * that.y + this.z * that.z;
    }

    /**
     * Cross product with the given vector.
     */
    public Vector cross(Vector that) {
        return new Vector(this.y * that.z - this.z * that.y, this.z * that.x - this.x * that.z,
                this.x * that.y - this.y * that.x);
    }

    /**
     * Add with the given vector.
     */
    public Vector add(Vector that) {
        return new Vector(this.x + that.x, this.y + that.y, this.z + that.z);
    }

    /**
     * Subtract by the given vector.
     */
    public Vector subtract(Vector that) {
        return new Vector(this.x - that.x, this.y - that.y, this.z - that.z);
    }

    /**
     * Multiply with the given scalar.
     */
    public Vector scale(float scalar) {
        return new Vector(scalar * this.x, scalar * this.y, scalar * this.z);
    }

    /**
     * String representation.
     */
    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }

}
