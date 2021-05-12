package net.bakaar.greetings.domain;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {
        "../../bdd/features"
},
        glue = "net.bakaar.greetings.domain.glue",
        tags = "not @stat")
public class CucumberLauncherIT {
}
