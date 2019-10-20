package org.toby.sensor.interfaces;

import KinectPV2.KinectPV2;
import gab.opencv.OpenCV;
import processing.core.PImage;

public interface LoadersInterface {
  PImage execute(PImage liveVideo, PImage body, KinectPV2 kinect, OpenCV openCV);
}
