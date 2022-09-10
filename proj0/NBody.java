public class NBody {
    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];

        double radius = readRadius(filename);
        Planet[] planets = readPlanets(filename);

        StdDraw.setScale(-radius, radius);
        StdDraw.clear();
        StdDraw.enableDoubleBuffering();

        for (double time = 0.0; time <= T; time += dt) {
            // draw background
            StdDraw.picture(0, 0, "images/starfield.jpg");
            // put planets on the universe background
            for (Planet planet : planets) {
                planet.draw();
            }
            // show single frame
            StdDraw.show();

            // update each planet's position and velocity for next frame
            double[] xForces= new double[planets.length], yForces = new double[planets.length];
            for (int i = 0; i < planets.length; i++) {
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }
            for (int i = 0; i < planets.length; i++) {
                planets[i].update(dt, xForces[i], yForces[i]);
            }
            // make it slower
            StdDraw.pause(10);
        }

        // print out the final state of the universe in the same format as the input.txt
        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                    planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
        }
    }

    public static double readRadius(String filename) {
        In in = new In(filename);
        int num = in.readInt();
        double radius = in.readDouble();
        in.close();
        return radius;
    }

    public static Planet[] readPlanets(String filename) {
        In in = new In(filename);
        int num = in.readInt();
        double radius = in.readDouble();

        Planet[] planets = new Planet[num];
        for (int i = 0; i < num; i++) {
            planets[i] = new Planet(in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readString());
        }

        return planets;
    }
}
