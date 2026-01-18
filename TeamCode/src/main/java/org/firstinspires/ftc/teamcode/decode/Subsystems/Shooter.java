package org.firstinspires.ftc.teamcode.decode.Subsystems;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Shooter {

    private MotorEx shooter;
    private MotorEx followerShooter;

    public double FAR_VELOCITY = 2600;
    public double CLOSE_VELOCITY = 2600;

    public Shooter(HardwareMap hardwareMap) {
        shooter = new MotorEx(hardwareMap, "leftShooterMotor", Motor.GoBILDA.BARE);
        followerShooter = new MotorEx(hardwareMap, "rightShooterMotor", Motor.GoBILDA.BARE);

        shooter.setZeroPowerBehavior(Motor.ZeroPowerBehavior.FLOAT);
        followerShooter.setZeroPowerBehavior(Motor.ZeroPowerBehavior.FLOAT);

        shooter.resetEncoder();
        followerShooter.resetEncoder();
    }

    public void shootArtifacts() {
        shooter.set(1);
        followerShooter.set(1);
    }


public void getVelocity(){
    shooter.getVelocity();
}
    public void velocityShooterFar(){
        shooter.setVelocity(FAR_VELOCITY);
        followerShooter.setVelocity(FAR_VELOCITY);

    }
    public void velocityShooterClose(){
        shooter.setVelocity(CLOSE_VELOCITY);
        followerShooter.setVelocity(CLOSE_VELOCITY);

    }


    public void stop() {
        shooter.set(0);
        followerShooter.set(0);
    }
}
