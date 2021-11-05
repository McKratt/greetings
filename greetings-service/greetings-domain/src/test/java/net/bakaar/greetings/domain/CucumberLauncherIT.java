package net.bakaar.greetings.domain;

import org.junit.platform.suite.api.IncludeEngines;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectFiles({
        @SelectFile("../bdd/features/GreetingsCreation.feature"),
        @SelectFile("../bdd/features/GreetingsUpdate.feature")
})
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "ch.mobi.jss.greetings.servicetest.controller.glue")
public class CucumberLauncherIT {
}
