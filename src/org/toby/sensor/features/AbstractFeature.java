package org.toby.sensor.features;

import KinectPV2.KinectPV2;
import processing.core.PImage;

public abstract class AbstractFeature {

  public abstract PImage executeFeature(PImage liveVideo, PImage body, KinectPV2 kinect);

}
