package net.bakaar.greetings.rest;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {
        "../../bdd/features"
},
        glue = "net.bakaar.greetings.rest.glue",
        tags = "not @stat")
public class CucumberLauncherIT {
}
