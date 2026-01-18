package org.firstinspires.ftc.teamcode.decode.TeleOp;

//import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
//import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcontroller.internal.FtcOpModeRegister;
import org.firstinspires.ftc.teamcode.decode.Subsystems.HoodServo;

@TeleOp
public class ServoTeleOp extends OpMode{

        //HoodServo servo = new HoodServo();
        public HoodServo servo = new HoodServo();
        @Override
        public void init(){
            servo.init(hardwareMap);


        }
        @Override
        public void loop(){

            if (gamepad1.right_trigger > 0.1) {
            servo.CRServo(gamepad1.right_trigger, true); // Forward
            }
            else if (gamepad1.left_trigger > 0.1) {
                servo.CRServo( gamepad1.left_trigger, false); // Reverse
            } else {
                servo.CRStop(); // Stop
            }

            //telemetry.addData("Servo Position", servo.getPower());

        }
}
