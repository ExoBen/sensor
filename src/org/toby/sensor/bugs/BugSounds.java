package org.toby.sensor.bugs;

import org.toby.sensor.interfaces.SoundsInterface;
import processing.core.PApplet;
import processing.sound.SoundFile;

import java.util.ArrayList;
import java.util.Random;

public class BugSounds implements SoundsInterface {

  private Random rand;
  private PApplet parent;
  private ArrayList<SoundFile> sounds;

  BugSounds(PApplet p) {
    parent = p;
    rand = new Random();
    loadSounds();
  }

  private void loadSounds() {
    String bug1Sound = "C:/Users/toby5/OneDrive/Work/NEoN/sensor/resources/audio/bug1.wav";
    SoundFile bug1 = new SoundFile(parent, bug1Sound);
    bug1.amp(0.5f);
    String bug2Sound = "C:/Users/toby5/OneDrive/Work/NEoN/sensor/resources/audio/bug2.wav";
    SoundFile bug2 = new SoundFile(parent, bug2Sound);
    bug2.amp(0.5f);
    String bug3Sound = "C:/Users/toby5/OneDrive/Work/NEoN/sensor/resources/audio/bug3.wav";
    SoundFile bug3 = new SoundFile(parent, bug3Sound);
    bug3.amp(0.4f);
    String bug4Sound = "C:/Users/toby5/OneDrive/Work/NEoN/sensor/resources/audio/bug4.wav";
    SoundFile bug4 = new SoundFile(parent, bug4Sound);
    bug4.amp(0.5f);
    String bug5Sound = "C:/Users/toby5/OneDrive/Work/NEoN/sensor/resources/audio/bug5.wav";
    SoundFile bug5 = new SoundFile(parent, bug5Sound);
    bug5.amp(0.4f);

    sounds = new ArrayList<>();
    sounds.add(bug1);
    sounds.add(bug2);
    sounds.add(bug3);
    sounds.add(bug4);
    sounds.add(bug5);
  }

  public void playSound() {
    SoundFile currentSound = sounds.get(rand.nextInt(sounds.size()));
    currentSound.play();
  }

}
