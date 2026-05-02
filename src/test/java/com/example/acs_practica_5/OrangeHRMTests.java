package com.example.acs_practica_5;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class OrangeHRMTests {
    
    private static final String BASE_URL = "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login";
    private static final String VALID_USERNAME = "Admin";
    private static final String VALID_PASSWORD = "admin123";
    private static final int WAIT_TIMEOUT = 10;
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT));
        System.out.println("\n========== STARTING ORANGEHRM TESTS ==========\n");
    }
    
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    @Test
    public void testTC01_SuccessfulLogin() {
        testTC01_SuccessfulLoginImpl(driver, wait);
    }

    @Test
    public void testTC02_FailedLoginWrongPassword() {
        testTC02_FailedLoginWrongPasswordImpl(driver, wait);
    }

    @Test
    public void testTC03_LoginBlankUsername() {
        testTC03_LoginBlankUsernameImpl(driver, wait);
    }

    @Test
    public void testTC04_LoginUIValidation() {
        testTC04_LoginUIValidationImpl(driver, wait);
    }

    @Test
    public void testTC05_DashboardLoad() {
        testTC05_DashboardLoadImpl(driver, wait);
    }

    @Test
    public void testTC06_SidebarMenuDashboard() {
        testTC06_SidebarMenuDashboardImpl(driver, wait);
    }

    @Test
    public void testTC07_NavigateRecruitment() {testTC07_NavigateRecruitmentImpl(driver, wait);}

    @Test
    public void testTC08_RecruitmentFilterVacancy() {testTC08_RecruitmentFilterVacancyImpl(driver, wait);}

    @Test
    public void testTC09_RecruitmentTableColumns() {testTC09_RecruitmentTableColumnsImpl(driver, wait);}

    @Test
    public void testTC10_NavigatePIM() {testTC10_NavigatePIMImpl(driver, wait);}

    @Test
    public void testTC11_PIMSearchEmployee() {testTC11_PIMSearchEmployeeImpl(driver, wait);}

    @Test
    public void testTC12_PIMFilterEmploymentStatus() {testTC12_PIMFilterEmploymentStatusImpl(driver, wait);}

    @Test
    public void testTC13_PIMAddEmployeeUI() {testTC13_PIMAddEmployeeUIImpl(driver, wait);}
    
    private void testTC01_SuccessfulLoginImpl(WebDriver driver, WebDriverWait wait) {
        try {
            driver.navigate().to(BASE_URL);
            loginAs(driver, wait, VALID_USERNAME, VALID_PASSWORD);
            
            wait.until(ExpectedConditions.urlContains("/dashboard"));
            
            if (driver.getCurrentUrl().contains("/dashboard")) {
                System.out.println("✅ PASS: TC-01 - Successful login. URL contains '/dashboard'");
            } else {
                System.out.println("❌ FAIL: TC-01 - URL does not contain '/dashboard'. Current URL: " + driver.getCurrentUrl());
            }
        } catch (Exception e) {
            System.out.println("❌ FAIL: TC-01 - " + e.getMessage());
        }
    }
    
    private void testTC02_FailedLoginWrongPasswordImpl(WebDriver driver, WebDriverWait wait) {
        try {
            driver.navigate().to(BASE_URL);
            loginAs(driver, wait, VALID_USERNAME, "wrongpass");
            
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h6[contains(text(), 'Dashboard')]")
            ));
            
            WebElement errorMessage = driver.findElement(
                By.xpath("//div[@role='alert']")
            );
            
            if (errorMessage.isDisplayed()) {
                System.out.println("✅ PASS: TC-02 - Error message 'Invalid credentials' visible");
            } else {
                System.out.println("❌ FAIL: TC-02 - Error message not visible");
            }
        } catch (Exception e) {
            System.out.println("❌ FAIL: TC-02 - " + e.getMessage());
        }
    }
    
    private void testTC03_LoginBlankUsernameImpl(WebDriver driver, WebDriverWait wait) {
        try {
            driver.navigate().to(BASE_URL);
            
            WebElement passwordField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.name("password")
                )
            );
            
            passwordField.clear();
            passwordField.sendKeys(VALID_PASSWORD);
            
            WebElement loginButton = driver.findElement(
                By.xpath("//button[@type='submit']")
            );
            loginButton.click();
            
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[@class='oxd-input-feedback']")
            ));
            
            WebElement requiredMessage = driver.findElement(
                By.xpath("//span[@class='oxd-input-feedback']")
            );
            
            if (requiredMessage.isDisplayed()) {
                System.out.println("✅ PASS: TC-03 - 'Required' message visible when username is blank");
            } else {
                System.out.println("❌ FAIL: TC-03 - 'Required' message not visible");
            }
        } catch (Exception e) {
            System.out.println("❌ FAIL: TC-03 - " + e.getMessage());
        }
    }
    
    private void testTC04_LoginUIValidationImpl(WebDriver driver, WebDriverWait wait) {
        try {
            driver.navigate().to(BASE_URL);
            
            WebElement usernameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.name("username")
                )
            );
            boolean usernameVisible = usernameField.isDisplayed();
            
            WebElement passwordField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.name("password")
                )
            );
            boolean passwordVisible = passwordField.isDisplayed();
            
            WebElement loginButton = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//button[@type='submit']")
                )
            );
            boolean loginButtonVisible = loginButton.isDisplayed();
            
            if (usernameVisible && passwordVisible && loginButtonVisible) {
                System.out.println("✅ PASS: TC-04 - All login UI elements are visible");
            } else {
                System.out.println("❌ FAIL: TC-04 - Some elements not visible. " +
                    "Username: " + usernameVisible + ", Password: " + passwordVisible + 
                    ", Button: " + loginButtonVisible);
            }
        } catch (Exception e) {
            System.out.println("❌ FAIL: TC-04 - " + e.getMessage());
        }
    }
    
    private void testTC05_DashboardLoadImpl(WebDriver driver, WebDriverWait wait) {
        try {
            driver.navigate().to(BASE_URL);
            loginAs(driver, wait, VALID_USERNAME, VALID_PASSWORD);
            
            wait.until(ExpectedConditions.urlContains("/dashboard"));
            
            WebElement dashboardElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h6[contains(text(), 'Dashboard')]")
                )
            );
            
            if (dashboardElement.isDisplayed()) {
                System.out.println("✅ PASS: TC-05 - Dashboard loaded successfully");
            } else {
                System.out.println("❌ FAIL: TC-05 - Dashboard element not visible");
            }
        } catch (Exception e) {
            System.out.println("❌ FAIL: TC-05 - " + e.getMessage());
        }
    }
    
    private void testTC06_SidebarMenuDashboardImpl(WebDriver driver, WebDriverWait wait) {
        try {
            driver.navigate().to(BASE_URL);
            loginAs(driver, wait, VALID_USERNAME, VALID_PASSWORD);
            
            wait.until(ExpectedConditions.urlContains("/dashboard"));
            
            WebElement pimMenu = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a//span[contains(text(), 'PIM')]")
                )
            );
            boolean pimVisible = pimMenu.isDisplayed();
            
            WebElement recruitmentMenu = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a//span[contains(text(), 'Recruitment')]")
                )
            );
            boolean recruitmentVisible = recruitmentMenu.isDisplayed();
            
            if (pimVisible && recruitmentVisible) {
                System.out.println("✅ PASS: TC-06 - Sidebar menu contains 'PIM' and 'Recruitment'");
            } else {
                System.out.println("❌ FAIL: TC-06 - Menu incomplete. PIM: " + pimVisible + 
                    ", Recruitment: " + recruitmentVisible);
            }
        } catch (Exception e) {
            System.out.println("❌ FAIL: TC-06 - " + e.getMessage());
        }
    }

    // TC-07: Navegar al módulo Recruitment
    private void testTC07_NavigateRecruitmentImpl(WebDriver driver, WebDriverWait wait) {
        try {
            driver.navigate().to(BASE_URL);
            loginAs(driver, wait, VALID_USERNAME, VALID_PASSWORD);

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Recruitment']"))).click();

            wait.until(ExpectedConditions.urlContains("/recruitment"));
            WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h6")));

            if (driver.getCurrentUrl().contains("/recruitment") && title.getText().equals("Recruitment")) {
                System.out.println("PASS: TC-07 - Navegacion a Recruitment exitosa.");
            } else {
                System.out.println("FAIL: TC-07 - Error en URL o título de Recruitment.");
            }
        } catch (Exception e) {
            System.out.println("FAIL: TC-07 - " + e.getMessage());
        }
    }

    // TC-08: Usar dropdown 'Vacancies' para filtrar
    private void testTC08_RecruitmentFilterVacancyImpl(WebDriver driver, WebDriverWait wait) {
        try {
            driver.navigate().to(BASE_URL);
            loginAs(driver, wait, VALID_USERNAME, VALID_PASSWORD);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Recruitment']"))).click();

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Vacancies']"))).click();

            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[text()='Vacancy']/ancestor::div[contains(@class, 'oxd-input-group')]//div[contains(@class, 'oxd-select-wrapper')]")));
            dropdown.click();

            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@role='listbox']//span[contains(text(), 'Software Engineer')]")));
            option.click();

            driver.findElement(By.xpath("//button[@type='submit']")).click();
            System.out.println("PASS: TC-08 - Filtro por dropdown ejecutado correctamente.");
        } catch (Exception e) {
            System.out.println("FAIL: TC-08 - " + e.getMessage());
        }
    }

    // TC-09: Validar que la tabla de candidatos muestra columnas correctas
    private void testTC09_RecruitmentTableColumnsImpl(WebDriver driver, WebDriverWait wait) {
        try {
            driver.navigate().to(BASE_URL);
            loginAs(driver, wait, VALID_USERNAME, VALID_PASSWORD);

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Recruitment']"))).click();

            WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("oxd-table-header")));

            String headerText = header.getText();

            boolean allColumnsPresent = headerText.contains("Vacancy") &&
                    headerText.contains("Hiring Manager") &&
                    headerText.contains("Status") &&
                    headerText.contains("Candidate") &&
                    headerText.contains("Date of Application");

            if (allColumnsPresent) {
                System.out.println("PASS: TC-09 - Columnas de la tabla Recruitment validadas correctamente.");
            } else {
                System.out.println("FAIL: TC-09 - Faltan columnas en la tabla. Encontrado: \n" + headerText);
            }
        } catch (Exception e) {
            System.out.println("FAIL: TC-09 - " + e.getMessage());
        }
    }

    // TC-10: Navegar al módulo PIM
    private void testTC10_NavigatePIMImpl(WebDriver driver, WebDriverWait wait) {
        try {
            driver.navigate().to(BASE_URL);
            loginAs(driver, wait, VALID_USERNAME, VALID_PASSWORD);

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='PIM']"))).click();
            wait.until(ExpectedConditions.urlContains("/pim"));

            if (driver.getCurrentUrl().contains("/pim")) {
                System.out.println("PASS: TC-10 - Navegacion a PIM exitosa.");
            } else {
                System.out.println("FAIL: TC-10 - No se pudo verificar la URL de PIM.");
            }
        } catch (Exception e) {
            System.out.println("FAIL: TC-10 - " + e.getMessage());
        }
    }

    // TC-11: Buscar empleado por nombre en PIM
    private void testTC11_PIMSearchEmployeeImpl(WebDriver driver, WebDriverWait wait) {
        try {
            driver.navigate().to(BASE_URL);
            loginAs(driver, wait, VALID_USERNAME, VALID_PASSWORD);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='PIM']"))).click();

            WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[text()='Employee Name']/parent::div/following-sibling::div//input")));
            searchInput.sendKeys("John");

            driver.findElement(By.xpath("//button[@type='submit']")).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("oxd-table-card")));
            System.out.println("PASS: TC-11 - Búsqueda de empleado en PIM completada.");
        } catch (Exception e) {
            System.out.println("FAIL: TC-11 - " + e.getMessage());
        }
    }

    // TC-12: Usar dropdown para ordenar lista de empleados
    private void testTC12_PIMFilterEmploymentStatusImpl(WebDriver driver, WebDriverWait wait) {
        try {
            driver.navigate().to(BASE_URL);
            loginAs(driver, wait, VALID_USERNAME, VALID_PASSWORD);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='PIM']"))).click();

            WebElement statusDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[text()='Employment Status']/parent::div/following-sibling::div//i")));
            statusDropdown.click();

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Full-Time Permanent']"))).click();

            driver.findElement(By.xpath("//button[@type='submit']")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("oxd-table-card")));
            System.out.println("PASS: TC-12 - Filtrado por Estatus de Empleo realizado.");
        } catch (Exception e) {
            System.out.println("FAIL: TC-12 - " + e.getMessage());
        }
    }

    // TC-13: Validar UI: campos visibles en formulario de empleado
    private void testTC13_PIMAddEmployeeUIImpl(WebDriver driver, WebDriverWait wait) {
        try {
            driver.navigate().to(BASE_URL);
            loginAs(driver, wait, VALID_USERNAME, VALID_PASSWORD);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='PIM']"))).click();

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Add Employee']"))).click();

            boolean isFirstNameVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("firstName"))).isDisplayed();
            boolean isMiddleNameVisible = driver.findElement(By.name("middleName")).isDisplayed();
            boolean isLastNameVisible = driver.findElement(By.name("lastName")).isDisplayed();
            boolean isEmployeeIdVisible = driver.findElement(By.xpath("//label[text()='Employee Id']")).isDisplayed();


            if (isFirstNameVisible && isMiddleNameVisible && isLastNameVisible && isEmployeeIdVisible) {
                System.out.println("PASS: TC-13 - Formulario de nuevo empleado validado correctamente.");
            } else {
                System.out.println("FAIL: TC-13 - Elementos del formulario no son visibles.");
            }
        } catch (Exception e) {
            System.out.println("FAIL: TC-13 - " + e.getMessage());
        }
    }
    
    private void loginAs(WebDriver driver, WebDriverWait wait, String username, String password) {
        try {
            WebElement usernameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.name("username")
                )
            );
            usernameField.clear();
            usernameField.sendKeys(username);
            
            WebElement passwordField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.name("password")
                )
            );
            passwordField.clear();
            passwordField.sendKeys(password);
            
            WebElement loginButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit']")
                )
            );
            loginButton.click();
            
        } catch (Exception e) {
            throw new RuntimeException("Error in loginAs method: " + e.getMessage(), e);
        }
    }
}
