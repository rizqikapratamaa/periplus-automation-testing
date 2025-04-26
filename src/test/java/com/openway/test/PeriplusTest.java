package com.openway.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;
import io.github.cdimascio.dotenv.Dotenv;

import static org.testng.Assert.*;

import java.util.List;
import java.util.NoSuchElementException;

public class PeriplusTest {
    WebDriver driver;

    @BeforeClass
    public void setup(){
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void testAddProductToChart() throws InterruptedException {
        // fo to the website
        driver.get("https://www.periplus.com/");
        Thread.sleep(1000);

        // click login button
        driver.findElement(By.linkText("Sign In")).click();
        Thread.sleep(1000);

        // fill login form
        Dotenv dotenv = Dotenv.load();
        String email = dotenv.get("EMAIL");
        String password = dotenv.get("PASSWORD");

        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.id("button-login")).click();
        Thread.sleep(4000);

        // search product
        WebElement searchBox = driver.findElement(By.id("filter_name"));
        searchBox.sendKeys("atomic habits");
        searchBox.submit();
        Thread.sleep(3000);

        // select first produck
        List<WebElement> products = driver.findElements(By.cssSelector(".single-product"));
        if (!products.isEmpty()) {
            products.get(0).click();
        } else {
            throw new NoSuchElementException("No products found on the page.");
        }
        Thread.sleep(3000);

        // add to cart
        driver.findElement(By.xpath("//button[contains(text(),'Add to Cart')]")).click();
        Thread.sleep(3000);

        // click the cart
        driver.findElement(By.id("show-your-cart")).click();
        Thread.sleep(2000);
        
        // verify the product in the cart
        WebElement productInCart = driver.findElement(By.cssSelector("p.product-name.limit-lines a"));
        assertTrue(productInCart.getText().toLowerCase().contains("atomic habits"));
        Thread.sleep(2000);
    }
    
    @AfterClass
    public void teardown(){
        driver.quit();
    }
}
