package net.bakaar.greetings.stat.domain;

import java.util.concurrent.CompletableFuture;

public interface StatRepository {
    CompletableFuture<Void> put(GreetingsStats stats);

    CompletableFuture<GreetingsStats> pop();
}
