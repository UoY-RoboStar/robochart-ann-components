import org.jcsp.lang.CSProcess;
import org.jcsp.lang.ChannelInput;
import org.jcsp.lang.Skip;

public class OutputProcess implements CSProcess {
	
	/*@ spec_public @*/ final int output_size;
	
	//@ public invariant \forall int i; 0 <= i < input_channel.length; input_channel[i] != null;
	//@ public invariant input_channel.length == output_size;
	/*@ spec_public @*/ final ChannelInput[] input_channel;
	
	//@ public invariant output.length == output_size;
	/*@ spec_public @*/ double[] output;
	

	//@ requires output_size > 0;
	//@ requires input_channel != null;
	//@ requires output.length == output_size;
	//@ requires input_channel.length == output_size;
	//@ requires \forall int i; 0 <= i < input_channel.length; input_channel[i] != null;
	//@ requires input_channel != null;
	//@ pure
	public OutputProcess(int output_size, ChannelInput[] input_channel, double[] output) {
		this.output_size = output_size;
		this.input_channel = input_channel;
		//Create output storage to store the final output. 
		this.output = output;
	}
	//requires int i within output size. 
	//Assures that when run, final output becomes something.
	//@ also ensures \forall int i; 0 <= i < output.length; output[i] >= 0 && output[i] <= 1;
	//@ also assignable output[*];
	public void run() {
		//@ maintaining i >= 0 && i <= output_size;
		for(int i = 0; i < output_size; i++) {
			double recieve = (Double) input_channel[i].read();
			output[i] = recieve;
			System.out.println("recieved: "+ recieve);
		}
		//new Skip().run();
		//System.out.println("Final out finished");
		//Give the collator, output to write on?
	}
}

//java -jar openjml.jar -cp "jcsp_nn;jcsp_nn/jcsp-1.1-rc4/jcsp.jar" -specspath "jcsp_nn/jml-jcsp;jcsp_nn\jml-jcsp\org\jcsp\lang" jcsp_nn/Final_Output.java -esc -verboseness 0
