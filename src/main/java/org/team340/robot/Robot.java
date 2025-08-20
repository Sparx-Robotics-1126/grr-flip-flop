package org.team340.robot;

import static edu.wpi.first.wpilibj2.command.Commands.*;


import edu.wpi.first.epilogue.Logged;
// import edu.wpi.first.epilogue.NotLogged;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Threads;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
// import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import org.team340.lib.util.DisableWatchdog;
import org.team340.lib.util.Profiler;
import org.team340.lib.util.Tunable;
// import org.team340.robot.commands.Autos;
// import org.team340.robot.commands.Routines;
import org.team340.robot.subsystems.Flipper;
import org.team340.robot.subsystems.XRPDrivetrain;

@Logged
public final class Robot extends TimedRobot {

    private final CommandScheduler scheduler = CommandScheduler.getInstance();


    // public final Routines routines;
    // public final Autos autos;

    private final CommandXboxController driveController;
      private final XRPDrivetrain m_xrpDrivetrain;
      private final Flipper m_flipper;
      private final boolean useTankDrive = false;

    public Robot() {
        DriverStation.silenceJoystickConnectionWarning(true);
        DisableWatchdog.in(scheduler, "m_watchdog");
        DisableWatchdog.in(this, "m_watchdog");

        // Configure logging
        DataLogManager.start();
        DriverStation.startDataLog(DataLogManager.getLog());
        // SignalLogger.enableAutoLogging(false);

        // Initialize subsystems
        m_xrpDrivetrain = new XRPDrivetrain();
        m_flipper = new Flipper();


        // Initialize compositions
        // routines = new Routines(this);
        // autos = new Autos(this);

        // Initialize controllers
        driveController = new CommandXboxController(0);

        // Create triggers
        // RobotModeTriggers.autonomous().whileTrue(autos.runSelectedAuto());

        configureButtonBindings();

        // Set thread priority
        waitSeconds(10.0)
            .until(DriverStation::isEnabled)
            .andThen(() -> Threads.setCurrentThreadPriority(true, 10))
            .schedule();
    }

  private void configureButtonBindings() {

    // arm.setDefaultCommand(new RunCommand(
    //   () -> arm.moveArm(drivController.getRightY()), arm
    // ));
    // arm.setDefaultCommand(new MoveArmWithJoystickCommand(arm, driveController.getHID()));
    driveController.x().whileTrue(m_flipper.moverArm(0) );
    driveController.y().whileTrue(m_flipper.moverArm(90) );
    driveController.b().whileTrue(m_flipper.moverArm(360) );

    if (useTankDrive){
    m_xrpDrivetrain.setDefaultCommand(
      new RunCommand(
          () -> m_xrpDrivetrain.tankDrive(
        driveController.getLeftY(), driveController.getRightY()), 
        m_xrpDrivetrain));
    }
    else{
      m_xrpDrivetrain.setDefaultCommand(
        new RunCommand(
            () -> m_xrpDrivetrain.arcadeDrive(
          driveController.getLeftY(), driveController.getLeftX()), 
          m_xrpDrivetrain));
    }
  }

    /**
     * Returns the current match time in seconds.
     */
    public double matchTime() {
        return Math.max(DriverStation.getMatchTime(), 0.0);
    }

    // @NotLogged
    // public double driverX() {
    //     return driver.getLeftX();
    // }

    // @NotLogged
    // public double driverY() {
    //     return driver.getLeftY();
    // }

    // @NotLogged
    // public double driverAngular() {
    //     return driver.getLeftTriggerAxis() - driver.getRightTriggerAxis();
    // }

    @Override
    public void robotPeriodic() {
        Profiler.start("robotPeriodic");
        Profiler.run("scheduler", scheduler::run);
        Profiler.run("tunables", Tunable::update);
        Profiler.end();
    }

    @Override
    public void simulationPeriodic() {}

    @Override
    public void disabledPeriodic() {}

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void teleopPeriodic() {}

    @Override
    public void testPeriodic() {}
}
