package org.team340.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.xrp.XRPServo;
import edu.wpi.first.wpilibj2.command.Command;

import org.team340.lib.util.command.GRRSubsystem;

public class Flipper extends GRRSubsystem {
    private final XRPServo servo = new XRPServo(4);

    public Flipper() {
    }


    public void moveArm(double angle){
    if (angle >= 0 && angle <= 180) {
        servo.setAngle(angle);
    } else {
        // System.out.println("Angle out of range: Provide an angle between 0 and 180 degrees.");
    }
}


public void moveToAngle(double angle){
    servo.setAngle(angle);
}


    public double getAngle(){
        return servo.getAngle();
    }

    public double getPosition(){
        return servo.getPosition();
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Arm Angle", getAngle());
        SmartDashboard.putNumber("Arm Position", getAngle());
        super.periodic();
    }

public Command moverArm(double angle){
    return run(() -> moveArm(angle));
}


}
