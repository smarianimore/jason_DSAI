
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

+dirty <- suck.

+pos(1) <- right.
+pos(2) <- down.
+pos(3) <- up.
+pos(4) <- left.

/* 
comments
- this is a reactive agent, which is easy to implement with Jason,
  but note that the language is meant for cognitive agents
- as a consequence, all plans have an empty context (normally used
  to check the situation currently believed by the agent)

problems
- the robot may leave dirt behind, and you will get messages like
    [VCWorld] suck in a clean location!
- reason: the events related to location ("+pos(...)") are
  selected before the "dirty" event, so the suck action is performed
  after the move action in a possibly clean location
*/
