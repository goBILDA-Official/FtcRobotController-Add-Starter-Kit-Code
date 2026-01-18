package org.firstinspires.ftc.teamcode.decode.testing;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.teamcode.decode.Subsystems.C70;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Autonomous(name = "AprilTagBasic1", group = "TestBasic")
public class AprilTagBasic1 extends OpMode {
   private static final boolean USE_WEBCAM = true;  // Set true to use a webcam, or false for a phone camera
    private static final int DESIRED_TAG_ID = 21;     // Choose the tag you want to approach or set to -1 for ANY tag.
    //public C70 camera = new C70();
    private AprilTagProcessor aprilTag;
    private AprilTagDetection desiredTag;
    private VisionPortal visionPortal;
    private Follower follower;
    private Timer pathTimer, opmodeTimer;
    private int pathState;

    // A new variable to track if the target tag has been found
    private boolean targetFound = false;

    // --------- Step 2: Define poses ---------
    private final Pose startPose = new Pose(56.0, 8.0, Math.toRadians(90));   // robot start
    private final Pose endPose   = new Pose(56.0, 36.0, Math.toRadians(90));  // end point

    // --------- Step 3: Define paths ---------
    private Path simplePath;       // single BezierLine
    private PathChain simpleChain; // if you want a chain

    public void buildPaths() {
        // Straight line from start → end
        simplePath = new Path(new BezierLine(startPose, endPose));
        simplePath.setLinearHeadingInterpolation(startPose.getHeading(), endPose.getHeading());

        // Example PathChain version (optional)
        simpleChain = follower.pathBuilder()
                .addPath(new BezierLine(startPose, endPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), endPose.getHeading())
                .build();
    }

    // --------- Step 4: FSM (path state machine) ---------
    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // State 0: Wait for the desired AprilTag
                if (targetFound) {
                    telemetry.addData("Status", "Target Found! Starting path...");
                    // Once the tag is found, move to the next state to follow the path
                    follower.followPath(simplePath);
                    setPathState(1);
                } else {
                    telemetry.addData("Status", "Searching for AprilTag ID: " + DESIRED_TAG_ID);
                }
                break;

            case 1: // State 1: Follow the path
                // This state now only runs after the tag has been seen
                follower.followPath(simplePath);
                setPathState(2);
                break;

            case 2: // State 2: Check if path is finished
                if (!follower.isBusy()) {
                    // Finished path → stop
                    setPathState(-1);
                }
                break;
        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    private void initAprilTag() {
        // Create the AprilTag processor by using a builder.
        aprilTag = new AprilTagProcessor.Builder().build();

        aprilTag.setDecimation(2);

        // Create the vision portal by using a builder.
        if (USE_WEBCAM) {
            visionPortal = new VisionPortal.Builder()
                    .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                    .addProcessor(aprilTag)
                    .build();
        } else {
            visionPortal = new VisionPortal.Builder()
                    .setCamera(BuiltinCameraDirection.FRONT)
                    .addProcessor(aprilTag)
                    .build();
        }
    }
    private void setManualExposure(int exposureMS, int gain) {
        if (visionPortal == null) {
            return;
        }

        // Wait for the camera to be open
        while (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            // This loop will run in init() and wait for the camera.
        }

        ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
        if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
            exposureControl.setMode(ExposureControl.Mode.Manual);
        }
        exposureControl.setExposure((long)exposureMS, TimeUnit.MILLISECONDS);

        GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
        gainControl.setGain(gain);
    }


    // NOTE: The `sleep()` and `stop()` methods are not available in an OpMode.
    // I have removed them from this method. The check for camera streaming should
    // be done in a loop in init_loop().


    // --------- Step 5: OpMode lifecycle ---------
    @Override
    public void init() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        initAprilTag(); // initialize the AprilTag processor
        //camera.init(hardwareMap);

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        buildPaths();

        telemetry.addData("Status", "Initialized. Waiting for camera and start.");
        telemetry.update();
    }

    @Override
    public void init_loop() {
        // You can add camera status checks here if needed
        if (visionPortal.getCameraState() == VisionPortal.CameraState.STREAMING) {
            telemetry.addData("Camera", "Ready");
            // Set exposure once the camera is streaming.
            setManualExposure(6, 250);
        } else {
            telemetry.addData("Camera", "Waiting...");
        }
        telemetry.update();
    }
    @Override
    public void loop() {
        // ---- AprilTag Detection Logic ----


        targetFound = false;
        desiredTag  = null;

        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                if ((DESIRED_TAG_ID < 0) || (detection.id == DESIRED_TAG_ID)) {
                    targetFound = true;
                    desiredTag = detection;
                    telemetry.addData("Found", "ID %d (%s)", desiredTag.id, desiredTag.metadata.name);
                    telemetry.addData("Range",  "%5.1f inches", desiredTag.ftcPose.range);
                    break; // Found the tag, so exit the loop
                }
            }
        }
        // ---- End Detection Logic ----

//        telemetry.addData("Found", "ID %d (%s)", desiredTag.id, desiredTag.metadata.name);
//        telemetry.addData("Range",  "%5.1f inches", desiredTag.ftcPose.range);

        // Run the state machine
        autonomousPathUpdate();

        // Update follower and telemetry
        follower.update();
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.update();
    }

    @Override
    public void start() {
        opmodeTimer.resetTimer();
        // Start in state 0, which waits for the AprilTag
        setPathState(0);
    }

    @Override
    public void stop() {
        // Close the vision portal to free up resources
        if (visionPortal != null) {
            visionPortal.close();
        }
    }
}
