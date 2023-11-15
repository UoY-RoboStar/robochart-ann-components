public class SegwayExample {
	
	public SegwayExample() {
		run();
	}
	
	public void run() {
		int insize = 2;
		int outsize = 1;
		int layerNo = 2;
		
		int[] layerstructure = new int[] {
			1,1
		};
		
		double[] input = new double[] {
			0,0
		};
		
		double[][][] weights = {
			{{1.22838, 0.132874}},
			{{0.744636}}
		};
		
		double[][] biases = {
			{0.125424},
			{-0.107753}
		};
		
		JCSP_NN nn = new JCSP_NN(insize, 
								outsize,
								input, 
								layerNo,
								layerstructure, 
								weights, biases);
				nn.run();
	}
	
	public static void main(String[] args) {
		new SegwayExample();
	}
}