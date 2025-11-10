package com.example.RodriguezPetVet.test.pruebasSistema;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
// Importamos JavascriptExecutor
import org.openqa.selenium.JavascriptExecutor; 
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MascotaSistemaTest {

    private WebDriver driver;
    // URL de tu aplicación local (asegúrate de que esté corriendo aquí)
    private final String BASE_URL = "http://localhost:8080/";
    // Tiempo de espera para elementos
    private final int TIMEOUT_SECS = 10;
    
    // Credenciales de prueba
    private final String USUARIO_TEST = "admin"; 
    private final String PASSWORD_TEST = "admin123"; 

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    /**
     * Método de utilidad para realizar el flujo de inicio de sesión.
     */
    private void realizarLogin() {
        driver.get(BASE_URL);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECS));
        
        WebElement campoUsuario = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username"))); 
        campoUsuario.sendKeys(USUARIO_TEST);

        WebElement campoPassword = driver.findElement(By.id("password")); 
        campoPassword.sendKeys(PASSWORD_TEST);

        WebElement botonLogin = driver.findElement(By.cssSelector("button[type='submit']")); 
        botonLogin.click();

        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login")));
    }


    /**
     * Test principal que realiza el flujo de Crear, Leer y Eliminar una Mascota.
     */
    @Test
    public void testCrudMascotaCompleto() {
        realizarLogin(); // 1. Iniciar sesión correctamente
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECS));
        JavascriptExecutor js = (JavascriptExecutor) driver; // Inicializamos el Executor
        
        final String NOMBRE_MASCOTA = "Gala - Prueba Selenium";
        final String NOMBRE_DUENO = "Carlos Prueba";

        // --- 2. CREAR (C) ---
        
        // 2.1 Navegar a la página de Historial Médico
        WebElement linkMascotas = wait.until(
            ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Historial Medico')]")) 
        );
        linkMascotas.click();
        
        wait.until(ExpectedConditions.urlContains("/historial_clinico")); 
        
        // 2.2 Clic en el botón para abrir el MODAL de registro
        WebElement btnAbrirModal = wait.until(
            ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Registrar Nueva Mascota')]")) 
        );
        btnAbrirModal.click();

        // 2.3 ESPERAR A QUE EL MODAL SEA VISIBLE
        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modalRegistroMascota")));

        // 2.4 Llenar el formulario DENTRO del modal 
        modal.findElement(By.id("nombre")).sendKeys(NOMBRE_MASCOTA);
        new Select(modal.findElement(By.id("sexo"))).selectByVisibleText("Macho"); 
        modal.findElement(By.id("peso")).sendKeys("35.5");
        modal.findElement(By.id("especie")).sendKeys("Perro"); 
        modal.findElement(By.id("edad")).sendKeys("3");
        new Select(modal.findElement(By.id("esterilizado"))).selectByVisibleText("Sí");
        modal.findElement(By.id("raza")).sendKeys("Labrador"); 
        modal.findElement(By.id("color")).sendKeys("Dorado");
        modal.findElement(By.id("medicinaPreventiva")).sendKeys("Todas las vacunas al día.");

        // Datos del Dueño
        modal.findElement(By.id("nombreDueno")).sendKeys(NOMBRE_DUENO);
        modal.findElement(By.id("telefonoDueno")).sendKeys("555-12345");
        modal.findElement(By.id("direccionDueno")).sendKeys("Calle Falsa 123");

        // 2.5 Guardar la nueva Mascota
        WebElement btnGuardar = modal.findElement(By.xpath(".//button[contains(text(), 'Registrar Mascota')]"));
        btnGuardar.click();

    } 

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}