package org.toby.sensor;

import KinectPV2.*;
import org.toby.sensor.animation.Intro;
import gab.opencv.*;
import org.toby.sensor.base.SilhouetteDrawing;
import org.toby.sensor.bugs.BugLoader;
import org.toby.sensor.features.FeatureLoader;
import org.toby.sensor.features.instances.SkeletonFeature;
import processing.core.PApplet;
import processing.core.PImage;
import processing.sound.*;

import java.util.ArrayList;
import java.util.Random;

import static org.toby.sensor.ImageLoader.*;
import static org.toby.sensor.UtilitiesAndConstants.*;

public class Sensor extends PApplet {

  private SilhouetteDrawing silhouette;
  private FeatureLoader feature;
  private boolean currentlyFeaturing = false;
  private BugLoader bug;
  private boolean currentlyBugging = false;

  private TextOverlay textOverlay;
  private Intro intro;
  private KinectPV2 kinect;
  private OpenCV openCV;
  private long timeBegin;
  private long introEndTime;
  private long timeOfLastFeature;

  private Random rand;
  private SoundFile softFuzz;
  private Integer phase;

  private ArrayList<PImage> standbys;
  private ArrayList<PImage> blues;
  private ArrayList<PImage> glitches;
  private int theChosenGlitch;
  private boolean seen = false;
  private boolean inIntro = true;
  private boolean inOutro = false;
  private PImage errorMessage;
  private boolean errorMessageToBeShown = false;
  private boolean errorMessageDone = false;
  private long outroStartTime;

  private static final int PHASE_ONE_LENGTH = 30000; // 30 seconds
  private static final int PHASE_TWO_LENGTH = 20000; // 20 seconds
  private static final int PHASE_THREE_LENGTH = 20000; // 20 seconds
  private static final int PHASE_FOUR_LENGTH = 120000; // 120 seconds
  private static final int PHASE_FIVE_LENGTH = 60000; // 60 seconds
  private static final int PHASE_SIX_LENGTH = 15000; // 15 seconds
  private static final int FREEZE_LENGTH = 2000; // 2 seconds
  private static final int BLUE_LENGTH = 10000; // 10 seconds
  private static final int BLACK_LENGTH = 10000; // 10 seconds

  public static void main(String[] args) {
    PApplet.main("org.toby.sensor.Sensor");
  }

  public void settings() {
    fullScreen();
  }

  public void setup() {
    background(0);
    rand = new Random();
    silhouette = new SilhouetteDrawing();
    feature = new FeatureLoader(this);
    bug = new BugLoader(this);
    textOverlay = new TextOverlay(this);
    timeBegin = System.currentTimeMillis();
    timeOfLastFeature = timeBegin;
    openCV = new OpenCV(this, 512, 424);
    intro = new Intro(this);
    standbys = loadStandby(this);
    blues = loadBlues(this);
    glitches = loadGlitches(this);
    theChosenGlitch = rand.nextInt(10);
    errorMessage = this.loadImage("C:/Users/toby5/OneDrive/Work/NEoN/sensor/resources/error/error1.png");
    setUpKinect(this);
    setUpSounds();
    textFont(createFont("C:/Users/toby5/OneDrive/Work/NEoN/sensor/resources/vcr.ttf", 48));
    background(0);
  }

  // -------------------------------------------------------------------------------------------------------------------

  public void draw() {
    if (inIntro) {
      if (!seen && kinect.getBodyTrackUser().size() > 0) {
        seen = true;
        intro.setTimeOfLast(System.currentTimeMillis());
      }
      if (!seen) {
        image(standbys.get((int) abs(System.currentTimeMillis() - timeBegin )/700 % 2), 0, HEIGHT_CUT);
        borders();
      } else {
        PImage introImage = intro.playIntro();
        inIntro = !intro.isIntroComplete();
        if (!inIntro) {
          introEndTime = System.currentTimeMillis();
          startFuzz();
        }
        image(introImage, 0, HEIGHT_CUT);
        borders();
      }
      return;
    } else if (inOutro) {
      long timeSinceOutroStart = System.currentTimeMillis() - outroStartTime;
      if (timeSinceOutroStart > BLACK_LENGTH + BLUE_LENGTH + FREEZE_LENGTH) {
        background(0);
        reset();
        return;
      } else if (timeSinceOutroStart > BLUE_LENGTH + FREEZE_LENGTH) {
        background(0);
        return;
      } else if (timeSinceOutroStart > FREEZE_LENGTH) {
        image(blues.get((int) abs(System.currentTimeMillis() - timeBegin) / 700 % 2), 0, HEIGHT_CUT);
        borders();
        return;
      } else {
        stopFuzz();
        image(glitches.get(theChosenGlitch), 0, HEIGHT_CUT);
        return;
      }
    }

    if (errorMessageToBeShown) {
      try {
        Thread.sleep(5000); // error time
      } catch (InterruptedException e) {
        return;
      } finally {
        errorMessageToBeShown = false;
      }
    }

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
    
    long timeSinceIntroEnd = System.currentTimeMillis() - introEndTime;

    if (timeSinceIntroEnd < PHASE_ONE_LENGTH) {
      phase = 1;
      shouldBug = false;
      toFeature = false;
    } else if (timeSinceIntroEnd < PHASE_ONE_LENGTH + PHASE_TWO_LENGTH) {
      phase = 2;
      shouldBug = rand.nextInt(200) == 0;
      timeSinceLastFeature = System.currentTimeMillis() - timeOfLastFeature;
      toFeature = (timeSinceLastFeature > 10000 && rand.nextInt(250) == 0) || timeSinceLastFeature > 15000;
    } else if (timeSinceIntroEnd < PHASE_ONE_LENGTH + PHASE_TWO_LENGTH + PHASE_THREE_LENGTH) {
      phase = 3;
      shouldBug = rand.nextInt(100) == 0;
      timeSinceLastFeature = System.currentTimeMillis() - timeOfLastFeature;
      toFeature = (timeSinceLastFeature > 5000 && rand.nextInt(150) == 0) || timeSinceLastFeature > 10000;
    } else if (timeSinceIntroEnd < PHASE_ONE_LENGTH + PHASE_TWO_LENGTH + PHASE_THREE_LENGTH + PHASE_FOUR_LENGTH) {
      phase = 4;
      shouldBug = rand.nextInt(200) == 0;
      timeSinceLastFeature = System.currentTimeMillis() - timeOfLastFeature;
      toFeature = (timeSinceLastFeature > 10000 && rand.nextInt(250) == 0) || timeSinceLastFeature > 15000;
    } else if (timeSinceIntroEnd < PHASE_ONE_LENGTH + PHASE_TWO_LENGTH + PHASE_THREE_LENGTH + PHASE_FOUR_LENGTH + PHASE_FIVE_LENGTH) {
      phase = 5;
      shouldBug = rand.nextInt(100) == 0;
      timeSinceLastFeature = System.currentTimeMillis() - timeOfLastFeature;
      toFeature = (timeSinceLastFeature > 5000 && rand.nextInt(150) == 0) || timeSinceLastFeature > 10000;
    } else if (timeSinceIntroEnd < PHASE_ONE_LENGTH + PHASE_TWO_LENGTH + PHASE_THREE_LENGTH + PHASE_FOUR_LENGTH + PHASE_FIVE_LENGTH + PHASE_SIX_LENGTH) {
      phase = 6;
      shouldBug = rand.nextInt(20) == 0;
      timeSinceLastFeature = System.currentTimeMillis() - timeOfLastFeature;
      toFeature = (timeSinceLastFeature > 1000 && rand.nextInt(100) == 0) || timeSinceLastFeature > 3000;
    } else {
      inOutro = true;
      outroStartTime = System.currentTimeMillis();
      return;
    }

    if (phase == 2 && !errorMessageDone) {
      image(errorMessage, 0, HEIGHT_CUT);
      errorMessageToBeShown = true;
      errorMessageDone = true;
      return;
    }

    PImage outputVideo;
    if (toFeature || currentlyFeaturing || shouldBug || currentlyBugging) {
      outputVideo = liveVideo;
      if (toFeature || currentlyFeaturing) {
        //featuring
        outputVideo = feature.execute(liveVideo, bodies, kinect, openCV, phase);
        currentlyFeaturing = feature.isCurrentlyFeaturing();
        timeOfLastFeature = System.currentTimeMillis();
      }
      if (shouldBug || currentlyBugging) {
        //bugging
        outputVideo = bug.execute(outputVideo, bodies, kinect, openCV, phase);
        currentlyBugging = bug.isCurrentlyBugging();
      }
      if (!(feature.isCurrentlyFeaturing() && feature.getCurrentFeature().equals(SkeletonFeature.class))) {
        image(outputVideo, 0, 180);
      }
    } else {
      //basing
      if (phase < 4) {
        image(liveVideo, 0, 180);
      }
      silhouette.execute(kinect, openCV, phase, this);
    }
    textOverlay.displayBodyCountOverlay(kinect.getBodyTrackUser().size(), phase);
    if (!currentlyFeaturing && !currentlyBugging) {
      textOverlay.addFaceText(kinect, phase);
    }
    textOverlay.info(currentTime);
    borders();
  }

  // -------------------------------------------------------------------------------------------------------------------

  private void setUpSounds() {
    String softFuzzSound = "C:/Users/toby5/OneDrive/Work/NEoN/sensor/resources/audio/vhs.wav";
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
    kinect.enableSkeletonDepthMap(true);
    kinect.init();
    kinect.setLowThresholdPC(100);
    kinect.setHighThresholdPC(4000);
  }

  private void reset() {
    inIntro = true;
    inOutro = false;
    seen = false;
    intro.resetCurrentImage();
    intro.setIntroComplete(false);
    timeBegin = System.currentTimeMillis();
    theChosenGlitch = rand.nextInt(10);
    errorMessageDone = false;
  }

  public void mousePressed() {
    if (mouseButton == RIGHT) {
      exit();
    }
  }

}
