package runner;


import config.ConfigReader;
import core.CoreManager;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

import java.util.ArrayList;
import java.util.List;

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
                Object[][] allScenarios = super.scenarios();
                ConfigReader configReader = CoreManager.getContext().getConfigReader();
                int totalShards = Integer.parseInt(configReader.get("totalShards", "1"));
                int currentShards = Integer.parseInt(configReader.get("currentShard", "1"));
                List<Object[]> result = new ArrayList<>();

                for (int i = 0; i < allScenarios.length; i++) {
                        if (i % totalShards == currentShards - 1) {
                                result.add(allScenarios[i]);
                        }
                }
                return result.toArray(new Object[0][]);
        }

//        @Parameters({"environment"})
//        @BeforeTest(alwaysRun = true)
//        public void defineEnvironment(String environment) {
////                EnvManager.setEnv(environment);
//                System.out.println(">>> Luồng " + Thread.currentThread().getId() + " đang setup chạy môi trường: " + environment);
//        }
}
