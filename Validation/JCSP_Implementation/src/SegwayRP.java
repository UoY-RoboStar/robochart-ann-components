import org.jcsp.lang.CSProcess;
import org.jcsp.lang.ChannelInput;
import org.jcsp.lang.ChannelOutput;

//Class feeds in input to the segway and recives output. 
public class SegwayRP implements CSProcess, Constants, SegwayChannels {
	private double currAngle;
	private double currGyroX;
	private double currGyroZ;
	private double currLeftVel;
	private double currRightVel;
	//For testing purposes.
	public double leftMotorVel;
	public double rightMotorVel;
	
	public SegwayRP(double currAngle, double currGyroX, double currGyroZ, double currLeftVel, double currRightVel) {
		this.currAngle = currAngle;
		this.currGyroX = currGyroX;
		this.currGyroZ = currGyroZ;
		this.currLeftVel = currLeftVel;
		this.currRightVel = currRightVel;
	}
	
	public void run() {
		//Only shows the visible behaviour, with the exact event names. 
		disableInterruptsCall.in().read();
		//System.out.println("Segway::disableInterruptsCall");
		enableInterruptsCall.in().read();
		//System.out.println("Segway::enableInterruptsCall");
		
		angle_in.out().write(currAngle);
		//System.out.println("Segway::angle.in." + currAngle);
		
		gyroX_in.out().write(currGyroX);
		//System.out.println("Segway::gyroX.in." + currGyroX);
		
		leftMotorVelocity_in.out().write(currLeftVel);
		//System.out.println("Segway::currLeftVel.in." + currLeftVel);
		
		rightMotorVelocity_in.out().write(currRightVel);
		//System.out.println("Segway::currRightVel.in." + currRightVel);
		
		gyroZ_in.out().write(currGyroZ);
		//System.out.println("Segway::currGyroZ.in." + currGyroZ);
		
		double leftMotorVel = (Double) setLeftMotorSpeedCall.in().read();
		//System.out.println("Segway::setLeftMotorSpeedCall." + leftMotorVel);
		

		double rightMotorVel = (Double) setRightMotorSpeedCall.in().read();
		//System.out.println("Segway::setRightMotorSpeedCall." + rightMotorVel);
		
		//for testing:
		this.leftMotorVel = leftMotorVel;
		this.rightMotorVel = rightMotorVel;
		
	}
}