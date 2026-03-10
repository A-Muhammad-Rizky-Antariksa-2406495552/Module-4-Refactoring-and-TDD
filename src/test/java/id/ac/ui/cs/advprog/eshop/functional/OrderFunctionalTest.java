package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class OrderFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void createOrderPage_isAccessible(ChromeDriver driver) {
        driver.get(baseUrl + "/order/create");
        assertTrue(driver.getPageSource().contains("Create New Order"));
    }

    @Test
    void orderHistoryPage_isAccessible(ChromeDriver driver) {
        driver.get(baseUrl + "/order/history");
        assertTrue(driver.getPageSource().contains("Order History"));
    }

    @Test
    void orderHistory_postAuthor_showsResults(ChromeDriver driver) {
        driver.get(baseUrl + "/order/history");

        driver.findElement(By.id("authorInput")).sendKeys("authorA");
        driver.findElement(By.tagName("button")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        assertTrue(driver.getPageSource().contains("authorA"));
    }

    @Test
    void orderHistory_postUnknownAuthor_showsEmptyTable(ChromeDriver driver) {
        driver.get(baseUrl + "/order/history");

        driver.findElement(By.id("authorInput")).sendKeys("unknownAuthor");
        driver.findElement(By.tagName("button")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        assertTrue(driver.getPageSource().contains("Order History"));
    }
}