#!/bin/bash

# scenarios=("random" "centerLeft")
scenarios=("read")
totalLoads=("0.4")
serverFuncs=("monotonic" "convex" "constant")
# networkFuncs=("constant" "pow 1" "pow 4")
networkFuncs=("pow 1" "pow 8" "constant")


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