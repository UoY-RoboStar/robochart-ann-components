#Validation testing, to support results from our CSP verification, on the network.

from Nnet_file_format_external import NNet
import numpy as np
import copy
import random


def evaluate_acasxu_1_1(): 
    acasxu = NNet("ACASXU_experimental_v2a_1_1.nnet")
    weights = acasxu.weights
    biases = acasxu.biases 
    reduced_weights = []
    reduced_biases = []
    reducedLayerNo = 4
    reduced_shape = [10,10,10,5]
    print(acasxu.maxes)
    print(acasxu.mins)
    for layer in range(reducedLayerNo):
        if(layer>0):
            reduced_weights.append(weights[layer][0:reduced_shape[layer], 0:reduced_shape[layer-1]])
        else:
            reduced_weights.append(weights[layer][0:reduced_shape[layer], :])
        reduced_biases.append(biases[layer][0:reduced_shape[layer]])
        
    #list of numpy arrays, how they are implemented.
    #If we assume they are normalised, ASSUMING INPUT IS NORMALISED, function is just this:
    # Evaluate the neural network
    #Input order should be; <2,3,4,5,1> no normalisation, so keep them normalised, 0.5 is highest, what is negative? keep positive for now.
    #highest to lowest, yes.
    #acasxu unnormalised ranges: 
    #[60760.0, 3.141593, 3.141593, 1200.0, 1200.0]
    #[0.0, -3.141593, -3.141593, 100.0, 0.0]
    #-0.5 AND 0.5, is normalised ranges, test on normalised ranges, easier to view orderings.
    inputs = np.array([0.01, 0.41, 0.35, 0.25, 0.17])
    #[0.1, 0.5, 0.4, 0.3, 0.2], max is OPTION 1. 0.01, still works. 
    #don't change ordering, find some more. option 3, highest. POSITIVE, works for positive, WE DON'T HANDLE NEGATIVE.
    #Random values, as long as they correspond to 
    test_number = 10000
    input_number = 5
    #input_order = [2,3,4,5,1]
    input_order = [3,2,1,2,5]
    #Doesn't matter about input order, still quite interesting, for any positive result, doesn't matter. 
    MAX_RANGE = 0.5
    for t in range(test_number): 
        inputs = np.zeros(input_number)
        #generate them IN ORDER.
        #Start from, range UPPER BOUNDED by the previous result.
        prev_result = MAX_RANGE
        for i in range(input_number):
            inputs[input_order[i]-1] = random.uniform(-0.5, 0.5)
            prev_result = inputs[input_order[i]-1]
        for layer in range(reducedLayerNo-1):
            inputs = np.maximum(np.dot(reduced_weights[layer],inputs)+reduced_biases[layer],0)
        outputs = np.dot(reduced_weights[-1],inputs)+reduced_biases[-1]
        decision = np.where(outputs == np.max(outputs))
        print("decision: ", decision)
  
if(__name__ == "__main__"):
    evaluate_acasxu_1_1()
