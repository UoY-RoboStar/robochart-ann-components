import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Parallel;

public class Segway implements CSProcess {
	SegwayRP segwayRP;
	SegwayController segwayController;
	AnglePID anglePIDC;
	AnglePIDANN anglePIDANN;
	
	public Segway(double currAngle, double currGyroX, double currGyroZ, double currLeftVel, double currRightVel) {		
		this.segwayRP = new SegwayRP(currAngle, currGyroX, currGyroZ, currLeftVel, currRightVel);
		this.segwayController = new SegwayController();
		this.anglePIDC = new AnglePID();
		this.anglePIDANN = new AnglePIDANN();
	}
	
	public void run() {
		new Parallel(new CSProcess[] {
				segwayRP,
				segwayController,
				anglePIDC
			  }).run();
	}
	
	public void runAnn() {
		new Parallel(new CSProcess[] {
				segwayRP,
				segwayController,
				anglePIDANN
			  }).run();
	}
	
}