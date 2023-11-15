import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Parallel;
import org.jcsp.lang.ChannelInput;
import org.jcsp.lang.ChannelOutput;
import org.jcsp.lang.Any2OneChannel;
import org.jcsp.lang.Channel;

//A class which defines a node used for the initial input.
//This node only has a ChannelOutput, to link to a 
//This type of Node_In does not take any input, it only transferrs its input, in the variable var.
public class Input_Node implements CSProcess{
	int link_layer_size;
	/*@ spec_public @*/ ChannelOutput output;
	double weight;
	
	
	//@ requires link_layer_size > 0;
	//@ requires out != null;
	//@ pure
	public Input_Node(double input, int link_layer_size, ChannelOutput out) {
		this.weight = input;
		this.output = out;
		this.link_layer_size = link_layer_size;
	}
	
	//@ also assignable output;
	public void run() {
		//transfer to other nodes along the out channel, does not use in channel.
		double transfer = weight;
		//Repeat the write along this channel for every node in the next layer
		for(int i = 0; i < link_layer_size; i++) {
			output.write(transfer);
		}
		//System.out.println("Input node finished");
		
	}
}
