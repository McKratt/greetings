package net.bakaar.greetings.application;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {
        "../../bdd/features"
},
        glue = "net.bakaar.greetings.application.glue",
        tags = "not @stat")
public class CucumberLauncherIT {
}
