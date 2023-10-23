//This interface contains all semantic constants, and is implemented by all classes.
public interface Constants {
	//ANN Component Constants
	final static int insize = 2;
	final static int outsize = 1;
	final static int layerNo = 2;
	final static int maxSize = 2;
	final static int[] layerstructure = new int[] {
		1,1
	};
	final static double[][][] weights = {
		{{1.22838, 0.132874}},
		{{0.744636}}
	};
	final static double[][] biases = {
		{0.125424},
		{-0.107753}
	};
	//Segway Constants
	final static double BalanceSTM_maxAngle = 1000;

	//Timed Constants
	final static int BalanceSTM_loopTime = 0;
	final static int BalanceSTM_startupDelay = 0;
	final static int BalanceSTM_speedUpdate = 0;
	final static int BalanceSTM_rotationUpdate = 0;
	final static int BalanceSTM_angleBudget = 0;
	final static int BalanceSTM_speedBudget = 0;
	final static int BalanceSTM_rotationBudget = 0;
	final static int BalanceSTM_motorBudget = 0;

	//SpeedPID constants:
	final static double SpeedPID_P = 1;
	final static double SpeedPID_I = 1;
	final static double SpeedPID_maxIntegral = 1;

	//RotationPID 
	final static double RotationPID_D = 1;

	//AnglePID
	final static double AnglePID_P = 60;
	final static double AnglePID_D = 0.6;
}
