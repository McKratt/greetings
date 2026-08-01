package net.bakaar.greetings.e2e;

import org.junit.platform.suite.api.*;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectFile("../bdd/features/GreetingsCreation.feature")
@SelectFile("../bdd/features/GreetingsUpdate.feature")
@SelectFile("../bdd/features/GreetingsStats.feature")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "net.bakaar.greetings.e2e.glue")
@IncludeTags({"e2e"})
public class E2eCucumberLauncherTest {
}
