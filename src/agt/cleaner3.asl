
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

+pos(X) : dirty <- .print("Found dirt at ", X); suck; !move.
+pos(_) : clean  <- !move.

// plans to move
+!move : pos(1) <- right.
+!move : pos(2) <- down.
+!move : pos(3) <- up.
+!move : pos(4) <- left.

/*
comments
- the moving strategy is re-factored to use a (perform) goal
  (the goal '!move')
- this agent reacts to the perception of its location, but the
  reaction creates a new goal (to move)
- to change the moving strategy we only need to change the
  way the goal "move" is achieved (the plans for triggering
  event '+!move')

problem
- suppose that actions may fail, in this case, after performing 'up',
  for example, the agent may remain in the same place and then no new
  location is perceived: the agent will stop moving
  (this is only conceptually a problem since the environment was not
  coded to simulate action failures, i.e., the environment model is
  deterministic).
*/
