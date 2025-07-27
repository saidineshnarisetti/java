package gradle.TestNG.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Login {
    WebDriver driver;
    Login(WebDriver driver) {
        this.driver = driver;
    }
    By emailField = By.name("email");
    By passwordField = By.name("password");
    By loginButton = By.id("loginButton");
    public void getEmailField(String email) {
         driver.findElement(emailField).sendKeys(email);
    }
    public void getPasswordField(String password) {
         driver.findElement(passwordField).sendKeys(password);
    }
    public void clickLoginButton() {
        driver.findElement(loginButton).click();
    }
}
