import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Parallel;

public class SegwayController implements CSProcess {
	private SpeedPID speedPID;
	private RotationPID rotationPID;
	private BalanceSTM balanceSTM;
	
	public SegwayController() {
		this.speedPID = new SpeedPID(0);
		this.rotationPID = new RotationPID();
		this.balanceSTM = new BalanceSTM();
	}
	
	public void run() {
		new Parallel(new CSProcess[] {
			speedPID,
			rotationPID,
			balanceSTM
		}).run();
	}

}
