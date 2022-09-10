public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    public static final double G = 6.67e-11;

    public Planet(double xxPos, double yyPos, double xxVel, double yyVel, double mass, String imgFileName) {
        this.xxPos = xxPos;
        this.yyPos = yyPos;
        this.xxVel = xxVel;
        this.yyVel = yyVel;
        this.mass = mass;
        this.imgFileName = imgFileName;
    }

    public Planet(Planet p) {
        this.xxPos = p.xxPos;
        this.yyPos = p.yyPos;
        this.xxVel = p.xxVel;
        this.yyVel = p.yyVel;
        this.mass = p.mass;
        this.imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet p) {
        double dx = p.xxPos - this.xxPos;
        double dy = p.yyPos - this.yyPos;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double calcForceExertedBy(Planet p) {
        double r = this.calcDistance(p);
        return G * this.mass * p.mass / (r * r);
    }

    public double calcForceExertedByX(Planet p) {
        double dx = p.xxPos - this.xxPos;
        double r = this.calcDistance(p);
        double f = this.calcForceExertedBy(p);
        return f * dx / r;
    }

    public double calcForceExertedByY(Planet p) {
        double dy = p.yyPos - this.yyPos;
        double r = this.calcDistance(p);
        double f = this.calcForceExertedBy(p);
        return f * dy / r;
    }

    public double calcNetForceExertedByX(Planet[] planets) {
        double fXNet = 0;
        for (Planet planet : planets) {
            if (this.equals(planet)) {
                continue;
            }
            fXNet += this.calcForceExertedByX(planet);
        }
        return fXNet;
    }

    public double calcNetForceExertedByY(Planet[] planets) {
        double fYNet = 0;
        for (Planet planet : planets) {
            if (this.equals(planet)) {
                continue;
            }
            fYNet += this.calcForceExertedByY(planet);
        }
        return fYNet;
    }

    public void update(double dt, double fXNet, double fYNet) {
        double aX = fXNet / this.mass;
        double aY = fYNet / this.mass;
        this.xxVel += dt * aX;
        this.yyVel += dt * aY;
        this.xxPos += dt * this.xxVel;
        this.yyPos += dt * this.yyVel;
    }

    public void draw(){
        StdDraw.picture(this.xxPos, this.yyPos, "images/" + imgFileName);
    }
}
