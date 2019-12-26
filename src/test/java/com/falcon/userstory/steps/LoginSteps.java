package com.falcon.userstory.steps;

import com.falcon.selenium.pages.LoginPage;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class LoginSteps {

    private LoginPage loginPage = null;

    @Before
    public void beforeScenario(Scenario scenario) throws Exception {
        loginPage = new LoginPage(scenario);
    }

    @After
    public void afterScenario(Scenario scenario) throws Exception {
        if(scenario.isFailed()) {
            loginPage.takeScreenshot(scenario);
        }
        loginPage.closeDriver();
        loginPage = null;
    }

}
