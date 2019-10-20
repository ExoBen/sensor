package org.toby.sensor;

import KinectPV2.*;
import org.toby.sensor.base.BaseLoader;
import org.toby.sensor.bugs.BugLoader;
import org.toby.sensor.features.FeatureLoader;
import processing.core.PApplet;
import processing.core.PImage;
import processing.sound.*;

import java.util.Random;

import static org.toby.sensor.UtilitiesAndConstants.*;

public class Sensor extends PApplet {

  private BaseLoader base;
  private FeatureLoader feature;
  private boolean currentlyFeaturing = false;
  private BugLoader bug;
  private boolean currentlyBugging = false;

  private TextOverlay textOverlay;
  private KinectPV2 kinect;
  private long timeBegin;
  private long timeOfLastFeature;

  private Random rand;
  private SoundFile softFuzz;
  private boolean starting;

  public static void main(String[] args) {
    PApplet.main("org.toby.sensor.Sensor");
  }

  public void settings() {
    fullScreen();
  }

  public void setup() {
    rand = new Random();
    base = new BaseLoader();
    feature = new FeatureLoader(this);
    bug = new BugLoader(this);
    textOverlay = new TextOverlay(this);
    timeBegin = System.currentTimeMillis();
    timeOfLastFeature = timeBegin;
    setUpKinect(this);
    setUpSounds();
    textFont(createFont("F:/SkyDrive/Work/NEoN/sensor/resources/vcr.ttf", 48));
  }

  // -------------------------------------------------------------------------------------------------------------------

  public void draw() {
    background(0);
    noTint();
    long currentTime = System.currentTimeMillis() - timeBegin;
    PImage bodies = Upscaler.upscaler(kinect.getBodyTrackImage().get(39, 32, KINECT_WIDTH, KINECT_HEIGHT), KINECT_WIDTH * KINECT_HEIGHT);
    bodies.filter(THRESHOLD);
    PImage liveVideo = kinect.getColorImage().get(LEFT_OFFSET, 0, MAIN_WIDTH, MAIN_HEIGHT);
    liveVideo.filter(GRAY);

    boolean shouldBug = rand.nextInt(40) == 0;
    long timeSinceLastFeature = System.currentTimeMillis() - timeOfLastFeature;
    boolean toFeature = (timeSinceLastFeature > 5000 && rand.nextInt(250) == 0) || timeSinceLastFeature > 8000;

    PImage outputVideo;
    if (starting) {
      textOverlay.startScreen(currentTime, true);
    } else {
      startFuzz();
      if (toFeature || currentlyFeaturing || shouldBug || currentlyBugging) {
        outputVideo = liveVideo;
        if (toFeature || currentlyFeaturing) {
          //featuring
          outputVideo = feature.execute(liveVideo, bodies, kinect);
          currentlyFeaturing = feature.isCurrentlyFeaturing();
          timeOfLastFeature = System.currentTimeMillis();
        }
        if (shouldBug || currentlyBugging) {
          //bugging
          outputVideo = bug.execute(outputVideo, bodies, kinect);
          currentlyBugging = bug.isCurrentlyBugging();
        }
      } else {
        //basing
        outputVideo = base.executeBase(liveVideo, bodies, kinect);
      }
      image(outputVideo, LEFT_DISPLAY_OFFSET, 0);
      textOverlay.displayBodyCountOverlay(kinect.getBodyTrackUser().size());
      textOverlay.info(currentTime, kinect);
    }
  }

  // -------------------------------------------------------------------------------------------------------------------

  private void setUpSounds() {
    String softFuzzSound = "F:/SkyDrive/Work/NEoN/sensor/resources/audio/vhs.wav";
    softFuzz = new SoundFile(this, softFuzzSound);
    softFuzz.loop();
    softFuzz.amp(0.2f); //volume
 }

  private void startFuzz() {
    if (!softFuzz.isPlaying()) {
      softFuzz.loop();
    }
  }

  private void stopFuzz() {
    if (softFuzz.isPlaying()) {
      softFuzz.stop();
    }
  }

  private void setUpKinect(Sensor sensor) {
    kinect = new KinectPV2(sensor);
    kinect.enableColorImg(true);
    kinect.enableBodyTrackImg(true);
    kinect.enableDepthImg(true);
    kinect.enableInfraredImg(true);
    kinect.init();
  }

  public void mousePressed() {
    if (mouseButton == RIGHT) {
      exit();
    }
  }

}