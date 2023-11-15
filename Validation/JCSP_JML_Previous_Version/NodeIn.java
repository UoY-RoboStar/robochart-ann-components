
/*
 * This files defines Node_In class, which defines a node_in process, used to deal with the input of every node. 
 * This file also defines a coallator, which is used to define a coallator for multiple Node_Ins to define a Node.
 */
import java.util.Arrays;

import org.jcsp.lang.AltingChannelInput;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.CSTimer;
import org.jcsp.lang.ChannelInput;
import org.jcsp.lang.Parallel;
import org.jcsp.lang.ChannelOutput;


public class NodeIn implements CSProcess {
	double weight;
	/*@ spec_public @*/ ChannelOutput output;
	ChannelInput input;
	//The size of the next layer.
	
	//Each node in needs a ChannelInput and a ChannelOutput, starting with
	//@ requires output != null;
	//@ pure
	public NodeIn(double weight, ChannelInput input, ChannelOutput output) {
		this.weight = weight;
		this.input = input;
		this.output = output;
	}
	
	//@ also assignable output;
	public void run() {
		double in;

		if(input != null) {
			in = (Double) input.read();
		}
		//For zero padding. 
		else {
			in = 0.0;
		}
		double transfer = (in * weight);
	
		//System.out.println("Node in transferring " + transfer);
		output.write(transfer);
	}
}

/*
 * Coallator waits until it recieves a number of inputs (doubles) along its AltingChannelInput equal to the 
 * total_inputs, it then sums these inputs, adds a bias term, and then applies an activation function to it, in this case, ReLU. 
 */
class Collator implements CSProcess {
	/*@ spec_public @*/ AltingChannelInput input;
	/*@ spec_public @*/ ChannelOutput output;
	//The number of inputs to listen for from input channel.
	//@ public invariant 0 < total_inputs <= Integer.MAX_VALUE;
	/*@ spec_public @*/ int total_inputs;
	//bias term for this node.
	double bias;
	//the running total of all values recieves 
	/*@ spec_public @*/ double total;
	//Size of the next layer.
	//@public invariant 0 < link_layer_size <= Integer.MAX_VALUE;
	/*@ spec_public @*/ int link_layer_size;
	
	//@ requires 0 < total_inputs <= Integer.MAX_VALUE;
	//@ requires link_layer_size > 0;
	//@ pure
	public Collator(int total_inputs, int link_layer_size, double bias, AltingChannelInput input, ChannelOutput output) {
		this.total_inputs = total_inputs;
		this.bias = bias;
		this.input = input;
		this.output = output;
		this.link_layer_size = link_layer_size;

	}
	
	//@ also assignable output, input, total;
	public void run() {
		int inputs_recieved = 0;
		CSTimer tim = new CSTimer();
		double cycles = 20.0;
		long interval = (int) (1000.0 / cycles);
		long timeout = 0;
		
		//Waits for a number of inputs from node_in equal to total_inputs, 
		//10, waits for 10 inputs. 
		//Each coallator writes 10 times, 
		//Each 
		
		//@ loop_invariant Long.MIN_VALUE <= timeout <= Long.MAX_VALUE;
		while(inputs_recieved < total_inputs) {
			long time = tim.read();
			if(time < Long.MAX_VALUE - interval) {
				timeout = time + interval;
			}
			else {
				timeout = time;
			}
			
			if(input.pending()) {
				double recieve = (Double) input.read();
				total += recieve;
				inputs_recieved++;
			}
			
			tim.after(timeout);
		}
		//Transfer along output channel. 
		double transfer = activation_function(total + bias);
		//System.out.println("Coallator output " + transfer);
		//System.out.println("Coallator bias "+ bias);
		
		for(int i = 0; i < link_layer_size; i++) {
			output.write(transfer);
		}
	}
	
	//@ ensures (\result == 0 || \result == in);
	//@ pure
	public double activation_function(double in) {
		//ReLU
		if(in >= 0) {
			return in;
		}
		else {
			return 0;
		}
	}
}

class Output_Collator implements CSProcess {
	/*@ spec_public @*/ AltingChannelInput input;
	//The number of inputs to listen for from input channel.
	//@ public invariant 0 < total_inputs <= Integer.MAX_VALUE;
	/*@ spec_public @*/ int total_inputs;
	//bias term for this node.
	double bias;
	//the running total of all values recieves 
	/*@ spec_public @*/ double total;
	//Size of the next layer.
	
	//@ requires 0 < total_inputs <= Integer.MAX_VALUE;
	//@ requires link_layer_size > 0;
	//@ pure
	public Output_Collator(int total_inputs, double bias, AltingChannelInput input) {
		this.total_inputs = total_inputs;
		this.bias = bias;
		this.input = input;

	}
	
	//@ also assignable output, input, total;
	public void run() {
		int inputs_recieved = 0;
		CSTimer tim = new CSTimer();
		double cycles = 20.0;
		long interval = (int) (1000.0 / cycles);
		long timeout = 0;
		
		//Waits for a number of inputs from node_in equal to total_inputs, 
		//10, waits for 10 inputs. 
		//Each coallator writes 10 times, 
		//Each 
		
		//@ loop_invariant Long.MIN_VALUE <= timeout <= Long.MAX_VALUE;
		while(inputs_recieved < total_inputs) {
			long time = tim.read();
			if(time < Long.MAX_VALUE - interval) {
				timeout = time + interval;
			}
			else {
				timeout = time;
			}
			
			if(input.pending()) {
				double recieve = (Double) input.read();
				total += recieve;
				inputs_recieved++;
			}
			
			tim.after(timeout);
		}
		//Transfer along output channel. 
		System.out.println("Output collator input: ", (total + bias));
		double transfer = activation_function(total + bias);
		System.out.println("Ouput Coallator output " + transfer);
		//System.out.println("Coallator bias "+ bias);
		
		System.out.println(""+transfer);
	}
	
	//@ ensures (\result == 0 || \result == in);
	//@ pure
	public double activation_function(double in) {
		//ReLU
		if(in >= 0) {
			System.out.println("positive in output collator");
			return in;
		}
		else {
			System.out.println("negative in output collator");
			return 0;
		}
	}
}
