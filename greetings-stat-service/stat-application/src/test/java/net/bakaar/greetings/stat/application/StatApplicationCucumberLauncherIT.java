package net.bakaar.greetings.stat.application;

import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectFiles(
        @SelectFile("../../bdd/features/GreetingsStats.feature")
)
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "net.bakaar.greetings.stat.application.glue")
class StatApplicationCucumberLauncherIT {
}
