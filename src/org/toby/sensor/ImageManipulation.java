package org.toby.sensor;

import processing.core.PImage;

import static org.toby.sensor.UtilitiesAndConstants.*;

public class ImageManipulation {

  ImageManipulation() {}

  public static PImage upscaler(PImage image, int resolution) {
    PImage upscaled = new PImage(KINECT_WIDTH*4, KINECT_HEIGHT*4);
    upscaled.loadPixels();
    for (int i = 0; i < resolution; i++) {
      upscaled.pixels[i*4] = image.pixels[i];
      upscaled.pixels[i*4+1] = image.pixels[i];
      upscaled.pixels[i*4+2] = image.pixels[i];
      upscaled.pixels[i*4+3] = image.pixels[i];
    }
    for (int i = image.height-1; i >=0; i--) {
      PImage line = upscaled.get(0, i, SET_WIDTH, 1);
      upscaled.set(0, i*4, line);
      upscaled.set(0, i*4+1, line);
      upscaled.set(0, i*4+2, line);
      upscaled.set(0, i*4+3, line);
    }
    return upscaled;
  }

  public static PImage cropper(PImage image) {
    return image.get(0, (image.height-SET_HEIGHT)/2, SET_WIDTH, image.height - (image.height-SET_HEIGHT)/2);
  }

}
