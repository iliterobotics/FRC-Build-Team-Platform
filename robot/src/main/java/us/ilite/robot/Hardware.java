	package us.ilite.robot;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.flybotix.hfr.util.log.ILog;
import com.flybotix.hfr.util.log.Logger;

import edu.wpi.cscore.VideoCamera;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
public class Hardware {
  private ILog mLog = Logger.createLog(Hardware.class);

  private Joystick mDriverJoystick;
  private Joystick mOperatorJoystick;
  private PowerDistributionPanel mPDP;
  public final AtomicBoolean mNavxReady = new AtomicBoolean(false);
  private PigeonIMU mPigeon;
  private CANifier mCanifier;
  private VideoCamera mVisionCamera;
  private DigitalInput mCarriageBeamBreak;
  
  Hardware() {
    
  }
  
  void init(
      Executor pInitializationPool,
      Joystick pDriverJoystick,
      Joystick pOperatorJoystick,
      PowerDistributionPanel pPDP,
      PigeonIMU pPigeon,
      CANifier pCanifier,
      //VideoCamera pVisionCamera,
      DigitalInput pCarriageBeamBreak
  ) {
    mDriverJoystick = pDriverJoystick;
    mOperatorJoystick = pOperatorJoystick;
    mPDP = pPDP;
    mPigeon = pPigeon;
    //mVisionCamera = pVisionCamera;
    //mVisionCamera.setFPS(30);
    mCarriageBeamBreak = pCarriageBeamBreak;
//    pInitializationPool.execute(() -> {
//      while(mAHRS.isCalibrating()) {
//        try {
//          Thread.sleep(20);
//        } catch (InterruptedException e) {
//          Thread.currentThread().interrupt();
//        }
//      }
//      mNavxReady.set(true);
//      mLog.info(System.currentTimeMillis() + " NAVX Calibrated");
//    });
    mCanifier = pCanifier;
  }
  
  public Joystick getDriverJoystick() { 
    return mDriverJoystick;
  }
  
  public Joystick getOperatorJoystick() {
    return mOperatorJoystick;
  }
  
  public PowerDistributionPanel getPDP() {
    return mPDP;
  }
  
  public VideoCamera getVisionCamera() {
    return mVisionCamera;
  }

  public CANifier getCanifier()
  {
	  return mCanifier;
  }
  
  public DigitalInput getCarriageBeamBreak() {
    return mCarriageBeamBreak;
  }
  
}

