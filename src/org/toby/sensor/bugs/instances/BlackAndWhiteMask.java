package org.toby.sensor.bugs.instances;

import KinectPV2.KinectPV2;
import org.toby.sensor.UtilitiesAndConstants;
import org.toby.sensor.bugs.AbstractBug;
import processing.core.PImage;

import static org.toby.sensor.UtilitiesAndConstants.BLACK;
import static org.toby.sensor.UtilitiesAndConstants.WHITE;

public class BlackAndWhiteMask extends AbstractBug {

  public BlackAndWhiteMask() {}

  public PImage executeBug(PImage liveVideo, PImage body, KinectPV2 kinect) {
    return UtilitiesAndConstants.twoTone(body, WHITE, BLACK);
  }

}
