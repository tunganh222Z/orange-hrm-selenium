package runner;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = "stepdefinitions",
        tags = "@smoke",
        plugin = {
                "pretty",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true
)
public class Runner_Test extends AbstractTestNGCucumberTests {
        @Override
        @DataProvider(parallel = true)
        public Object[][] scenarios() {
                return super.scenarios();
        }

//        @Parameters({"environment"})
//        @BeforeTest(alwaysRun = true)
//        public void defineEnvironment(String environment) {
////                EnvManager.setEnv(environment);
//                System.out.println(">>> Luồng " + Thread.currentThread().getId() + " đang setup chạy môi trường: " + environment);
//        }
}
