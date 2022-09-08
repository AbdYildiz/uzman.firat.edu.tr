import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Search {
    public WebDriver driver = new ChromeDriver();
    public Actions act = new Actions(driver);
    public SoftAssert soft = new SoftAssert();
    public JavascriptExecutor js = (JavascriptExecutor) driver;

    @BeforeClass public void beforeClass(){
        driver.get("https://uzman.firat.edu.tr/search-results?indexes%5B0%5D=on&indexes%5B1%5D=on&indexes%5B2%5D=on&indexes%5B3%5D=on&indexes%5B4%5D=on&indexes%5B5%5D=on&indexes%5B6%5D=on&indexes%5B7%5D=on&indexes%5B8%5D=on&indexes%5B9%5D=on&arama=mu");
        driver.manage().window().maximize();
    }

    @Test (dataProvider = "getData") public void checkButtons(String tab) throws IOException, InterruptedException {
        driver.switchTo().window(tab);

        List<WebElement> toProfile = driver.findElements(By.cssSelector("td a"));
        for (WebElement a: toProfile) {
            String url = a.getAttribute("href");
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("HEAD");
            conn.connect();
            soft.assertTrue(conn.getResponseCode() < 400,a.getText() + "          BROKEN LINK");
        }

        List<WebElement> details = driver.findElements(By.cssSelector("td button"));
        for (WebElement e: details){
            e.click();
            Thread.sleep(300);
            soft.assertTrue(driver.findElement(By.cssSelector(".btn-close")).isEnabled());
            driver.findElement(By.cssSelector(".btn.btn-secondary")).click();
            Thread.sleep(300);
            js.executeScript("javascript:window.scrollBy(0,50)");
        }
        soft.assertAll();
    }

    @DataProvider public Object[] getData() throws InterruptedException {
        List<WebElement> results = driver.findElements(By.cssSelector(".thumbnail"));
        for (int i = 0; i < results.size(); i++) {
            act.keyDown(Keys.LEFT_CONTROL).perform();
            results.get(i).click();
            Thread.sleep(300);
        }
        driver.close();

        Set<String> windowHandles = driver.getWindowHandles();
        Iterator<String> it = windowHandles.iterator();

        Object[] data = new Object[windowHandles.size()];
        for (int i = 0; i < windowHandles.size(); i++) {
            data[i] = it.next();
        }
        return data;
    }


    @AfterClass public void afterClass() throws InterruptedException {
        Thread.sleep(2000);
        driver.quit();
    }
}
