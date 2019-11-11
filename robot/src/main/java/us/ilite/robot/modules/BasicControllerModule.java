package us.ilite.robot.modules;

import java.util.HashMap;
import java.util.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.flybotix.hfr.codex.Codex;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import us.ilite.common.config.SystemSettings;
import us.ilite.common.types.input.ELogitech310;

import javax.naming.ldap.Control;

public class BasicControllerModule extends Module {

    private final Codex<Double, ELogitech310> mController;
    private final Map<ELogitech310, TalonSRX> mSRXs = new HashMap<>();
    private BasicArcadeDrive mDrive;
    private Joystick mDriverJoystick;
    private Joystick mOperatorJoystick;

    private final Map<ELogitech310, Solenoid> mPneumaticButtons = new HashMap<>();
    private final Map<ELogitech310, Boolean> mPneumaticStates = new HashMap<>();
    private final Map<ELogitech310, Boolean> mButtonStates = new HashMap<>();

    public BasicControllerModule(Codex<Double, ELogitech310> pController, BasicArcadeDrive pDrive) {
        mDriverJoystick = new Joystick(0);
        mDriverJoystick.setThrottleChannel(1);
        mDriverJoystick.setTwistChannel(4);
        mOperatorJoystick = new Joystick(1);

        mDrive = pDrive;
        mController = pController;
        mSRXs.put(ELogitech310.LEFT_X_AXIS, new TalonSRX(SystemSettings.kDriveLeftMasterTalonId));
        mSRXs.put(ELogitech310.RIGHT_X_AXIS, new TalonSRX(SystemSettings.kDriveRightMasterTalonId));

        mPneumaticButtons.put(ELogitech310.L_BTN, new Solenoid(0));
        mPneumaticStates.put(ELogitech310.L_BTN, false);
        mButtonStates.put(ELogitech310.L_BTN, false);
        mPneumaticButtons.put(ELogitech310.R_BTN, new Solenoid(3));
        mPneumaticStates.put(ELogitech310.R_BTN, false);
        mButtonStates.put(ELogitech310.R_BTN, false);
        
        mPneumaticButtons.put(ELogitech310.START, new Solenoid(1));
        mPneumaticStates.put(ELogitech310.START, false);
        mButtonStates.put(ELogitech310.START, false);

        mPneumaticButtons.put(ELogitech310.A_BTN, new Solenoid(2));
        mPneumaticStates.put(ELogitech310.A_BTN, false);
        mButtonStates.put(ELogitech310.A_BTN, false);
    }

    @Override
    public void modeInit(double pNow) {

    }

    @Override
    public void periodicInput(double pNow) {

    }

    @Override
    public void update(double pNow) {
        updateDriveTrain();
    }

    public void updateDriveTrain() {
        DriveMessage mDriveMessage = DriveMessage.fromThrottleAndTurn(mDriverJoystick.getThrottle(), mDriverJoystick.getTwist());
        mDriveMessage.setNeutralMode(NeutralMode.Brake);
        mDriveMessage.setControlMode(ControlMode.PercentOutput);
        mDrive.setDriveMessage(mDriveMessage);
    }

    public void updatePneumatics() {

    }

    @Override
    public void shutdown(double pNow) {

    }

}