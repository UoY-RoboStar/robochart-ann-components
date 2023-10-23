import org.jcsp.lang.AltingChannelInput;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.CSTimer;
import org.jcsp.lang.ChannelInput;
import org.jcsp.lang.ChannelOutput;


public class NodeIn implements CSProcess, Constants {
	ChannelOutput output;
	ChannelInput input;
	int layer;
	int node;
	int index;
	
	public NodeIn(int layer, int node, int index, ChannelInput input, ChannelOutput output) {
		this.layer = layer;
		this.node = node;
		this.index = index;
		this.input = input;
		this.output = output;
	}
	
	public void run() {
		double in;
		
		in = (Double) input.read();
		double transfer = in * weights[layer][node][index];

		output.write(transfer);
	}
}

class Collator implements CSProcess, Constants {
	 AltingChannelInput input;
	 ChannelOutput output;
	 
	 double sum;
	 
	 int layer;
	 int node; 
	 int inpSize; 
	
	public Collator(int layer, int node, int inpSize, AltingChannelInput input, ChannelOutput output) {
		this.layer = layer;
		this.node = node;
		this.inpSize = inpSize;
		this.input = input;
		this.output = output;
	}
	
	public void run() {
		int inputs_recieved = 0;
		CSTimer tim = new CSTimer();
		double cycles = 20.0;
		long interval = (int) (1000.0 / cycles);
		long timeout = 0;
	
		while(inputs_recieved < inpSize) {
			long time = tim.read();
			if(time < Long.MAX_VALUE - interval) {
				timeout = time + interval;
			}
			else {
				timeout = time;
			}
			
			if(input.pending()) {
				double recieve = (Double) input.read();
				sum += recieve;
				inputs_recieved++;
			}
			
			tim.after(timeout);
		}
		
		double transfer = ReLU(sum + biases[layer][node]);
		
		if(layer < layerNo) {
			
		  for(int i = 0; i < layerstructure[layer]; i++) {
			  output.write(transfer);
		  }
		}
		else {
			output.write(transfer);
		}
	}
	
	public double ReLU(double in) {
		if(in >= 0) {
			return in;
		}
		else {
			return 0;
		}
	}
}
