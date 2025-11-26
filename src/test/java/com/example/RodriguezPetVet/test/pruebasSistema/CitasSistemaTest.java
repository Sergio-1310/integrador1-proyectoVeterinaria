package com.example.RodriguezPetVet.test.pruebasSistema;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CitasSistemaTest {

    private WebDriver driver;
    
    private final String BASE_URL = "http://localhost:8080/"; 
    private final int TIMEOUT_SECS = 10;

    @BeforeEach
    public void setUp() {
        
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        
        driver.manage().window().maximize(); 
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIMEOUT_SECS));
    }

    @Test
    public void testLoginYNavegarACitas() {
        // Credenciales de prueba
        final String USUARIO_TEST = "admin"; 
        final String PASSWORD_TEST = "admin123"; 

        // 1. Navegar a la página principal (seremos redirigidos a /login)
        driver.get(BASE_URL);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECS));
        WebElement campoUsuario = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username"))); 
        campoUsuario.sendKeys(USUARIO_TEST);
      
        WebElement campoPassword = driver.findElement(By.id("password")); 
        campoPassword.sendKeys(PASSWORD_TEST);

      
        WebElement botonLogin = driver.findElement(By.cssSelector("button[type='submit']")); 
        botonLogin.click();
        
       
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login"))); 

        WebElement linkCitas = driver.findElement(By.partialLinkText("Citas"));
        linkCitas.click();
        
        wait.until(ExpectedConditions.urlContains("/citas"));
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/citas"), "FALLÓ: No se pudo navegar a la página de Citas después del login. URL actual: " + currentUrl);
    }

    @AfterEach
    public void tearDown() {
        // Cierra el navegador al finalizar la prueba
        if (driver != null) {
            driver.quit();
        }
    }
}