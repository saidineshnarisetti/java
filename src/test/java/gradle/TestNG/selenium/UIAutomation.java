package gradle.TestNG.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class UIAutomation {
    private static String address = "localhost";
    private static String port = "3000";
    public static String baseUrl = String.format("http://%s:%s", address, port);

    static WebDriver driver;
    static Customer customer;
    private static String CreatedEmail;
    @BeforeTest()
    public void setup() {
        driver = new ChromeDriver();
        customer = new Customer.Builder().build();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        System.out.println("Setup completed with base URL: " + baseUrl);
        System.out.println("Customer Email: " + customer.getEmail()
                + ", Password: " + customer.getPassword()
                + ", Security Answer: " + customer.getSecurityAnswer());
    }
    @Test(priority = 1)
    public void RegisterUser() {
        driver.get(baseUrl);
        driver.findElement(By.xpath("//button[contains(@aria-label,'Close Welcome Banner')]")).click();
        driver.findElement(By.xpath("//span[contains(text(), \" Account \")]")).click();
        driver.findElement(By.id("navbarLoginButton")).click();
        driver.findElement(By.id("newCustomerLink")).click();
        CreatedEmail =customer.getEmail();
        driver.findElement(By.id("emailControl")).sendKeys(CreatedEmail);
        driver.findElement(By.id("passwordControl")).sendKeys(customer.getPassword());
        driver.findElement(By.id("repeatPasswordControl")).sendKeys(customer.getPassword());
        WebElement securityQuestionSelect = driver.findElement(By.xpath("//mat-select[@name='securityQuestion']"));
        securityQuestionSelect.click();
        WebElement securityQuestionOption = driver.findElement(By.xpath("//span[contains(text(), 'Your eldest siblings middle name?')]"));
        securityQuestionOption.click();
        WebElement securityAnswerField = driver.findElement(By.id("securityAnswerControl"));
        securityAnswerField.sendKeys(customer.getSecurityAnswer());
        WebElement registerButton = driver.findElement(By.id("registerButton"));
        Assert.assertEquals(driver.getCurrentUrl(), baseUrl + "/#/register");
        registerButton.click();
    }
    @Test(priority = 2, dependsOnMethods = {"RegisterUser"})
    public void Login() throws InterruptedException {

        driver.findElement(By.id("email")).sendKeys(CreatedEmail);
        driver.findElement(By.id("password")).sendKeys(customer.getPassword());
        Assert.assertEquals(driver.getCurrentUrl(), baseUrl + "/#/login");
        driver.findElement(By.id("loginButton")).click();
        //Thread.sleep(2000); // Wait for login to complete
        System.out.println(driver.getTitle());
        Assert.assertEquals("OWASP Juice Shop",driver.getTitle());
    }
    @Test(priority = 3, dependsOnMethods = {"Login"})
    public void PostReview() {
        List<WebElement> productList = driver.findElements(By.xpath("//img[@role=\"button\"]"));
        System.out.println("Product List Size: " + productList.size());
        driver.findElement(By.xpath("//img[@alt=\"Banana Juice (1000ml)\"]")).click();
        driver.findElement(By.xpath("//span[contains(@class, 'mat-expansion-indicator')]")).click();
        String prdReview = "Great product, highly recommend!";
        driver.findElement(By.xpath("//textarea[@placeholder='What did you like or dislike?']")).sendKeys(prdReview);
        driver.findElement(By.xpath("//span[contains(text(), 'Submit')]")).click();
        //Thread.sleep(5000); // Wait for review to be posted
        String reviewXpath = String.format("//p[contains(text(), \"%s\")]", prdReview);
        WebElement reviewElement = driver.findElement(By.xpath(reviewXpath));
        System.out.println("Review Element: " + reviewElement.getText());
        Assert.assertTrue(reviewElement.isDisplayed());

    }

    @AfterTest()
    public void tearDown(){
        driver.quit();
    }

}


