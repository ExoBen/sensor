package org.toby.sensor.bugs;

import KinectPV2.KinectPV2;
import org.toby.sensor.bugs.instances.*;
import org.toby.sensor.bugs.instances.Invert;
import org.toby.sensor.interfaces.LoadersInterface;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.Random;

public class BugLoader implements LoadersInterface {

  private BugSounds sounds;
  private Random rand;
  private AbstractBug currentBug;

  private BlackAndWhiteMask blackAndWhiteMask;
  private DarkLightGreyMask darkLightGreyMask;
  private LightDarkGreyMask lightDarkGreyMask;
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
    darkLightGreyMask = new DarkLightGreyMask();
    lightDarkGreyMask = new LightDarkGreyMask();
    blackAndWhiteVideo = new BlackAndWhiteVideo();
    invert = new Invert();
    downloadedStatic = new DownloadedStatic(p);
  }

  public PImage execute(PImage liveVideo, PImage body, KinectPV2 kinect) {
    PImage outputVideo;

    if (currentBug != null) {
      outputVideo = executeBug(liveVideo, body, kinect);
      if (System.currentTimeMillis() > bugStartTime + currentBugLength) {
        currentBug = null;
        currentlyBugging = false;
      }
    } else {
      int dice = rand.nextInt(6);
      switch (dice) {
        case 0:
          currentBug = darkLightGreyMask;
          break;
        case 1:
          currentBug = lightDarkGreyMask;
          break;
        case 2:
          currentBug = blackAndWhiteMask;
          break;
        case 3:
          currentBug = blackAndWhiteVideo;
          break;
        case 4:
          currentBug = invert;
          break;
        default:
          currentBug = downloadedStatic;
      }
      sounds.playSound();
      currentBugLength = rand.nextInt(200) + 100;
      currentlyBugging = true;
      bugStartTime = System.currentTimeMillis();
      outputVideo = executeBug(liveVideo, body, kinect);
    }
    return outputVideo;
  }

  public PImage executeDownloadedStatic(PImage liveVideo, PImage body, KinectPV2 kinect) {
    currentBug = downloadedStatic;
    sounds.playSound();
    currentBugLength = rand.nextInt(100) + 200;
    currentlyBugging = true;
    bugStartTime = System.currentTimeMillis();
    return executeBug(liveVideo, body, kinect);
  }

  public boolean isCurrentlyBugging() {
    return currentlyBugging;
  }

  private PImage executeBug(PImage liveVideo, PImage body, KinectPV2 kinect) {
    return currentBug.executeBug(liveVideo, body, kinect);
  }
}
