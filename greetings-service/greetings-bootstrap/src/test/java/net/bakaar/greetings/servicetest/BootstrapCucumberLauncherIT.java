package net.bakaar.greetings.servicetest;

import io.cucumber.junit.platform.engine.Cucumber;
import org.testcontainers.containers.PostgreSQLContainer;

@Cucumber
public class BootstrapCucumberLauncherIT {

    //  @Container FIXME This annotation doesn't seem to work with Cucumber even if I put it in the step file
    public static final PostgreSQLContainer dbContainer = new PostgreSQLContainer("postgres")
            .withDatabaseName("greetings")
            .withUsername("foo")
            .withPassword("secret");

    static {
        dbContainer.start();
    }
}
