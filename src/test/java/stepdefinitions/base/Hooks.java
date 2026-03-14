package stepdefinitions.base;

import allureReport.AllureReport;
import config.ConfigReader;
import core.CoreManager;
import core.ui.driver.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import validate.ValidateManager;

public class Hooks {

    @Before
    public void setUp(Scenario scenario) {
        DriverFactory.getConfig();
        ConfigReader configReader = CoreManager.getContext().getConfigReader();
        if (!configReader.get("context").equals("api")) {
            DriverFactory.initDriver();
        }
        CoreManager.getContext().setReportListener(new AllureReport());
    }

    @After
    public void tearDown() {
        ValidateManager.assertAll();
        CoreManager.unload();
    }

    @AfterStep
    public void afterStep(Scenario scenario) {

    }

}
