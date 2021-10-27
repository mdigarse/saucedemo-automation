package com.saucedemo.qa.pages;

import com.saucedemo.qa.base.TestBase;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends TestBase {

    @FindBy(xpath = "//div[@class='bot_column']")
    WebElement saucedemoLogo;

    @FindBy(xpath = "//input[@id='user-name']")
    WebElement userName;

    @FindBy(xpath = "//input[@id='password']")
    WebElement password;

    @FindBy(xpath = "//input[@id='login-button']")
    WebElement loginBtn;

    @FindBy(xpath = "//button[@id='react-burger-menu-btn']")
    WebElement menuBtn;

    @FindBy(css = "h3[data-test='error']")
    WebElement errorLabel;

    //Initializing the Page Objects:
    public LoginPage() {
        PageFactory.initElements(driver, this);
    }

    public String validateLoginPageTitle() {
        return driver.getTitle();
    }

    public boolean validateSaucedemoImage() {
        return saucedemoLogo.isDisplayed();
    }

    public HomePage login(String un, String pwd) {
        userName.sendKeys(un);
        password.sendKeys(pwd);
        loginBtn.click();
        return new HomePage();
    }

    public boolean isLoginSuccessful() {
        return menuBtn.isDisplayed();
    }

    public String getErrorMsg() {
        return errorLabel.getText();
    }

    public void openSite() {
        driver.get(prop.getProperty("url"));
    }
}
