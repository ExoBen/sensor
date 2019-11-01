package org.toby.sensor.features;

import org.toby.sensor.interfaces.SoundsInterface;
import processing.core.PApplet;
import processing.sound.SoundFile;

import java.util.ArrayList;
import java.util.Random;

class FeatureSounds implements SoundsInterface {

  private Random rand;
  private PApplet parent;
  private ArrayList<SoundFile> sounds;

  FeatureSounds(PApplet p) {
    parent = p;
    rand = new Random();
    loadSounds();
  }

  private void loadSounds() {
    String feature1Sound = "C:/Users/toby5/OneDrive/Work/NEoN/sensor/resources/audio/feature1.wav";
    SoundFile feature1 = new SoundFile(parent, feature1Sound);
    feature1.amp(0.3f);
    String feature2Sound = "C:/Users/toby5/OneDrive/Work/NEoN/sensor/resources/audio/feature2.wav";
    SoundFile feature2 = new SoundFile(parent, feature2Sound);
    feature2.amp(0.3f); //volume

    sounds = new ArrayList<>();
    sounds.add(feature1);
    sounds.add(feature2);
  }

  public void playSound() {
    SoundFile currentSound = sounds.get(rand.nextInt(sounds.size()));
    currentSound.play();
  }

}