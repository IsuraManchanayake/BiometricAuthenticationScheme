/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biometricauthenticationscheme;

import java.io.File;
import java.io.FileNotFoundException;
import static java.lang.Math.abs;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Isura Manchanayake
 */
public class FaceBiometric implements Biometric {

    private final HashMap<PointName, PointXY> markedPoints;

    public FaceBiometric() {
        markedPoints = new HashMap<>();
    }

    public HashMap<PointName, PointXY> getMarkedPoints() {
        return this.markedPoints;
    }

    @Override
    public String authenticate() {

        String bestMatchName = null;
        double bestDistanceSum = Double.MAX_VALUE;
        System.out.println(bestDistanceSum);
        try {
            Scanner fileScanner = new Scanner(new File("data"));
//            Scanner fileScanner = new Scanner(getClass().getResourceAsStream("/data/data"));

            while (true) {
                boolean ok = true;
                try {
                    String name = fileScanner.next();
                    System.out.println(name);
//                    System.out.println(name + " ds");
                    HashMap<PointName, PointXY> pointData = new HashMap<>();

                    for (PointName pointName : PointName.values()) {
                        double x = fileScanner.nextDouble();
                        double y = fileScanner.nextDouble();
                        pointData.put(pointName, new PointXY(x, y));
                        System.out.println(pointName.name() + " " + x + " " + y);
                        System.out.println(pointName.name() + " " + markedPoints.get(pointName).x + " " + markedPoints.get(pointName).y);
                    }

                    PointName from = PointName.PT_PUPIL;
                    PointName to = PointName.PT_NOSE_BOTTOM;
                    double orig_dist_pupil__left_outer_corner
                            = pointData.get(from).distance(pointData.get(to));
                    double user_dist_pupil__left_outer_corner
                            = markedPoints.get(from).distance(markedPoints.get(to));

                    double ratio
                            = orig_dist_pupil__left_outer_corner
                            / user_dist_pupil__left_outer_corner;

                    for (PointName pointName : PointName.values()) {
                        markedPoints.get(pointName).scale(ratio);
                        System.out.println(pointName.name() + " " + markedPoints.get(pointName).x + " " + markedPoints.get(pointName).y);
                    }

                    double distanceSum = 0.0;
                    for (PointName pointName : PointName.values()) {
                        double mx = markedPoints.get(pointName).x;
                        double my = markedPoints.get(pointName).y;
                        double x = pointData.get(pointName).x;
                        double y = pointData.get(pointName).y;
                        distanceSum += pointData.get(pointName).distance(markedPoints.get(pointName));
                        if (abs(mx - x) > 15 || abs(my - y) > 15) {
                            System.out.println(pointName.name() + " check failed");
                            System.out.println(mx + " " + my + " " + x + " " + y);
                            ok = false;
//                            return null;
                        }
                    }
                    System.out.println(distanceSum);
                    if (distanceSum < bestDistanceSum && ok) {
                        System.out.println("working");
                        bestDistanceSum = distanceSum;
                        bestMatchName = name;
                        System.out.println(name);
                    }
                    System.out.println(bestMatchName);

                } catch (Exception e) {
                    break;
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(FaceBiometric.class.getName()).log(Level.SEVERE, null, ex);
        }

        return bestMatchName;
    }

}
