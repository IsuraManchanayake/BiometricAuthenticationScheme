/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biometricauthenticationscheme;

import static java.lang.Math.sqrt;

/**
 *
 * @author Isura Manchanayake
 */
public class PointXY {

    double x;
    double y;

    public PointXY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public PointXY(PointXY pt) {
        this.x = pt.x;
        this.y = pt.y;
    }

    public PointXY() {
        this.x = 0.0;
        this.y = 0.0;
    }

    @Override
    public String toString() {
        return this.x + " " + this.y;
//        return "(" + this.x + ", " + this.y + ")";
    }

    public void setRelativeXY(PointXY origin) {
        x -= origin.x;
        y -= origin.y;
    }

    public double distance(PointXY point) {
        return sqrt((this.x - point.x) * (this.x - point.x)
                + (this.y - point.y) * (this.y - point.y));
    }

    public void scale(double ratio) {
        this.x *= ratio;
        this.y *= ratio;
    }
}
