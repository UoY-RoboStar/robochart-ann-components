import java.util.Arrays;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.ChannelInput;
import org.jcsp.lang.ChannelOutput;
import org.jcsp.lang.Parallel;

//Bias can be null,  weights and layer_input CANNOT be. We know it works with same conditions, so it must be something about weights and layer input, 

//In this class, layer size is required to be the same as input_size. 	
class InputProcess implements CSProcess {
	//Input can be any valid double array.
	//@ public invariant input.length == layer_size;
	/*@ spec_public @*/ double[] input;
	
	//@ public invariant layer_output.length == layer_size;
	//@ public invariant \forall int i; 0 <= i < layer_output.length; layer_output[i] != null;
	/*@ spec_public @*/ ChannelOutput[] layer_output;
	
	//@ public invariant layer_size >= 1;
	/*@ spec_public @*/ int layer_size;
	
	//@ public invariant next_layer_size > 0;
	/*@ spec_public @*/ int next_layer_size;
	
	LayerType type;
	
	//Requires they are the same size, as there is an input node for every input value.
	
	//@ requires input != null && layer_output != null;
	//@ requires \forall int i; 0 <= i < layer_output.length; layer_output[i] != null;
	//@ requires (input.length == layer_output.length);
	//@ requires input.length >= 1;
	//@ requires layer_output.length > 0;
	//@ requires next_layer_size > 0;
	//@ pure
	public InputProcess(double[] input, ChannelOutput[] layer_output, int next_layer_size) {
		this.input = input;
		this.type = LayerType.Input;
		this.next_layer_size = next_layer_size;
		this.layer_output = layer_output;
		this.layer_size = input.length;
		//System.out.println("next layer size: "+ this.next_layer_size);
	}
		
	
	//@ pure
	public void run() {
		Input_Node[] nodes = new Input_Node[layer_size];
		
		//@ loop_invariant 0 <= i <= layer_size;
		for(int i = 0; i < layer_size; i++) {
			nodes[i] = new Input_Node(input[i], next_layer_size, layer_output[i]);
		}
		
		new Parallel(nodes).run();
		//System.out.println("input layer finished");
	}
	
}
