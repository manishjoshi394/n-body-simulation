/* 
 * Copyright (C) 2018 Manish Joshi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import dependencies.*;

import java.awt.Color;

/**
 *
 * @author manishjoshi394
 */
public class Particle {

    private static final double INFINITY = Double.POSITIVE_INFINITY;
    private static Color defaultColor = Color.BLACK;
    private static boolean useRadiusUpscaling = false;

    private double rx, ry;                  // position
    private double vx, vy;                  // velocity
    private final double radius;            // radius of particle 
    private final double mass;              // mass of the particle

    private int count;                      // number of collisions the particle has been through till now
    private Color color;                    // the color of this particle

    /**
     * Initializes a particle with random position and velocity.
     */
    public Particle() {
        this.count = 0;

        this.rx = StdRandom.uniform(0.5, 0.95);
        this.ry = StdRandom.uniform(0.5, 0.95);
        this.vx = StdRandom.uniform(-0.5, 0.5);
        this.vy = StdRandom.uniform(-0.5, 0.5);
        this.radius = 0.01;
        this.mass = 0.5;
        this.color = defaultColor;
    }

    /**
     * Initializes a Particle with given specified parameters.
     *
     * @param rx <em>x</em> coordinate of the position
     * @param ry <em>y</em> coordinate of the position
     * @param vx <em>x</em> component of the velocity
     * @param vy <em>y</em> component of the velocity
     * @param radius the radius
     * @param mass the mass
     * @param color the color
     */
    public Particle(double rx, double ry, double vx, double vy, double radius, double mass, Color color) {
        this.count = 0;

        this.rx = rx;
        this.ry = ry;
        this.vx = vx;
        this.vy = vy;

        // 0.03 is used as minimum observable radius onscreen
        if (!radiusUpscalingEnabled() && radius < 0.003) {
            this.radius = radius;
            System.out.println("WARNING : Particle radius too small to be observed, \n"
                    + "HINT : use radius upscaling function in Particle class to upscale the size to minimum observable.");
        } else if (radiusUpscalingEnabled() && radius < 0.003) {
            this.radius = 0.003;
        } else {
            this.radius = radius;
        }
        this.mass = mass;
        this.color = color;
    }

    /**
     * Draws this particle to the Standard Draw.
     */
    public void draw() {
        StdDraw.setPenColor(color);
        StdDraw.filledCircle(rx, ry, radius);
    }

    /**
     * Move this particle on a straight with its velocity for specified time.
     *
     * @param dt the time specified
     */
    public void move(double dt) {
        rx += vx * dt;
        ry += vy * dt;
    }

    /**
     * Returns the total number of collisions involving this particle.
     *
     * @return the total number of collisions involving this particle
     */
    public int count() {
        return count;
    }

    /**
     * Returns the time required by {@code this} particle to collide with
     * {@code that} particle.
     * <p>
     * If you don't understand the Physics used here. Refer this book-site,
     * <a href = "https://algs4.cs.princeton.edu/61event/index.php#6.1">Algorithms
     * 4th Edition</a>
     * <p>
     * I am doing this project as an exercise after reading that book.
     *
     * @param that the other particle.
     * @return the time required for collision between this particle and that
     * particle
     */
    public double timeToHit(Particle that) {
        if (this == that) {
            return INFINITY;
        }
        double dx = that.rx - this.rx;          // x-axis displacement
        double dy = that.ry - this.ry;          // y-axis displacement
        double dvx = that.vx - this.vx;         // x-axis relative velocity
        double dvy = that.vy - this.vy;         // y-axis relative velocity

        // dot product of vector dr and vector dv; predicts the existence of finite time to collide
        double dvdr = dvx * dx + dvy * dy;
        if (dvdr > 0) {
            return INFINITY;        // I know this physics is weird
        }

        double dvdv = dvx * dvx + dvy * dvy;    // magnitude of dv
        if (dvdv == 0) {
            return INFINITY;        // Means relative velocity is zero
        }

        double drdr = dx * dx + dy * dy;    // magnitude of dr vector

        double sigma = that.radius + this.radius;       // sum of radii of colliding particles

        // this discriminant comes from solution of a quadratic equation, see some Physics dude.
        double discriminant = dvdr * dvdr - dvdv * (drdr - sigma * sigma);
        if (drdr < sigma * sigma) {
            System.out.println("Particles overlap !!! UNEXPECTED behaviour expected :p");
        }
        if (discriminant < 0) {
            return INFINITY;    // there are no solutions to equation or the time to collide 
        }
        return -(dvdr + Math.sqrt(discriminant)) / dvdv;
        // we ignore the other solution (Why ? because there can be two collisons in Mathematics; think about it, try it)
    }

    /**
     * Returns the time until this particle hits a Horizontal wall assuming no
     * Intervening collision.
     *
     * @return the time until this particle hits a Horizontal wall assuming no
     * Intervening collision
     */
    public double timeToHitHorizontalWall() {
        if (vy < 0) {
            return (radius - ry) / vy;
        } else if (vy > 0) {
            return (1.0 - ry - radius) / vy;
        } else {
            return INFINITY;
        }
    }

    /**
     * Returns the time until this particle hits a Vertical swall assuming no
     * Intervening collision.
     *
     * @return the time until this particle hits a Vertical wall assuming no
     * Intervening collision
     */
    public double timeToHitVerticalWall() {
        if (vx < 0) {
            return (radius - rx) / vx;
        } else if (vx > 0) {
            return (1.0 - rx - radius) / vx;
        } else {
            return INFINITY;
        }
    }

    /**
     * Updates the velocities of this and {@code that} particle acccording to
     * the laws of elastic collision. Assumes the particles collide at this
     * moment.
     * <p>
     * If you don't understand the Physics used here. Refer this book-site,
     * <a href = "https://algs4.cs.princeton.edu/61event/index.php#6.1">Algorithms
     * 4th Edition</a>
     * <p>
     * I am doing this project as an exercise after reading that book.
     *
     * @param that
     */
    public void bounceOff(Particle that) {
        double dx = that.rx - this.rx;
        double dy = that.ry - this.ry;
        double dvx = that.vx - this.vx;
        double dvy = that.vy - this.vy;
        // dot product of dv vector and dr vector
        double dvdr = dvx * dx + dvy * dy;
        // sum of the radii of this and that particle
        double sigma = this.radius + that.radius;

        // total magnitude of impulse exchanged on collision
        double j = 2 * dvdr * this.mass * that.mass / ((this.mass + that.mass) * sigma);

        // x and y components of the Impulse 
        double jx = j * dx / sigma;
        double jy = j * dy / sigma;

        // update velocity according to momentum change given by impulse
        this.vx += jx / this.mass;
        this.vy += jy / this.mass;
        that.vx -= jx / that.mass;
        that.vy -= jy / that.mass;

        // update collision counts
        this.count++;
        that.count++;
    }

    /**
     * Updates the velocity of this particle upon collision with a Vertical
     * wall. Assumes the collision is undergoing at this instant. Reflects the
     * x-component of the velocity.
     */
    public void bounceOffVerticalWall() {
        this.vx = -this.vx;
        count++;
    }

    /**
     * Updates the velocity of this particle upon collision with a Horizontal
     * wall. Assumes that the paricle collides with the wall at this instant.
     * Reflects the velocity in y-direction.
     */
    public void bounceOffHorizontalWall() {
        this.vy = -this.vy;
        count++;
    }

    /**
     * Returns the kinetic energy of this particle.
     *
     * @return the kinetic energy of this particle
     */
    public double kineticEnergy() {
        // elementary formula 1/2 * m * v * v 
        return (mass * (vx * vx + vy * vy)) / 2;
    }

    /**
     * Set new default color for randomly generated particles. Does NOT do
     * anything for particles constructed with specific colors.
     *
     * @param newColor the new default color for random particles
     */
    public void setDefaultColor(Color newColor) {
        Particle.defaultColor = newColor;
    }

    /**
     * If this setting is turned on, small sized particles are upscaled to
     * minimum observable radius onscreen which otherwise might be invisible due
     * to extremely small size.
     * <p>
     * By default this is disabled.
     *
     * @param yes if {@code true} up-scaling is enabled, other not.
     */
    public static void useRadiusUpscaling(boolean yes) {
        if (yes) {
            useRadiusUpscaling = true;
        } else {
            useRadiusUpscaling = false;
        }
    }

    /**
     * Returns true if radius upscaling is enabled.
     *
     * @return true if radius upscaling is enabled
     */
    public static boolean radiusUpscalingEnabled() {
        return useRadiusUpscaling;
    }

    /**
     * For unit testing of the class Particle.java
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Particle p = new Particle(0, .5, 1, 0, 0.001, 1, Color.RED);
        //Particle q = new Particle(.5, 1, -1, 0, 0.05, 1, Color.BLACK);
        System.out.println(p.timeToHitVerticalWall());

        p.draw();
        //q.move(0.5);
        // p.draw();
        // q.draw();
        //System.out.println(q.vy);;
        p.bounceOffVerticalWall();
        //System.out.println(q.vy);;
        p.move(0.0);
        p.draw();
        //q.draw();
        StdDraw.show();
        // StdDraw.filledCircle(.1, .1, .2);
    }
}
