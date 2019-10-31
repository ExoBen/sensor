package org.toby.sensor.bugs.instances;

import KinectPV2.KinectPV2;
import org.toby.sensor.bugs.AbstractBug;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

import static org.toby.sensor.ImageLoader.loadGlitches;

public class Glitches extends AbstractBug {

  private ArrayList<PImage> glitches;
  private int frame = 0;

  public Glitches(PApplet p) {
    glitches = loadGlitches(p);
  }

  public PImage executeBug(PImage liveVideo, PImage body, KinectPV2 kinect) {
    frame++;
    if (frame%10 == 0) {
      frame = 0;
    }
    return glitches.get(frame);
  }

}
