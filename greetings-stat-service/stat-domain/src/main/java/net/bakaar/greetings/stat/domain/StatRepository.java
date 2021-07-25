package net.bakaar.greetings.stat.domain;

import java.util.concurrent.CompletableFuture;

// I don't want to have Reactor Class here because then this interface can only by implemented by Reactor stack which it is not my intention.
public interface StatRepository {
    void put(GreetingsStats stats);

    // If I don't make it Future, I have an issue in Netty during running the blocking implementation.
    CompletableFuture<GreetingsStats> pop();
}
