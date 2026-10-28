// Husband: prefers the movie. Protocol from slide 41 ("A better battle of the sexes").
// offer(Tonight, Later, N): Tonight's activity now, then Later's activity for the next N weekends.

preferred(movie).
other(friends).

!start.

+!start : preferred(P) <-
    .wait(1500);
    !say(wife, "Movie?");
    .send(wife, tell, proposal(P)).

// Wife counters with her own activity: propose a plan alternating activities
+proposal(Other)[source(wife)] : preferred(P) & other(O) <-
    .wait(1500);
    !say(wife, "Tonight movie, next time friends");
    .send(wife, tell, offer(P, O, 1)).

// Wife counter-offers: concede after the second round
+offer(T, L, N)[source(wife)] : N < 2 & preferred(P) & other(O) <-
    .wait(1500);
    N1 = N + 1;
    .concat("Tonight movie, next ", N1, " weekends friends", Msg);
    !say(wife, Msg);
    .send(wife, tell, offer(P, O, N1)).

+offer(T, L, N)[source(wife)] : N >= 2 <-
    .wait(1500);
    !say(wife, "Deal!");
    .send(wife, tell, accept(offer(T, L, N)));
    !execute(T, L, N).

+deal(offer(T, L, N))[source(wife)] <- .print("Wife confirmed the deal.").

+!execute(T, L, N) <-
    attend(0, T);
    for (.range(I, 1, N)) { .wait(1500); attend(I, L) }.

+!say(To, Msg) <- say(husband, To, Msg).
