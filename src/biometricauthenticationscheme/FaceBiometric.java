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
public class FaceBiometric implements Biometric {

    private double dist_left_pupil__right_pupil;
    private double dist_pupil__left_iris_corner;
    private double dist_pupil__right_iris_corner;
    private double dist_pupil__eye_upper_line;
    private double dist_pupil__eye_lower_line;
    private double dist_pupil__eye ;

    @Override
    public boolean authenticate() {
        return true;
    }
    
    @Override
    public void scale() {
        
    }
    
}
