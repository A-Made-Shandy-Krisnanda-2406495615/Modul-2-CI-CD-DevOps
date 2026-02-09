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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class EditProductFunctionalTest {
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
    void editPage_whenProductNotFound_redirectsToList(ChromeDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.get(baseUrl + "/product/edit/not-found");

        wait.until(ExpectedConditions.titleIs("Product List"));
        assertEquals("Product List", driver.getTitle());
    }

    @Test
    void userCanEditProductAndSeeItUpdatedInList(ChromeDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        String originalName = "product-" + UUID.randomUUID();
        String originalQuantity = "10";
        createProduct(driver, wait, originalName, originalQuantity);

        By originalProductRow = productRow(originalName, originalQuantity);
        wait.until(webDriver -> !webDriver.findElements(originalProductRow).isEmpty());

        WebElement row = driver.findElement(originalProductRow);
        row.findElement(By.xpath(".//button[normalize-space()='Edit']")).click();

        wait.until(ExpectedConditions.titleIs("Edit Product"));

        WebElement nameInput = driver.findElement(By.id("nameInput"));
        WebElement quantityInput = driver.findElement(By.id("quantityInput"));
        assertEquals(originalName, nameInput.getAttribute("value"));
        assertEquals(originalQuantity, quantityInput.getAttribute("value"));

        String updatedName = originalName + "-updated";
        String updatedQuantity = "25";

        nameInput.clear();
        nameInput.sendKeys(updatedName);

        quantityInput.clear();
        quantityInput.sendKeys(updatedQuantity);

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.titleIs("Product List"));

        By updatedProductRow = productRow(updatedName, updatedQuantity);
        wait.until(webDriver -> !webDriver.findElements(updatedProductRow).isEmpty());

        assertTrue(driver.findElements(updatedProductRow).size() >= 1);
        assertTrue(driver.findElements(originalProductRow).isEmpty());
    }

    private void createProduct(ChromeDriver driver, WebDriverWait wait, String name, String quantity) {
        driver.get(baseUrl + "/product/create");
        wait.until(ExpectedConditions.titleIs("Create New Product"));

        WebElement nameInput = driver.findElement(By.id("nameInput"));
        WebElement quantityInput = driver.findElement(By.id("quantityInput"));

        nameInput.sendKeys(name);
        quantityInput.sendKeys(quantity);

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
