test1: Output
cost = 3.4, visitOrder = [1, 8, 2, 9, 11, 3, 4, 5, 10, 6, 7]
cost = 1.4, visitOrder = [1, 7, 8, 2, 3, 4, 6, 10, 5, 9, 11]
cost = 3.4, visitOrder = [1, 8, 2, 9, 11, 3, 4, 5, 10, 6, 7]
heuristic: cost = 3.3969307170000005, 0 milliseconds
mine: cost = 3.3969307170000005, 0 milliseconds
backtrack: cost = 1.3566775349999998, 75 milliseconds

The heuristic method finds the path quick, but at a higher path cost. 
The backtracking method finds the path slower, but at a lower path cost. 

The backtracking method seemed to find the node not only with a better path cost,
but also has a different order in which it traverses the graph. I think this varies from
the heuristic because the recursive call is calling a depth first search while the heuristic
is a breadth first search. The methods traverse different ways in this, so they usually 
different paths when the graph is big enough.
test2: Output
cost = 3.4, visitOrder = [1, 8, 2, 9, 11, 3, 4, 5, 10, 6, 7]
cost = 1.4, visitOrder = [1, 7, 8, 2, 3, 4, 6, 10, 5, 9, 11]
cost = 3.4, visitOrder = [1, 8, 2, 9, 11, 3, 4, 5, 10, 6, 7]
heuristic: cost = 3.3969307170000005, 0 milliseconds
mine: cost = 3.3969307170000005, 0 milliseconds
backtrack: cost = 1.3566775349999998, 74 milliseconds

test3: output
cost = 3.4, visitOrder = [1, 8, 2, 9, 11, 3, 4, 5, 10, 6, 7]
cost = 1.4, visitOrder = [1, 7, 8, 2, 3, 4, 6, 10, 5, 9, 11]
cost = 3.4, visitOrder = [1, 8, 2, 9, 11, 3, 4, 5, 10, 6, 7]
heuristic: cost = 3.3969307170000005, 0 milliseconds
mine: cost = 3.3969307170000005, 0 milliseconds
backtrack: cost = 1.3566775349999998, 78 milliseconds