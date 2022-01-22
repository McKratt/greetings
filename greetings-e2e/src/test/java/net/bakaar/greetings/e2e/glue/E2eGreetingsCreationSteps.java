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
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.ninja_squad.dbsetup.Operations.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.equalTo;

public class E2eGreetingsCreationSteps {

    private static final DockerComposeContainer environment = new DockerComposeContainer(
            new File("src/test/resources/compose-test.yaml"))
            .withExposedService("greetings_1", 8080)
            .withExposedService("stats_1", 8080);

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
        assertThat(version).isEqualTo("2.0.0");
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
        assertThat(statVersion).isEqualTo("2.0.0");
    }

    private final RequestSpecification request = given().log().all(true).contentType("application/json")
            .filters(new ResponseLoggingFilter()).accept("application/json");
    private static final Operation DELETE_ALL = deleteAllFrom("T_GREETINGS");

    private Response response;
    private final String identifier = UUID.randomUUID().toString();
    private final String url = String.format("http://localhost:%d/rest/api/v1/greetings",
            environment.getServicePort("greetings", 8080));

    @AfterAll
    static void afterAll() {
        environment.stop();
    }

    @Given("an existing {word} greeting")
    public void an_existing_greeting(String type) {
        // FIXME Make it dynamic from DB Values.
        var typeId = switch (type.toUpperCase()) {
            case "ANNIVERSARY" -> 1;
            case "CHRISTMAS" -> 2;
            case "BIRTHDAY" -> 3;
            default -> throw new IllegalArgumentException(type);
        };
        Operation operation =
                sequenceOf(
                        DELETE_ALL,
                        insertInto("T_GREETINGS")
                                .columns("PK_T_GREETINGS", "S_IDENTIFIER", "S_NAME", "FK_TYPE", "TS_CREATEDAT")
                                .values(999, identifier, "Dummy", typeId, LocalDateTime.now())
                                .build()
                );
        DbSetup dbSetup = new DbSetup(new DriverManagerDestination("jdbc:postgresql://localhost:15432/greetings", "greeting", "123456"), operation);
        dbSetup.launch();
    }

    @When("I create a(n) {word} greeting for {word}")
    public void iCreateAGreetingForName(String type, String name) {
        response = request.body("""
                {
                   "type": "%s",
                   "name": "%s"
                }""".formatted(type, name)).post(url);
    }

    @When("I change the type to {word}")
    public void i_change_the_type_to_birthday(String type) {
        response = request.body("""
                {
                  "newType":"%s"
                }
                """.formatted(type)).put(url + "/" + identifier);
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
}
