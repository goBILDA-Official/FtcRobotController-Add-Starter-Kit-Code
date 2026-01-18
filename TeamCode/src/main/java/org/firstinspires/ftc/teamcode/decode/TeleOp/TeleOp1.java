package org.firstinspires.ftc.teamcode.decode.TeleOp;



import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.decode.Subsystems.HoodServo;

import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "TeleOp1 w/servo ")// WORKS
public class TeleOp1 extends LinearOpMode {
    private IMU imu;
    public double servoPower;

    public HoodServo servo = new HoodServo();

    @Override
    public void runOpMode() throws InterruptedException {

        servo.init(hardwareMap);
        DcMotor frontLeft = hardwareMap.dcMotor.get("frontLeftMotor");
        DcMotor backLeft = hardwareMap.dcMotor.get("backLeftMotor");
        DcMotor frontRight = hardwareMap.dcMotor.get("frontRightMotor");
        DcMotor backRight = hardwareMap.dcMotor.get("backRightMotor");
        DcMotor flyWheelMotor = hardwareMap.dcMotor.get("rightShooterMotor");
        DcMotor followerWheelMotor = hardwareMap.dcMotor.get("leftShooterMotor");
        DcMotor intake = hardwareMap.dcMotor.get("intakeMotor");
        DcMotor loader = hardwareMap.dcMotor.get("loaderMotor");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        // Retrieve the IMU from the hardware map
        imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.DOWN,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);

        double flyPower = 0;




        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            // --- mecanum Drive Control ---
            double y = -gamepad1.left_stick_y;    // forward/backward
            double x = gamepad1.left_stick_x; // left/right strafe with multiplier
            double rx = gamepad1.right_stick_x;   // rotation

            if (gamepad1.left_stick_button) {
                imu.resetYaw();
            }

            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            // Rotate the movement direction counter to the bot's rotation
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            rotX = rotX * 1.1;  // Counteract imperfect strafing

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            double frontLeftPower = (rotY + rotX + rx) / denominator;
            double backLeftPower = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower = (rotY + rotX - rx) / denominator;


            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);

            flyPower += gamepad1.right_trigger * 0.05;
            flyPower -= gamepad1.left_trigger * 0.05;
            flyPower = Math.max(0, Math.min(1, flyPower));
            double output = flyPower;
            flyWheelMotor.setPower(flyPower);
            followerWheelMotor.setPower(output);


            if (gamepad1.right_bumper) {


                intake.setPower(1);
            } else if (gamepad1.left_bumper) {


                intake.setPower(-1);
            } else {
                intake.setPower(0);
            }


            if (gamepad1.y) {
                loader.setDirection(DcMotorSimple.Direction.FORWARD);

                loader.setPower(1);
            } else if (gamepad1.a) {

                loader.setPower(-1);

            } else if (gamepad1.dpad_up) {
                loader.setPower(1);
                intake.setPower(1);
            } else {
                loader.setPower(0);
            }
            if (gamepad1.x) { // goes down
                servoPower += 0.5;
                servo.CRServo( servoPower, true); // Forward
            }
            else if (gamepad1.b) {// goes up
                servoPower -= 0.5;
                servo.CRServo( servoPower, false); // Reverse
            }
//            else if (gamepad1.dpad_down){
//                servo.CRServo(1, false);
//            }
            else{
                servo.CRStop();
            }

           // telemetry.addData("Servo Position", servo.getPower());


            telemetry.addData("Front Left Powe0r", frontLeft.getPower());
            telemetry.addData("Front Right Power", frontRight.getPower());
            telemetry.addData("Back Lft Power", backLeft.getPower());
            telemetry.addData("Back Right Power", backRight.getPower());
            telemetry.addData("intake Power", intake.getPower());
            telemetry.update();
        }
    }
}



