/*
 * This is a class for running the neural network, the most abstract class
 * for starting the main files of the JCSPNNs.
 * 
 *  Results: With barriers per layer, makes negligile difference in runtime. 
 *  Basically identical with acas xu, as network is deeper, less efficient. 
 *  Making one crew per node now. 
 *  
 *  Enable concurrent writing, with concurrent reading now. 
 *  one crew with a single double in it, per node. 
 *  
 *  After this, a single shared double array with no crew lock, just the barrier.
 *  
 *  It's basically the same with using a shared resource, as crew, it is faster 
 *  using multiple writers than using a shared resource and modifying it. 
 *  With or witout Crew,
 *  When running both one after another, if first, faster,
 *  Whoever runs first is faster, less memory, not all destructed, object still in scope, 
 *  something.
 *  
 *  One last try, instead of coallators waiting, the node ins have another barrier
 *  and update an array internally, ave a second internal barrier. 
 *  
 */

//Only import it should need for the crew version is jcspnn_main crew.

public class Main {
	
	public Main() {
		//With tests, setting seed to long seed = 12
		//comparison();
		//run_crew();
		run_main();
		
	}
	//New crew: worse on higher dimensionality models, creates internal arrays.
	//Microseconds:
	//Crew preset: 62,154,500:
	//Main preset: 196,626,300
	
	//Milliseconds
	//2.4x speedup with new crew.
	//New crew acas xu: 5.7 seconds:4 seconds:4.66
	//Main: 13.5 seconds :13.3
	//Garbage 
	
	//For 5 (20x10)
	//Crew: 43,167,240,200 (43 seconds): 55,083,573,400
	//main: 9,639,838,400 (9 seconds)
	
	//New crew mnist: 31 seconds, 2 minutes with other running at the same time.
	
	//Crew main without anything: 56 seconds, still uses a lot of memory. 
	
	//Execute second is worse. 
	
	
	//
	
	//Main: 
	//Worse 
	
	
	//8 core machine. 
	//Acas xu: 5 (50x6)
	//16, 15 seconds, vs 13 seconds.
	//Duration of crew: 16,320,786,600:15866955100:16280218400
	//Duration of main: 13189569500:13219171400:13224505900
	
	//Duration of new crew: 5.7 seconds. 
	
	
	
	//True numbers, crew still slower.
	//About 20-25% slower
	
	//MNIST: 784 (50x3)
	//50 seconds vs 80.
	//Duration of crew: 79,219,949,800
	//Duration of main: 50,205,075,400 
	//About 60% slower. 
	
	//Lower dimensionality model, lower gap?
	
	
	//Deep network: 1000 (10)
	//53 seconds vs 51.
	//Duration of crew: 53,651,871,200
	//Duration of main: 51,058,169,200
	
	//Deep network: 1 (100x2)
	
	//Duration of crew: 7,206,144,300
	//Duration of main: 5,835,024,700
	
	
	
	
	
	
	
	//Deep: 100
	
	public void run_main() {
		JCSPNN_Main main = new JCSPNN_Main();
		long startTime = System.nanoTime();
		main.run_tests();
		long endTime = System.nanoTime();
		long duration_main = (endTime - startTime);
		System.out.println("Duration of main: "+ duration_main);
	}
	
	
	public static void main(String[] args) {
		new Main();
	}
}
