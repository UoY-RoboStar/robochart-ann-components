#!/bin/bash
command="../build/Marabou ../resources/nnet/anglepid_ann/anglepidann.nnet ../resources/properties/anglepid_property_test.txt"

file_path="logs/anglepid_conformance_testing.txt"
property_file="anglepid_property.txt"
new_command="../build/Marabou ../resources/nnet/anglepid_ann/anglepidann.nnet $property_file"

runs=101
#Counter to show how many increments has been done. 
increment_counter=0
#To show the increment needs to be done for the end
#end_val="0.009"	
#get end_val="0.01" so, we have overlapping bounds, but should still work, also now we don't have gaps. 
end_val="0.01"
#To show how much it is to increment 0.001
increment_val_min="0.01"
#Epsilon increases by, needs to increase by the target value, 
#Epsilon = target_val + 
#Needs to get lower bound, 
#Need to make an upper bound property as well, 
#Is 0.085, passes with 0.085
#Proved for 0.05.
#Failed for 0.01.
#Try 0.03? Passed. 
#Try 0.085, 
#0.02, PASSED for 0.02
#Failed for 0.015. 
#Failed for 0.016
#Passed for 0.017, this is our minimum.
epsilon="0.085" #Epsilon
x0_min="0"
x0_max="0"
x1_min="0"
x1_max="0"
#Y0 just has a max, which is a <=, which starts at -0.085, the error bound
#It 
#Formula for y0_min and max

#set fulfills that property. 
spec=`echo "(0.92 * $x0_min) + (0.08 * $x1_min)" | bc -l`
y0_min=`echo "$spec + $epsilon" | bc -l`
y0_max=`echo "$spec - $epsilon" | bc -l`
#Reset file.
echo "" > $file_path
#Update file in loop and overwrite variable
i_dec="0"
j_dec="0"
#1-based index. [1-100]
for i in $(seq $runs $END)
do
	for j in $(seq $runs $END)
	do
		
	   #In every loop, we run the upper and lower bound property 
	   
		#First, we set property file. 
		#We have a counter i, $i that goes from i 
		#Then run it. 
	   #We check every combination, update i every 100, j every 1. 
	   #min property setup: 
	   #Formula, 100, [0-100], starts at 1, need to decement
	   i_dec=`echo "$i - 1" | bc -l`
	   j_dec=`echo "$j - 1" | bc -l`
	   
	   #[0-100], 99th, 100 should be, 1, x_min = i + (increment_val_min * i)
	   x0_min=`echo "($increment_val_min * $i_dec)" | bc -l`
	   x0_max=`echo "($x0_min + $end_val)" | bc -l`
	   x1_min=`echo "($increment_val_min * $j_dec)" | bc -l`
	   x1_max=`echo "($x1_min + $end_val)" | bc -l`
	   #Then update specification: 
	   
	   #All we need now is to update x0 and x1 min and max:
	   
	   spec_min=`echo "(0.92 * $x0_min) + (0.08 * $x1_min)" | bc -l`
	   spec_max=`echo "(0.92 * $x0_max) + (0.08 * $x1_max)" | bc -l`
	   
	   #Update y0_min: 
	   y0_min=`echo "$spec_min + $epsilon" | bc -l`
printf "x0 >= %g
x0 <= %g
x1 >= %g
x1 <= %g
y0 >= %g" $x0_min $x0_max $x1_min $x1_max $y0_min > $property_file
	   run_lower_bound=$($new_command)
	   echo "Run_$i_lower_bound:" >> $file_path
	   echo $run_lower_bound >> $file_path
	   echo "" >> $file_path
	   
	   #Update y0_max
	   y0_max=`echo "$spec_max - $epsilon" | bc -l`
	   #Max property setup: 
printf "x0 >= %g
x0 <= %g
x1 >= %g
x1 <= %g
y0 <= %g" $x0_min $x0_max $x1_min $x1_max $y0_max > $property_file

	   #Then change property file and run again and store in upper bound.
	   run_upper_bound=$($new_command)
	   echo "Run_$i_upper_bound:" >> $file_path
	   echo $run_upper_bound >> $file_path
	   
	   echo "" >> $file_path 
	done
	echo "i runs done: $i out of $runs"
done

echo "$new_command"

