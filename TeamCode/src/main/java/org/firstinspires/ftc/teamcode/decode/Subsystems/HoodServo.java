package org.firstinspires.ftc.teamcode.decode.Subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class HoodServo {
    private Servo hoodservo;
    private CRServo CRservo;

    public void init(HardwareMap hardwareMap){
        //CRservo = hardwareMap.get(CRServo.class,"hoodServo");
       hoodservo = hardwareMap.get(Servo.class, "hoodServo");
    }
    public void setHoodservo(double angle){


       hoodservo.setPosition(angle);


    }

    public void CRServo(double power, boolean isForward){
//        if (isForward == true){// goes down
//            CRservo.setDirection(DcMotorSimple.Direction.FORWARD);
//            CRservo.setPower(power);
//        }
//        else if( isForward == false){ // goes up
//            CRservo.setDirection(DcMotorSimple.Direction.REVERSE);
//            CRservo.setPower(power);
//        }

    }

    public void CRStop(){
        //CRservo.setPower(0);
    }

    //public double getPower(){
//        double power = CRservo.getPower();
//        return power;
    //}
    public double getPosition(){
       double postion = hoodservo.getPosition();
        return postion;
    }
}
