package gradle.TestNG.selenium;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import java.time.Duration;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class JuiceTest {
    private static String address = "localhost";
    private static String port = "3000";
    private static String baseUrl = String.format("http://%s:%s", address, port);
    static WebDriver driver;
    static Customer customer;


    @BeforeTest
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        //TODO Task1: Add your credentials to customer i.e. email, password and security answer.
        customer = new Customer.Builder().build();
        System.out.println(customer.getEmail());
        System.out.println(customer.getPassword());
        System.out.println(customer.getSecurityAnswer());
        Login login = new Login(driver);

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

        JSONObject registrationPayload = new JSONObject()
                .put("email", customer.getEmail())
                .put("password", customer.getPassword())
                .put("securityQuestion", new JSONObject().put("id", 1))
                .put("securityAnswer", customer.getSecurityAnswer());
        given()
                .header("Content-Type", "application/json")
                .body(registrationPayload.toString())
                .when()
                .post(baseUrl + "/api/Users/")
                .then()
                .log().all()
                .statusCode(201);
    }

    @AfterTest
    public void teardown() {
        driver.quit();
    }

    //TODO Task2: Login and post a product review using Selenium
    @Test(priority = 1)
    public void loginAndPostProductReviewViaUi() throws InterruptedException {
        driver.get(baseUrl + "/#/login");

        // TODO Dismiss popup (click close)
        driver.findElement(By.xpath("//button[contains (@aria-label, 'Close Welcome Banner')]")).click();

        Login login = new Login(driver);
        login.getEmailField(customer.getEmail());
        login.getPasswordField(customer.getPassword());
        login.clickLoginButton();

        //Thread.sleep(5000);
        System.out.println(driver.getTitle());
        Assert.assertEquals("OWASP Juice Shop",driver.getTitle());
        //driver.findElement(By.id("navbarAccount")).isDisplayed();

        // TODO Navigate to product and post review

        // TODO Assert that the review has been created successfully

    }

    // TODO Task3: Login and post a product review using the Juice Shop API
    @Test(priority = 2)
    public void loginAndPostProductReviewViaApi() {
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
        // TODO Implement login via API to retrieve token
        // TODO Retrieve token via login API

        // TODO Use token to post review to product

        // TODO Assert that the product review has persisted
    }
}
