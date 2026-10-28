
/*

Very simple vacuum cleaner agent in a world that has only four locations.

Perceptions:
. dirty: the current location has dirty
. clean: the current location is clean
. pos(X): the agent position is X (0 < X < 5).

Actions:
. suck: clean the current location
. left, right, up, down: move the agent

*/

// plans for dirty location
+pos(1) : dirty <- suck; right.
+pos(2) : dirty <- suck; down.
+pos(3) : dirty <- suck; up.
+pos(4) : dirty <- suck; left.

// plans for clean location
+pos(1) : clean <- right.
+pos(2) : clean <- down.
+pos(3) : clean <- up.
+pos(4) : clean <- left.

/* 
comments
- again a rather reactive agent
- the selection of plans is based on context (perceptual beliefs
  in this case)
- it solves the problems of the previous solution

problems
- the moving strategy is coded in two sets of plans, so to change
  the strategy we need change all plans
- if you leave the agent running for a long time, it eventually stops.
  the reason is that sometimes the robot does not perceive neither
  dirty nor clean and thus no plan are selected.
*/
