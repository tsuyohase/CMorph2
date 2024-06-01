#!/bin/bash

scenarios=("random" "centerLeft")
totalLoads=("0.2" "0.4" "0.6" "0.8")
serverFuncs=("monotonic" "convex" "constant")
# networkFuncs=("constant" "pow 1" "pow 4")
networkFuncs=("pow 8")


for scenario in "${scenarios[@]}"
do
  for totalLoad in "${totalLoads[@]}"
  do
    for serverFunc in "${serverFuncs[@]}"
    do
      for networkFunc in "${networkFuncs[@]}"
      do
        gradle simulator --args="-scenario ${scenario} -totalLoad ${totalLoad} -serverFunc ${serverFunc} -networkFunc ${networkFunc}"
      done
    done
  done
done