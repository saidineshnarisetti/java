package gradle.TestNG.selenium;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import java.time.Duration;
import java.util.List;

import io.restassured.path.json.JsonPath;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class JuiceTest2 {
    private static String address = "localhost";
    private static String port = "3000";
    private static String baseUrl = String.format("http://%s:%s", address, port);
    static WebDriver driver;
    static Customer customer;
    static String CreatedEmail;
    static String ProductName ="Apple Pomace";
    static String TestReview;


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
        String RegisterRes =
                given()
                .header("Content-Type", "application/json")
                .body(registrationPayload.toString())
                .when()
                .post(baseUrl + "/api/Users/")
                .then()
                .log().all()
                .statusCode(201)
                .extract().asString();
        JsonPath jsonPath = new JsonPath(RegisterRes);
        CreatedEmail = jsonPath.getString("data.email");
        System.out.println("Created Email: " + CreatedEmail);
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
        login.getEmailField(CreatedEmail);
        login.getPasswordField(customer.getPassword());
        login.clickLoginButton();

        System.out.println(driver.getTitle());
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(driver -> driver.getTitle().equals("OWASP Juice Shop"));
        Assert.assertEquals("OWASP Juice Shop",driver.getTitle());
        //driver.findElement(By.id("navbarAccount")).isDisplayed();

        TestReview = "This is a test review for the Apple Pomace product by "+CreatedEmail ;
        // TODO Navigate to product and post review
        String productXpath = String.format("//div[text()=\" %s \"]", ProductName);

        driver.findElement(By.xpath(productXpath)).click();
        driver.findElement(By.xpath("//textarea[@placeholder=\"What did you like or dislike?\"]")).sendKeys(TestReview);
        driver.findElement(By.xpath("//span[text()=\" Submit \"]")).click();
        driver.findElement(By.xpath("//span[@style=\"transform: rotate(0deg);\"]")).click();
        String reviewXpath = String.format("//p[contains(text(), \"%s\")]", TestReview);
        WebDriverWait waitForReview = new WebDriverWait(driver, Duration.ofSeconds(5));
        waitForReview.until(driver -> driver.findElement(By.xpath(reviewXpath)).isDisplayed());
        WebElement reviewElement = driver.findElement(By.xpath(reviewXpath));
        System.out.println("Review Text"+   reviewElement.getText());
        Assert.assertEquals(reviewElement.getText(), TestReview);

        // TODO Assert that the review has been created successfully

    }

    // TODO Task3: Login and post a product review using the Juice Shop API
    @Test(priority = 2)
    public void loginAndPostProductReviewViaApi() {
        // Example HTTP request with assertions using Rest Assured. Can be removed.
        String status = given()
                .header("Content-Type", "application/json")
                .queryParam("q","Apple Pomace")
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
                .extract().asString();
                //.path("status");

        System.out.println(String.format("Status value is: %s", status));
        // TODO Implement login via API to retrieve token
        JSONObject loginPayload = new JSONObject()
                .put("email", CreatedEmail)
                .put("password", customer.getPassword());

        String loginResponse =
                given().
                header("Content-Type", "application/json")
                .body(loginPayload.toString())
                .when()
                .post(baseUrl +"/rest/user/login")
                .then()
                        .statusCode(200)
                .extract().asString();
        JsonPath jsonPath = new JsonPath(loginResponse);
        String LoginToken = jsonPath.get("authentication.token");
        System.out.println("Login Token: " + LoginToken);


        JSONObject reviewPayload = new JSONObject()
                .put("message", TestReview)
                .put("author", CreatedEmail);
        System.out.println("Review Payload: " + reviewPayload.toString());

//        String PutPrdreviewRes =
//                given()
//                .header("Content-Type", "application/json")
//                .header("Authorization", "Bearer " + LoginToken)
//                .body(reviewPayload.toString())
//                .when()
//                .put(baseUrl + "/rest/products/24/reviews")
//                .then()
//                .statusCode(201)
//                .extract().asString();
//        JsonPath reviewJsonPath = new JsonPath(PutPrdreviewRes);
//        Assert.assertEquals(reviewJsonPath.get("staus"), "success");

        String GetPrdreviewRes =
                given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + LoginToken)
                .when()
                .get(baseUrl + "/rest/products/24/reviews")
                .then()
                .statusCode(200)
                .extract().asString();
        JsonPath GetPrdRes = new JsonPath(GetPrdreviewRes);
        System.out.println(GetData.GetReviews(GetPrdreviewRes));
        System.out.println("Product Comments: " + GetPrdRes.get("data.message"));

        GetPrdRes.getList("data.message").forEach(review -> {
            System.out.println("Reviews: " + review);
            if (review.equals(TestReview)) {
                Assert.assertTrue(review.toString().contains(TestReview));
                System.out.println("Review found: " + review.toString());
            }
        });
        // TODO Retrieve token via login API

        // TODO Use token to post review to product

        // TODO Assert that the product review has persisted
    }
}
