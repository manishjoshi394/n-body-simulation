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
/**
 * The 2D-N-Particle Elastic collision simulator class. The class provides a
 * relatively efficient API for any simulations the client might want to do with
 * elastic 2-D particles. It deals with the events occurring during simulation.
 * It uses the {@code Particle} data type to achieve this goal. Events are
 * scheduled for each possible collisions and are processed in the chronological
 * order.
 *
 * @author Manish Joshi
 */

import dependencies.*;
import java.awt.Color;
import java.util.PriorityQueue;

public class CollisionSystem {

    private double HZ = 0.5;    // redraw frequency as redraws per clock tick (in Simulator time)

    private PriorityQueue<Event> pq;    // the event priority queue
    private Particle[] particles;       // the array of particles
    private double t = 0.0;                   // simulation clock time

    /**
     * Constructs the simulator class with given array of particles.
     *
     * @param particles the array of particles
     */
    public CollisionSystem(Particle[] particles) {
        // make a defensive copy to support immuatability
        this.particles = particles.clone();

        // set default double buffering 
        useDoubleBuffering(true);
    }

    // pushes the upcoming collision events to the priority queue if they occur within the specified time limit
    private void predict(Particle a, double limit) {
        if (a == null) {
            return;
        }
        for (Particle p : particles) {
            // check possible collisions b/w a and p
            double dt = a.timeToHit(p);
            if (t + dt <= limit) {
                // if collision is possible within time limit, add to the pq
                pq.add(new Event(t + dt, a, p));
            }
        }
        double dtV = a.timeToHitVerticalWall();
        if (t + dtV <= limit) {
            pq.add(new Event(t + dtV, null, a));
        }
        double dtH = a.timeToHitHorizontalWall();
        if (t + dtH <= limit) {
            pq.add(new Event(t + dtH, a, null));
        }
    }

    // Handles the Redraw event by redrawing all the particles with updated positions
    private void redraw(double limit) {
        StdDraw.clear();    // clear the canvas
        for (Particle p : particles) {
            p.draw();       // redraw each paricle
        }
        StdDraw.show();     // in case double buffering is used in StdDraw
        StdDraw.pause(20);  // freeze StdDraw for 20 ms so that frame may be observed

        // schedule redraw of frames based on Framerate frequency 
        if (t < limit) {
            pq.add(new Event(t + 1.0 / HZ, null, null));
        }
    }

    /**
     * A switch for a setting for the StdDraw library, may improve frame-rates
     * during simulation.
     *
     * @param yes if {@code true} double buffering is enabled (this is default),
     * otherwise it's disabled
     */
    public static void useDoubleBuffering(boolean yes) {
        if (yes) {
            StdDraw.enableDoubleBuffering();
        } else {
            StdDraw.disableDoubleBuffering();
        }
    }

    /**
     * Simulates the system of particles for the given amount of time limit
     * using Event driven programming.
     *
     * @param limit the time limit for simulation in seconds
     */
    public void simulate(double limit) {
        // initialize the PQ with collision events and redraw event
        pq = new PriorityQueue<>();
        for (Particle a : particles) {
            predict(a, limit);
        }
        pq.add(new Event(0, null, null));       // add redraw event

        // the main event driven simulation loop
        while (!pq.isEmpty()) {

            // get impending event, drive the simulation, discard if invalids
            Event e = pq.remove();
            if (!e.isValid()) {
                continue;
            }

            // advance all particles in time and bring them to time of current event
            for (Particle p : particles) {
                p.move(e.time - t);
            }
            t = e.time;         // advance the clock

            // update the particle velocities
            Particle a = e.a, b = e.b;
            if (a != null && b != null) {
                a.bounceOff(b);
            } else if (a != null) {
                a.bounceOffHorizontalWall();
            } else if (b != null) {
                b.bounceOffVerticalWall();
            } else {
                redraw(limit);
                continue;
            }

            predict(a, limit);      // add new events related to a 
            predict(b, limit);      // and b
        }
        System.out.println("Simulation over !");
    }

    /**
     * Sets the number of redraw events per second. This value should be set in
     * proportion to the average speed of the paricles in the system. TOO HIGH
     * HZ with small speeds with result in extremely slow motion while TOO LOW
     * HZ with high speed might cause very fast and messy animation.
     * <p>
     * 0.5 is used as default value.
     *
     * @param HZ the new value of redraw frequency
     */
    public void setRedrawHZ(double HZ) {
        this.HZ = HZ;
    }

    /**
     * ************************************************************************
     * This class encapsulates the details associated with an event during
     * simulation. Event implicitly can be of three types based on whether a or
     * b are null or not. We use this strategy to avoid use of an Event type
     * classifying variable in the class.
     * <pre>
     *      - a and b both null:         redraw event
     *      - a null, b not null:        collision with vertical wall
     *      - a not null, b null:        collision with horizontal wall
     *      - a and b both not null      binary collision between a and b
     * </pre >
     **************************************************************************
     */
    private static class Event implements Comparable<Event> {

        public double time;             // time till collision event
        public Particle a, b;           // the paricles which shall collide
        public int countA, countB;      // collision counts at Event creation

        // creates a new event scheduled at given time involving a and b
        public Event(double time, Particle a, Particle b) {
            this.time = time;
            this.a = a;
            this.b = b;
            if (a != null) {
                countA = a.count();
            } else {
                countA = -1;    // sentinel value
            }
            if (b != null) {
                countB = b.count();
            } else {
                countB = -1;
            }
        }

        @Override
        public int compareTo(Event that) {
            double dt = this.time - that.time;
            if (dt < 0) {
                return -1;
            } else if (dt > 0) {
                return +1;
            } else {
                return 0;
            }
        }

        // has any intervening event has occured since creation of this event
        public boolean isValid() {
            if (a != null && a.count() != countA) {
                return false;
            }
            if (b != null && b.count() != countB) {
                return false;
            }
            return true;
        }

    }

    //  for unit testing of the class
    public static void main(String[] args) {
        
        StdDraw.setCanvasSize(1000, 1000);
        
        // the array of particles
        Particle[] particles;
        Particle.useRadiusUpscaling(true);
        // create n random particles
        if (args.length == 1) {
            int n = Integer.parseInt(args[0]);
            particles = new Particle[n];
            for (int i = 0; i < n; i++)
                particles[i] = new Particle();
        }

        // or read from standard input
        else {
            int n = StdIn.readInt();
            System.out.println(n + "particles, Reading from STDIN...");
            particles = new Particle[n];
            for (int i = 0; i < n; i++) {
                double rx     = StdIn.readDouble();
                double ry     = StdIn.readDouble();
                double vx     = StdIn.readDouble();
                double vy     = StdIn.readDouble();
                double radius = StdIn.readDouble();
                double mass   = StdIn.readDouble();
                int r         = StdIn.readInt();
                int g         = StdIn.readInt();
                int b         = StdIn.readInt();
                Color color   = new Color(r, g, b);
                particles[i] = new Particle(rx, ry, vx, vy, radius, mass, color);
                System.out.println("Particle added " + radius);
            }
        }

        // create collision system and simulate
        CollisionSystem system = new CollisionSystem(particles);
     system.setRedrawHZ(10);
        system.simulate(10000);
           }

}
