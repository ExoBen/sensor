package org.toby.sensor;

import KinectPV2.*;
import processing.core.PApplet;
import processing.core.PImage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static KinectPV2.SkeletonProperties.*;
import static java.lang.Math.floor;
import static org.toby.sensor.UtilitiesAndConstants.*;

class TextOverlay {

  private PApplet parent;
  private Random rand;

  TextOverlay(PApplet p) {
    parent = p;
    rand = new Random();
  }

  void info(long currentTime) {
    // time
    parent.textSize(48); // medium
    parent.fill(255); //white
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy\nHH:mm:ss");
    String dateTime = sdf.format(new Date());
    parent.text(dateTime, SET_WIDTH-270, SET_HEIGHT+HEIGHT_CUT-85);

    // flashing red recording
    parent.text("REC", 50, HEIGHT_CUT+70);
    if ((int)floor(currentTime/1000) % 2 == 0) {
      parent.fill(255, 0, 0); // red
      parent.stroke(255, 0, 0); // red
      parent.strokeWeight(1f);
      parent.rect(150, HEIGHT_CUT+39, 16, 24);
      parent.rect(146, HEIGHT_CUT+43, 24, 16);
    }
  }

  void displayBodyCountOverlay(int bodies, int phase) {
    parent.textSize(48); // medium text
    parent.fill(255); // white
    parent.text(bodies, SET_WIDTH-70, HEIGHT_CUT+70);
    parent.text(phase, SET_WIDTH-70, HEIGHT_CUT+140);
  }

  void addFaceText(KinectPV2 kinect) {
    ArrayList<KSkeleton> skeletonArray =  kinect.getSkeletonDepthMap();
    ArrayList<PImage> bodyTrackList = kinect.getBodyTrackUser();
    ArrayList<ArrayList<String>> stats = new ArrayList<>();
    for (PImage pImage : bodyTrackList) {
      int[] pixels = pImage.pixels;
      long color = 0;
      for (int j = 0; j < pixels.length; j = j + 100) {
        int pixel = pixels[j];
        if (pixel != 0) {
          color = pixel;
          break;
        }
      }
      ArrayList<String> stat = new ArrayList<>();
      stat.add(String.valueOf(color % 4 + 1));
      stat.add(String.valueOf(color / 57 % 101));
      stat.add(String.valueOf((color / 43 % 121)));
      stat.add(String.valueOf((color / 23 % 201) - 100));
      stats.add(stat);
    }
    for (int k = 0; k < skeletonArray.size(); k++) {
      KSkeleton skeleton = skeletonArray.get(k);
      KJoint head = skeleton.getJoints()[JointType_ShoulderRight];
      float headX = head.getX() * 3.56f;
      float headY = head.getY() * 3.56f;
      parent.textSize(16); // medium text

      if (k+1 > stats.size()) {
        break;
      }
      try {
        ArrayList<String> stat = stats.get(k);

        int classText = Integer.valueOf(stat.get(0));
        int deception = Integer.valueOf(stat.get(1)) + rand.nextInt(5) - 2;
        int opChange = 0;
        if (rand.nextInt(10) == 0) {
          opChange = 1;
        }
        int opposition = Integer.valueOf(stat.get(2)) + opChange;
        int convChange = 0;
        if (rand.nextInt(5) == 0) {
          convChange = -1;
        }
        int convergence = Integer.valueOf(stat.get(3)) + convChange;

        parent.text(
          "Class: " + classText + "\n" +
            "Deception: " + deception + "%\n" +
            "Opposition: " + opposition + "%\n" +
            "Convergence: " + convergence + "%",
          headX + (1950 - headX) / 9, headY - 250);
      } catch (Exception e) {
        break;
      }
    }
  }

}
