package gradle.junit.selenium;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import java.time.Duration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

class JuiceTest {
    private static String address = "localhost";
    private static String port = "3000";
    private static String baseUrl = String.format("http://%s:%s", address, port);

    static WebDriver driver;
    static Customer customer;

    @BeforeAll
    static void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // For Chrome 109+
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
//        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        //TODO Task1: Add your credentials to customer i.e. email, password and security answer.
        customer = new Customer.Builder().build();
        System.out.println(customer.getEmail());
        System.out.println(customer.getPassword());
        System.out.println(customer.getSecurityAnswer());

        //Register the customer via UI

//        driver.get(baseUrl + "/#/register");
//        driver.findElement(By.xpath("//button[contains (@aria-label, 'Close Welcome Banner')]")).click();
//        driver.findElement(By.id("emailControl")).sendKeys(customer.getEmail());
//        driver.findElement(By.id("passwordControl")).sendKeys(customer.getPassword());
//        driver.findElement(By.id("repeatPasswordControl")).sendKeys(customer.getPassword())
//        driver.findElement(By.xpath("//mat-select[@name=\"securityQuestion\"]")).click();
//        driver.findElement(By.xpath("//span[contains (text(), 'Your eldest siblings middle name?' )]")).click();
//        driver.findElement(By.id("securityAnswerControl")).sendKeys(customer.getSecurityAnswer());
//        driver.findElement(By.id("registerButton")).click();

        String registrationPayload = String.format(
                "{\"email\":\"%s\",\"password\":\"%s\",\"securityQuestion\":{\"id\":1},\"securityAnswer\":\"%s\"}",
                customer.getEmail(), customer.getPassword(), customer.getSecurityAnswer()
        );
        given()
                .header("Content-Type", "application/json")
                .body(registrationPayload)
                .when()
                .post(baseUrl + "/api/Users/")
                .then()
                .log().all()
                .statusCode(201);
    }

    @AfterAll
    static void teardown() {
        driver.quit();
    }

    //TODO Task2: Login and post a product review using Selenium
    @Test
    void loginAndPostProductReviewViaUi() throws InterruptedException {
        driver.get(baseUrl + "/#/login");

        // TODO Dismiss popup (click close)
        //driver.findElement(By.xpath("//button[contains (@aria-label, 'Close Welcome Banner')]")).click();
        try {
            WebElement closeButton = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains (@aria-label, 'Close Welcome Banner')]")));
            closeButton.click();
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Welcome banner not found, continuing...");
        }

        // Login with credentials
        WebElement emailField = driver.findElement(By.name("email"));
        WebElement passwordField = driver.findElement(By.name("password"));
        //WebElement loginButton = driver.findElement(By.id("loginButton"));
        WebElement loginButton = new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.elementToBeClickable(By.id("loginButton")));
        loginButton.click();

        emailField.sendKeys(customer.getEmail());
        passwordField.sendKeys(customer.getPassword());
        loginButton.click();
        Thread.sleep(5000);
        System.out.println(driver.getTitle());
        Assertions.assertEquals("OWASP Juice Shop",driver.getTitle());
        //driver.findElement(By.id("navbarAccount")).isDisplayed();

        // TODO Navigate to product and post review

        // TODO Assert that the review has been created successfully

    }

    // TODO Task3: Login and post a product review using the Juice Shop API
    @Test
    void loginAndPostProductReviewViaApi() {
        // Example HTTP request with assertions using Rest Assured. Can be removed.
        String status = given()
                .header("Content-Type", "application/json")
                .when()
                .get(baseUrl + "/rest/products/search")
                .then()
                .statusCode(200)
                .body("status", equalTo("success") )
                .body("data", hasItem(
                        allOf(
                                hasEntry("image", "apple_pressings.jpg"),
                                hasEntry("name", "Apple Pomace"))
                ))
                .extract()
                .path("status");

        System.out.println(String.format("Status value is: %s", status));

        // TODO Retrieve token via login API

        // TODO Use token to post review to product

        // TODO Assert that the product review has persisted
    }
}
