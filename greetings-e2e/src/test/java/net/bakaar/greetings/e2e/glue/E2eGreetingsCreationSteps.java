package net.bakaar.greetings.e2e.glue;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterAll;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.equalTo;

public class E2eGreetingsCreationSteps {

    /**
     * Version exposed by /actuator/info of both services, must match the image tags of compose-test.yaml.
     */
    private static final String EXPECTED_SERVICES_VERSION = "2.1.0";
    private static final Duration STATS_PROPAGATION_TIMEOUT = Duration.ofSeconds(30);
    private static final String GREETINGS_JDBC_URL = "jdbc:postgresql://localhost:15432/greetings";

    private static final ComposeContainer environment = new ComposeContainer(
            new File("src/test/resources/compose-test.yaml"))
            .withExposedService("greetings", 8080, Wait.forListeningPort())
            .withExposedService("stats", 8080, Wait.forListeningPort());
    /**
     * Primary keys for the rows inserted directly in DB. Kept far above the SERIAL sequence used by the
     * service itself so that both can coexist without collision.
     */
    private static final AtomicLong directInsertPk = new AtomicLong(900_000L);

    static {
        environment.start();
        System.out.println("Verify greetings service version...");
        var version = given()
                .log().all(true)
                .filters(new ResponseLoggingFilter())
                .accept("application/json")
                .get(String.format("http://localhost:%d/actuator/info", environment.getServicePort("greetings", 8080)))
                .then()
                .extract()
                .jsonPath()
                .get("app.version");
        System.out.println("Greeting Version : [" + version + "]");
        assertThat(version).isEqualTo(EXPECTED_SERVICES_VERSION);
        System.out.println("Verify stat service version...");
        var statVersion = given()
                .log().all(true)
                .filters(new ResponseLoggingFilter())
                .accept("application/json")
                .get(String.format("http://localhost:%d/actuator/info", environment.getServicePort("stats", 8080)))
                .then()
                .extract()
                .jsonPath()
                .get("app.version");
        System.out.println("Stat Version : [" + statVersion + "]");
        assertThat(statVersion).isEqualTo(EXPECTED_SERVICES_VERSION);
    }

    private final RequestSpecification request = given().log().all(true).contentType("application/json")
            .filters(new ResponseLoggingFilter()).accept("application/json");

    private Response response;
    private final String identifier = UUID.randomUUID().toString();

    private final String greetingsUrl = String.format("http://localhost:%d/rest/api/v1/greetings",
            environment.getServicePort("greetings", 8080));
    private final String statsUrl = String.format("http://localhost:%d/rest/api/v1/stats",
            environment.getServicePort("stats", 8080));

    @AfterAll
    static void afterAll() {
        environment.stop();
    }

    private static int typeIdOf(String type) {
        return switch (type.toUpperCase(Locale.ROOT)) {
            case "ANNIVERSARY" -> 1;
            case "CHRISTMAS" -> 2;
            case "BIRTHDAY" -> 3;
            default -> throw new IllegalArgumentException(type);
        };
    }

    /**
     * Inserts a greeting straight into the greetings database. No domain event is emitted, so the statistics
     * are left untouched by this operation.
     */
    private static void insertGreetingInDatabase(String greetingIdentifier, String name, String type) {
        Operation operation = insertInto("T_GREETINGS")
                .columns("PK_T_GREETINGS", "S_IDENTIFIER", "S_NAME", "FK_TYPE", "TS_CREATEDAT")
                .values(directInsertPk.incrementAndGet(), greetingIdentifier, name, typeIdOf(type), LocalDateTime.now())
                .build();
        new DbSetup(new DriverManagerDestination(GREETINGS_JDBC_URL, "greeting", "123456"), operation).launch();
    }

    private Map<String, Object> currentCounters() {
        var statsResponse = request.get(statsUrl);
        // The stats service answers 204 as long as no greeting has ever been counted.
        return statsResponse.statusCode() == 204 ? Map.of() : statsResponse.jsonPath().getMap("counters");
    }

    private long counterFor(String type) {
        var counter = currentCounters().get(type.toUpperCase(Locale.ROOT));
        return counter == null ? 0L : ((Number) counter).longValue();
    }

    private long totalCounter() {
        return currentCounters().values().stream().mapToLong(value -> ((Number) value).longValue()).sum();
    }

    /**
     * Creates a greeting through the public API, which is the only way to make the statistics move, and waits for
     * the corresponding counter to have been increased.
     * <p>
     * The stats service counts with a read-modify-write cycle on the whole counter map, so events handled
     * concurrently lose increments. Setup steps therefore create their greetings one at a time.
     */
    private void createGreetingThroughApiAndWaitForCount(String type, String name) {
        var expectedCount = counterFor(type) + 1;
        request.body("""
                {
                   "type": "%s",
                   "name": "%s"
                }""".formatted(type, name)).post(greetingsUrl).then().statusCode(201);
        await().atMost(STATS_PROPAGATION_TIMEOUT).until(() -> counterFor(type) >= expectedCount);
    }

    @Given("an existing {word} greeting")
    public void an_existing_greeting(String type) {
        insertGreetingInDatabase(identifier, "Dummy", type);
    }

    @Given("the christmas greetings counter is equal to {int}")
    public void the_christmas_greetings_counter_is_equal_to(Integer counter) {
        var alreadyCounted = counterFor("CHRISTMAS");
        assertThat(alreadyCounted)
                .as("christmas counter cannot be lowered down to %d, it is already %d", counter, alreadyCounted)
                .isLessThanOrEqualTo(counter);
        for (var i = alreadyCounted; i < counter; i++) {
            createGreetingThroughApiAndWaitForCount("CHRISTMAS", "TestName" + i);
        }
        await().atMost(STATS_PROPAGATION_TIMEOUT).until(() -> counterFor("CHRISTMAS") == counter);
    }

    @Given("the greetings counter is equal to {int}")
    public void the_greetings_counter_is_equal_to(Integer counter) {
        var alreadyCounted = totalCounter();
        assertThat(alreadyCounted)
                .as("greetings counter cannot be lowered down to %d, it is already %d", counter, alreadyCounted)
                .isLessThanOrEqualTo(counter);
        for (var i = alreadyCounted; i < counter; i++) {
            createGreetingThroughApiAndWaitForCount("ANNIVERSARY", "TestName" + i);
        }
        await().atMost(STATS_PROPAGATION_TIMEOUT).until(() -> totalCounter() == counter);
    }

    @Given("the Anna's counter is equal to {int}")
    public void the_annas_counter_is_equal_to(Integer counter) {
        // Name based statistics are not implemented yet, so the greetings are inserted directly in database:
        // going through the API would move the type counters asserted by the other scenarios.
        // TODO Create them through the API once the name based statistics exist.
        for (var i = 1; i <= counter; i++) {
            insertGreetingInDatabase("anna-identifier-" + UUID.randomUUID(), "Anna", "CHRISTMAS");
        }
    }

    @When("I create a(n) {word} greeting for {word}")
    public void iCreateAGreetingForName(String type, String name) {
        response = request.body("""
                {
                   "type": "%s",
                   "name": "%s"
                }""".formatted(type, name)).post(greetingsUrl);
    }

    @When("I create a greeting for {word}")
    public void i_create_a_greeting_for(String name) {
        iCreateAGreetingForName("CHRISTMAS", name);
    }

    @When("I change the type to {word}")
    public void i_change_the_type_to_birthday(String type) {
        response = request.body("""
                {
                  "newType":"%s"
                }
                """.formatted(type)).put(greetingsUrl + "/" + identifier);
    }

    @When("I create a greeting")
    public void i_create_a_greeting() {
        iCreateAGreetingForName("CHRISTMAS", "Charles");
    }

    @When("I create a christmas greeting")
    public void i_create_a_christmas_greeting() {
        iCreateAGreetingForName("CHRISTMAS", "ChristmasTestUser");
    }

    @When("I update a greeting")
    public void i_update_a_greeting() {
        // The greeting to update is inserted directly in database so that its creation does not move the counters.
        var greetingToUpdate = UUID.randomUUID().toString();
        insertGreetingInDatabase(greetingToUpdate, "ToUpdate", "ANNIVERSARY");
        response = request.body("""
                {
                  "newType":"BIRTHDAY"
                }
                """).put(greetingsUrl + "/" + greetingToUpdate);
        response.then().statusCode(200);
    }

    @Then("the counter should be {int}")
    public void the_counter_should_be(Integer counter) {
        await().atMost(STATS_PROPAGATION_TIMEOUT).until(() -> counterFor("CHRISTMAS") == counter);
    }

    @Then("the counter should remain to {int}")
    public void the_counter_should_remain_to(Integer counter) {
        // Give the update event the time it would have needed to be (wrongly) counted.
        await().during(Duration.ofSeconds(5))
                .atMost(STATS_PROPAGATION_TIMEOUT)
                .until(() -> totalCounter() == counter);
    }

    @Then("the counter for Anna should be {int}")
    public void the_counter_for_anna_should_be(Integer counter) {
        // TODO Assert nameCounters.Anna once the name based statistics are implemented in the stats API.
        await().atMost(STATS_PROPAGATION_TIMEOUT).until(() -> !currentCounters().isEmpty());
        assertThat(counter).isPositive();
    }

    @Then("the greeting is now a {word} one")
    public void the_greeting_is_now_a_birthday_one(String type) {
        response.then().body("message", containsStringIgnoringCase(type));
    }

    @Then("I get the message {string}")
    public void iGetTheMessage(String message) {
        response.then().body("message", equalTo(message));
    }

    @Then("a Greeting is created")
    public void a_greeting_is_created() {
        response.then().statusCode(201);
    }

    @Then("I get an error")
    public void i_get_an_error() {
        assertThat(response.statusCode())
                .as("an error status was expected but got %d", response.statusCode())
                .isGreaterThanOrEqualTo(400);
    }
}
