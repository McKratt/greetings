package net.bakaar.greetings.e2e;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;

@RunWith(Cucumber.class)
@CucumberOptions(features = {
        "../bdd/features/GreetingsCreation.feature",
        "../bdd/features/GreetingsUpdate.feature"
//        "../bdd/features/GreetingsStats.feature"
},
        glue = "net.bakaar.greetings.e2e.glue",
        tags = "@e2e")
public class CucumberLauncherTest {

    @ClassRule
    public static DockerComposeContainer environment = new DockerComposeContainer(
            new File("src/test/resources/compose-test.yaml"))
            .withExposedService("greetings_1", 8080);
}
