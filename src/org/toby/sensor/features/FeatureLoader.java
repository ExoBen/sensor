package org.toby.sensor.features;

import KinectPV2.KinectPV2;
import gab.opencv.OpenCV;
import org.toby.sensor.features.instances.*;
import org.toby.sensor.interfaces.LoadersInterface;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.toby.sensor.UtilitiesAndConstants.*;

public class FeatureLoader implements LoadersInterface {

  private PApplet parent;
  private FeatureSounds sounds;
  private Random rand;
  private AbstractFeature currentFeature;

  // features
  private DepthImage depthImage;
  private BlackAndWhiteStatic blackAndWhiteStatic;

  private boolean currentlyFeaturing;
  private long featureStartTime;
  private int frame;
  private ArrayList<PImage> statics;
  private Integer[] startingPoints = new Integer[] {0,3,6,9};
  private Integer startingPoint;


  public FeatureLoader(PApplet p) {
    parent = p;
    sounds = new FeatureSounds(p);
    rand = new Random();
    depthImage = new DepthImage();
    blackAndWhiteStatic = new BlackAndWhiteStatic();
    statics = loadStatics(p);
  }

  public PImage execute(PImage liveVideo, PImage body, KinectPV2 kinect, OpenCV openCV) {
    PImage outputImage;
    if (currentFeature != null) {
      if (frame < 20) {
        outputImage = statics.get(startingPoint + frame/2);
      } else {
        outputImage = executeFeature(liveVideo, body, kinect);
        if (System.currentTimeMillis() > featureStartTime + 3000) {
          currentFeature = null;
          currentlyFeaturing = false;
        }
      }
      frame++;
    } else {
      int dice = rand.nextInt(2);
      switch (dice) {
        case 0:
          currentFeature = depthImage;
          break;
        default:
          currentFeature = blackAndWhiteStatic;
      }
      sounds.playSound();
      currentlyFeaturing = true;
      featureStartTime = System.currentTimeMillis();
      frame = 0;
      startingPoint = startingPoints[rand.nextInt(4)];
      outputImage = executeFeature(liveVideo, body, kinect);
    }
    PImage border = new PImage(SET_WIDTH, SET_HEIGHT+HEIGHT_CUT);
    border.set(0, HEIGHT_CUT, outputImage);
    return border;
  }

  public boolean isCurrentlyFeaturing() {
    return currentlyFeaturing;
  }

  private PImage executeFeature(PImage liveVideo, PImage body, KinectPV2 kinect) {
    return currentFeature.executeFeature(liveVideo, body, kinect);
  }
}

