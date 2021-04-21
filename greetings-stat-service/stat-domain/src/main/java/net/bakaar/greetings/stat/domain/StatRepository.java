package net.bakaar.greetings.stat.domain;

import java.util.concurrent.CompletableFuture;
//TODO refactor, domain should not know if it is reactive or not, so no CompletableFuture here.
public interface StatRepository {
    CompletableFuture<Void> put(GreetingsStats stats);

    CompletableFuture<GreetingsStats> pop();
}
