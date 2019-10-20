package org.toby.sensor.bugs.instances;

import KinectPV2.KinectPV2;
import org.toby.sensor.UtilitiesAndConstants;
import org.toby.sensor.bugs.AbstractBug;
import processing.core.PImage;

public class LightDarkGreyMask extends AbstractBug {

  public LightDarkGreyMask() {}

  public PImage executeBug(PImage liveVideo, PImage body, KinectPV2 kinect) {
    return UtilitiesAndConstants.twoTone(body, -2302756, -14803426);
  }

}
