package net.bakaar.greetings.application;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("GreetingsCreation.feature")
@SelectClasspathResource("GreetingsUpdate.feature")
@SelectClasspathResource("GreetingsStats.feature")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "net.bakaar.greetings.application.glue")
public class ApplicationCucumberLauncherIT {
}
