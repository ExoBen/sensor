package org.toby.sensor.animation;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

import static org.toby.sensor.ImageLoader.loadIntros;

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
      if (now - timeOfLast > 2500) {
        currentImage = 2;
        timeOfLast = now;
      } else if ((now - timeOfLast)/700 % 2 == 1) { // time of flash
        currentImage = 1;
      } else if ((now - timeOfLast)/700 % 2 == 0) { // time of flash
        currentImage = 0;
      }
    } else if (currentImage == 25) {
      // time of pause on last code image
      if (now - timeOfLast > 3000) {
        currentImage++;
      }
    } else if (currentImage > 25) {
      currentImage++;
      timeOfLast = now;
    } else {
      // time between line changes
      if (now - timeOfLast > 80) {
        currentImage++;
        timeOfLast = now;
      }
    }
    if (currentImage == 50) {
      currentImage--;
      introComplete = true;
    }
    return introImages.get(currentImage);
  }

  public void setTimeOfLast(long timeOfLast) {
    this.timeOfLast = timeOfLast;
  }

  public void resetCurrentImage() {
    this.currentImage = 0;
  }

  public void setIntroComplete(boolean introComplete) {
    this.introComplete = introComplete;
  }

  public boolean isIntroComplete() {
    return introComplete;
  }
}
