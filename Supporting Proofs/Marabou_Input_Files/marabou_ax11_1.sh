#!/bin/bash
command="../build/Marabou ../resources/nnet/acasxu/ACASXU_experimental_v2a_1_1.nnet ../resources/properties/acas_property_1.txt"
file_path="logs/ax11_1_marabou.txt"
runs=10

#Reset file.
echo "" > $file_path
#Update file in loop and overwrite variable
for i in $(seq $runs $END)
do
   echo "Run_$i:"
   run_tmp=$($command)
   echo "Run_$i:" >> $file_path
   echo $run_tmp >> $file_path
   echo "" >> $file_path
done

echo "$command"

