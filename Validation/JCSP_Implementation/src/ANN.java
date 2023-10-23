import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Channel;
import org.jcsp.lang.ChannelInput;
import org.jcsp.lang.ChannelOutput;
import org.jcsp.lang.One2AnyChannel;
import org.jcsp.lang.Parallel;

public class ANN implements CSProcess, Constants {
	//Private layerRes channel.
	private One2AnyChannel[][] layerRes;
	
	//Visible channel ends.
	public ChannelOutput[] visibleInputs;
	public ChannelInput[] visibleOutputs;

	public ANN() {
		this.layerRes = new One2AnyChannel[layerNo+1][];
		layerRes[0] = Channel.one2anyArray(insize);
		for(int i = 1; i < layerRes.length; i++) {
			layerRes[i] = Channel.one2anyArray(layerstructure[i-1]);
		}
		this.visibleInputs = Channel.getOutputArray(layerRes[0]);
		this.visibleOutputs = Channel.getInputArray(layerRes[layerNo]);
	}

	
	public void run() { 
		ChannelInput[] output_layer_input = 
				Channel.getInputArray(layerRes[layerNo-1]);
		
		ChannelOutput[] output_layer_output = 
			Channel.getOutputArray(layerRes[layerNo]);
		
		HiddenLayers hiddenlayers = new HiddenLayers(layerRes); 

		OutputLayer outputlayer = new OutputLayer(output_layer_input, output_layer_output);
        
		new Parallel(new CSProcess[] {
			(HiddenLayers) hiddenlayers,
			(OutputLayer) outputlayer
		}).run();
	}
}

class AnglePIDANN implements CSProcess, BalanceSTMChannels {
	private ANN ann;
	private NormIO normIO;
	
	public AnglePIDANN() {
		ann = new ANN();
		normIO = new NormIO(ann.visibleInputs, ann.visibleOutputs);
	}
	public void run() {
		new Parallel(new CSProcess[] {
			ann,
			normIO
		}).run();
		
	}
}
//Class represents NormInput and NormOutput in the CSP ANN semantics. 
//Receives input from BalanceSTM, passes it to the ANN, recieves the ANN's output through its visible channel ends, and passes this back to
//BalanceSTM. 
class NormIO implements CSProcess, BalanceSTMChannels {
	private ChannelOutput[] ann_inputs;
	private ChannelInput[] ann_outputs;
	//Normalisation Constants
	private final static double currNewError_max = 30;
	private final static double currNewError_min = -30;
	private final static double currDiff_max = 250;
	private final static double currDiff_min = -250;
	private final static double angleOutput_min = -1950;
	private final static double angleOutput_max = 1950;
	
	public NormIO(ChannelOutput[] ann_inputs, ChannelInput[] ann_outputs) {
		this.ann_inputs = ann_inputs;
		this.ann_outputs = ann_outputs;
	}
	double normalise(double min, double max, double x) {
		return (x - min) / (max - min);
	}
	double denormalise(double min, double max, double x) {
		return (x * (max - min)) + min;
	}

	public void run() {
		double currNewError = (Double) anewError_in.in().read();
		double currDiff = (Double) adiff_in.in().read();
		ann_inputs[0].write(normalise(currNewError_min, currNewError_max, currNewError));
		ann_inputs[1].write(normalise(currDiff_min, currDiff_max, currDiff));
		
		double annoutput = (Double) ann_outputs[0].read();
		angleOutputE_out.out().write(denormalise(angleOutput_min, angleOutput_max, annoutput));
	}
}