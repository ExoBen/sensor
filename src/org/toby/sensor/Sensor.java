package org.toby.sensor;

import KinectPV2.*;
import gab.opencv.*;
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
  private OpenCV openCV;
  private long timeBegin;
  private long timeOfLastFeature;

  private Random rand;
  private SoundFile softFuzz;
  private boolean starting;
  private Integer phase = 0;

  private int phaseOneLength = 35000;
  //  private int phaseTwoLength = 215000;
  private int phaseTwoLength = 65000;
  //  private int phaseThreeLength = 275000;
  private int phaseThreeLength = 95000;
//   private int phaseFourLength = 125000;
  private int phaseFourLength = 125000;

  public static void main(String[] args) {
    PApplet.main("org.toby.sensor.Sensor");
  }

  public void settings() {
    fullScreen();
  }

  public void setup() {
    rand = new Random();
    base = new BaseLoader(phase);
    feature = new FeatureLoader(this);
    bug = new BugLoader(this);
    textOverlay = new TextOverlay(this);
    timeBegin = System.currentTimeMillis();
    timeOfLastFeature = timeBegin;
    openCV = new OpenCV(this, 512, 424);
    setUpKinect(this);
    setUpSounds();
    textFont(createFont("F:/SkyDrive/Work/NEoN/sensor/resources/vcr.ttf", 48));
    background(0);
  }

  // -------------------------------------------------------------------------------------------------------------------

  public void draw() {
    long currentTime = System.currentTimeMillis() - timeBegin;
    PImage bodies = ImageManipulation.cropper(
        ImageManipulation.upscaler(kinect.getBodyTrackImage().get(16, 13, KINECT_WIDTH, KINECT_HEIGHT), KINECT_WIDTH * KINECT_HEIGHT)
    );
    bodies.filter(THRESHOLD);
    PImage liveVideo = kinect.getColorImage().get(0, HEIGHT_CUT, SET_WIDTH, SET_HEIGHT);
    liveVideo.filter(GRAY);

    boolean shouldBug;
    long timeSinceLastFeature;
    boolean toFeature;

    if (System.currentTimeMillis() - timeBegin < phaseOneLength) {
      phase = 1;
      shouldBug = false;
      toFeature = false;
    } else if (System.currentTimeMillis() - timeBegin < phaseTwoLength) {
      phase = 2;
      shouldBug = rand.nextInt(200) == 0;
      timeSinceLastFeature = System.currentTimeMillis() - timeOfLastFeature;
      toFeature = (timeSinceLastFeature > 10000 && rand.nextInt(100) == 0) || timeSinceLastFeature > 14000;
    } else if (System.currentTimeMillis() - timeBegin < phaseThreeLength) {
      phase = 3;
      shouldBug = rand.nextInt(70) == 0;
      timeSinceLastFeature = System.currentTimeMillis() - timeOfLastFeature;
      toFeature = (timeSinceLastFeature > 4000 && rand.nextInt(250) == 0) || timeSinceLastFeature > 9000;
    } else {
      phase = 4;
      shouldBug = rand.nextInt(40) == 0;
      timeSinceLastFeature = System.currentTimeMillis() - timeOfLastFeature;
      toFeature = (timeSinceLastFeature > 4000 && rand.nextInt(250) == 0) || timeSinceLastFeature > 7000;
    }

    PImage outputVideo;
    if (starting) {
      textOverlay.startScreen(currentTime, true);
    } else {
      startFuzz();
      if (toFeature || currentlyFeaturing || shouldBug || currentlyBugging) {
        outputVideo = liveVideo;
        if (toFeature || currentlyFeaturing) {
          //featuring
          outputVideo = feature.execute(liveVideo, bodies, kinect, openCV);
          currentlyFeaturing = feature.isCurrentlyFeaturing();
          timeOfLastFeature = System.currentTimeMillis();
        }
        if (shouldBug || currentlyBugging) {
          //bugging
          outputVideo = bug.execute(outputVideo, bodies, kinect, openCV);
          currentlyBugging = bug.isCurrentlyBugging();
        }
        image(outputVideo, 0, 180);
      } else {
        //basing
        if (phase < 4) {
          image(liveVideo, 0, 180);
        }
        base.execute(liveVideo, bodies, kinect, openCV, this);
      }
      borders();
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


  private void borders() {
    this.fill(0);
    this.stroke(0);
    this.rect(0, 0, SET_WIDTH, HEIGHT_CUT);
    this.rect(0, SET_HEIGHT+HEIGHT_CUT, SET_WIDTH, 1080);
  }

  private void setUpKinect(Sensor sensor) {
    kinect = new KinectPV2(sensor);
    kinect.enableColorImg(true);
    kinect.enableBodyTrackImg(true);
    kinect.enableDepthImg(true);
    kinect.enableInfraredImg(true);
    kinect.enablePointCloud(true);
    kinect.init();
    kinect.setLowThresholdPC(100);
    kinect.setHighThresholdPC(2700);
  }

  public void mousePressed() {
    if (mouseButton == RIGHT) {
      exit();
    }
  }

}