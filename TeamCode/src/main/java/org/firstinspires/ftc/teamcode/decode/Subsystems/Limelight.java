//package org.firstinspires.ftc.teamcode.decode.Subsystems;
//
//import com.qualcomm.hardware.limelightvision.LLResult;
//import com.qualcomm.hardware.limelightvision.LLResultTypes;
//import com.qualcomm.hardware.limelightvision.Limelight3A;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
//import org.firstinspires.ftc.teamcode.testing.AprilTagLimelight;
//
//import java.util.List;
//
//public class Limelight {
//        private Limelight3A limelight;
//        private HardwareMap hardwareMap;
//
//        private final int RED_GOAL_PIPELINE = 0;
//        private final int BLUE_GOAL_PIPELINE = 1;
//        private final int OBELISK_PIPELINE = 2;
//
//        public Limelight(HardwareMap hardwareMap) {
//            limelight = hardwareMap.get(Limelight3A.class, "limelight");
//
//            limelight.setPollRateHz(100);
//        }
//
//        public void setRedGoalPipeline() {
//            limelight.pipelineSwitch(RED_GOAL_PIPELINE);
//        }
//
//        public void setBlueGoalPipeline() {
//            limelight.pipelineSwitch(BLUE_GOAL_PIPELINE);
//        }
//
//        public void setObeliskPipeline() {
//            limelight.pipelineSwitch(OBELISK_PIPELINE);
//        }
//
//        public void start() {
//            limelight.start();
//        }
//
//        public boolean isConnected(){
//            return limelight.isConnected();
//        }
//
//        public double getYawTarget() {
//            LLResult result = limelight.getLatestResult();
//            if (result != null && result.isValid() && result.getStaleness() < 100) {
//                return result.getTx();
//            }
//
//            return 0;
//
//        }
//
//        public boolean isAlignedWithGoal() {
//            LLResult result = limelight.getLatestResult();
//            if (result != null && result.isValid() && result.getStaleness() < 100) {
//                return result.getTx() <= 3.0 && result.getTx() >= -3.0;
//            }
//
//            return false;
//        }
//
//        public boolean seesTarget() {
//            // checks to see if there are any AprilTags
//            return limelight.getLatestResult().getFiducialResults() != null;
//        }
//
//        public double getDistanceTarget(boolean red, Telemetry telemetry) {
//            LLResult result = limelight.getLatestResult();
//            if (result != null && result.isValid() && result.getStaleness() < 100) {
//                List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
//
//                for (LLResultTypes.FiducialResult fiducial : fiducials) {
//                    if (fiducial.getFiducialId() != (red ? 24 : 20))
//                        continue;
//                    Pose3D targetPose = fiducial.getCameraPoseTargetSpace();
//
//                    telemetry.addData("Pose X", targetPose.getPosition().x);
//                    telemetry.addData("Pose Y", targetPose.getPosition().y);
//                    telemetry.addData("Pose Z", targetPose.getPosition().z);
//
//                    return Math.sqrt(Math.pow(targetPose.getPosition().x, 2) + Math.pow(targetPose.getPosition().z, 2));
//                }
//            }
//            return 0;
//        }
//
//        public AprilTagLimelight getPoseEstimate(double heading) {
//            LLResult result = limelight.getLatestResult();
//            limelight.updateRobotOrientation(heading);
//
//            if (result != null && result.isValid()) {
//                Pose3D botpose_mt2 = result.getBotpose_MT2();
//                if (botpose_mt2 != null) {
//                    double x = botpose_mt2.getPosition().x;
//                    double y = botpose_mt2.getPosition().y;
//
//                    return new AprilTagLimelight();
//                }
//            }
//
//            return null;
//        }
//    }
//
