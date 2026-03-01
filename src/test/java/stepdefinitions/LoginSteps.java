package stepdefinitions;

import core.base.DriverFactory;
import io.cucumber.java.en.Given;
import org.openqa.selenium.By;
import org.testng.Assert;

import static core.base.DriverFactory.*;

public class LoginSteps {
    @Given("User opens login page")
    public void goToLoginPage() {
        getDriver().get("https://www.google.com/");
        System.out.println("test vkl 123 123");
    }

    @Given("Do nothing")
    public void doNothing() {
//        Assert.assertTrue(1+1 == 3);
        DriverFactory.getDriver().findElement(By.id("123123"));
    }
}
