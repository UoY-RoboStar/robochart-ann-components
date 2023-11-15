/*
 * Node is a class that represents a single node in a neural network, it takes each input from an array
 * of channel inputs, and applies an activation function to the summed total of all of these inputs.
 * 
 * it takes in:
 * 
 * (input_size, the number of inputs this node recieves.
 * Weights, the weights of this node, the weighting to each input dimension. 
 * 
 * 
 */
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Parallel;
import org.jcsp.lang.ChannelInput;
import org.jcsp.lang.ChannelOutput;
import org.jcsp.lang.Any2OneChannel;
import org.jcsp.lang.Channel;

public class Node implements CSProcess {
	//The input size of this node.
	//@ public invariant 0 < input_size;
	/*@ spec_public @*/ int input_size;
	
	//The weights of this node.
	//@ public invariant weights != null ==> weights.length == input_size;
	/*@ spec_public nullable @*/ double[] weights;
	
	//the bias of this node.
	double bias;
	
	//@ public invariant node_inputs.length == input_size;
	//@ public invariant \forall int i; 0 <= i < node_inputs.length; node_inputs[i] != null;
	/*@ spec_public @*/ ChannelInput[] node_inputs;
	
	ChannelOutput node_output;
	
	//@ public invariant link_layer_size > 0;
	/*@ spec_public @*/ int link_layer_size;
	
	
	//@ requires input_size > 0 && link_layer_size > 0;
	//@ requires weights != null ==> weights.length == input_size;
	//@ requires node_inputs != null;
	//@ requires \forall int i; 0 <= i < node_inputs.length; node_inputs[i] != null;
	//@ requires node_inputs.length == input_size;
	//@ pure
	public Node(int input_size, int link_layer_size, /*@ nullable @*/ double[] weights, double bias, ChannelInput[] node_inputs, ChannelOutput node_output) {
		this.input_size = input_size;
		this.weights = weights;
		this.node_inputs = node_inputs;
		this.node_output = node_output;
		this.link_layer_size = link_layer_size;
		this.bias = bias;		
	}
	//java -jar openjml.jar -cp "jcsp_nn;jcsp_nn/jcsp-1.1-rc4/jcsp.jar" -specspath "jcsp_nn/jml-jcsp" jcsp_nn/Node.java -esc -verboseness 0

	//@ pure
	public void run() {
		
		CSProcess[] node_ins = new CSProcess[input_size];
		//Declare all processes that will  be necessary.

		final CSProcess collator;
		
		//Have multiple JCSP_NN, have them communicate with one process.
		//create array of read channels, pass all to coallator, and use them as guards for alternative. 
		//One2OneChannel[] array = Channel.one2oneArray(int size);
		//Then array[i].in() for input channels and with one output, Channel.getOutputArray(array);
		
		//Node_In to collator channel.
		//Channel to communicate between the node_ins and the collator. 
		Any2OneChannel to_collator = Channel.any2one();
		//Need each input node connecting to each node_in, one input_node to one node_in. 
		//System.out.println("Input size :"+ input_size + " Weights length: "+ weights.length + " node inputs length: "+ node_inputs.length);
		
		if(weights != null) {
			//@ loop_invariant 0 <= i <= input_size;
			//@ loop_invariant weights != null;
			for(int i = 0; i < input_size; i++) {
				node_ins[i] = new NodeIn(weights[i], node_inputs[i], to_collator.out());
			}
		}
		
		collator = new Collator(input_size, link_layer_size, bias, to_collator.in(), node_output);
		
		
		//Non-deterministic interleaving parallel, alphabetised parallel by using channels, out and in. 
		//new CSProcess[] {csp_1, csp_2} creates a new array of csprocess to be used in paralell, new Parallel() takes as an argument
		//an array of csp processes, cannot create an array of an array of csprocesses.
		new Parallel ( //overall interleaving/automatic 'alphabetised'
			new CSProcess[] {
				//Can initialise arrays of processes with new Parallel(array)
				//Each new Parallel is, in itself, a CSProcess.
				//Inidividually interleaved, 
				new Parallel(node_ins),
				collator
			}
		).run();
	}
	
}


class Output_Node implements CSProcess {
	//The input size of this node.
	//@ public invariant 0 < input_size;
	/*@ spec_public @*/ int input_size;
	
	//The weights of this node.
	//@ public invariant weights != null ==> weights.length == input_size;
	/*@ spec_public nullable @*/ double[] weights;
	
	//the bias of this node.
	double bias;
	
	//@ public invariant node_inputs.length == input_size;
	//@ public invariant \forall int i; 0 <= i < node_inputs.length; node_inputs[i] != null;
	/*@ spec_public @*/ ChannelInput[] node_inputs;
	
	//@ public invariant link_layer_size > 0;
	/*@ spec_public @*/ int link_layer_size;
	
	
	//@ requires input_size > 0 && link_layer_size > 0;
	//@ requires weights != null ==> weights.length == input_size;
	//@ requires node_inputs != null;
	//@ requires \forall int i; 0 <= i < node_inputs.length; node_inputs[i] != null;
	//@ requires node_inputs.length == input_size;
	//@ pure
	public Output_Node(int input_size, /*@ nullable @*/ double[] weights, double bias, ChannelInput[] node_inputs) {
		this.input_size = input_size;
		this.weights = weights;
		this.node_inputs = node_inputs;
		this.bias = bias;		
	}
	//java -jar openjml.jar -cp "jcsp_nn;jcsp_nn/jcsp-1.1-rc4/jcsp.jar" -specspath "jcsp_nn/jml-jcsp" jcsp_nn/Node.java -esc -verboseness 0

	//@ pure
	public void run() {
		
		CSProcess[] node_ins = new CSProcess[input_size];
		//Declare all processes that will  be necessary.

		final CSProcess collator;
		
		//Have multiple JCSP_NN, have them communicate with one process.
		//create array of read channels, pass all to coallator, and use them as guards for alternative. 
		//One2OneChannel[] array = Channel.one2oneArray(int size);
		//Then array[i].in() for input channels and with one output, Channel.getOutputArray(array);
		
		//Node_In to collator channel.
		//Channel to communicate between the node_ins and the collator. 
		Any2OneChannel to_collator = Channel.any2one();
		//Need each input node connecting to each node_in, one input_node to one node_in. 
		//System.out.println("Input size :"+ input_size + " Weights length: "+ weights.length + " node inputs length: "+ node_inputs.length);
		
		if(weights != null) {
			//@ loop_invariant 0 <= i <= input_size;
			//@ loop_invariant weights != null;
			for(int i = 0; i < input_size; i++) {
				node_ins[i] = new NodeIn(weights[i], node_inputs[i], to_collator.out());
			}
		}
		
		collator = new Output_Collator(input_size, bias, to_collator.in());
		
		
		//Non-deterministic interleaving parallel, alphabetised parallel by using channels, out and in. 
		//new CSProcess[] {csp_1, csp_2} creates a new array of csprocess to be used in paralell, new Parallel() takes as an argument
		//an array of csp processes, cannot create an array of an array of csprocesses.
		new Parallel ( //overall interleaving/automatic 'alphabetised'
			new CSProcess[] {
				//Can initialise arrays of processes with new Parallel(array)
				//Each new Parallel is, in itself, a CSProcess.
				//Inidividually interleaved, 
				new Parallel(node_ins),
				collator
			}
		).run();
	}
	
}

