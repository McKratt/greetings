package net.bakaar.greetings.stat.message;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "../../bdd/features/GreetingsStats.feature",
        glue = "net.bakaar.greetings.stat.message.glue")
public class CucumberLauncherIT {
}
