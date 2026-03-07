package stepdefinitions.base;

import allureReport.AllureReport;
import core.CoreManager;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import uiEngine.driver.DriverFactory;
import validate.ValidateManager;

public class Hooks {

    @Before
    public void setUp(Scenario scenario) {
        DriverFactory.initDriver();
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
