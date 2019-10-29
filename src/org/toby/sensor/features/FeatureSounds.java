package org.toby.sensor.features;

import org.toby.sensor.interfaces.SoundsInterface;
import processing.core.PApplet;
import processing.sound.SoundFile;

class FeatureSounds implements SoundsInterface {

  private PApplet parent;
  private SoundFile feature1;


  FeatureSounds(PApplet p) {
    parent = p;
    loadSounds();
  }

  private void loadSounds() {
    String clickBuzzSound = "C:/Users/toby5/OneDrive/Work/NEoN/sensor/resources/audio/feature1.wav";
    feature1 = new SoundFile(parent, clickBuzzSound);
    feature1.amp(0.3f);
  }

  public void playSound() {
    feature1.play();
  }

}