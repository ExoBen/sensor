package animation;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

import static org.toby.sensor.UtilitiesAndConstants.loadIntros;

public class Intro {

  private ArrayList<PImage> introImages;
  private int currentImage = 0;
  private long timeOfLast;

  private boolean introComplete = false;

  public Intro(PApplet parent) {
    introImages = loadIntros(parent);
    timeOfLast = System.currentTimeMillis();
  }

  public PImage playIntro() {
    long now = System.currentTimeMillis();
    if (currentImage == 0 || currentImage == 1) {
      // time of loading
      if (now - timeOfLast > 5000) {
        currentImage = 2;
        timeOfLast = now;
      } else if ((now - timeOfLast)/700 % 2 == 1) { // time of flash
        currentImage = 1;
      } else if ((now - timeOfLast)/700 % 2 == 0) { // time of flash
        currentImage = 0;
      }
    } else if (currentImage == 25) {
      // time of end image
      if (now - timeOfLast > 3000) {
        currentImage++;
      }
    } else {
      // time between line changes
      if (now - timeOfLast > 80) {
        currentImage++;
        timeOfLast = now;
      }
    }
    if (currentImage == 26) {
      currentImage--;
      introComplete = true;
    }
    return introImages.get(currentImage);
  }

  public boolean isIntroComplete() {
    return introComplete;
  }
}
