# 2-Dimensional elastic N-Particle simulation

## Introduction
This is an Elastic version of the classic [N-body simulation problem](https://en.wikipedia.org/wiki/N-body_simulation). Here bodies under consideration are small particles like molecules in a gas. The motion involved is the same as in the dynamics of chemical reactions, atomic diffusion, sphere packing, the stability of the rings around Saturn, the phase transitions of cerium and cesium, one-dimensional self-gravitating systems, and front propagation.[[reference]](https://arxiv.org/ftp/physics/papers/0405/0405089.pdf)
<p>
  <img src = "/docs/img/p10.gif" align = "left"/>
  <img src = "/docs/img/p2000.gif" align = "right"/>
</p>

This program simulates the motion of N colliding particles according to the laws of elastic collision using event-driven simulation. The same techniques apply to other domains that involve physical modeling of particle systems. For example, in Molecular Dynamics to understand and predict properties of physical systems at the particle level; others include computer graphics, computer games, and robotics.

### Hard sphere model
The hard sphere model (billiard ball model) is an idealized model of the motion of atoms or molecules in a container. The focus here is on the two-dimensional version called the hard disc model. The salient properties of this model are listed below.

- N particles in motion, confined in the unit box.
- Particle i has known position (rxi, ryi), velocity (vxi, vyi), mass mi, and radius σi.
- Particles interact via elastic collisions with each other and with the reflecting boundary.
- No other forces are exerted. Thus, particles travel in straight lines at constant speed between collisions.
This simple model plays a central role in statistical mechanics, a field which relates macroscopic observables (e.g., temperature, pressure, diffusion constant) to microscopic dynamics (motion of individual atoms and molecules). Maxwell and Boltzmann used the model to derive the distribution of speeds of interacting molecules as a function of temperature; Einstein used it to explain the Brownian motion of pollen grains immersed in water.[[reference]](https://introcs.cs.princeton.edu/java/assignments/collisions.html)

**See the API documentation here: http://manishjoshi394.github.io/n-body-simulation**

## Using default Simulator client
The `CollisionSystem` class has been configured to run some sample simulations out of the box.
You can compile and run `CollisionSystem` with sample files in the directory `/sample-data-files`.
There are a number of files in the `/sample-data-files` configured to produce interesting outputs.

Here are the steps for using them,
- Download a copy of the repository as zip or clone the repository manually.
- Extract the contents to location of your choice.
- Open terminal and goto that same location.
- Compile using `javac CollisionSystem.java`
- Run using `java CollisionSystem < sample-data-files/<file-name.txt>`. Replace `<file-name.txt>` with sample data file of your choice. 

For example:
If I do-
- `javac CollisionSystem.java`
- `java CollisionSystem < sample-data-files/diffusion.txt`
- Then, a Diffision of particles simulation window appears as shown on the right. [Thanks to GIPHY, where i made the GIFs from onscreen recordings](https://giphy.com) 
<img src = "/docs/img/diffusion.gif" align = "right"/>

Simulation stops after some minutes as configured in the default `main()` method inside `CollisionSystem` class. 

**NOTE: If motion is too fast or too slow, you can change redraw frequency by editing the call to `setRedrawHZ()` in second last line in `main()` method of the class `CollisionSystem`; Increasing this value tends to smoothen the animation.**

## Configuring the Simulator; How to use the API ?
- Download a copy of the repository as zip or clone the repository manually.
- Copy the repository contents to your Project's working directory.
- Now you shall be able to create objects of `Particle` and `CollisionSystem` within your Java project.
- Read the API specification [here](http://manishjoshi394.github.io/n-body-simulation) and **use the simulator the way you want** (By writing some code of course).

### API Reference: http://manishjoshi394.github.io/n-body-simulation

#### CREDITS
This project was in my TODO list for a long time, about half a year now. So this week i did this one and i must say, IT WAS AMAZING. 
The sample data files used here belong to the original authors of the `StdDraw` class which i have used as a dependency here. You can pay their page on the same a visit [here](https://algs4.cs.princeton.edu/61event/index.php#6.1). All dependency files are part of the [Educational algs4 library](https://algs4.cs.princeton.edu/code/algs4.jar) which i use regularly in my projects. Many thanks to the library's authors for releasing it under GNU-GPL-v3 license. 
Credits to [GIPHY](giphy.com) for letting me convert my recorded videos of simulations to GIFs.
