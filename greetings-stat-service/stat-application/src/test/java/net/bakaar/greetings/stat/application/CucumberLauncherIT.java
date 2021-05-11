package net.bakaar.greetings.stat.application;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "../../bdd/features/GreetingsStats.feature",
        glue = "net.bakaar.greetings.stat.application.glue")
public class CucumberLauncherIT {
}
