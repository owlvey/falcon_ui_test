package com.falcon.selenium.pages;

import com.falcon.selenium.wrapper.Driver;
import cucumber.api.Scenario;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.io.IOException;

public class LoginPage extends BasePage {

    @FindBy(xpath = "//button[contains(.,'Log In As Guest')]")
    private WebElement btn_loginGuest;
    @FindBy(xpath="//*[@id='mat-select-0']/div/div[1]")
    private WebElement cb_tipoDocumento;
    @FindBy(xpath="//*[@id=\"mat-input-0\"]")
    private WebElement txt_NumeroDocumento;
    @FindBy(xpath="//button[@class='mat-raised-button mat-primary']")
    private WebElement btn_Continuar;

    public LoginPage(Scenario scenario) throws Exception {
        if(driver != null)
            return;

        driver = new Driver();
        PageFactory.initElements(driver.getDriver(), this);
    }

    public void loginAsGuest() {
        btn_loginGuest.click();
    }

    public boolean validarPantallaLogin() {
        return cb_tipoDocumento.isDisplayed();
    }

    public void presionarBotonContinuar() {
        btn_Continuar.click();
    }

    public void closeDriver() {
        driver.tearDown();
    }

    public void takeScreenshot(Scenario scenario) throws IOException {
        if(driver!=null)
            driver.takeScreenShot(scenario);
    }
}
