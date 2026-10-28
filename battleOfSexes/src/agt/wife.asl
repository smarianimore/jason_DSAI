// Wife: prefers meeting friends. Protocol from slide 41 ("A better battle of the sexes").
// offer(Tonight, Later, N): Tonight's activity now, then Later's activity for the next N weekends.

preferred(friends).
other(movie).

// Husband proposes his favourite activity: counter with mine
+proposal(_)[source(husband)] : preferred(P) <-
    .wait(1500);
    !say(husband, "Friends?");
    .send(husband, tell, proposal(P)).

// Husband's offer: counter-offer with mirrored roles and same length
+offer(_, _, N)[source(husband)] : preferred(P) & other(O) <-
    .wait(1500);
    if (N == 1) { Msg = "Tonight friends, next time movie" }
    else { .concat("Tonight friends, next ", N, " times movie", Msg) }
    !say(husband, Msg);
    .send(husband, tell, offer(P, O, N)).

// Husband accepted my last offer
+accept(offer(T, L, N))[source(husband)] <-
    .wait(1500);
    !say(husband, "Deal!");
    .send(husband, tell, deal(offer(T, L, N)));
    !execute(T, L, N).

+!execute(T, L, N) <-
    attend(0, T);
    for (.range(I, 1, N)) { .wait(1500); attend(I, L) }.

+!say(To, Msg) <- say(wife, To, Msg).
