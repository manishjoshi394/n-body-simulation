/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.awt.Color;

/**
 *
 * @author manishjoshi394
 */
public class Particle {

    private final double INFINITY = Double.POSITIVE_INFINITY;

    private double rx, ry;                  // position
    private double vx, vy;                  // velocity
    private final double radius;            // radius of particle 
    private final double mass;              // mass of the particle

    private int count;
    private Color color;

    /**
     * Initializes a particle with random position and velocity.
     */
    public Particle() {
        this.count = 0;

        this.rx = StdRandom.uniform(0, 1);
        this.ry = StdRandom.uniform(0, 1);
        this.vx = StdRandom.uniform(-0.5, 0.5);
        this.vy = StdRandom.uniform(-0.5, 0.5);
        this.radius = 0.01;
        this.mass = 0.5;
        this.color = Color.BLACK;
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
        this.radius = radius;
        this.mass = mass;
        this.color = color;
    }

    /**
     * Draws this particle to the Standard Draw.
     */
    public void draw() {
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
     *
     * @param that the other particle.
     * @return the time required for collision between this particle and that
     * particle
     */
    public double timeToHit(Particle that) {
        if (this == that) {
            return INFINITY;
        }

        double tX = this.convergenceTimeX(that);
        double tY = this.convergenceTimeY(that);

        if (tX == -1 && tY == -1) {
            return 0;
        } else if (tX == -1) {
            return tY;
        } else if (tY == -1) {
            return tX;
        } else if (tX != tY) {
            return INFINITY;
        } else {
            // tX is equal to tY in this case
            return tX;
        }
    }

    private double convergenceTimeX(Particle that) {
        // x-axis displacement taking into account radii of particles
        double dX = this.rx - that.rx;

        // relative x-axis velocity of this particle w.r.t that particle 
        double relVelocityX = this.vx - that.vx;

        // if particles have same X-coordinate and do not move w.r.t. one another at all
        if (dX == 0 && relVelocityX == 0) {
            // X coordinates are always the same for both particles
            return -1;
        } else if (relVelocityX == 0) {
            // dX != 0 but relVelocity == 0 so time required = INFINITY
            return INFINITY;
        } else if (dX == 0) {
            // dX == 0 but relVelocity != 0 so time required to collide in future = INFINITY
            return INFINITY;
        }

        // check relative velocity directions
        if (Math.signum(dX) == Math.signum(relVelocityX)) {
            return INFINITY;
        } else {
            // this must be added to dX before calculating time using velocity value
            // essentially reduces the magnitude of dX by the sum of particles' radii
            double radiusFactor = (-1) * Math.signum(dX) * (this.radius + that.radius);
            return -(dX + radiusFactor) / relVelocityX;
        }
    }

    private double convergenceTimeY(Particle that) {
        // y-axis displacement taking into account the radii of the particles
        double dY = this.ry - that.ry;

        // relative y-axis velocity of this particle w.r.t that particle 
        double relVelocityY = this.vy - that.vy;

        // if particles have same Y-coordinate and do not move w.r.t. one another at all
        if (dY == 0 && relVelocityY == 0) {
            return -1;
        } else if (relVelocityY == 0) {
            // dY != 0 but relVelocityY == 0 so time required = INFINITY
            return INFINITY;
        } else if (dY == 0) {
            // dY == 0 but relVelocity != 0 so time required to collide in future = INFINITY
            return INFINITY;
        }

        // check relative velocity direction 
        if (Math.signum(dY) == Math.signum(relVelocityY)) {
            // y coordinates diverge, time required = Infinity
            return INFINITY;
        } else {
            // this must be added to dY before calculating time using velocity value
            // essentially reduces the magnitude of dY by the sum of particles' radii
            double radiusFactor = (-1) * Math.signum(dY) * (this.radius + that.radius);
            return -(dY + radiusFactor) / relVelocityY;
        }
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
            return (1.0 - rx - radius) / vy;
        } else {
            return INFINITY;
        }
    }

    /**
     *
     * @param b
     */
    public void bounceOff(Particle b) {
        
    }

    public void bounceOffVerticalWall() {
        vx = -vx;
        count++;
    }

    public void bounceOffHorizontalWall() {
        vy = -vy;
        count++;
    }

    /**
     * Returns the kinetic energy of this particle.
     *
     * @return the kinetic energy of this particle
     */
    public double kineticEnergy() {
        return (mass * (vx * vx + vy * vy)) / 2;
    }

    // for unit testing of the class
    public static void main(String[] args) {
        Particle p = new Particle(.5, 0, 0, 1, 0.03, 0, Color.BLACK);
        Particle q = new Particle(.5, 1, 0.0, -1, 0.05, 0, Color.BLACK);
        System.out.println(p.timeToHit(q));
        p.move(0.46);
        q.move(0.46);
        p.draw();
        q.draw();
        // StdDraw.filledCircle(.1, .1, .2);
    }
}
