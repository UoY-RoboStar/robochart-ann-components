import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Parallel;
import org.jcsp.lang.One2AnyChannel;
import org.jcsp.lang.Channel;
import org.jcsp.lang.ChannelOutput;
import org.jcsp.lang.ChannelInput;

public class HiddenLayers implements CSProcess, Constants {
	
	public One2AnyChannel[][] layerRes;

	public HiddenLayers(One2AnyChannel[][] layerRes) {
		this.layerRes = layerRes;
	}
	
	public void run() {
		
		HiddenLayer[] layers = new HiddenLayer[layerNo-1];
		
		for(int i = 0; i < layerNo-1; i++) {
			ChannelInput[] input_channels = Channel.getInputArray(layerRes[i]);
			ChannelOutput[] output_channels = Channel.getOutputArray(layerRes[i+1]);

			int inpSize = input_channels.length;
			
			layers[i] = new HiddenLayer(i, layerstructure[i], inpSize, input_channels, output_channels);
		}
		
		new Parallel (
			(HiddenLayer[]) layers
		).run();
	}
}