package org.toby.sensor.base;

import KinectPV2.KinectPV2;
import gab.opencv.OpenCV;
import org.toby.sensor.interfaces.LoadersInterface;
import processing.core.PApplet;
import processing.core.PImage;

public class BaseLoader {

  private SilhouetteDrawing silhouetteDrawing;

  public BaseLoader() {
    silhouetteDrawing = new SilhouetteDrawing();
  }

  public void execute(PImage liveVideo, PImage body, KinectPV2 kinect, OpenCV openCV, PApplet parent) {
    silhouetteDrawing.execute(liveVideo, body, kinect, openCV, parent);
  }

}

