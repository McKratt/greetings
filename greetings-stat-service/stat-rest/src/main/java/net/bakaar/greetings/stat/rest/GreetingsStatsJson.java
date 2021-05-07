package net.bakaar.greetings.stat.rest;

import java.util.Map;

public record GreetingsStatsJson(Map<String, Long> counters) {

}
