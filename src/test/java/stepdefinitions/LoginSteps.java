package stepdefinitions;

import core.CoreManager;
import io.cucumber.java.en.Given;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginSteps {
    private WebDriver actions() {
        return CoreManager.getContext().getDriver();
    }

    @Given("User opens login page")
    public void goToLoginPage() {
        actions().get("https://www.facebook.com/");
    }

    @Given("Do nothing")
    public void doNothing() {
        actions().findElement(By.xpath("...."));
    }
}
