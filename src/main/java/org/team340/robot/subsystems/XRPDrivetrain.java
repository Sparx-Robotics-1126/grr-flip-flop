// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team340.robot.subsystems;

import java.util.function.DoubleSupplier;

import org.team340.lib.util.command.GRRSubsystem;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.xrp.XRPMotor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class XRPDrivetrain extends GRRSubsystem {
  private static final double kGearRatio =
      (30.0 / 14.0) * (28.0 / 16.0) * (36.0 / 9.0) * (26.0 / 8.0); // 48.75:1
  private static final double kCountsPerMotorShaftRev = 12.0;
  private static final double kCountsPerRevolution = kCountsPerMotorShaftRev * kGearRatio; // 585.0
  private static final double kWheelDiameterInch = 2.3622; // 60 mm

  // The XRP has the left and right motors set to
  // channels 0 and 1 respectively
  private final XRPMotor m_leftMotor = new XRPMotor(0);
  private final XRPMotor m_rightMotor = new XRPMotor(1);

  // The XRP has onboard encoders that are hardcoded
  // to use DIO pins 4/5 and 6/7 for the left and right
  private final Encoder m_leftEncoder = new Encoder(4, 5);
  private final Encoder m_rightEncoder = new Encoder(6, 7);

  // Set up the differential drive controller
  private final DifferentialDrive m_diffDrive =
      new DifferentialDrive(m_leftMotor::set, m_rightMotor::set);

  /** Creates a new XRPDrivetrain. */
  public XRPDrivetrain() {
    // Use inches as unit for encoder distances
    m_leftEncoder.setDistancePerPulse((Math.PI * kWheelDiameterInch) / kCountsPerRevolution);
    m_rightEncoder.setDistancePerPulse((Math.PI * kWheelDiameterInch) / kCountsPerRevolution);
    resetEncoders();

    // Invert right side since motor is flipped
    m_rightMotor.setInverted(false);
  }



  public void arcadeDrive(double xaxisSpeed, double zaxisRotate) {

  SmartDashboard.putNumber("Drive Speed", xaxisSpeed);

  m_diffDrive.arcadeDrive(xaxisSpeed, zaxisRotate);

  }

//   public void arcadeDrive(double xaxisSpeed, double zaxisRotate, double distance) {
//     // m_diffDrive.arcadeDrive(xaxisSpeed, zaxisRotate);
// // Now with your changes:
//     // Positive xaxisSpeed (joystick pushed forward) = Forward motion
//     // Negative xaxisSpeed (joystick pulled back) = Backward motion

//     // Block forward motion when object is too close (≤ 2cm)
//     if (xaxisSpeed > 0 && distance > 0 && distance <= 2.0) {
//       xaxisSpeed = 0;
//       SmartDashboard.putBoolean("Forward Motion Blocked", true);
//   } else {
//       SmartDashboard.putBoolean("Forward Motion Blocked", false);
//   }

//   // Block backward motion when distance reading is valid (not -1)
//   // (assuming -1 means "safe to go backward")
//   if (xaxisSpeed < 0 && distance != -1) {
//       xaxisSpeed = 0;
//       SmartDashboard.putBoolean("Backward Motion Blocked", true);
//   } else {
//       SmartDashboard.putBoolean("Backward Motion Blocked", false);
//   }

//   SmartDashboard.putNumber("Drive Speed", xaxisSpeed);
//   SmartDashboard.putNumber("Distance Reading", distance);

//   m_diffDrive.arcadeDrive(xaxisSpeed, zaxisRotate);

//   }
public void tankDrive(double leftY, double rightY) {

  m_diffDrive.tankDrive(leftY, rightY);
}
  public void tankDrive(double leftY, double rightY, double distance) {
    // For clarity: 
    // Positive values = Forward motion
    // Negative values = Backward motion
    // distance == -1 means "safe to go backward" (no obstacle)
    // distance != -1 means "obstacle detected" (not safe to go backward)

    boolean leftTryingToGoBackward = (leftY < 0);
    boolean rightTryingToGoBackward = (rightY < 0);
    boolean safeToGoBackward = (distance == -1);

    // Block backward motion if it's not safe
    if (!safeToGoBackward) {
        // If not safe to go backward, zero out any backward motion attempts
        if (leftTryingToGoBackward) {
            leftY = 0;
        }
        if (rightTryingToGoBackward) {
            rightY = 0;
        }

        // Indicate that backward motion is blocked if either side was trying to go backward
        if (leftTryingToGoBackward || rightTryingToGoBackward) {
            SmartDashboard.putBoolean("Backward Motion Blocked", true);
        } else {
            SmartDashboard.putBoolean("Backward Motion Blocked", false);
        }
    } else {
        SmartDashboard.putBoolean("Backward Motion Blocked", false);
    }

    // Display debug information
    SmartDashboard.putNumber("Left Drive Speed", leftY);
    SmartDashboard.putNumber("Right Drive Speed", rightY);
    SmartDashboard.putNumber("Distance Reading", distance);

    m_diffDrive.tankDrive(leftY, rightY);
  }

  public void resetEncoders() {
    m_leftEncoder.reset();
    m_rightEncoder.reset();
  }

  public double getLeftDistanceInch() {
    return m_leftEncoder.getDistance();
  }

  public double getRightDistanceInch() {
    return m_rightEncoder.getDistance();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
