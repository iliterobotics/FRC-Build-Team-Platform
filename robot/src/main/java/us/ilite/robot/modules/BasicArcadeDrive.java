package us.ilite.robot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.flybotix.hfr.codex.Codex;
import com.flybotix.hfr.util.log.ILog;
import com.flybotix.hfr.util.log.Logger;

import us.ilite.common.config.SystemSettings;
import us.ilite.common.lib.control.DriveController;
import us.ilite.common.types.input.EInputScale;
import us.ilite.common.types.input.ELogitech310;
import us.ilite.robot.Hardware;

/**
 * Class for running all drive train control operations from both autonomous and
 * driver-control.
 */
public class BasicArcadeDrive extends Module {
	private final ILog mLogger = Logger.createLog(BasicArcadeDrive.class);

	private final Codex<Double, ELogitech310> mController;

	private static final double MAX_POWER_OUTPUT = 0.5;
	private DriveMessage mDriveMessage;
	private Hardware mHardware;

	public BasicArcadeDrive(Codex<Double, ELogitech310> pController) {
		mController = pController;
        mHardware = new Hardware();
		
	}

	@Override
	public void modeInit(double pNow) {
	    setDriveMessage(DriveMessage.kNeutral);
	}

	@Override
	public void periodicInput(double pNow) {
	}

	@Override
	public void update(double pNow) {
        mHardware.setDriveMessage(mDriveMessage);
	}

    public void setDriveMessage(DriveMessage pDriveMessage) {
        double left = clamp(pDriveMessage.leftOutput, MAX_POWER_OUTPUT);
        double right = clamp(pDriveMessage.rightOutput, MAX_POWER_OUTPUT);
        mDriveMessage = new DriveMessage(left, right);
    }

    private static double clamp(double pValue, double pClamp) {
        return Math.min(Math.max(pValue, -pClamp), pClamp);
    }

    @Override
	public void shutdown(double pNow) {
	}


}
	
	



	



  

	