package emailcucumber;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import java.io.File;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.*;

public class Stepdefs {

  private static final String LOGIN_EMAIL = "";
  private static final String LOGIN_PASSWORD = "";
  private static WebDriver driver;
  private static WebDriverWait wait;
  private final String url = "https://mail.google.com";

  private String recEmail;
  private String filename;

  @Given("I am in the New Message window")
  public void iAmInMessage() {
    confirmInitState();

    driver.findElement(By.xpath("//div[@class='T-I J-J5-Ji T-I-KE L3']")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//textarea[contains(@aria-label, 'To')]")));
    driver.findElement(By.xpath(".//textarea[contains(@aria-label, 'To')]")).click();
    driver.findElement(By.name("subjectbox")).click();
    driver.findElement(By.name("subjectbox")).sendKeys("Test Email");
    driver.findElement(By.xpath("(.//*[@aria-label='Message Body'])[2]")).click();
    driver.findElement(By.xpath("(.//*[@aria-label='Message Body'])[2]")).sendKeys("This is an auto-generated mail");
  }

  @And("I have entered a valid email: {string}")
  public void iHaveEnteredAValidRecipientSubjectAndMessage(String email) {
    recEmail = email;
    driver.findElement(By.xpath(".//div[contains(@class, 'aoD hl')]")).click();
    driver.findElement(By.xpath("//textarea[@class='vO']")).sendKeys(email);
  }

  @When("I click the {string} button")
  public void iClickTheButton(String buttonName) {
    driver.findElement(By.xpath(".//div[@aria-label='"+buttonName+"' and @role='button']")).click();
  }

  @When("I switch to {string} mode")
  public void iSwitchToMode(String modeName) {
    WebElement photoFrame = driver.findElement(By.xpath("//iframe[contains(@src, 'picker')]"));
    driver.switchTo().frame(photoFrame);
    driver.findElement(By.xpath("//div[@value='"+modeName+"']")).click();
  }

  @When("I select a valid image {string}")
  public void iSelectAValidImageFile(String filepath) {
    File f = new File(filepath);
    filename = f.getName();
    WebElement fileInput = driver.findElement(By.xpath("//input[@type='file']"));
    fileInput.sendKeys(filepath);
  }

  @Then("the image should be attached to the email")
  public void theImageShouldBeAttachedToTheEmail() {
    driver.switchTo().defaultContent();
    driver.switchTo().activeElement();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//div[starts-with(@aria-label,'Attachment')]/a")));
    WebElement attach = driver.findElement(By.xpath(".//div[starts-with(@aria-label,'Attachment')]"));
    String label = attach.getAttribute("aria-label");
    assert label.contains(filename);
  }

  @And("the image should be included after sending the email")
  public void theImageShouldBeIncludedAfterSendingTheEmail() {
    driver.findElement(By.xpath(".//div[@class='J-J5-Ji btA']")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//span[@class='bAq']")));
    driver.findElement(By.xpath(".//span[@id='link_vsm']")).click();
    assert driver.findElement(By.xpath(".//img[@title='"+filename+"']")).isDisplayed();
  }


  @And("select a photo {string}")
  public void selectAPhoto(String photoname) {
    filename = photoname;
    driver.switchTo().activeElement();
    driver.findElement(By.xpath(".//*[@class='Mf-Cp-Qk-re' and @title='"+photoname+"']")).click();
    driver.findElement(By.xpath(".//div[@id='picker:ap:0' and @role='button']")).click();
  }

  @And("I select an image {string} larger than 25mb")
  public void iSelectAnImageFileLargerThanMb(String filepath) {
    File f = new File(filepath);
    filename = f.getName();
    WebElement fileInput = driver.findElement(By.xpath("//input[@type='file']"));
    fileInput.sendKeys(filepath);
  }

  @Then("a large file warning should be displayed")
  public void aLargeFileWarningShouldBeDisplayed() {
    driver.switchTo().activeElement();
    String warning = driver.findElement(By.xpath("//div[@role='dialog']//div[@class='HyIydd']")).getText();
    assert warning.contains("Your file is larger than 25 MB");
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//div[contains(@class,'gmail_chip gmail_drive_chip')]")));
  }

  @And("a link to file should be included in the email")
  public void aLinkToFileShouldBeIncludedInTheEmail() {
    WebElement attach = driver.findElement(By.xpath("//div[contains(@class,'gmail_chip gmail_drive_chip')]/a"));
    String label = attach.getAttribute("aria-label");
    assert label.contains(filename);
  }

  private void confirmInitState() {
    if(driver == null) {
      resetState();
    }
  }

  private void resetState() {
    System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
    driver = new ChromeDriver();
    wait = new WebDriverWait(driver, 30);
    driver.manage().window().maximize();
    driver.get(url);
    driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
    WebElement email_phone = driver.findElement(By.xpath("//input[@id='identifierId']"));
    email_phone.sendKeys(LOGIN_EMAIL);
    driver.findElement(By.id("identifierNext")).click();
    WebElement password = driver.findElement(By.xpath("//input[@name='password']"));
    wait.until(ExpectedConditions.elementToBeClickable(password));
    password.sendKeys(LOGIN_PASSWORD);
    driver.findElement(By.id("passwordNext")).click();
  }

  private void confirmEmailSent() {
    driver.findElement(By.xpath("//a[contains(@href, '#sent')]")).click();
    assert driver.findElement(By.xpath(".//span[@email='"+recEmail+"']")).isDisplayed();
  }

  private void tearDown() {
    driver.quit();
  }
}