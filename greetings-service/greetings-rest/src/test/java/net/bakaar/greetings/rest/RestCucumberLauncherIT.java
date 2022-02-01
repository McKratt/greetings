package net.bakaar.greetings.rest;

import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectFiles({
        @SelectFile("../../bdd/features/GreetingsCreation.feature"),
        @SelectFile("../../bdd/features/GreetingsUpdate.feature")
})
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "net.bakaar.greetings.rest.glue")
public class RestCucumberLauncherIT {
}