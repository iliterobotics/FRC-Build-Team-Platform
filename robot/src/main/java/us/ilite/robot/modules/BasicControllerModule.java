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

    // HatchFlower mHatch;

    private final Map<ELogitech310, Solenoid> mPneumaticButtons = new HashMap<>();
    private final Map<ELogitech310, Boolean> mPneumaticStates = new HashMap<>();
    private final Map<ELogitech310, Boolean> mButtonStates = new HashMap<>();

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

        // mHatch =  new HatchFlower(mController);
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

            // start = pressed = 0, changed = 0
            boolean pressed = mController.isSet(btn);
            boolean changed = (pressed != mButtonStates.get(btn));

            if(pressed && changed) {
                mPneumaticStates.put(btn, !mPneumaticStates.get(btn));
            }

            mButtonStates.put(btn, pressed);

            // switch(btn) {
            //     case L_BTN:
            //         if(pressed && changed) {
            //             mHatch.captureHatch();
            //         }
            //     break;
            //     case R_BTN:
            //         if(pressed && changed) {
            //             mHatch.pushHatch();
            //         }
            //     break;
            //     default:
                    mPneumaticButtons.get(btn).set(mPneumaticStates.get(btn));
            // }
            
            // mPneumaticButtons.get(btn).set(pressed);
        }

    }

    @Override
    public void shutdown(double pNow) {

    }

}