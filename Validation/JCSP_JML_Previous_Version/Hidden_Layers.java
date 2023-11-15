import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Parallel;
import org.jcsp.lang.One2AnyChannel;
import org.jcsp.lang.Channel;
import org.jcsp.lang.ChannelOutput;
import org.jcsp.lang.ChannelInput;

public class Hidden_Layers implements CSProcess {
	//Number of hidden layers. Equals jcsp_nn's layer_number - 1. 
	int layer_number;
	int input_size;
	int[] layer_sizes;
	double[] input;
	double[][][] weights;
	double[][] biases;
	One2AnyChannel[][] hl_res;
	
	
	//weights and biases are the full weights and biases, but we don't use all of them because
	//of 
	//We don't need output_size. 
	//Needs to take an array of hl_res, it is created and managed in the higher layer, JCSP_NN.
	public Hidden_Layers(int input_size, double[] input, int layer_number, int[] layer_sizes, 
			double[][][] weights, double[][] biases, One2AnyChannel[][] hl_res) {
		this.input_size = input_size;
		this.input = input;
		this.layer_number = layer_number;
		this.layer_sizes = layer_sizes;
		this.weights = weights;
		this.biases = biases;
		this.hl_res = hl_res;
	}
	
	public void run() {
		
		//Create layers. 
		//Array of all layer csprocesses. 
		Layer[] layers = new Layer[layer_number];
		
		
		//First layer is always an input layer.
		
		//Create first layer.
		
		//All layer types subclasses of Layer.
		//Always starts with an input_layer, 
		
		
		//First layer is custom, always an Input_Layer.
		//Does not count as a layer, is not a true layer, it is the start of the
		//model, does not count as a layer for layer_sizes, if use a parameter couldn't have reusability. 
		
		InputProcess inputprocess = new InputProcess(input, Channel.getOutputArray(hl_res[0]), layer_sizes[0]);
				
		//Make middle layers.
		//corresponds to [0..layer_number] -> [1..channel_number]
		//layer 0 -> output array of layer_channels[1]
		
		//@ loop_invariant 0 <= i <= layer_number;
		for(int i = 0; i < layer_number; i++) {
			//first layer cannot have an input channel. 
			ChannelInput[] input_channels = Channel.getInputArray(hl_res[i]);
			ChannelOutput[] output_channels = Channel.getOutputArray(hl_res[i+1]);
		
			//Size of this layer. 
			int layer_size = layer_sizes[i];
			int layer_input_size = input_channels.length;
					
			//Get weights and biases for this layer
			double[][] layer_weights = weights[i];
			double[] layer_biases = biases[i];
			
			//Input size is now set here. 
			
			//layer_size is output_channels.length
			layers[i] = new Layer(layer_size, layer_input_size, layer_weights, layer_biases, input_channels, output_channels);

		}
		
		new Parallel(new CSProcess[] {
			inputprocess,
			new Parallel(layers)
		}).run();
	}
}