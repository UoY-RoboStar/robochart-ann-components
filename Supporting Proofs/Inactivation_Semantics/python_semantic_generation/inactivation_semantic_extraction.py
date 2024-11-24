
import random
import statistics
from itertools import chain, combinations
import copy

def main():
    print("")

def calculate_input_layer_max(weights, biases, max_val): 
    #Returns a list of all of the layer maximums, given their entire parameters; 
    #Can be negative, maximum is sum of the absolute value of all of them. Maximum under any conditions. 
    maximums = []
    for i in range(0, len(biases)):
        maximums.append( (sum(list([abs(x) for x in weights[i]]))) + biases[i])
    return maximums
  
#FOr a layer, we need maximum from previous layers. Has to be positive now. 
def gen_layer_conditions(layer): 
    input_weights = [[5.40062e-02,-2.61092e+00,-1.80027e-01,2.42194e-01,1.41407e-01],
[-1.12374e+00,2.63619e-02,-9.17929e-03,5.56230e-02,-3.27635e-01],
[1.96019e-01,2.42159e-01,6.38452e-01,-4.78265e-01,1.42577e-01],
[-1.63015e+00,-3.44447e-02,-6.05492e-03,1.12076e-02,-1.04997e-02],
[-3.55133e-01,5.65969e-01,2.28267e-01,1.77342e-01,-2.08078e-01]]
    
    input_biases = [2.27630e-01,
-1.88762e-01,
5.34094e-02,
-3.77861e-01,
-8.12531e-02]
    maximums = calculate_input_layer_max(input_weights, input_biases, 0.5)
    #Apply the maximum weighting we can, to the weights, from previous layer:
    
    weights = [[-3.41918e-02,1.49570e+00,-1.53371e+00,4.05053e-02,1.64350e-01],
[4.02002e-01,2.24022e-01,-4.67160e-02,1.63890e-01,-1.60826e-01],
[-1.41575e+00,-7.47429e-02,6.04386e-02,4.82656e-02,-5.60096e-02],
[-1.44498e+00,4.48403e-02,8.88916e-03,-2.50230e-01,6.53312e-02],
[-1.12740e+00,2.39891e-02,-9.56711e-03,8.92796e-03,-4.58007e-03]]
    
    biases = [-5.88651e-01,7.46065e-02,-3.92887e-01,-3.56303e-01,-1.67712e-01]
    
    #Yes, all of them for the layer, by the appropriate maximum, 
    for i in range(0, len(weights)):
            for j in range(0, len(weights[i])):
                weights[i][j] *= maximums[j]
    
    condition_strings = []
    for n in range(0, len(biases)): 
        if(biases[n] < 0):
            print("inactivation conditions")
            print(biases[n])
            condition_strings.append(neg_weight_extraction(layer, n, weights[n], biases[n]))
        else:
            print("activation conditions")
            condition_strings.append(pos_weight_extraction(layer, n, weights[n], biases[n]))
    for s in condition_strings:
        print(s)


#these define, assume as is standard, 0 mean and an even range, -0.5..0.5
#statistical properties.
#we dont care how many, were not genetating slices ^ slices clnditions, we just care about the MAX VALUE THE INPUT COULD TAKE


def gen_input_conditions(): 
    weights = [[5.40062e-02,-2.61092e+00,-1.80027e-01,2.42194e-01,1.41407e-01],
[-1.12374e+00,2.63619e-02,-9.17929e-03,5.56230e-02,-3.27635e-01],
[1.96019e-01,2.42159e-01,6.38452e-01,-4.78265e-01,1.42577e-01],
[-1.63015e+00,-3.44447e-02,-6.05492e-03,1.12076e-02,-1.04997e-02],
[-3.55133e-01,5.65969e-01,2.28267e-01,1.77342e-01,-2.08078e-01]]
    
    biases = [2.27630e-01,
-1.88762e-01,
5.34094e-02,
-3.77861e-01,
-8.12531e-02]
    calculate_input_layer_max(weights, biases, 0.5)
    #We will have a bunch of strings, one per line, we need to group all of them, for FDR, one at a time: 
    conditions = []
    for i in range(0, len(weights)):
        input_conditions(0, 1, i, weights[i], biases[i], conditions)
    
    #Print all of them, by the biases: Order them manaully.
    #for i in range(0, len(biases)): 
        #print(conditions[i])
    
#Make this a function for one: 
def input_conditions(mean, ran, node, weights, bias, conditions):
	maxi = ran / 2
	#we now need only to look at the absolute value of every node, under max, this represents the maximum poositive and negative values the weights could take.
	#these are what we model, the conditions, so WE JUST CARE IF THEY ARE > 0, ACTIVE, OR <= 0, INACTIVE, THATS HOW WE MODEL THEM.
	#weights = [5.40062e-02,-2.61092e+00,-1.80027e-01,2.42194e-01,1.41407e-01]
	#bias = 2.27630e-01
	#absolute value
	#max is always the same
	
	for i in range(0, len(weights)):
		weights[i] = abs(weights[i]) * maxi
	#now we have maximums, if we now take powersets, compare to bias
	#is it the same? yes, it should be. 
	powerlists = list_powset(weights)
	#remove empty list from powerset, we dont need it.
	cons = []
	if(bias > 0):
		cons = [is_activation_condition(l, bias) for l in powerlists]
	else:
		cons = [is_inactivation_condition(l, bias) for l in powerlists]
        
	activation_lists = [l for l in powerlists if cons[powerlists.index(l)] == True]
	indexify_list(activation_lists, weights)
	activation_lists = list(filter(lambda x: len(x) > 0, activation_lists))
	if(bias > 0): 
		conditions.append(activation_print_conversion(activation_lists, 1, node, len(weights)))
	else: 
		conditions.append(inactivation_print_conversion(activation_lists, 1, node, len(weights)))
	    

#Print conversion, to FDR, for the activation logic: will be because we do have a positive bias. 
#Positive or negative, these are ACTIVE, if they are ACTIVE. 
#We add a constant one, this is active, we need to add: for the NUMBER OF WEIGHTS WE HAVE.
#1 or 0 indexed, for the sequence? 1-indexed, we need to add one.  
#Cuz were using sequences, we need to be using sets, check that the rest of them, we need to check all of them. 
#We need to group all of them for FDR. 
def activation_print_conversion(activation_lists, layer, node, no_weights):
    s = "ActivationLogic(" + str(layer) + "," + str(node+1) + ",edge_results) = \n "
    #The constant formula, for all of them, for the activity semantics: 
    
    s += "("
    for i in range(0, no_weights):
        s += "(extract_sequence("+str(i+1)+", edge_results) == active or extract_sequence("+str(i+1)+", edge_results) == uncertain)"
        if(i != no_weights-1): 
            s += " and "
    s += ")"
    if(len(activation_lists) > 0):
       s += " or \n"
    
    for i in range(0, len(activation_lists)):
        #New DNF for every list, 
        s += "("
        for j in range(0, no_weights): 
            #If the index is in the list, active, otherwise, inactive. 
            if(j in activation_lists[i]): 
                #We need to add one because sequences in FDR are 1-indexed, 
                s += "extract_sequence(" + str(j+1) + "," + "edge_results) == active"
            else: 
                #These are conditions, if any are unsure, we can only do the basic checks, need them to be active or inactive"
                s += "extract_sequence(" + str(j+1) + "," + "edge_results) == inactive"
                
            if(j != no_weights-1):
                s += " and "
            
        if(i != len(activation_lists)-1):
            s += ") or \n"
        else: 
            s += ") \n"
    return s
        
def inactivation_print_conversion(activation_lists, layer, node, no_weights):
    s = "InActivationLogic(" + str(layer) + "," + str(node+1) + ",edge_results) = \n "
    #The constant formula, for all of them, for the activity semantics: 
    
    s += "("
    for i in range(0, no_weights):
        s += "(extract_sequence("+str(i+1)+", edge_results) == inactive)"
        if(i != no_weights-1): 
            s += " and "
    s += ")"
    if(len(activation_lists) > 0):
        s += "or \n"
    
    for i in range(0, len(activation_lists)):
        #New DNF for every list, 
        s += "("
        for j in range(0, no_weights): 
            #If the index is in the list, active, otherwise, inactive. 
            if(j in activation_lists[i]): 
                #We need to add one because sequences in FDR are 1-indexed, 
                s += "extract_sequence(" + str(j+1) + "," + "edge_results) == active"
            else: 
                #These are conditions, if any are unsure, we can only do the basic checks, need them to be active or inactive"
                s += "extract_sequence(" + str(j+1) + "," + "edge_results) == inactive"
                
            if(j != no_weights-1):
                s += " and "
            
        if(i != len(activation_lists)-1):
            s += ") or \n"
        else: 
            s += ") \n"
    return s
        

    
	
def indexify_list(activation_lists, weights): 
    for i in range(0, len(activation_lists)):
        for j in range(0, len(activation_lists[i])):
            activation_lists[i][j] = weights.index(activation_lists[i][j])
            
def is_inactivation_condition(l, bias):
	#true, if the sum is less than or equal to the bias, the absolute value, then it must be inactive, still, 
	if(sum(l) <= abs(bias)):
		return True
	else:
		return False
		
#positive? if the absolute value of sum is greater tham the bias, calls a different function

#will be all the NEGATIVE WEIGHTS, SUM THEN ABSOLUTE VALUE, LESS THAN SRRICTLY THE WEIGHT, 0 IS INACTIVE.
#for positive biases:
def is_activation_condition(l, bias):
		if(abs(sum(l)) < bias):
			return True
		else:
			return False
			
def pos_weight_extraction(layer, node, weights, bias):
		mod_list = list(weights)
		#need to collect different values, just the negative values, for positive weights
		mod_list = list(filter(lambda x: x <= 0, mod_list))
		print("mod list: ", mod_list)
		powerlists = list_powset(mod_list)
		print(powerlists)
		#remove empty list from powerset, we dont need it.
		activation_cons = [is_activation_condition(l, bias) for l in powerlists]
		print(activation_cons)
		
		#print all sequences
		
		#translate them into indicies.
		activation_lists = [powerlists[i] for i in range(0, len(powerlists)) if activation_cons[i] == True]
		print(activation_lists)
		
		#convert to index of original list
		for i in range(0, len(activation_lists)):
			for j in range(0, len(activation_lists
			[i])):
				print("weight: ", weights.index(activation_lists[i][j]))
				activation_lists[i][j] = weights.index(activation_lists[i][j])
		return activation_print_conversion(activation_lists, layer, node, len(weights))        
		

		
def neg_weight_extraction(layer, node, weights, bias):
		#dont remove because we need index.
		#we need the original index to make the predictae, but we need to seach the smallest positive value, make sublists.
		mod_list = list(weights)
		#need to collect different values, just the negative values, for positive weights
		mod_list = list(filter(lambda x: x > 0, mod_list))
		print("mod list: ", mod_list)
		powerlists = list_powset(mod_list)
		print(powerlists)
		inactivation_cons = [is_inactivation_condition(l, bias) for l in powerlists ]
		print(inactivation_cons)
		
		#print all sequences
		
		#translate them into indicies.
		inactivation_lists = [powerlists[i] for i in range(0, len(powerlists)) if inactivation_cons[i] == True]
		print(inactivation_lists)
		
		#convert to index of original list
		for i in range(0, len(inactivation_lists)):
			for j in range(0, len(inactivation_lists
			[i])):
				print("weight: ", weights.index(inactivation_lists[i][j]))
				inactivation_lists[i][j] = weights.index(inactivation_lists[i][j])
		return inactivation_print_conversion(inactivation_lists, layer, node, len(weights))

		#find all powersets of these, get those combinations whose 
		
		#assumes all are different, otherwi
		#need two nested fors, need to check every one before this. do it for all, not that expensive surely.
		#get the unary predicates first
		#wait, all unary, tertiary, all possible combinations of all weights. 
		#only recurse if, 
		#when do we not? some subset, 
		#BECAUSE ITS ALL POSITIVE, IF ONE FAILS, ALL COMBINATIONS OF PREVIOUS WILL ALSO FAIL.
		#need to find
		

#start from lowest, might as well, if we get any we cant do, we can stop there, 
#if we get all 3, condition is z and y and x

#if z and y do, then if z and x do, all powersets. we have the list powerset code. 


def powerset(iterable):
    s = list(iterable)
    return chain.from_iterable(combinations(s, r) for r in range(len(s)+1))

def list_powset(l):
	listpow = list(powerset(l))
	powlist = [list(x) for x in listpow]
	return powlist


if(__name__ == "__main__"):
	#gen_input_conditions()
    gen_layer_conditions(2)