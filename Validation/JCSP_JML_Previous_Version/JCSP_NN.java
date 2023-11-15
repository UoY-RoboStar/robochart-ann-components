/*
 * This is the complete jcsp_nn, with n layers, either feed-forward or convolution,
 * will add max/min/avg pooling as well, with
 * 
 *  Implemented in a doubly-linked list format, with each layer having a link to the next
 *  and previous layers. 
 */

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Channel;
import org.jcsp.lang.ChannelInput;
import org.jcsp.lang.ChannelOutput;
import org.jcsp.lang.One2AnyChannel;
import org.jcsp.lang.Parallel;

public class JCSP_NN implements CSProcess {
	
	//@public invariant output_size > 0;
	/*@ spec_public @*/ int output_size;
	
	//@ public invariant final_output.length == output_size;
	// public invariant \forall int i; 0 <= i < final_output.length; final_output[i] >= 0 && final_output[i] <= 1; 
	/*@ spec_public @*/ double[] final_output;
	
	//@ public invariant input_size > 0;
	/*@ spec_public @*/ final int input_size;
	
	//@ public invariant input.length == input_size;
	/*@ spec_public @*/ final double[] input; //Initial input
	
	//Number of layers
	//@ public invariant 0 < layer_number < Integer.MAX_VALUE - 1;
	//@ public invariant layer_number > 0;
	/*@ spec_public @*/ final int layer_number;
	
	//Size of each layer, of size hidden_layer_number
	//@ public invariant layer_sizes.length == layer_number;
	//@ public invariant \forall int i; 0 <= i < layer_sizes.length; layer_sizes[i] > 0;
	/*@ spec_public @*/ final int[] layer_sizes;
	
	
	//Custom enum type, storing the layer types for easy definition. 
	//Cannot set input layers, done internally.
	
	
	//Weight and bias invariants: 
	//@ public invariant weights.length == layer_number;
	//@ public invariant biases.length == layer_number;
	//@ public invariant \forall int i; 0 <= i < layer_sizes.length; (weights[i].length == layer_sizes[i] && biases[i].length == layer_sizes[i]);
	//@ public invariant \forall int i; 0 <= i < weights[0].length; weights[0][i].length == input_size;
	//@ public invariant \forall int i; 0 <= i < layer_number; (weights[i] != null) && (biases[i] != null);
	//@ public invariant \forall int i; 1 <= i < weights.length; \forall int j; 0 <= j < weights[i].length; weights[i][j].length == layer_sizes[i-1];
	
	//Weights storage, in 3d jagged array.
	//Structure is [layer][node][input_no]
	/*@ spec_public @*/ final double[][][] weights;
	//Structure is [layer][node]
	/*@ spec_public @*/ final double[][] biases;
	
	//Define pre conditions on how the model should be defined here.
	//Assume objects are non null by defualt (jml syntax)
	//Layer_sizes can be any size and shape, but it does define the overall size of weights and biases.
	
	//layer, node, input no
	//Assignable \everything as it is is a constructor.
	//Requires weights and biases to be a very specific shape.
	//Passes compilation.
	
    //@ requires layer_number > 0;
	//@ requires input_size > 0;
	//@ requires output_size > 0;
	//@ requires input.length == input_size;
	//@ requires 0 < layer_number < Integer.MAX_VALUE - 1;
    //@ requires layer_types.length == layer_number;
	//@ requires layer_sizes.length == layer_number;
    //@ requires input.length == input_size;
    //@ requires \forall int i; 0 <= i < layer_sizes.length; (weights[i].length == layer_sizes[i] && biases[i].length == layer_sizes[i]);
    //@ requires \forall int i; 0 <= i < layer_sizes[0]; weights[0][i].length == input_size;
    //@ requires \forall int i; 1 <= i < weights.length; \forall int j; 0 <= j < weights[i].length; weights[i][j].length == layer_sizes[i-1];
	//@ requires \forall int i; 0 <= i < layer_sizes.length; layer_sizes[i] > 0;
	//@ requires weights.length == layer_number;
	//@ requires biases.length == layer_number;
	//@ requires \forall int i; 0 <= i < layer_number; (weights[i] != null) && (biases[i] != null);
    //@ pure
	public JCSP_NN(int input_size, int output_size, double[] input, int layer_number, int[] layer_sizes, 
			double[][][] weights, double[][] biases) {
		this.input_size = input_size;
		this.output_size = output_size;
		this.input = input;
		this.layer_number = layer_number;
		this.layer_sizes = layer_sizes;
		this.weights = weights;
		this.biases = biases;
		final_output = new double[output_size];
	}

	
	//Also because overrides CSProcess run() technically.
	
	//It respects all post conditions of run. That;s the only reason why it works, because runs post condition assigns final_output.
	// also ensures final_output[0] <= 1.0 && final_output[0] > 0;
	//@ also assignable final_output;
	public void run() {
		//Create hl_res here. 
		//CHANGE LAYER_NUMBER TO LAYERNO, change channel_number to something else. 
		int channel_number = layer_number + 1;
		//[0,1]
		
		One2AnyChannel[][] layerRes = new One2AnyChannel[channel_number][];
		
		//Create channels.
		
		//Set manually for layer 1

		//This loop iterates through all layer_sizes, translates 0..layer_number(-1) to index 1..channel_number(-1)

		//@ loop_invariant 1 <= i <= channel_number && (\forall int x; x >= 1 && x < i; layer_channels[x] != null && layer_channels[x].length == layer_sizes[x-1]);
		for(int i = 1; i < channel_number; i++) {
			layerRes[i] = Channel.one2anyArray(layer_sizes[i-1]);
		}
		
		layerRes[0] = Channel.one2anyArray(input_size);
		
		//Channel 2d array for the entire structure, array of arrays for inter layer communication.
		//Needs 1 more channel array than layer number, to communicate with the final_output process.
		//hl_res needs to be here. We give the array to 
		//Output layer does not write on anything, so, output channel. It should be the channel before.
		//It is layer_number-1 because it is the second index, from the previous, 
		ChannelInput[] output_channel_input = 
				Channel.getInputArray(layerRes[layer_number-1]);
		
		//final output channel, that the output channel, collator  
		ChannelOutput[] output_channel_output = 
				Channel.getOutputArray(layerRes[channel_number-1]);
		
		//Actual final output channel. 
		ChannelInput[] outputprocess_input = 
				Channel.getInputArray(layerRes[channel_number-1]);
		
		//Give Hidden_Layer, the same array, don't make a copy, but give layer number as -1. 
		//Channels, also need channels to connect from these to the end. 
		//(LayerType[] layer_types, int input_size, double[] input, int layer_number, int[] layer_sizes, 
		//	double[][][] weights, double[][] biases
		Hidden_Layers hidden_layers = new Hidden_Layers(input_size, input, (layer_number - 1), layer_sizes, weights, biases, layerRes); 
		
		//output_size is final_output_channels.lengthss
		//Output layer is a layer, but it does not write on anything, like previous one did. 
		
		//Its weights is, weights[layer_number-1] and biases[layer_number-1]
		//hl_res, hl_res[layer_number-1], needs to just read on a channel, can read on same channel, doesn't have to write anything. 
		//Need to have a custom collator, 
		//need a channeloutput as well, 
		
		//Create output layer:
		
		//Size of this layer. 
		int layer_size = layer_sizes[layer_number-1];
		int layer_input_size = output_channel_input.length;
				
		//Get weights and biases for this layer
		double[][] layer_weights = weights[layer_number-1];
		double[] layer_biases = biases[layer_number-1];
			
		Output_Layer output_layer = new Output_Layer(layer_size, layer_input_size, layer_weights, layer_biases, output_channel_input, output_channel_output);

		
		OutputProcess outputprocess = new OutputProcess(output_channel_output.length, outputprocess_input, final_output);
		
		
		new Parallel(new CSProcess[] {
				(Hidden_Layers) hidden_layers,
				(Output_Layer) output_layer
				//(OutputProcess) outputprocess
		}).run();
		//System.out.println("jcsp nn finished");

	}
	
}
