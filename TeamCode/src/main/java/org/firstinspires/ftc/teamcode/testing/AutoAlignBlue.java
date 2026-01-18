//package org.firstinspires.ftc.teamcode.testing;
//
//import com.qualcomm.hardware.limelightvision.Limelight3A;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//
//public abstract class AutoAlignBlue extends LinearOpMode {
//    private Limelight3A limelight = new Limelight3A(hardwareMap);
//    private final boolean IS_RED = false;
//
//
//    public void runOpMode{
//        if (IS_RED) {
//            limelight.setRedGoalPipeline();
//        }
//        else {
//            limelight.setBlueGoalPipeline();
//        }
//
//        if (IS_RED) {
//            drivetrain.setStartingPose(RobotConstants.Auto.LAST_REMEMBERED_POSE);
//        }
//        else {
//            if (RobotConstants.Auto.LAST_REMEMBERED_POSE.getHeading() == 0) {
//                RobotConstants.Auto.LAST_REMEMBERED_POSE = RobotConstants.Auto.LAST_REMEMBERED_POSE.setHeading(Math.toRadians(180));
//            }
//            drivetrain.setStartingPose(RobotConstants.Auto.LAST_REMEMBERED_POSE);
//        }
//
//    }
//
//}
//
