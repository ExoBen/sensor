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
  private DownloadedStatic downloadedStatic;

  private boolean currentlyBugging;
  private long bugStartTime;
  private int currentBugLength;

  public BugLoader(PApplet p) {
    sounds = new BugSounds(p);
    rand = new Random();
    blackAndWhiteMask = new BlackAndWhiteMask();
    blackAndWhiteVideo = new BlackAndWhiteVideo();
    invert = new Invert();
    downloadedStatic = new DownloadedStatic(p);
  }

  public PImage execute(PImage liveVideo, PImage body, KinectPV2 kinect, OpenCV openCV) {
    PImage outputImage;

    if (currentBug != null) {
      outputImage = executeBug(liveVideo, body, kinect);
      if (System.currentTimeMillis() > bugStartTime + currentBugLength) {
        currentBug = null;
        currentlyBugging = false;
      }
    } else {
      int dice = rand.nextInt(6);
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
        default:
          currentBug = downloadedStatic;
      }
      sounds.playSound();
      currentBugLength = rand.nextInt(200) + 300;
      currentlyBugging = true;
      bugStartTime = System.currentTimeMillis();
      outputImage = executeBug(liveVideo, body, kinect);
    }
    PImage border = new PImage(SET_WIDTH, SET_HEIGHT+HEIGHT_CUT);
    border.set(0, HEIGHT_CUT, outputImage);
    return border;
  }

  public boolean isCurrentlyBugging() {
    return currentlyBugging;
  }

  private PImage executeBug(PImage liveVideo, PImage body, KinectPV2 kinect) {
    return currentBug.executeBug(liveVideo, body, kinect);
  }
}
