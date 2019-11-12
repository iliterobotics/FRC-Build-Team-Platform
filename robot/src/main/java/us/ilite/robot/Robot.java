package us.ilite.robot;

import java.net.SocketException;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.flybotix.hfr.codex.Codex;
import com.flybotix.hfr.util.log.ELevel;
import com.flybotix.hfr.util.log.ILog;
import com.flybotix.hfr.util.log.Logger;

import edu.wpi.first.networktables.ConnectionInfo;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.*;
import us.ilite.common.config.SystemSettings;
import us.ilite.common.io.Network;
import us.ilite.common.types.input.ELogitech310;
import us.ilite.lib.drivers.Clock;
import us.ilite.robot.modules.BasicArcadeDrive;
import us.ilite.robot.modules.BasicControllerModule;
import us.ilite.robot.modules.ModuleList;

public class Robot extends TimedRobot {

    private ILog mLogger = Logger.createLog(this.getClass());

    // It sure would be convenient if we could reduce this to just a
    // LoopManager...Will have to test timing of Codex first
    private ModuleList mRunningModules = new ModuleList();

    private Clock mSystemClock = new Clock();

    public Codex<Double, ELogitech310> mController = Codex.of.thisEnum(ELogitech310.class);
    private Compressor compressor = new Compressor();

    private Hardware mHardware;
    private Joystick mDriverJoystick;
    private BasicArcadeDrive mDriveTrain;
    private BasicControllerModule mControlModule;

    @Override
    public void robotInit() {

        // Init the actual robot
        Logger.setLevel(ELevel.DEBUG);
        mLogger.info("Starting Robot Initialization...");

        Network.getInstance();
        try {
            us.ilite.robot.Network.getInstance().printConnections();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        NetworkTableInstance.getDefault().getTable("TEST_TABLE");
        DriverStation ds;
        mHardware = new Hardware();

        mDriveTrain = new BasicArcadeDrive(mController);
        mControlModule = new BasicControllerModule(mController, mDriveTrain);

        mRunningModules.setModules();

        mDriverJoystick = new Joystick(0);

        compressor.start();

        ConnectionInfo[] connections = NetworkTableInstance.getDefault().getConnections();
        if(connections == null || connections.length == 0) {
            mLogger.warn("No connections found!");
        }
        for(ConnectionInfo connection : NetworkTableInstance.getDefault().getConnections()){
            System.out.println(connection);
            mLogger.info(connection.remote_ip + " : " + connection.remote_port);
        }
        mLogger.info("Finished Robot Initialization...");
    }

    /**
     * This contains code run in ALL robot modes.
     * It's also important to note that this runs AFTER mode-specific code
     */
    @Override
    public void robotPeriodic() {
        mSystemClock.cycleEnded();
    }

    @Override
    public void autonomousInit() {
        mRunningModules.setModules(mControlModule, mDriveTrain);
        mRunningModules.modeInit(mSystemClock.getCurrentTime());
        mRunningModules.periodicInput(mSystemClock.getCurrentTime());
    }

    @Override
    public void autonomousPeriodic() {
        mRunningModules.periodicInput(mSystemClock.getCurrentTime());
        mRunningModules.update(mSystemClock.getCurrentTime());
    }

    @Override
    public void teleopInit() {
        mLogger.info("Starting Teleop Initialization...");
        mRunningModules.setModules(mDriveTrain, mControlModule);
        mRunningModules.modeInit(mSystemClock.getCurrentTime());
        mRunningModules.periodicInput(mSystemClock.getCurrentTime());
    }

    @Override
    public void teleopPeriodic() {

        ELogitech310.map(mController, mDriverJoystick);
//        mRunningModules.periodicInput(mSystemClock.getCurrentTime());
//        mRunningModules.update(mSystemClock.getCurrentTime());
        new TalonSRX(SystemSettings.kDriveRightMasterTalonId).set(ControlMode.PercentOutput, 0.5);
        new TalonSRX(SystemSettings.kDriveLeftMasterTalonId).set(ControlMode.PercentOutput, 0.5);
    }

    @Override
    public void disabledInit() {
        mLogger.info("Disabled Initialization");
        mRunningModules.shutdown(mSystemClock.getCurrentTime());
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void testInit() {
    }

    @Override
    public void testPeriodic() {
    }
}
