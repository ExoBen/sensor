package org.toby.sensor;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

public class ImageLoader {

  public static ArrayList<PImage> loadStatics(PApplet parent) {
    ArrayList<PImage> statics = new ArrayList<>();
    for (int i = 1; i <= 25; i++) {
      String background = "C:/Users/toby5/OneDrive/Work/NEoN/sensor/resources/imagesOfStatic/static" + i + ".png";
      statics.add(parent.loadImage(background));
    }
    return statics;
  }

  public static ArrayList<PImage> loadIntros(PApplet parent) {
    ArrayList<PImage> statics = new ArrayList<>();
    for (int i = 0; i <= 50; i++) {
      String boots = "C:/Users/toby5/OneDrive/Work/NEoN/sensor/resources/boot/" + i + ".png";
      statics.add(parent.loadImage(boots));
    }
    return statics;
  }

  public static ArrayList<PImage> loadStandby(PApplet parent) {
    ArrayList<PImage> standbys = new ArrayList<>();
    standbys.add(parent.loadImage("C:/Users/toby5/OneDrive/Work/NEoN/sensor/resources/boot/standby1.png"));
    standbys.add(parent.loadImage("C:/Users/toby5/OneDrive/Work/NEoN/sensor/resources/boot/standby2.png"));
    return standbys;
  }

  public static ArrayList<PImage> loadBlues(PApplet parent) {
    ArrayList<PImage> blues = new ArrayList<>();
    blues.add(parent.loadImage("C:/Users/toby5/OneDrive/Work/NEoN/sensor/resources/outro/blue1.png"));
    blues.add(parent.loadImage("C:/Users/toby5/OneDrive/Work/NEoN/sensor/resources/outro/blue2.png"));
    return blues;
  }

  public static ArrayList<PImage> loadGlitches(PApplet parent) {
    ArrayList<PImage> glitches = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      glitches.add(parent.loadImage("C:/Users/toby5/OneDrive/Work/NEoN/sensor/resources/glitch/glitch"+i+".jpg"));
    }
    return glitches;
  }

}
