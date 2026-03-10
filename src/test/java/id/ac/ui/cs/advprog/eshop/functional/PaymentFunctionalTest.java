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
class PaymentFunctionalTest {

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
    void paymentDetailPage_isAccessible(ChromeDriver driver) {
        driver.get(baseUrl + "/payment/detail");
        assertTrue(driver.getPageSource().contains("Payment Detail"));
    }

    @Test
    void paymentAdminListPage_isAccessible(ChromeDriver driver) {
        driver.get(baseUrl + "/payment/admin/list");
        assertTrue(driver.getPageSource().contains("Payment List"));
    }

    @Test
    void paymentDetailById_notFound_showsPage(ChromeDriver driver) {
        driver.get(baseUrl + "/payment/detail/nonexistent-id");
        assertTrue(driver.getPageSource().contains("Payment Detail"));
    }

    @Test
    void paymentAdminDetailById_notFound_showsPage(ChromeDriver driver) {
        driver.get(baseUrl + "/payment/admin/detail/nonexistent-id");
        assertTrue(driver.getPageSource().contains("Payment Detail"));
    }

    @Test
    void paymentAdminList_showsPage(ChromeDriver driver) {
        driver.get(baseUrl + "/payment/admin/list");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        assertTrue(driver.getPageSource().contains("Payment List"));
    }
}