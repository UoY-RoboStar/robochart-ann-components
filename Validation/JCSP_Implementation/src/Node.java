import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Parallel;
import org.jcsp.lang.ChannelInput;
import org.jcsp.lang.ChannelOutput;
import org.jcsp.lang.Any2OneChannel;
import org.jcsp.lang.Channel;

public class Node implements CSProcess, Constants {
	ChannelInput[] nodeInputs;
	ChannelOutput nodeOutput;
	
	int layer;
	int node;
	int inpSize;
	
	public Node(int layer, int node, int inpSize, ChannelInput[] nodeInputs, ChannelOutput nodeOutput) {
		this.layer = layer;
		this.node = node;
		this.inpSize = inpSize;
		this.nodeInputs = nodeInputs;
		this.nodeOutput = nodeOutput;
	}
	
	public void run() {
		CSProcess[] nodeIns = new CSProcess[inpSize];
		final CSProcess collator;
		Any2OneChannel nodeOut = Channel.any2one();
		for(int i = 0; i < inpSize; i++) {
			nodeIns[i] = new NodeIn(layer, node, i, nodeInputs[i], nodeOut.out());
		}
		collator = new Collator(layer, node, inpSize, nodeOut.in(), nodeOutput);
		new Parallel (
			new CSProcess[] {
				new Parallel(nodeIns),
				(Collator) collator
			}
		).run();
	}
	
}

