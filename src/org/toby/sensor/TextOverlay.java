package org.toby.sensor;

import KinectPV2.KinectPV2;
import processing.core.PApplet;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Math.floor;
import static org.toby.sensor.UtilitiesAndConstants.*;

class TextOverlay {

  private PApplet parent;

  TextOverlay(PApplet p) {
    parent = p;
  }

  void info(long currentTime, KinectPV2 kinect) {
    // time
    parent.textSize(48);
    parent.fill(255);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy\nHH:mm:ss");
    String dateTime = sdf.format(new Date());
    parent.text(dateTime, RIGHT_DISPLAY_OFFSET - 270, 995);

    // flashing red recording
    parent.text("REC", LEFT_OFFSET + 50, 70);
    if ((int)floor(currentTime/1000) % 2 == 0) {
      parent.fill(255, 0, 0);
      parent.stroke(255, 0, 0);
      parent.rect(LEFT_OFFSET + 150, 39, 16, 24);
      parent.rect(LEFT_OFFSET + 146, 43, 24, 16);
    }

  }

  void displayBodyCountOverlay(int bodies) {
    System.out.println(bodies);
    parent.textSize(48);
    parent.fill(255);
    parent.text(bodies, RIGHT_DISPLAY_OFFSET -70, 70);
  }

  void startScreen(long currentTime, boolean starting) {
    if (starting) {
      parent.textSize(48);
      parent.fill(255);
      parent.text("STARTING", LEFT_OFFSET + 50, 70);
    }

    parent.textSize(100);
    parent.text("COME AND DIE", 387, 520);
  }

}
