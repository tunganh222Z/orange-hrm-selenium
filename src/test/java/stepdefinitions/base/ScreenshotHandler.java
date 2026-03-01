package stepdefinitions.base;

import io.cucumber.java.Scenario;

public class ScreenshotHandler implements core.strategy.ScreenshotHandler {
    Scenario scenario;
    ScreenshotHandler(Scenario scenario) {
        this.scenario = scenario;
    }
    @Override
    public void handle(byte[] screenshot) {
        this.scenario.attach(
                screenshot,
                "image/png",
                "Failure Screenshot"
        );
    }
}
