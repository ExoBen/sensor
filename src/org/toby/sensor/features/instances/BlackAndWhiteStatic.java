package org.toby.sensor.features.instances;

import KinectPV2.KinectPV2;
import org.toby.sensor.Upscaler;
import org.toby.sensor.features.AbstractFeature;
import processing.core.PConstants;
import processing.core.PImage;

import static org.toby.sensor.UtilitiesAndConstants.KINECT_HEIGHT;
import static org.toby.sensor.UtilitiesAndConstants.KINECT_WIDTH;

public class BlackAndWhiteStatic extends AbstractFeature {

  public PImage executeFeature(PImage liveVideo, PImage body, KinectPV2 kinect) {
    PImage depth = Upscaler.upscaler(kinect.getDepthImage(), KINECT_WIDTH*KINECT_HEIGHT);
    depth.filter(PConstants.THRESHOLD, 0.5f);
    return depth;
  }

}