
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
        maximums.append( (sum(list([(abs(x) * max_val) for x in weights[i]]))) + biases[i])
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
    #Hiding the maximums for now. 
    #maximums = calculate_input_layer_max(input_weights, input_biases, 0.5)
    #Apply the maximum weighting we can, to the weights, from previous layer:
    
    weights = [[-3.41918e-02,1.49570e+00,-1.53371e+00,4.05053e-02,1.64350e-01],
[4.02002e-01,2.24022e-01,-4.67160e-02,1.63890e-01,-1.60826e-01],
[-1.41575e+00,-7.47429e-02,6.04386e-02,4.82656e-02,-5.60096e-02],
[-1.44498e+00,4.48403e-02,8.88916e-03,-2.50230e-01,6.53312e-02],
[-1.12740e+00,2.39891e-02,-9.56711e-03,8.92796e-03,-4.58007e-03]]
    
    biases = [-5.88651e-01,7.46065e-02,-3.92887e-01,-3.56303e-01,-1.67712e-01]
    
    maximums = calculate_input_layer_max(input_weights, input_biases, 0.5)
    #Yes, all of them for the layer, by the appropriate maximum, 
    for i in range(0, len(weights)):
            for j in range(0, len(weights[i])):
                weights[i][j] *= maximums[j]
                print("maximums: ", maximums[j])
    
    condition_strings = []
    for n in range(0, len(biases)): 
        if(biases[n] < 0):
            condition_strings.append(neg_weight_extraction(layer, n, weights[n], biases[n]))
        else:
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
        input_conditions(0, 1.0, i, weights[i], biases[i], conditions)
    
    #Print all of them, by the biases: Order them manaully.
    for i in range(0, len(biases)): 
        print(conditions[i])
    
#Make this a function for one: 
def input_conditions(mean, ran, node, weights, bias, conditions):
    maxi = ran / 2
    #Make a copy, need what the original sign was: 
    original_weights = list(weights)
    
    for i in range(0, len(weights)):
        weights[i] = abs(weights[i]) * maxi
    #now we have maximums, if we now take powersets, compare to bias
    #is it the same? yes, it should be. 
    #remove empty list from powerset, we dont need it.
    weight_powerset = list_powset(weights)
    actual_lists = []
    #If it is a combination we need to worry about, find out what phases each must be in. 
    #We need to test, for every combination, we need to test 
    #They are ALL POSITIVE NOW, we need to just flip each to negative, test what need to be negative. 
    #DOESN'T WORK, that filter, because it assumes they are all negative, they might not be. 
    #We may not have true counterexamples here, just possible:
    #All possibilities, because, could be negative, 
    if(bias > 0):
        #Test every combination: 
        #Every combination of the powerlists, yes, 
        for i in range(0,len(weight_powerset)):
            l = list(weight_powerset[i])
            for i in range(0, len(l)):
                l[i] *= -1
                for j in range(0,len(l)):
                    l[j] *= -1
                    sum_cons = sum(l)
                    #If this beats the bias, we are positive, then we may have a condition.
                    #No, this is for when the sum is going to make the whole thing negative. when the sum + bias < 0 
                    #If the maximum value, evaluated, is less than 0, then THERE IS A CHANCE
                    #THAT THIS PATTERN IS A FAILURE CASE, IF IT ISN'T, THERE IS NO RISK, IT IS RISKY IF THIS 
                    #HOLDS. 
                    if((sum_cons + bias) < 0):
                        #Make a new copy, I think it is changing this: 
                         actual_lists.append(list(l))
    else:
        for i in range(0,len(weight_powerset)):
            l = list(weight_powerset[i])
            for i in range(0, len(l)):
                l[i] *= -1
                for j in range(0,len(l)):
                    l[j] *= -1
                    sum_cons = sum(l)
                    #If this beats the bias, we are positive, then we may have a condition.
                    #No, this is for when the sum is going to make the whole thing negative. when the sum + bias < 0 
                    #If the maximum value, evaluated, is less than 0, then THERE IS A CHANCE
                    #THAT THIS PATTERN IS A FAILURE CASE, IF IT ISN'T, THERE IS NO RISK, IT IS RISKY IF THIS 
                    #HOLDS. 
                    if((sum_cons + bias) >= 0):
                        #Make a new copy, I think it is changing this: 
                        actual_lists.append(list(l))
    #Because of the duplicates. 
    
    #Negative, inactive ones, only need to worry if that INPUT IS ACTUALLY INACTIVE.
    #Translate to not, or is: 
    for i in range(0, len(actual_lists)):
        for j in range(0, len(actual_lists[i])):
            #We don't know the original value of all of them. 
            index_of_weight = weights.index(abs(actual_lists[i][j]))
            #Always the same, because if negative, inactivity means NEGATIVE, in an edge. 
            #Even if the weight is negative, NEEDS TO BE NEGATIVE. Positive input, doesn't matter. 
            #IF THE END RESULT, its about the end result. 
            if(actual_lists[i][j] <= 0.0):
               actual_lists[i][j] = -(weights.index(abs(actual_lists[i][j]))+1)
            else: 
                actual_lists[i][j] = (weights.index(abs(actual_lists[i][j]))+1)

    #Remove duplicates of lists, 
    
    import itertools
    actual_lists.sort()
    actual_lists = list(k for k,_ in itertools.groupby(actual_lists))
    print("Indexified lists: ")
    #Printing out all combinations, of lists, in reality, which ones are not 
    #If ALL of these indicies are contained within one of the actual_lists, then must be unsafe. 
    list1 = [1,2,3,4,5]
    #Powerlist, used as indicies, 
    #Test if any of these, if it satisfies the condition, sublist, 
    print("list1:")
    invalid = 0
    for x in range(0,5):
        list1[x] *= -1
        for y in range(0,5):
            list1[y] *= -1
            found = False
            for l in actual_lists:
                if(set(l).issubset(list1) and not found):
                    invalid+=1
                    found = True
    #print("number of invalid lists:", invalid)
                
            
    #What are the combinations, that we actually accept? That we don't return unsure on? 
    #for l in actual_lists:
        #print(l)
    #print("number of conditions", len(actual_lists))
    if(bias > 0): 
        conditions.append(input_activation_print_conversion(actual_lists, 1, node, len(weights)))
    else: 
        conditions.append(input_inactivation_print_conversion(actual_lists, 1, node, len(weights)))
	    


def input_inactivation_print_conversion(activation_lists, layer, node, no_weights):
    s = "Activation(" + str(layer) + "," + str(node+1) + ",edge_results) = \n "
    #The constant formula, for all of them, for the activity semantics: 
    
    for i in range(0, len(activation_lists)):
        #New DNF for every list, 
        s += "("
        for j in range(0, len(activation_lists[i])): 
            #We need to add one because sequences in FDR are 1-indexed, 
            #If its negative it denotes an inactive condition: 
            if(activation_lists[i][j] < 0): 
                s += "extract_sequence(" + str(abs(activation_lists[i][j])) + "," + "edge_results) == inactive"
            else: 
                s += "extract_sequence(" + str(activation_lists[i][j]) + "," + "edge_results) == active"
            
            if(j != len(activation_lists[i])-1):
                s += " and "
            
        if(i != len(activation_lists)-1):
            s += ") or \n"
        else: 
            s += ") \n"
    return s


def input_activation_print_conversion(activation_lists, layer, node, no_weights):
    s = "InActivation(" + str(layer) + "," + str(node+1) + ",edge_results) = \n "
    #The constant formula, for all of them, for the activity semantics: 
    
    for i in range(0, len(activation_lists)):
        #New DNF for every list, 
        s += "("
        for j in range(0, len(activation_lists[i])): 
            #We need to add one because sequences in FDR are 1-indexed, 
            #If its negative it denotes an inactive condition: 
            if(activation_lists[i][j] < 0): 
                s += "extract_sequence(" + str(abs(activation_lists[i][j])) + "," + "edge_results) == inactive"
            else: 
                s += "extract_sequence(" + str(activation_lists[i][j]) + "," + "edge_results) == active"
            
            if(j != len(activation_lists[i])-1):
                s += " and "
            
        if(i != len(activation_lists)-1):
            s += ") or \n"
        else: 
            s += ") \n"
    return s


#Print conversion, to FDR, for the activation logic: will be because we do have a positive bias. 
#Positive or negative, these are ACTIVE, if they are ACTIVE. 
#We add a constant one, this is active, we need to add: for the NUMBER OF WEIGHTS WE HAVE.
#1 or 0 indexed, for the sequence? 1-indexed, we need to add one.  
#Cuz were using sequences, we need to be using sets, check that the rest of them, we need to check all of them. 
#We need to group all of them for FDR. 

#NO BASE INSECURITY. 
def activation_print_conversion(activation_lists, layer, node, no_weights):
    s = "InActivation(" + str(layer) + "," + str(node+1) + ",edge_results) = \n "
    #The constant formula, for all of them, for the activity semantics: 
    
    for i in range(0, len(activation_lists)):
        #New DNF for every list, 
        s += "("
        for j in range(0, len(activation_lists[i])): 
            #We need to add one because sequences in FDR are 1-indexed, 
            s += "extract_sequence(" + str(activation_lists[i][j]+1) + "," + "edge_results) == inactive"
            
            if(j != len(activation_lists[i])-1):
                s += " and "
            
        if(i != len(activation_lists)-1):
            s += ") or \n"
        else: 
            s += ") \n"
    return s

#No base insecurity either, 
def inactivation_print_conversion(activation_lists, layer, node, no_weights):
    s = "Activation(" + str(layer) + "," + str(node+1) + ",edge_results) = \n "
    #The constant formula, for all of them, for the activity semantics: 

    for i in range(0, len(activation_lists)):
        #New DNF for every list, 
        s += "("
        for j in range(0, len(activation_lists[i])): 
            #We need to add one because sequences in FDR are 1-indexed, 
            s += "extract_sequence(" + str(activation_lists[i][j]+1) + "," + "edge_results) == active"
            
            if(j != len(activation_lists[i])-1):
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

#If the sum is STRICTLY GREATER, otherwise, who cares.
#This is now WHEN WE ARE UNSURE ABOUT THE INACTIVATION, when it could be positive, but we aren't sure.  
def is_inactivation_condition(l, bias):
	#true, if the sum is less than or equal to the bias, the absolute value, then it must be inactive, still, 
	if(sum(l) > abs(bias)):
		return True
	else:
		return False
		
#positive? if the absolute value of sum is greater tham the bias, calls a different function

#This is actually, what are those conditions under which WE ARE UNSURE. 

#bias is positive. 0.8
#if the absolute sum of all is less than the bias, we must have activation.
#Why the absolulte value? because its negative, a bunch of negate numbers together. 
#We need to encode the UNSURE SEMANTICS. 
#When, This is the UNSURE CONDITIONS. 
#Now these are THE ONLY CONDITIONS UNDER WHICH WE ARE UNSURE, EVERY OTHER CONDITOIN WE ARE SURE OF, FALSE MEANS ACTIVE HERE. 
def is_activation_condition(l, bias):
		if(abs(sum(l)) >= bias):
			return True
		else:
			return False
			
def pos_weight_extraction(layer, node, weights, bias):
		mod_list = list(weights)
		#need to collect different values, just the negative values, for positive weights
		mod_list = list(filter(lambda x: x <= 0, mod_list))
		powerlists = list_powset(mod_list)
		#remove empty list from powerset, we dont need it.
		activation_cons = [is_activation_condition(l, bias) for l in powerlists]
		
		#print all sequences
		
		#translate them into indicies.
		activation_lists = [powerlists[i] for i in range(0, len(powerlists)) if activation_cons[i] == True]
		
		#convert to index of original list
		for i in range(0, len(activation_lists)):
			for j in range(0, len(activation_lists
			[i])):
				activation_lists[i][j] = weights.index(activation_lists[i][j])
		return activation_print_conversion(activation_lists, layer, node, len(weights))        
		

		
def neg_weight_extraction(layer, node, weights, bias):
		#dont remove because we need index.
		#we need the original index to make the predictae, but we need to seach the smallest positive value, make sublists.
		mod_list = list(weights)
		#need to collect different values, just the negative values, for positive weights
		mod_list = list(filter(lambda x: x > 0, mod_list))
		powerlists = list_powset(mod_list)
		inactivation_cons = [is_inactivation_condition(l, bias) for l in powerlists ]
		
		#print all sequences
		
		#translate them into indicies.
		inactivation_lists = [powerlists[i] for i in range(0, len(powerlists)) if inactivation_cons[i] == True]

		#convert to index of original list
		for i in range(0, len(inactivation_lists)):
			for j in range(0, len(inactivation_lists
			[i])):
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
	gen_input_conditions()
	#gen_layer_conditions(2)