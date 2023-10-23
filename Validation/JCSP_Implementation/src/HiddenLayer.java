import org.jcsp.lang.CSProcess;
import org.jcsp.lang.ChannelInput;
import org.jcsp.lang.ChannelOutput;
import org.jcsp.lang.Parallel;

public class HiddenLayer implements CSProcess, Constants {
	ChannelInput[] layerInput;
	ChannelOutput[] layerOutput;
	Node[] nodes;
	
	int layer; 
	int size;
	int inpSize;
	
	public HiddenLayer(int layer, int size, int inpSize, ChannelInput[] layerInput, ChannelOutput[] layerOutput) {
		this.layer = layer;
		this.size = size;
		this.inpSize = inpSize;
		this.layerInput = layerInput;
		this.layerOutput = layerOutput;
	}
	 
	public void run() { 
		nodes = new Node[size];
		
		for(int i = 0; i < size; i++) {
			nodes[i] = new Node(layer, i, inpSize, layerInput, layerOutput[i]);
		}
		
		new Parallel((Node[]) nodes).run();
	}
}
