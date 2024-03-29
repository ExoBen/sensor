package org.toby.sensor.bugs.instances;

import KinectPV2.KinectPV2;
import org.toby.sensor.bugs.AbstractBug;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

import static org.toby.sensor.ImageLoader.loadStatics;

public class LongDownloadedStatic extends AbstractBug {

  private ArrayList<PImage> statics;
  private int frame = 0;

  public LongDownloadedStatic(PApplet p) {
    statics = loadStatics(p);
  }

  public PImage executeBug(PImage liveVideo, PImage body, KinectPV2 kinect) {
    frame++;
    if (frame%25 == 0) {
      frame = 0;
    }
    return statics.get(frame);
  }

}
