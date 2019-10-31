package org.toby.sensor.bugs;

import KinectPV2.KinectPV2;
import gab.opencv.OpenCV;
import org.toby.sensor.bugs.instances.*;
import org.toby.sensor.bugs.instances.Invert;
import org.toby.sensor.interfaces.LoadersInterface;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.Random;

import static org.toby.sensor.UtilitiesAndConstants.*;

public class BugLoader implements LoadersInterface {

  private BugSounds sounds;
  private Random rand;
  private AbstractBug currentBug;

  private BlackAndWhiteMask blackAndWhiteMask;
  private BlackAndWhiteVideo blackAndWhiteVideo;
  private Invert invert;
  private LongDownloadedStatic longDownloadedStatic;
  private Glitches glitches;

  private boolean currentlyBugging;
  private long bugStartTime;
  private int currentBugLength;

  public BugLoader(PApplet parent) {
    sounds = new BugSounds(parent);
    rand = new Random();
    blackAndWhiteMask = new BlackAndWhiteMask();
    blackAndWhiteVideo = new BlackAndWhiteVideo();
    invert = new Invert();
    longDownloadedStatic = new LongDownloadedStatic(parent);
    glitches = new Glitches(parent);
  }

  public PImage execute(PImage liveVideo, PImage body, KinectPV2 kinect, OpenCV openCV, int phase) {
    PImage outputImage;

    if (currentBug != null) {
      outputImage = executeBug(liveVideo, body, kinect);
      if (System.currentTimeMillis() > bugStartTime + currentBugLength) {
        currentBug = null;
        currentlyBugging = false;
      }
    } else {
      int dice;
      if (phase > 4) {
        dice = rand.nextInt(6);
      } else {
        dice = rand.nextInt(5);
      }
      switch (dice) {
        case 0:
          currentBug = blackAndWhiteMask;
          break;
        case 1:
          currentBug = blackAndWhiteVideo;
          break;
        case 2:
          currentBug = invert;
          break;
        case 3:
        case 4:
          currentBug = longDownloadedStatic;
          break;
        default:
          currentBug = glitches;
      }
      sounds.playSound();
      currentBugLength = rand.nextInt(200) + 200;
      currentlyBugging = true;
      bugStartTime = System.currentTimeMillis();
      outputImage = executeBug(liveVideo, body, kinect);
    }
    return outputImage;
  }

  public boolean isCurrentlyBugging() {
    return currentlyBugging;
  }

  private PImage executeBug(PImage liveVideo, PImage body, KinectPV2 kinect) {
    return currentBug.executeBug(liveVideo, body, kinect);
  }
}
