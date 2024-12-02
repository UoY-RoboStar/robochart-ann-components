#!/bin/bash
command="../build/Marabou ../resources/nnet/tn/tn.nnet ../resources/properties/tn_properties/tn_property_1.txt"
file_path="logs/tn_p1_marabou.txt"
runs=10

#Reset file.
echo "" > $file_path
#Update file in loop and overwrite variable
for i in $(seq $runs $END)
do
   run_tmp=$($command)
   echo "Run_$i:" >> $file_path
   echo $run_tmp >> $file_path
   echo "" >> $file_path 
done

echo "$command"

