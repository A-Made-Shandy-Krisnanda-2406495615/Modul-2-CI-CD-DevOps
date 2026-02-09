package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import java.time.Duration;
import java.util.UUID;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class CreateProductFunctionalTest {
    /**
     * The port number assigned to the running application during test execution.
     * Set automatically during each test run by Spring Framework's test context.
     */
    @LocalServerPort
    private int serverPort;

    /**
     * The base URL for testing. Default to {@code http://localhost}-
     */
    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void userCanCreateProductAndSeeItInList(ChromeDriver driver) {
        String productName = "testing-product";
        String productQuantity = "100";

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.get(baseUrl);
        wait.until(ExpectedConditions.titleIs("eShop"));
        assertEquals("eShop", driver.getTitle());

        driver.findElement(By.linkText("View Products")).click();
        wait.until(ExpectedConditions.titleIs("Product List"));

        driver.findElement(By.linkText("Create Product")).click();
        wait.until(ExpectedConditions.titleIs("Create New Product"));

        driver.findElement(By.id("nameInput")).sendKeys(productName);
        driver.findElement(By.id("quantityInput")).sendKeys(productQuantity);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.titleIs("Product List"));
        By createdProductRow = By.xpath(String.format(
                "//tbody/tr[td[1][normalize-space()='%s'] and td[2][normalize-space()='%s']]",
                productName,
                productQuantity));

        wait.until(webDriver -> !webDriver.findElements(createdProductRow).isEmpty());
        assertTrue(driver.findElements(createdProductRow).size() >= 1);
    }
}
