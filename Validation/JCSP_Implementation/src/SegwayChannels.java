import org.jcsp.lang.Channel;
import org.jcsp.lang.One2OneChannel;

//This interface contains the channels used by the Segway process.
//Just the visible channels of the Segway.
//One2One channels? 
public interface SegwayChannels {
	final static One2OneChannel disableInterruptsCall = Channel.one2one(); 
	final static One2OneChannel enableInterruptsCall = Channel.one2one();
	final static One2OneChannel angle_in = Channel.one2one();
	final static One2OneChannel gyroX_in = Channel.one2one();
	final static One2OneChannel leftMotorVelocity_in = Channel.one2one();
	final static One2OneChannel rightMotorVelocity_in = Channel.one2one();
	final static One2OneChannel gyroZ_in = Channel.one2one();
	final static One2OneChannel setRightMotorSpeedCall = Channel.one2one();
	final static One2OneChannel setLeftMotorSpeedCall = Channel.one2one();
}

//Interface containing the balancestmchannels, it also uses the SegwayChannels. 
interface BalanceSTMChannels extends SegwayChannels {
	//AnglePID channels
	final static One2OneChannel anewError_in = Channel.one2one();
	final static One2OneChannel adiff_in = Channel.one2one();
	final static One2OneChannel angleOutputE_out = Channel.one2one();
	
	//SpeedPID channels
	final static One2OneChannel snewError_in = Channel.one2one();
	final static One2OneChannel speedOutputE_out = Channel.one2one();
		
	//RotationPID channels
	final static One2OneChannel rdiff_in = Channel.one2one();
	final static One2OneChannel rotationOutputE_out = Channel.one2one();
	
	
}
