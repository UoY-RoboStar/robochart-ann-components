import java.util.Scanner;
import java.util.Random;

//java -cp ";jcsp-1.1-rc4/jcsp-core.jar" Segway
//Correct output, final out nn_max: 0.9992809592079999
//for 1,1
//final out nn_max: 0.9992809592079999
//final out nn_min: 0.0

//final out nn_max: 1.486678
//final out nn_min: 0.125424

//We can use //new Skip().run();
//java -jar openjml.jar -cp "jcsp_nn;jcsp_nn/jcsp-1.1-rc4/jcsp.jar" -specspath "jcsp_nn/jml-jcsp;jcsp_nn\jml-jcsp\org\jcsp\lang" jcsp_nn/Final_Output.java -esc -verboseness 0

public class Segway {
	public Segway() {
		int input_size = 2;
		int output_size = 1;
		int layer_number = 2;
		int[] layer_sizes = new int[] {
			1,1
		};
		
		//double[] input_min = new double[] {
			//0.0166667,0.002
		//};
		
		/*double[] input_max = new double[] {
			1,1
		};*/
		
		//bias: 0.125424weight 0: 1.22838weight 1: 0.132874
		//bias: -0.107753weight 0: 0.744636
		
		double[][][] weights = {
			{{1.22838, 0.132874}},
			{{0.744636}}
		};
		
		double[][] biases = {
			{0.125424},
			{-0.107753}
		};
		
		/*JCSP_NN nn_min = new JCSP_NN(input_size, 
								output_size,
								input_min, 
								layer_number,
								layer_sizes, 
								weights, biases);*/
		
		double x_update = 1.0/60.0;
		double y_update = 1.0/500.0;
		
		/*double[] input = new double[] {0.0166667, 0.002};
		JCSP_NN nn = new JCSP_NN(input_size, 
								output_size,
								input, 
								layer_number,
								layer_sizes, 
								weights, biases);
				nn.run();*/
				
		for(int i = 0; i < 60; i++) {
			for(int j = 0; j < 500; j++) {
				double x = (x_update * (i+1));
				double y = (y_update * (j+1));
				
				double[] input = {x, y};
				
				JCSP_NN nn = new JCSP_NN(input_size, 
								output_size,
								input, 
								layer_number,
								layer_sizes, 
								weights, biases);
								
				nn.run();
				
			}
		}
		
		
		
		//test_method(final_out);
		//@ assert final_out <= 1 && final_out >= 0;	
		// assert final_out == 0.9992809592079999;
		
		
		//System.out.println("input of ");
		//max is: 0.9992809592079999
		//min is: 0.0010850745095936642
		
		//double max = nn_max.final_output[0];
		// assert max <= 1 && max >= 0;
		
		//double[] max = nn_max.final_output;
		//double[] min = nn_min.final_output;
		//int output_size = max.length;
		//assert max[0] >= 0;
		
		/*for(int i = 0; i < input_size; i++) {
			System.out.println("input: "+ i + " max: "+ input_max[i] + " min: "+ input_min[i]);
		}*/
		
		//double[] safe_zone_min = new double[] { 0.0};
		//double[] safe_zone_max = new double[] { 1.0};
		
		//display_result(output_size, max, min, safe_zone_min, safe_zone_max);
		
		/*Scanner scan = new Scanner(System.in);
		String input = "";

		while(!input.equals("q")) {
			input = scan.nextLine();
			double in1 = Double.parseDouble(input);
			input = scan.nextLine();
			double in2 = Double.parseDouble(input);
			double[] input_test = new double[] {
				in1, in2
			};
			JCSP_NN nn_test = new JCSP_NN(layer_types, 
								input_size, 
								input_test, 
								layer_number,
								layer_sizes, 
								weights, biases);
		
			nn_test.run();
			
			double result = nn_test.final_output[0];
			System.out.println("result: " + result);
		}*/
		
	}
	
	// requires \forall int i; i <= 0 < output_size; max[i] >= min[i];
	// requires \forall int i; i <= 0 < output_size; (min[i] >= safe_zone_min[i]) && (max[i] <= safe_zone_max[i]);
	public void display_result(int output_size, double[] max, double[] min, double[] safe_zone_min, double[] safe_zone_max) {
		//for(int i = 0; i < max.length; i++) {
			//System.out.println("final out index i between: "+ min[i] + " and " + max[i]);
		//}
	}
	
	public static void main(String[] args) {
		new Segway();
	}
}