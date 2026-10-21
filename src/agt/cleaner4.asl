
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

!clean. // initial goal

+!clean : clean <- !move; !clean.
+!clean : dirty <- suck; !move; !clean.
-!clean         <- !clean.

+!move : pos(1) <- right.
+!move : pos(2) <- down.
+!move : pos(3) <- up.
+!move : pos(4) <- left.

/* 
comments
- this agent is not reactive at all; it has no behaviour which is
  triggered by an external event, that is, a perception of change
  in the environment (note however that BDI agents typically have
  both goal-directed and reactive behaviour)
- instead, the agent has a *maintenance goal* (the '!clean' goal) and
  is blindly committed towards it (if it ever fails, the goal is just
  adopted again -- see below)
- this goal is implemented by a form of infinite loop using recursive
  plans: all plans to achieve 'clean' finish by adding 'clean' itself
  as a new goal
- if anything fails in an attempt to achieve the goal 'clean', the
  contingency plan (-!clean) reintroduces the goal again at all
  circumstances (note the empty plan context); this is what causes the
  "blind commitment" behaviour mentioned above
- this agent is thus more 'robust' against action failures
*/
