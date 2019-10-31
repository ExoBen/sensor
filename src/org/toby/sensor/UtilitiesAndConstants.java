package org.toby.sensor;

import processing.core.PImage;

public class UtilitiesAndConstants {

  public static final int BLACK = -16777216;
  public static final int WHITE = -1;
  public static final int SET_WIDTH = 1920;
  public static final int SET_HEIGHT = 720;
  public static final int HEIGHT_CUT = 180;

  public static final int KINECT_WIDTH = 480;
  public static final int KINECT_HEIGHT = 398;

  public UtilitiesAndConstants() {}

  public static PImage twoTone(PImage body, int foreground, int background) {
    PImage mask = new PImage(body.width, body.height);
    int bodyUpscaledSize = (body.width * body.height);
    for (int i = 0; i < bodyUpscaledSize; i++) {
      if (body.pixels[i] == BLACK) {
        mask.pixels[i] = foreground;
      } else {
        mask.pixels[i] = background;
      }
    }
    return mask;
  }

}
