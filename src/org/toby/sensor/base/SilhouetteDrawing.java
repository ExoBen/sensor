package org.toby.sensor.base;

import KinectPV2.KinectPV2;
import gab.opencv.*;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;

class SilhouetteDrawing {

  void execute(PImage liveVideo, PImage body, KinectPV2 kinect, OpenCV openCV, PApplet parent) {
    parent.fill(0, 2);
    parent.rect(0, 0, 1920, 1080);

    parent.noFill();
    parent.strokeWeight(2.5f);

    openCV.loadImage(kinect.getPointCloudDepthImage());
    openCV.gray();
    openCV.threshold(50);

    ArrayList<Contour> contours = openCV.findContours(false, false);

    float polygonFactor = 10;
    for (Contour contour : contours) {
      contour.setPolygonApproximationFactor(polygonFactor);
      if (contour.numPoints() > 50) {
        parent.stroke(255, 200);
        parent.beginShape();

        for (PVector point : contour.getPolygonApproximation().getPoints()) {
          parent.vertex(point.x*3.4f, point.y*3.4f);
        }
        parent.endShape();
      }
    }
    parent.noStroke();
  }
}
