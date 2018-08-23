# 2-Dimensional elastic N-Particle simulation

**See the API documentation here: http://manishjoshi394.github.io/n-body-simulation**

### Introduction
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
