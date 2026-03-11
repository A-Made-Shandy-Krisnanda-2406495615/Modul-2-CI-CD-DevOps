package id.ac.ui.cs.advprog.eshop.functional;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import io.github.bonigarcia.seljup.SeleniumJupiter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class PaymentFunctionalTest {
    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    private String baseUrl;
    private Payment payment;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
        payment = createPayment();
    }

    @Test
    void userCanViewPaymentDetailById(ChromeDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.get(baseUrl + "/payment/detail/" + payment.getId());

        wait.until(ExpectedConditions.titleIs("Payment Detail"));
        assertEquals("Payment Detail", driver.getTitle());
        assertEquals(payment.getId(), driver.findElement(By.id("paymentIdValue")).getText());
        assertEquals("SUCCESS", driver.findElement(By.id("paymentStatusValue")).getText());
    }

    @Test
    void adminCanSeeAllPayments(ChromeDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.get(baseUrl + "/payment/admin/list");

        wait.until(ExpectedConditions.titleIs("Payment List"));
        assertEquals("Payment List", driver.getTitle());
        By paymentRow = By.xpath(String.format("//tbody/tr[td[1][normalize-space()='%s']]", payment.getId()));
        wait.until(webDriver -> !webDriver.findElements(paymentRow).isEmpty());
        assertTrue(driver.findElements(paymentRow).size() >= 1);
    }

    @Test
    void adminCanRejectPaymentAndOrderStatusBecomesFailed(ChromeDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.get(baseUrl + "/payment/admin/detail/" + payment.getId());

        wait.until(ExpectedConditions.titleIs("Payment Admin Detail"));
        driver.findElement(By.id("rejectButton")).click();

        wait.until(ExpectedConditions.titleIs("Payment Admin Detail"));
        assertEquals("REJECTED", driver.findElement(By.id("paymentStatusValue")).getText());
        assertEquals("FAILED", driver.findElement(By.id("orderStatusValue")).getText());
    }

    private Payment createPayment() {
        Product product = new Product();
        product.setProductId(UUID.randomUUID().toString());
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);

        List<Product> products = new ArrayList<>();
        products.add(product);

        Order order = new Order(
                UUID.randomUUID().toString(),
                products,
                System.currentTimeMillis() / 1000,
                "Safira Sudrajat"
        );
        orderService.createOrder(order);

        return paymentService.addPayment(order, "VOUCHER_CODE", Map.of(
                "voucherCode", "ESHOP1234ABC5678"
        ));
    }
}
