package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import java.time.Duration;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class DeleteProductFunctionalTest {
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
    void userCanDeleteProductAndItDisappearsFromList(ChromeDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        String productName = "product-" + UUID.randomUUID();
        String productQuantity = "7";
        createProduct(driver, wait, productName, productQuantity);

        By productRow = productRow(productName, productQuantity);
        wait.until(webDriver -> !webDriver.findElements(productRow).isEmpty());

        WebElement row = driver.findElement(productRow);
        row.findElement(By.xpath(".//button[normalize-space()='Delete']")).click();

        wait.until(ExpectedConditions.titleIs("Product List"));
        wait.until(webDriver -> webDriver.findElements(productRow).isEmpty());
        assertTrue(driver.findElements(productRow).isEmpty());
    }

    private void createProduct(ChromeDriver driver, WebDriverWait wait, String name, String quantity) {
        driver.get(baseUrl + "/product/create");
        wait.until(ExpectedConditions.titleIs("Create New Product"));

        driver.findElement(By.id("nameInput")).sendKeys(name);
        driver.findElement(By.id("quantityInput")).sendKeys(quantity);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.titleIs("Product List"));

        By createdProductRow = productRow(name, quantity);
        wait.until(webDriver -> !webDriver.findElements(createdProductRow).isEmpty());
        assertTrue(driver.findElements(createdProductRow).size() >= 1);
    }

    private static By productRow(String name, String quantity) {
        return By.xpath(String.format(
                "//tbody/tr[td[1][normalize-space()='%s'] and td[2][normalize-space()='%s']]",
                name,
                quantity));
    }
}
