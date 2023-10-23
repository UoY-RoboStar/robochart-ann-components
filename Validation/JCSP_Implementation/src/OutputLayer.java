import org.jcsp.lang.CSProcess;
import org.jcsp.lang.ChannelInput;
import org.jcsp.lang.ChannelOutput;
import org.jcsp.lang.Parallel;

public class OutputLayer implements CSProcess, Constants {
	ChannelInput[] layer_input;
	ChannelOutput[] layer_output;
	Node[] nodes;
	
	int input_size;
	int next_layer_size;

	public OutputLayer(ChannelInput[] layer_input, ChannelOutput[] layer_output) {
		this.layer_input = layer_input;
		this.layer_output = layer_output;
		this.input_size = layerstructure[layerNo-2];
		this.next_layer_size = layer_output.length;
	}
	 
	public void run() { 
		nodes = new Node[layerstructure[layerNo-1]];
		
		for(int i = 0; i < layerstructure[layerNo-1]; i++) {
			nodes[i] = new Node(layerNo-1, i, layerstructure[layerNo-2], layer_input, layer_output[i]);
		}
		new Parallel(nodes).run();
	}
}
