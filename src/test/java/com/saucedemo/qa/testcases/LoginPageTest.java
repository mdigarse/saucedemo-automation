package com.saucedemo.qa.testcases;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.saucedemo.qa.base.TestBase;
import com.saucedemo.qa.pages.LoginPage;
import com.saucedemo.qa.util.TestUtil;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;

public class LoginPageTest extends TestBase {
    LoginPage loginPage;
    ExtentReports extent;
    ExtentTest extentTest;

    public LoginPageTest() {
        super();
    }

    @BeforeClass
    public void setUp() {
        initialization();
        loginPage = new LoginPage();
        ExtentHtmlReporter reporter = new ExtentHtmlReporter("./test-output/Extent.html");
        reporter.config().setEncoding("utf-8");
        reporter.config().setReportName("saucedemo login test report");
        reporter.config().setTheme(Theme.STANDARD);
        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    @Test()
    public void saucedemoLogoImageTest() {
        loginPage.openSite();
        extentTest = extent.createTest("saucedemoLogoImageTest");
        boolean flag = loginPage.validateSaucedemoImage();
        Assert.assertTrue(flag);
    }

    @Test()
    public void loginWithValidUserNameAndPasswordTest() {
        loginPage.openSite();
        extentTest = extent.createTest("loginWithValidUserNameAndPasswordTest");
        loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
        Assert.assertTrue(loginPage.isLoginSuccessful());
    }

    @Test()
    public void loginWithInValidUserNameAndPasswordTest() {
        loginPage.openSite();
        extentTest = extent.createTest("loginWithInValidUserNameAndPasswordTest");
        loginPage.login("dfdgg", "fdgfg");
        Assert.assertEquals("Epic sadface: Username and password do not match any user in this service", loginPage.getErrorMsg());
        // Epic sadface in the error text should be an image and not text
    }

    @Test()
    public void loginWithValidUserNameAndInvalidPasswordTest() {
        loginPage.openSite();
        extentTest = extent.createTest("loginWithValidUserNameAndInvalidPasswordTest");
        loginPage.login(prop.getProperty("username"), "fdcdcece#$");
        Assert.assertEquals("Epic sadface: Username and password do not match any user in this service", loginPage.getErrorMsg());
        // Epic sadface in the error text should be an image and not text
    }

    @Test()
    public void loginWithoutUserNameTest() {
        loginPage.openSite();
        extentTest = extent.createTest("loginWithoutUserNameTest");
        loginPage.login("", prop.getProperty("password"));
        System.out.println(loginPage.getErrorMsg());
        Assert.assertEquals("Epic sadface: Username is required", loginPage.getErrorMsg());
        // Epic sadface in the error text should be an image and not text
    }

    @Test()
    public void loginWithoutPasswordTest() {
        loginPage.openSite();
        extentTest = extent.createTest("loginWithoutPasswordTest");
        loginPage.login(prop.getProperty("username"), "");
        System.out.println(loginPage.getErrorMsg());
        Assert.assertEquals("Epic sadface: Password is required", loginPage.getErrorMsg());
        // Epic sadface in the error text should be an image and not text
    }

    @Test()
    public void loginWithLockedOutUserTest() {
        loginPage.openSite();
        extentTest = extent.createTest("loginWithLockedOutUserTest");
        loginPage.login(prop.getProperty("lockedUser"), prop.getProperty("password"));
        System.out.println(loginPage.getErrorMsg());
        Assert.assertEquals("Epic sadface: Sorry, this user has been locked out.", loginPage.getErrorMsg());
        // Epic sadface in the error text should be an image and not text
    }

    @AfterMethod
    public void createReport(ITestResult result) throws IOException {
        String methodName = result.getMethod().getMethodName();
        if (result.getStatus() == ITestResult.FAILURE) {
            String exceptionMessage = Arrays.toString(result.getThrowable().getStackTrace());
            extentTest.fail("<details><summary><b><font color=red>Exception Occured, Click to see details:"
                    + "</font></b></summary>" + exceptionMessage.replaceAll(",", "<br>") + "</details> \n");
            String path = TestUtil.getScreenshot(driver, result.getMethod().getMethodName());
            try {
                extentTest.fail("<b><font color=red>" + "Screenshot of failure" + "</font></b>",
                        MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            } catch (IOException e) {
                extentTest.fail("test failed and cannot attach screenshot");
            }
            String logText = "<b> Test Method " + methodName + "Failed</b>";
            Markup m = MarkupHelper.createLabel(logText, ExtentColor.RED);
            extentTest.log(Status.FAIL, m);
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            String logText = "<b> Test Method " + methodName + "Success</b>";
            Markup m = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
        } else if (result.getStatus() == ITestResult.SKIP) {
            String logText = "<b> Test Method " + methodName + "Skip</b>";
            Markup m = MarkupHelper.createLabel(logText, ExtentColor.YELLOW);
        }
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
        extent.flush();
    }
}
