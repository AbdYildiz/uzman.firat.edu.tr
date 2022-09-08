import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.List;

public class MainPage {
    public WebDriver driver = new ChromeDriver();
    public SoftAssert soft = new SoftAssert();
    public JavascriptExecutor js = (JavascriptExecutor) driver;
    public Actions act = new Actions(driver);

    @BeforeSuite public void beforeSuite(){
        driver.get("https://uzman.firat.edu.tr/");
    }

    @BeforeMethod public void beforeMethod(){
        driver.navigate().to("https://uzman.firat.edu.tr/");
    }

    @Test public void emptySearchBox(){
        WebElement ele = driver.findElement(By.id("search"));
        ele.sendKeys("",Keys.ENTER);
        String message = (String)js.executeScript("return arguments[0].validationMessage;", ele);
        Assert.assertEquals(message,"Please fill out this field.");
    }

    @Test public void emptyFieldSearch(){
        act.moveToElement(driver.findElement(By.cssSelector(".input-select-click"))).perform();
        driver.findElement(By.id("clearSelectAll")).click();

        List<WebElement> fields = driver.findElements(By.cssSelector(".input-select input"));
        for (WebElement e: fields) {
            boolean f = e.isSelected();
            soft.assertFalse(f);
        }
        soft.assertAll();

        WebElement ele = driver.findElement(By.id("search"));
        ele.sendKeys("mustafa",Keys.ENTER);
        String message = (String)js.executeScript("return arguments[0].validationMessage;", ele);
        System.out.println(message);
        Assert.assertEquals(message,"- indexes alanı gereklidir.");
    }

    @Test public void checkFields() throws InterruptedException {
        act.moveToElement(driver.findElement(By.cssSelector(".input-select-click"))).perform();
        List<WebElement> fields = driver.findElements(By.cssSelector(".input-select input"));

        for (int i = 1; i < fields.size(); i++) {
            fields.get(i).click();
            boolean f = fields.get(i).isSelected();
            soft.assertFalse(f);
        }
        Thread.sleep(500);
        if (fields.get(0).isSelected() == false) {
            for (int i = 1; i < fields.size(); i++) {
                boolean f = fields.get(i).isSelected();
                soft.assertFalse(f);
            }
        }
        Thread.sleep(500);

        if (fields.get(0).isSelected() == true) {
            for (int i = 1; i < fields.size(); i++) {
                boolean t = fields.get(i).isSelected();
                soft.assertTrue(t);
            }
        }
        Thread.sleep(500);

        for (int i = 1; i < fields.size(); i++) {
            fields.get(i).click();
            boolean t = fields.get(i).isSelected();
            soft.assertTrue(t);
        }
        Thread.sleep(500);
        soft.assertAll();
    }

    @Test public void searchButton(){
        String caracter = "mustafa";
        driver.findElement(By.id("search")).sendKeys(caracter);
        driver.findElement(By.cssSelector(".btn-search")).click();
        String element = driver.findElement(By.xpath("//p[contains(text(),'mustafa')]")).getText();
        Assert.assertTrue(element.contains(caracter));
    }

    @Test (dataProvider = "clickable",timeOut = 5000) public void links(WebElement el, String url){
        el.click();
        Assert.assertEquals(driver.getCurrentUrl(),url);
        driver.navigate().back();
    }

    @DataProvider public Object[][] clickable(){
        Object[][] click = new Object[3][2];
        click[0][0] = driver.findElement(By.xpath("//a[contains(text(),'Hakkımızda')]"));
        click[0][1] = "https://uzman.firat.edu.tr/about-us";

        click[1][0] = driver.findElement(By.xpath("//a[contains(text(),'Fırat Üniversitesi')]"));
        click[1][1] = "https://www.firat.edu.tr/tr";

        click[2][0] = driver.findElement(By.xpath("//a[contains(text(),'Dijital Dönüşüm ve Yazılım Ofisi')]"));
        click[2][1] = "http://ddyo.firat.edu.tr/tr";

        return click;
    }

    @AfterSuite public void afterSuite() throws InterruptedException {
        Thread.sleep(2000);
        driver.quit();
    }

}