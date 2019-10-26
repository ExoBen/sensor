package org.toby.sensor.base;

import KinectPV2.KinectPV2;
import gab.opencv.*;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;

import static org.toby.sensor.UtilitiesAndConstants.HEIGHT_CUT;
import static org.toby.sensor.UtilitiesAndConstants.SET_HEIGHT;

class SilhouetteDrawing {

  private Integer phase;

  SilhouetteDrawing(Integer phase) {
    this.phase = phase;
  }

  void execute(PImage liveVideo, PImage body, KinectPV2 kinect, OpenCV openCV, PApplet parent) {
    float polygonFactor;
    switch (phase) {
      case 1:
        polygonFactor = 1f;
        break;
      case 2:
        polygonFactor = 3f;
        break;
      case 3:
        polygonFactor = 8f;
        break;
      default:
        polygonFactor = 12f;
    }

    if (phase > 3) {
      parent.fill(0, 2);
      parent.stroke(0);
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
        parent.stroke(255, 200);
        parent.beginShape();

        for (PVector point : contour.getPolygonApproximation().getPoints()) {
          parent.vertex(point.x*3.4f+130, point.y*3.4f-90);
        }
        parent.endShape();
      }
    }
    parent.noStroke();
  }
}
