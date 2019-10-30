package org.toby.sensor.base;

import KinectPV2.KinectPV2;
import gab.opencv.*;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Random;

import static org.toby.sensor.UtilitiesAndConstants.HEIGHT_CUT;
import static org.toby.sensor.UtilitiesAndConstants.SET_HEIGHT;

public class SilhouetteDrawing {

  Random rand;
  boolean goldenTicket;

  public SilhouetteDrawing() {
    rand = new Random();
  }

  public void execute(KinectPV2 kinect, OpenCV openCV, int phase, PApplet parent) {
    float polygonFactor;
    switch (phase) {
      case 1:
        polygonFactor = 1.2f;
        break;
      case 2:
        polygonFactor = 3f;
        break;
      case 3:
        polygonFactor = 5f;
        break;
      default:
        polygonFactor = 8f;
    }

    boolean wasGolden = goldenTicket;
    int changeOfAFlashHappening = 15;
    goldenTicket = (phase == 6 && rand.nextInt(changeOfAFlashHappening) == 1);

    if (phase > 3) {
      if (goldenTicket) {
        parent.fill(180, 240);
        parent.stroke(180);
      } else if (wasGolden) {
        parent.fill(0, 250);
        parent.stroke(0);
       }else {
        parent.fill(0, 2);
        parent.stroke(0);
      }
      parent.rect(0, HEIGHT_CUT, 1920, SET_HEIGHT);
    }

    parent.noFill();
    parent.strokeWeight(2.5f);

    openCV.loadImage(kinect.getPointCloudDepthImage());
    openCV.gray();
    openCV.threshold(50);

    ArrayList<Contour> contours = openCV.findContours(false, false);

    for (Contour contour : contours) {
      contour.setPolygonApproximationFactor(polygonFactor);
      if (contour.numPoints() > 50) {
        if (goldenTicket) {
          parent.stroke(0, 200);
        } else {
          parent.stroke(255, 200);
        }
        parent.stroke(255, 200);
        parent.beginShape();

        for (PVector point : contour.getPolygonApproximation().getPoints()) {
          float q1 = 0;
          float q2 = 0;
          float convertedX = point.x*4f;
          float convertedY = point.y*3.5f-60;
          if (convertedX > 800) {
            q1 = (800 - convertedX)/4f;
          } else {
            q1 = (800 - convertedX)/3.6f;
          }
          q2 = -(convertedY)/7;
          parent.vertex(convertedX + q1, convertedY + q2);
        }
        parent.endShape();
      }
    }
    parent.noStroke();
  }
}
