package net.bakaar.greetings.servicetest;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {
        "../../bdd/features/GreetingsCreation.feature",
        "../../bdd/features/GreetingsUpdate.feature"
},
        glue = "net.bakaar.greetings.servicetest.glue")
public class CucumberLauncher {
}
