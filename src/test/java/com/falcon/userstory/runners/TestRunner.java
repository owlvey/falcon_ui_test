package com.falcon.userstory.runners;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {
                "pretty",
                "html:target/cucumberHtmlReport",
                "json:target/userstory-report.json"
        },
        glue = {"com/falcon/userstory/steps"},
        features = "src/test/java/com/falcon/userstory/smoke/Demo.feature",
        tags = {"@uat", "@id_3"}
)

public class TestRunner {
        
}
