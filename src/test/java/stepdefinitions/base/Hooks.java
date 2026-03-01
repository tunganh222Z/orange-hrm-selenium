package stepdefinitions.base;

import config.ConfigReader;
import core.base.DriverFactory;
import core.context.ExecutionContext;
import core.context.ScreenshotBus;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.MDC;

public class Hooks {

    @Before
    public void setUp(Scenario scenario) {
        System.out.println("Running scenario: " + scenario.getName());
        ScreenshotBus.register(
                new ScreenshotHandler(scenario)
        );
        setUpTimeout(scenario);
        MDC.put("scenario", scenario.getName());
        DriverFactory.initDriver();
    }

    @After
    public void tearDown() {
        ExecutionContext.clear();
        DriverFactory.quitDriver();
        ScreenshotBus.clear();
        MDC.clear();
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
    }

    private void setUpTimeout (Scenario scenario) {
        if (scenario.getSourceTagNames().contains("@fast")) {
            ExecutionContext.setTimeout(5);
        } else if (scenario.getSourceTagNames().contains("@slow")) {
            ExecutionContext.setTimeout(60);
        } else {
            ExecutionContext.setTimeout(
                    Integer.parseInt(ConfigReader.get("timeout"))
            );
        }
    }
}
