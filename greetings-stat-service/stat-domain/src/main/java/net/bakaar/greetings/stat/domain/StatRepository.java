package net.bakaar.greetings.stat.domain;

public interface StatRepository {
    void put(GreetingsStats stats);

    GreetingsStats pop();
}
