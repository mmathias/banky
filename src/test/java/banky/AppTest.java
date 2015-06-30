package banky;

import cucumber.api.CucumberOptions;
import cucumber.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features", format = { "pretty", "html:target/cucumber", "json:target/cucumber.json" })
public class AppTest {

}

