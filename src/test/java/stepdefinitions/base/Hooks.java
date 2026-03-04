package stepdefinitions.base;

import allureReport.AllureReport;
import core.base.CoreManager;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import utils.ValidateManager;

public class Hooks {

    @Before
    public void setUp(Scenario scenario) {
        WebDriver driver = new FirefoxDriver();
        CoreManager.setDriver(driver);
        CoreManager.setListener(new AllureReport());
    }

    @After
    public void tearDown() {
        CoreManager.quitDriver();
        ValidateManager.assertAll();
    }

    @AfterStep
    public void afterStep(Scenario scenario) {

    }

}
