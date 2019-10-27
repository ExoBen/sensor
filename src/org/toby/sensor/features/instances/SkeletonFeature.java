package org.toby.sensor.features.instances;

import KinectPV2.*;
import org.toby.sensor.features.AbstractFeature;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

public class SkeletonFeature extends AbstractFeature {

  private PApplet parent;

  public SkeletonFeature (PApplet p) {
    parent = p;
  }

  public PImage executeFeature(PImage liveVideo, PImage body, KinectPV2 kinect) {
    parent.fill(255); //white
    parent.stroke(255); // white
    parent.strokeWeight(20);
    ArrayList<KSkeleton> skeletonArray =  kinect.getSkeletonDepthMap();
    parent.image(new PImage(1920, 720), 0, 180);
    for (KSkeleton skeleton : skeletonArray) {
      drawBody(skeleton.getJoints());
    }
    return new PImage(1920, 720);
  }

  //DRAW BODY
  void drawBody(KJoint[] joints) {
    drawBone(joints, KinectPV2.JointType_Head, KinectPV2.JointType_Neck);
    drawBone(joints, KinectPV2.JointType_Neck, KinectPV2.JointType_SpineShoulder);
    drawBone(joints, KinectPV2.JointType_SpineShoulder, KinectPV2.JointType_SpineMid);
    drawBone(joints, KinectPV2.JointType_SpineMid, KinectPV2.JointType_SpineBase);
    drawBone(joints, KinectPV2.JointType_SpineShoulder, KinectPV2.JointType_ShoulderRight);
    drawBone(joints, KinectPV2.JointType_SpineShoulder, KinectPV2.JointType_ShoulderLeft);
    drawBone(joints, KinectPV2.JointType_SpineBase, KinectPV2.JointType_HipRight);
    drawBone(joints, KinectPV2.JointType_SpineBase, KinectPV2.JointType_HipLeft);

    // Right Arm
    drawBone(joints, KinectPV2.JointType_ShoulderRight, KinectPV2.JointType_ElbowRight);
    drawBone(joints, KinectPV2.JointType_ElbowRight, KinectPV2.JointType_WristRight);
    drawBone(joints, KinectPV2.JointType_WristRight, KinectPV2.JointType_HandRight);
    drawBone(joints, KinectPV2.JointType_HandRight, KinectPV2.JointType_HandTipRight);
    drawBone(joints, KinectPV2.JointType_WristRight, KinectPV2.JointType_ThumbRight);

    // Left Arm
    drawBone(joints, KinectPV2.JointType_ShoulderLeft, KinectPV2.JointType_ElbowLeft);
    drawBone(joints, KinectPV2.JointType_ElbowLeft, KinectPV2.JointType_WristLeft);
    drawBone(joints, KinectPV2.JointType_WristLeft, KinectPV2.JointType_HandLeft);
    drawBone(joints, KinectPV2.JointType_HandLeft, KinectPV2.JointType_HandTipLeft);
    drawBone(joints, KinectPV2.JointType_WristLeft, KinectPV2.JointType_ThumbLeft);

    // Right Leg
    drawBone(joints, KinectPV2.JointType_HipRight, KinectPV2.JointType_KneeRight);
    drawBone(joints, KinectPV2.JointType_KneeRight, KinectPV2.JointType_AnkleRight);
    drawBone(joints, KinectPV2.JointType_AnkleRight, KinectPV2.JointType_FootRight);

    // Left Leg
    drawBone(joints, KinectPV2.JointType_HipLeft, KinectPV2.JointType_KneeLeft);
    drawBone(joints, KinectPV2.JointType_KneeLeft, KinectPV2.JointType_AnkleLeft);
    drawBone(joints, KinectPV2.JointType_AnkleLeft, KinectPV2.JointType_FootLeft);

    drawJoint(joints, KinectPV2.JointType_HandTipLeft);
    drawJoint(joints, KinectPV2.JointType_HandTipRight);
    drawJoint(joints, KinectPV2.JointType_FootLeft);
    drawJoint(joints, KinectPV2.JointType_FootRight);

    drawJoint(joints, KinectPV2.JointType_ThumbLeft);
    drawJoint(joints, KinectPV2.JointType_ThumbRight);

    drawJoint(joints, KinectPV2.JointType_Head);
  }

  void drawBone(KJoint[] joints, int jointType1, int jointType2) {
    parent.pushMatrix();
    parent.translate(joints[jointType1].getX()*3.56f, joints[jointType1].getY()*2.55f);
    parent.strokeWeight(1);
    parent.ellipse(0, 0, 10, 10);
    parent.popMatrix();
    parent.strokeWeight(2);
    parent.line(joints[jointType1].getX()*3.56f, joints[jointType1].getY()*2.55f,
        joints[jointType2].getX()*3.56f, joints[jointType2].getY()*2.55f);
  }

  void drawJoint(KJoint[] joints, int jointType) {
    parent.pushMatrix();
    parent.translate(joints[jointType].getX()*3.56f, joints[jointType].getY()*2.55f);
    parent.strokeWeight(1);
    parent.ellipse(0, 0, 10, 10);
    parent.popMatrix();
  }

}
