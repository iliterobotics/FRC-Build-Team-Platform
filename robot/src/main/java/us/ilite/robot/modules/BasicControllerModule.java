package us.ilite.robot.modules;

import java.util.HashMap;
import java.util.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.flybotix.hfr.codex.Codex;

import edu.wpi.first.wpilibj.Solenoid;
import us.ilite.common.config.SystemSettings;
import us.ilite.common.types.input.ELogitech310;

public class BasicControllerModule extends Module {

    private final Codex<Double, ELogitech310> mController;

    private final Map<ELogitech310, TalonSRX[]> mSRXs = new HashMap<>();

    private final Map<ELogitech310, Solenoid> mPneumaticButtons = new HashMap<>();

    public BasicControllerModule(Codex<Double, ELogitech310> pController) {
        mController = pController;
        mSRXs.put(ELogitech310.B_BTN, new TalonSRX[]{
            new TalonSRX(SystemSettings.sBTalonMasterId),
            new TalonSRX(SystemSettings.sBTalonFollowerId)
        });
        mSRXs.put(ELogitech310.X_BTN, new TalonSRX[]{
            new TalonSRX(SystemSettings.sXTalonMasterId),
            new TalonSRX(SystemSettings.sXTalonFollowerId)
        });
        mSRXs.put(ELogitech310.Y_BTN, new TalonSRX[]{
            new TalonSRX(SystemSettings.sYTalonMasterId),
            new TalonSRX(SystemSettings.sYTalonFollowerId)
        });

        mPneumaticButtons.put(ELogitech310.L_BTN, new Solenoid(0));
        mPneumaticButtons.put(ELogitech310.START, new Solenoid(1));
        mPneumaticButtons.put(ELogitech310.A_BTN, new Solenoid(2));
        mPneumaticButtons.put(ELogitech310.R_BTN, new Solenoid(3));
    }

    @Override
    public void modeInit(double pNow) {

    }

    @Override
    public void periodicInput(double pNow) {

    }

    @Override
    public void update(double pNow) {
        
		double throttle1 = -mController.get(ELogitech310.LEFT_TRIGGER_AXIS);
		double throttle2 = mController.get(ELogitech310.RIGHT_TRIGGER_AXIS);
        double throttle = throttle1 + throttle2;
        
        for(ELogitech310 btn : mSRXs.keySet()) {
            if(mController.isSet(btn)) {
                for(TalonSRX ctrl : mSRXs.get(btn)) {
                    ctrl.set(ControlMode.PercentOutput, throttle);
                }
            }
        }

        for(ELogitech310 btn : mPneumaticButtons.keySet()) {
            mPneumaticButtons.get(btn).set(mController.isSet(btn));
        }
    }

    @Override
    public void shutdown(double pNow) {

    }

}