/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biometricauthenticationscheme;

/**
 *
 * @author Isura Manchanayake
 */
public class PointXY {
    
    private final double x;
    private final double y;
    
    public PointXY(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public PointXY() {
        this.x = 0.0;
        this.y = 0.0;
    }
    
    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}
