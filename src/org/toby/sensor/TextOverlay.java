package org.toby.sensor;

import KinectPV2.*;
import processing.core.PApplet;
import processing.core.PImage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static KinectPV2.SkeletonProperties.*;
import static java.lang.Math.floor;
import static org.toby.sensor.UtilitiesAndConstants.*;

class TextOverlay {

  private PApplet parent;

  TextOverlay(PApplet p) {
    parent = p;
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
    for (int i = 0; i < bodyTrackList.size(); i=i+100) {
      int[] pixels = bodyTrackList.get(i).pixels;
      long color = 0;
      for (int pixel : pixels) {
        if (pixel != color) {
          color = pixel;
          break;
        }
      }
      String[] stats =  new String[]{
          String.valueOf(color%4+1),
          String.valueOf(color/57%101),
          String.valueOf((color/43%121)),
          String.valueOf((color/23%201)-100)
      };
      for (KSkeleton skeleton : skeletonArray) {
        KJoint head = skeleton.getJoints()[JointType_ShoulderRight];
        float headX = head.getX() * 3.56f;
        float headY = head.getY() * 3.56f;
        parent.textSize(16); // medium text
        parent.text(
            "Class: " + stats[0] + "\n" +
                "Deception: " + stats[1] + "%\n" +
                "Opposition: " + stats[2] + "%\n" +
                "Convergence: " + stats[3] + "%",
            headX + (1950 - headX) / 9, headY - 250);
      }
    }



  }

}
