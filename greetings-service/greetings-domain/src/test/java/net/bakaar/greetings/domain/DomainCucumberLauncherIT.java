package net.bakaar.greetings.domain;

import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResources({
        @SelectClasspathResource("GreetingsCreation.feature"),
        @SelectClasspathResource("GreetingsUpdate.feature")
})
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "net.bakaar.greetings.domain.glue")
class DomainCucumberLauncherIT {
}
