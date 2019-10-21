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
    parent.textSize(48); // medium
    parent.fill(255); //white
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy\nHH:mm:ss");
    String dateTime = sdf.format(new Date());
    parent.text(dateTime, SET_WIDTH-270, SET_HEIGHT+HEIGHT_CUT-85);

    // flashing red recording
    parent.text("REC", 50, HEIGHT_CUT+70);
    if ((int)floor(currentTime/1000) % 2 == 0) {
      parent.fill(255, 0, 0); // red
      parent.stroke(255, 0, 0); // white
      parent.rect(150, HEIGHT_CUT+39, 16, 24);
      parent.rect(146, HEIGHT_CUT+43, 24, 16);
    }
  }

  void displayBodyCountOverlay(int bodies) {
    parent.textSize(48); // medium text
    parent.fill(255); //white
    parent.text(bodies, SET_WIDTH-70, HEIGHT_CUT+70);
  }

  void startScreen(long currentTime, boolean starting) {
    if (starting) {
      parent.textSize(48); // medium text
      parent.fill(255); //white
      parent.text("STARTING", 50, HEIGHT_CUT+70);
    }

    parent.textSize(100); //big text
    parent.text("COME AND DIE", 387, 520);
  }

}
