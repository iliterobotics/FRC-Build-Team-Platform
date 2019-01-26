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

/**
 * Class for running all drive train control operations from both autonomous and
 * driver-control.
 */
public class BasicArcadeDrive extends Module {
	private final ILog mLogger = Logger.createLog(BasicArcadeDrive.class);

	private final Codex<Double, ELogitech310> mController;

	private static final double MAX_POWER_OUTPUT = 0.5;

	private TalonSRX mLeftTalon = new TalonSRX(SystemSettings.kDriveLeftMasterTalonId);
	private TalonSRX mRightTalon = new TalonSRX(SystemSettings.kDriveRightMasterTalonId);

	public BasicArcadeDrive(Codex<Double, ELogitech310> pController) {
		mController = pController;

		
	}

	private static double clamp(double pValue, double pClamp) {
		return Math.min(Math.max(pValue, -pClamp), pClamp);
	}
	


	@Override
	public void modeInit(double pNow) {
	  	setDriveMessage(DriveMessage.kNeutral);
	}

	private void setDriveMessage(DriveMessage pMessage) {
		double left = clamp(pMessage.leftOutput, MAX_POWER_OUTPUT);
		double right = clamp(pMessage.rightOutput, MAX_POWER_OUTPUT);
		mLeftTalon.set(pMessage.leftControlMode, left);
		mRightTalon.set(pMessage.rightControlMode, right);
	}

	@Override
	public void periodicInput(double pNow) {
	}

	@Override
	public void update(double pNow) {
		double rotate = mController.get(ELogitech310.LEFT_Y_AXIS);
		rotate = EInputScale.EXPONENTIAL.map(rotate, 2);
		
		double throttle =  mController.get(ELogitech310.RIGHT_X_AXIS);
		
		setDriveMessage(new DriveMessage(throttle - rotate, throttle + rotate));
	}
	
	@Override
	public void shutdown(double pNow) {
	}


}
	
	



	



  

	