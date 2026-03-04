package stepdefinitions;

import core.base.CoreManager;
import io.cucumber.java.en.Given;
import org.openqa.selenium.By;
import utils.ValidateManager;

public class LoginSteps {
    @Given("User opens login page")
    public void goToLoginPage() {
        CoreManager.getDriver().get("https://www.facebook.com/");
    }

    @Given("Do nothing")
    public void doNothing() {
        CoreManager.getDriver().findElement(By.xpath("...."));
    }
}
