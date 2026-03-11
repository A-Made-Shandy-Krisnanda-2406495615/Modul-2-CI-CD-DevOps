package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import java.time.Duration;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class CreateOrderFunctionalTest {
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
    void userCanCreateOrderAndSeeItInHistory(ChromeDriver driver) {
        String author = "Safira Sudrajat";
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.get(baseUrl);
        wait.until(ExpectedConditions.titleIs("eShop"));
        assertEquals("eShop", driver.getTitle());

        driver.findElement(By.linkText("Create Order")).click();
        wait.until(ExpectedConditions.titleIs("Create Order"));

        driver.findElement(By.id("authorInput")).sendKeys(author);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.titleIs("Order History"));
        driver.findElement(By.id("authorInput")).sendKeys(author);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath(String.format("//tbody/tr[td[2][normalize-space()='%s']]", author))));
    }
}
