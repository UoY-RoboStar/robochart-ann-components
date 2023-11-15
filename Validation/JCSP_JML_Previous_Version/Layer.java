
/*
 * Layer is a class that has multiple nodes, it takes in a 2d array of 
 * weights, and passes on each weight to the respective node. 
 * 
 * It takes in an input array, to feed to each node, it will always be the same array
 * so passes the same array onto each node. 
 * 
 * It takes an array of output channels to transfer the output along, distibuting them
 * one per node. 
 * 
 * Take a layer pointer for the previous layer and get input dim and kernel size, if it is
 * a layer with a kernel. 
 * 
 * Next layer, don't have a pointer to next layer, easier to construct, otherwise have to partially
 * construct objects.
 */

import java.util.Arrays;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.ChannelInput;
import org.jcsp.lang.ChannelOutput;
import org.jcsp.lang.Parallel;

//Fully-Connected Layer implementation.
public class Layer implements CSProcess {
	
	//number of nodes in this layer.
	//@ public invariant layer_size >= 1;
	/*@ spec_public @*/ int layer_size; 
	
	//Weights of every node in this layer. 
	//Weights[node][input].
	
	//@ public invariant weights != null ==> weights.length == layer_size;
	//@ public invariant weights != null ==> \forall int i; 0 <= i < weights.length; weights[i].length == input_size;
	/*@ spec_public nullable @*/ double[][] weights; 
	
	//Bias of nodes in this layer
	//@ public invariant bias != null ==> bias.length == layer_size;
	/*@ spec_public nullable @*/ double[] bias;
	
	//Array of csprocess inputs to this layer, will be the layer output of the previous layer.
	//@ public invariant (layer_input != null) ==> layer_input.length == input_size;
	//@ public invariant (layer_input != null) ==> \forall int i; 0 <= i < layer_input.length; layer_input[i] != null;
	/*@ spec_public nullable @*/ ChannelInput[] layer_input;
	
	//Array of csprocess outputs from this layer, equal to the node number
	//@ public invariant layer_output.length == layer_size;
	//@ public invariant \forall int i; 0 <= i < layer_output.length; layer_output[i] != null;
	/*@ spec_public @*/ ChannelOutput[] layer_output;
	
	//Array of nodes that are created during runtime
	//@ public invariant nodes != null ==> nodes.length == layer_size;
	/*@ spec_public nullable @*/ Node[] nodes;
	
	//Number of inputs to this layer, dimensionality of this layer.
	//@ public invariant input_size > 0;
	/*@ spec_public @*/ int input_size;
	
	//needs to be requirement of calling link.
	//@ public invariant next_layer_size > 0;
	/*@ spec_public @*/int next_layer_size;
	
	//The next and previous layers, that this layer connects to. 
	/*@ spec_public nullable @*/ Layer next_layer;
	/*@ spec_public nullable @*/ Layer prev_layer;
	
	//@ requires input_size > 0;
	//@ requires layer_size >= 1;
	//@ requires (bias != null) ==> bias.length == layer_size;
	//@ requires (weights != null) ==> weights.length == layer_size;
	//@ requires (weights != null) ==> \forall int i; 0 <= i < weights.length; weights[i].length == input_size;
	//@ requires (layer_input != null) ==> layer_input.length == input_size;
	//@ requires (layer_input != null) ==> \forall int i; 0 <= i < layer_input.length; layer_input[i] != null;
	//@ requires layer_output.length == layer_size;
	//@ requires \forall int i; 0 <= i < layer_output.length; layer_output[i] != null;
	//@ pure
	public Layer(int layer_size, int input_size, /*@ nullable @*/ double[][] weights, /*@ nullable @*/ double[] bias, /*@ nullable @*/ ChannelInput[] layer_input, ChannelOutput[] layer_output) {
		this.layer_size = layer_size;
		this.weights = weights;
		this.bias = bias;
		this.layer_input = layer_input;
		this.layer_output = layer_output;
		//Setting it as a default value for jml.
		this.input_size = input_size;
		this.next_layer_size = layer_output.length;
	}
	 
	//This requires weights to be not null? method in input layer does not, a normal layer should fulfill this regardless, when creating a normal layer.  
	//In node dealt with with just if weights != null, have it as a pre condition see if it works in Node?
	//Methods called is: get_kernel_input_sizes, that's it. Might be worth splitting into multiple files.  
	
	//next layer size could be 0. 
	//possibly null deference, not possiblenullpointer, 
	
	//Cannot establish input_size and next_layer_size > 0 for some reason? but only runtime, 
	

	//@ also assignable nodes;
	public void run() { 
	
		nodes = new Node[layer_size];
		//All nodes have the same input size, 
		//if fully connected next layer.
		
		if(weights != null && bias != null && layer_input != null) {
			//@ loop_invariant 0 <= i <= layer_size;
			for(int i = 0; i < layer_size; i++) {
				nodes[i] = new Node(input_size, next_layer_size, weights[i], bias[i], layer_input, layer_output[i]);
			}
		}
		
		new Parallel(nodes).run();
		//.out.println("Layer finished");
	}
}
