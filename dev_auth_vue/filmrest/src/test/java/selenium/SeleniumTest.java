package selenium;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SeleniumTest {

    static WebDriver driver;

    @BeforeAll
    static void start() {
        System.setProperty("webdriver.chrome.driver", "D:/Distrib/chromedriver_win32(1)/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    }


    @Test
    @Order(1)
    void singInNurseTest() {
        driver.get("http://localhost:3000/login");
        WebElement username = driver.findElement(By.xpath("//*[@id=\"app\"]/div/div/form/div[1]/input"));
        WebElement password = driver.findElement(By.xpath("//*[@id=\"app\"]/div/div/form/div[2]/input"));
        WebElement submitButton = driver.findElement(By.xpath("//*[@id=\"app\"]/div/div/form/button"));
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        username.sendKeys("nurse");//проверка медсестры
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        password.sendKeys("999999");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        submitButton.click();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        System.out.println(driver.getCurrentUrl());
        System.out.println(driver.getTitle());
        Assert.assertEquals("http://localhost:3000/login", driver.getCurrentUrl());
    }
    @Test
    @Order(2)
    void singInHeadTest() {
        driver.get("http://localhost:3000/login");
        WebElement username = driver.findElement(By.xpath("//*[@id=\"app\"]/div/div/form/div[1]/input"));
        WebElement password = driver.findElement(By.xpath("//*[@id=\"app\"]/div/div/form/div[2]/input"));
        WebElement submitButton = driver.findElement(By.xpath("//*[@id=\"app\"]/div/div/form/button"));
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        username.sendKeys("head");//проверка главы
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        password.sendKeys("999999");
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        submitButton.click();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        System.out.println(driver.getCurrentUrl());
        System.out.println(driver.getTitle());
        //Assert.assertEquals("http://localhost:3000/medicinelist", driver.getCurrentUrl());
        String text = driver.findElement(By.xpath("//*[@id=\"app\"]/nav/div/div[2]/ul/li/a")).getText();
        Assert.assertTrue(text.contains("Выйти (head)"));

    }
    @Test
    @Order(3)
    void singInErrorTest() {
        driver.get("http://localhost:3000/login");
        WebElement username = driver.findElement(By.xpath("//*[@id=\"app\"]/div/div/form/div[1]/input"));
        WebElement password = driver.findElement(By.xpath("//*[@id=\"app\"]/div/div/form/div[2]/input"));
        WebElement submitButton = driver.findElement(By.xpath("//*[@id=\"app\"]/div/div/form/button"));
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        username.sendKeys("gdsd");//проверка главы
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        password.sendKeys("098");
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        submitButton.click();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        System.out.println(driver.getCurrentUrl());
        System.out.println(driver.getTitle());
        //Assert.assertEquals("Кинотеатр", driver.getTitle());
        String text = driver.findElement(By.xpath("//*[@id=\"app\"]/div/div/form/div[1]")).getText().trim();
        //Assert.assertTrue(text.contains("Не верный логин или пароль!!!"));
        Assert.assertEquals("http://localhost:3000/login", driver.getCurrentUrl());
    }

}

