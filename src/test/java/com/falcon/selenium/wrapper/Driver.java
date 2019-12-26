package com.falcon.selenium.wrapper;

import com.falcon.selenium.core.DriverManager;
import cucumber.api.Scenario;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class Driver {

    private WebDriver driver;
    protected WebDriverWait wait;
    private String browserName;

    private String timeOutString = "30";
    private final int TIMEOUT = Integer.parseInt(timeOutString);


    public Driver() throws MalformedURLException, InterruptedException {
        long threadId = Thread.currentThread().getId();
        this.driver = DriverManager.GetDriver();
    }

    public WebDriver getDriver() {
        return this.driver;
    }
    public String pageURL() {
        return driver.getCurrentUrl();
    }

    public WebElement find(By locator) {
        WebElement element = null;
        try {
            element = driver.findElement(locator);
        } catch (NoSuchElementException e) {
            //driver.quit();
            throw new NoSuchElementException(
                    "No se ha encontrado ningun elemento web en la ruta indicada: "
                            + locator);
        }
        return element;
    }

    public List<WebElement> findElements(By locator) {
        List<WebElement> element = null;
        try {
            element = driver.findElements(locator);
        } catch (NoSuchElementException e) {
            //driver.quit();
            throw new NoSuchElementException(
                    "No se ha encontrado ningun elemento web en la ruta indicada: "
                            + locator);
        }
        return element;
    }

    public WebElement findExists(By locator) {
        WebElement element = null;
        try {
            element = driver.findElement(locator);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(
                    "No element found at: "
                            + locator);
        }
        return element;
    }

    public void sendKeys(String text, By locator) {
        find(locator).sendKeys(text);
    }

    public String getVisibleText(By locator) {
        waitForElement(locator);
        return find(locator).getText();
    }

    public ArrayList<String> getVisibleTexts(By locator) {
        final List<WebElement> elements = driver.findElements(locator);
        final ArrayList<String> list = new ArrayList<String>();
        for (final WebElement webElement : elements) {
            list.add(webElement.getText());
        }
        return list;
    }

    public String getValue(By locator) {
        return find(locator).getAttribute("value");
    }

    public String getValue(By locator, String attributeName) {
        return find(locator).getAttribute(attributeName);
    }

    public boolean getValueChecked(By locator,@NotNull String attributeName) {
        boolean valor = false;
        if (attributeName.equals("checked")) {
            find(locator).getAttribute(attributeName);
            valor= true;
        }
        return valor;
    }

    public void click(By locator) {
        find(locator).click();
    }

    public void clickAction(By locator) {
        WebElement element = driver.findElement(locator);
        Actions ob = new Actions(driver);
        ob.moveToElement(element);
        ob.click(element);
        Action action  = ob.build();
        action.perform();
    }

    public void mouseHoverAction(By locator) {
        WebElement element = driver.findElement(locator);
        Actions ob = new Actions(driver);
        ob.moveToElement(element).perform();
    }

    public void clear(By locator) {
        find(locator).click();
        find(locator).clear();
    }

    public void clickAndWait(By locator) {
        sleep(3);
        find(locator).click();
        waitForPageToLoad();
    }

    public void clickAndWaitforElement(By locator, By locatorToWait) {
        find(locator).click();
        waitForPageToLoad();
        waitForElementVisible(locatorToWait);
    }

    public void clickPrueba(By locator) {
        waitForElementVisible(locator);
        int timeout=0;
        while(timeout<100) {
            try {
                find(locator).click();
                timeout=100;
            }catch(Exception e){
                timeout=timeout+2;
            }
        }
    }

    public boolean elementIsVisible(By locator) {
        return find(locator).isDisplayed();
    }

    public boolean elementIsPresent(By locator) {
        try {
            if(locator == null)
                return false;

            driver.findElement(locator);
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    public boolean elementIsEnabled(By locator) {
        try {
            if(locator == null)
                return false;

            driver.findElement(locator);
        } catch (NoSuchElementException e) {
            return false;
        }
        return driver.findElement(locator).isEnabled();
    }

    public void backPage(){
        driver.navigate().back();
    }

    public void enterKeys(By locator) {
        find(locator).sendKeys(Keys.ENTER);
        sleep(2);
    }

    public boolean isChecked(By locator) {
        return find(locator).isSelected();
    }

    public boolean isEnabled(By locator) {
        return find(locator).isEnabled();
    }

    public void waitForElement(By expectedLocator) {
        Wait<WebDriver> wait = new WebDriverWait(driver, TIMEOUT);
        wait.until(ExpectedConditions
                .visibilityOfElementLocated(expectedLocator));
    }

    public void waitForElement(WebElement expectedLocator) {
        Wait<WebDriver> wait = new WebDriverWait(driver, TIMEOUT);
        wait.until(ExpectedConditions
                .visibilityOf(expectedLocator));
    }

    public void waitForElementClickable(By expectedLocator) {
        Wait<WebDriver> wait = new WebDriverWait(driver, TIMEOUT);
        wait.until(ExpectedConditions
                .elementToBeClickable(expectedLocator));
    }

    public void waitForElementVisible(By expectedLocator) {
        Wait<WebDriver> wait = new WebDriverWait(driver, TIMEOUT);
        wait.until(ExpectedConditions
                .visibilityOfElementLocated(expectedLocator));
    }

    public void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            throw new WebDriverException( "Unexpected error on Thread: " + Thread.currentThread().getId());
        }
    }

    public void waitForPageToLoad() {
        Wait<WebDriver> wait = new WebDriverWait(driver, TIMEOUT);
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript(
                        "return document.readyState").equals("complete");
            }
        };
        try {
            wait.until(expectation);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void tearDown() {
        DriverManager.ReleaseDriver();
    }

    public void takeScreenShot(@NotNull Scenario scenario) {
        byte[] screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
        scenario.embed(screenshot, "image/png");
    }

    public void SeekBarPorcentaje(By seekElement, Integer value) {
            WebElement seek_bar=driver.findElement(seekElement);
            setHValue(seek_bar,value);
    }

    public void setHValue(@NotNull WebElement slider, double value) {
        double minValue = Double.parseDouble(slider.getAttribute("aria-valuemin"));
        double maxValue = Double.parseDouble(slider.getAttribute("aria-valuemax"));
        int sliderH = slider.getSize().height;
        int sliderW = slider.getSize().width;
        Actions action = new Actions(driver);
        action.moveToElement(slider, (int) ((value-minValue) * sliderW / (maxValue - minValue)), sliderH / 2).click().build().perform();
        if (value==maxValue) {
            action.moveToElement(slider, (int) (sliderW - 1), sliderH / 2).click().build().perform();
        }
    }

    public void OpenNewTab(){
        ((JavascriptExecutor)driver).executeScript("window.open()");
    }

    public void SwitchToTab(int tabID) {
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabID));
    }

    public void SetURLIntoTab(String url){
        driver.get(url);
    }

    public void SwitchToFrame(By locator) {
        driver.switchTo().defaultContent();
        Wait<WebDriver> wait = new WebDriverWait(driver, TIMEOUT);
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
    }

    public void CloseTab() {
        String originalHandle = driver.getWindowHandle();
        for(String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalHandle)) {
                driver.close();
                driver.switchTo().window(handle);
            }
        }
    }
}
