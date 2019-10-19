package org.toby.sensor.bugs.instances;

import KinectPV2.KinectPV2;
import org.toby.sensor.Upscaler;
import org.toby.sensor.bugs.AbstractBug;
import processing.core.PConstants;
import processing.core.PImage;

import static org.toby.sensor.UtilitiesAndConstants.*;
import static org.toby.sensor.UtilitiesAndConstants.BLACK;

public class BWStaticOnSavedBackground extends AbstractBug {

  public PImage executeBug(PImage liveVideo, PImage body, PImage savedBackground, KinectPV2 kinect) {
    PImage depth = Upscaler.upscaler(kinect.getDepthImage(), KINECT_WIDTH*KINECT_HEIGHT);
    depth.filter(PConstants.THRESHOLD, 0.5f);

    PImage mask = new PImage(body.width, body.height);
    int bodyUpscaledSize = (body.width * body.height);
    for (int i = 0; i < bodyUpscaledSize; i++) {
      if (body.pixels[i] == BLACK) {
        mask.pixels[i] = depth.pixels[i];
      } else {
        mask.pixels[i] = BLACK;
      }
    }
    return mask;
  }
}
