import org.jcsp.lang.CSProcess;

public class BalanceSTM implements CSProcess, BalanceSTMChannels, Constants {
	public BalanceSTM() {}
	
	public void run() {
		disableInterruptsCall.out().write(0);
		////System.out.println("disableInterruptsCall");
		
		enableInterruptsCall.out().write(0);
		//System.out.println("enableInterruptsCall");
		
		
		double currAngle = (Double) angle_in.in().read();
		//System.out.println("angle.in?" + currAngle);
		
		double currGyroX = (Double) gyroX_in.in().read();
		//System.out.println("currGyroX.in?" + currGyroX);
		
		anewError_in.out().write(currAngle);
		//System.out.println("anewError.out!" + currAngle);
		
		adiff_in.out().write(currGyroX);
		//System.out.println("adiff.out!" + currGyroX);
		
		double angleOutput = (Double) angleOutputE_out.in().read();
		//System.out.println("angleOutputE.out?" + angleOutput);
		
		double currLeftVel = (Double) leftMotorVelocity_in.in().read();
		double currRightVel = (Double) rightMotorVelocity_in.in().read();
		//System.out.println("leftMotorVelocity.in?" + currLeftVel);
		//System.out.println("rightMotorVelocity.in?" + currRightVel);
		
		snewError_in.out().write(currLeftVel + currRightVel);
		//System.out.println("snewError.in!" + (currLeftVel + currRightVel));
		
		double speedOutput = (Double) speedOutputE_out.in().read();
		//System.out.println("speedOutputE.in?" + speedOutput);
		
		double currGyroZ = (Double) gyroZ_in.in().read();
		//System.out.println("gyroZ.in?" + currGyroZ);
		
		rdiff_in.out().write(currGyroZ);
		//System.out.println("rdiff.in!" + currGyroZ);
		
		double rotationOutput = (Double) rotationOutputE_out.in().read();

		if( -BalanceSTM_maxAngle <= currAngle && currAngle <= BalanceSTM_maxAngle) {
			double leftMotor = angleOutput + speedOutput - rotationOutput;
			double rightMotor = angleOutput + speedOutput + rotationOutput;

			setLeftMotorSpeedCall.out().write(leftMotor);
			
			
			setRightMotorSpeedCall.out().write(rightMotor);
			//System.out.println("setRightMotorSpeedCall." + rightMotor);
	
		}
		
		if( -BalanceSTM_maxAngle > currAngle || currAngle > BalanceSTM_maxAngle) {
			setLeftMotorSpeedCall.out().write(0.0);
			//System.out.println("setLeftMotorSpeedCall." + 0);
			
			setRightMotorSpeedCall.out().write(0.0);
			//System.out.println("setRightMotorSpeedCall." + 0);
		}
		
	}
}

class AnglePID implements CSProcess, Constants, BalanceSTMChannels {
	public AnglePID() {}
	
	public void run() {
		double currNewError = (Double) anewError_in.in().read();
		//System.out.println("APID::anewError.in?" + currNewError);
		double currDiff = (Double) adiff_in.in().read();
		//System.out.println("APID::adiff.in?" + currDiff);
		
		double angleOutput = (AnglePID_P * currNewError) + (AnglePID_D * currDiff);
		angleOutputE_out.out().write(angleOutput);
		//System.out.println("APID::angleOutputE.out!" + angleOutput);
	
	}
}


class SpeedPID implements CSProcess, Constants, BalanceSTMChannels {
	//State of SpeedPID, implemented using memory processes in CSP.
	private double speedIntegral;
	
	public SpeedPID(double speedIntegral) {
		this.speedIntegral = speedIntegral;
	}
	
	public void run() {
		double currNewError = (Double) snewError_in.in().read();
		//System.out.println("SPID::snewError.in?" + currNewError);
		speedIntegral += currNewError;
		
		if(speedIntegral > SpeedPID_maxIntegral) {
			speedIntegral = SpeedPID_maxIntegral;
		}
		else if(speedIntegral < - SpeedPID_maxIntegral) {
			speedIntegral = - SpeedPID_maxIntegral;
		}
		double speedOutput = (SpeedPID_P * currNewError) + (SpeedPID_I * speedIntegral);
		speedOutputE_out.out().write(speedOutput);
		//System.out.println("SPID::speedOutputE.out!" + speedOutput);
	}
}

class RotationPID implements CSProcess, Constants, BalanceSTMChannels {

	public RotationPID() {}
	
	public void run() {
		double currDiff = (Double) rdiff_in.in().read();
		//System.out.println("RPID::rdiff.in?" + currDiff);
		
		double rotationOutput = RotationPID_D * currDiff;
		rotationOutputE_out.out().write(rotationOutput);
		//System.out.println("RPID::rotationOutputE.out!" + rotationOutput);
	}
}