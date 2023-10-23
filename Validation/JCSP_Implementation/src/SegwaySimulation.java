import java.util.Random;

public class SegwaySimulation {
	public SegwaySimulation() {}
	
	public void random_testing() {
		//AnglePID testing, with ANN.
		//Real ranges of currAngle is -30 to 30, currGyroX is -250 to 250.
		int test_number = 10;
		Random rand = new Random();
		 double currNewError_max = 30;
		 double currNewError_min = -30;
		 double currDiff_max = 250;
		 double currDiff_min = -250;
		
		for(int i = 0; i < test_number; i++) {
			double currAngle = currNewError_min + (currNewError_max - currNewError_min) * rand.nextDouble();
			double currGyroX = currDiff_min + (currDiff_max - currDiff_min) * rand.nextDouble();
			System.out.println("testing with currAngle : " + currAngle + " and currGyroX : "+ currGyroX);
			double currGyroZ = 1;
			double currLeftVel = 1;
			double currRightVel = 1;
			Segway test = new Segway(currAngle, currGyroX, currGyroZ, currLeftVel, currRightVel);
			test.run();
			double currLeftVel_O = test.segwayRP.leftMotorVel;
			double currRightVel_O = test.segwayRP.rightMotorVel;
			
			test.runAnn();
			double currLeftVel_ANN = test.segwayRP.leftMotorVel;
			double currRightVel_ANN = test.segwayRP.rightMotorVel;
			
			double diff_left = currLeftVel_ANN - currLeftVel_O;
			double diff_right = currRightVel_ANN - currRightVel_O;
			System.out.println("diff_left: "+ diff_left);
			System.out.println("diff_right: "+ diff_right);
		}
	}

	public static void main(String[] args) {
		new SegwaySimulation().random_testing();
	}
}
