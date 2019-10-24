package org.toby.sensor.features.instances;

import KinectPV2.KinectPV2;
import org.toby.sensor.ImageManipulation;
import org.toby.sensor.features.AbstractFeature;
import processing.core.PImage;

import static org.toby.sensor.UtilitiesAndConstants.*;

public class DepthImage extends AbstractFeature {

  public PImage executeFeature(PImage liveVideo, PImage body, KinectPV2 kinect) {
    return ImageManipulation.cropper(
        ImageManipulation.upscaler(kinect.getDepthImage().get(16, 13, KINECT_WIDTH, KINECT_HEIGHT), KINECT_WIDTH*KINECT_HEIGHT)
    );
  }

}
