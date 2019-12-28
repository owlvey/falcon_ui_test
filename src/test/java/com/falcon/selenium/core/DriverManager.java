package com.falcon.selenium.core;

import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import java.net.MalformedURLException;

public class DriverManager {

    private String timeOutString = "30";
    private final int TIMEOUT = Integer.parseInt(timeOutString);
    private static String OS = System.getProperty("os.name").toLowerCase();
    private static final ThreadLocal<Long> ID = new ThreadLocal<Long>();
    private static final ThreadLocal<WebDriver> DriverInstance = new ThreadLocal<WebDriver>();
    private static  String environment;
    public static String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public DriverManager()
    {}
    public static Long GetID(){
        Long id = ID.get();
        if ( id == null){
            id = Thread.currentThread().getId();
            ID.set(id);
        }
        return id;
    }

    public static void ReleaseDriver(){
        WebDriver d = DriverManager.DriverInstance.get();
        if ( d != null){
            try{
                d.close();
                d.quit();
            }
            finally {
                DriverManager.DriverInstance.remove();
            }
        }
    }

    private static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    private static boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }

    private static boolean isUnix() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
    }

    public static WebDriver GetDriver() throws MalformedURLException,InterruptedException {
        WebDriver driver = DriverManager.DriverInstance.get();

        if(driver != null)
            return driver;

        String ui_server = Resources.getProperty("general","UI_SERVER");
        String driver_kind = Resources.getProperty("general", "DRIVER_KIND");
        String browser = Resources.getProperty("general", "BROWSER");

        if(driver_kind == null  || driver_kind.isEmpty()){
            driver_kind = "local";
        }

        driver_kind = driver_kind.toLowerCase();

        if(browser == null  || browser.isEmpty()){
            browser = "safari";
        }

        browser = browser.toLowerCase();

        System.out.println("=========================================");
        System.out.println("Thread Id: " + GetID());
        System.out.println("ui server  " + ui_server);
        System.out.println("ui runtime " + driver_kind);
        System.out.println("ui browser " + browser);
        System.out.println("=========================================");

        DesiredCapabilities capabilities = null;
        switch(driver_kind) {
            case "local":
                switch(browser) {
                    case "safari":
                        if(isMac()) {
                            System.setProperty("webdriver.safari.noinstall", "true");
                            driver = new SafariDriver();
                            //driver.get(Resources.getProperty("general","URL_ENV"));
                            driver.get(getEnvironment());

                        }
                        if(isWindows()) {
                            throw new IllegalArgumentException("Invalid: Not supported on this OS");
                        }
                        break;
                    case "firefox":
                        System.setProperty("webdriver.gecko.driver", System.getenv("FIREFOX_DRIVER"));
                        capabilities = new DesiredCapabilities();
                        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                        capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                        driver = new FirefoxDriver(capabilities);
                        //driver.get(Resources.getProperty("general", "URL_ENV"));
                        driver.get(getEnvironment());
                        Thread.sleep(2000);
                        break;
                    case "chrome_headless":
                    case "chrome":

                        String chromeDriver = System.getenv("CHROMEWEBDRIVER");
                        if (chromeDriver == null){
                            System.setProperty("webdriver.chrome.driver", "C:\\tools\\chromedriver.exe");
                        }

                        ChromeOptions options = new ChromeOptions();
                        if(browser.equals("chrome_headless")) {
                            options.addArguments("--headless");
                        }
                        driver = new ChromeDriver(options);
                        String s=getEnvironment();
                        System.out.println("getEnvironment:"+s);
                        driver.get("http://www.google.com");
                        Thread.sleep(2000);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid: " + browser);
                }
                break;
            case "hub":
                switch (browser) {
                    case "firefox":
                        break;
                    case "chrome":
                        break;
                        default:
                            throw new IllegalArgumentException("Invalid: " + browser);
                }
                driver = new RemoteWebDriver(new URL(ui_server), capabilities);
                driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                break;
                default:
                    throw new IllegalArgumentException("Invalid: " + driver_kind);

        }
        driver.manage().window().maximize();
        DriverManager.DriverInstance.set(driver);
        return driver;
    }

}
