package com.example.RodriguezPetVet.test.pruebasSistema;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor; 
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class InventarioSistemaTest {

    private WebDriver driver;
    private WebDriverWait wait; 
    private JavascriptExecutor js;

    // --- Constantes de Configuración ---
    private final String BASE_URL = "http://localhost:8080"; 
    private final int TIMEOUT_SECS = 10;
    
    // --- Credenciales ---
    private final String USUARIO_TEST = "admin"; 
    private final String PASSWORD_TEST = "admin123"; 

    // --- Constantes del Producto (Solo se usan Nombre, Precio y Stock) ---
    private final String NOMBRE_PRODUCTO = "Vacuna - Test Selenium " + System.currentTimeMillis(); 
    private final String PRECIO = "25.50";
    private final String STOCK = "50";


    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECS));
        js = (JavascriptExecutor) driver; 
    }

    /**
     * Método de utilidad para realizar el flujo de inicio de sesión.
     */
    private void realizarLogin() {
        driver.get(BASE_URL + "/login"); 
        
        WebElement campoUsuario = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username"))); 
        campoUsuario.sendKeys(USUARIO_TEST);

        WebElement campoPassword = driver.findElement(By.id("password")); 
        campoPassword.sendKeys(PASSWORD_TEST);

        WebElement botonLogin = driver.findElement(By.cssSelector("button[type='submit']")); 
        botonLogin.click();

        // Esperar la redirección a /index
        wait.until(ExpectedConditions.urlToBe(BASE_URL + "/index"));
        System.out.println("DEBUG: Login exitoso y en /index.");
    }

    
    @Test
    public void testCrearProducto() throws InterruptedException {
        realizarLogin(); 
        
        // Localizador para el botón que nos lleva a Crear Nuevo Producto
        By btnCrearLinkLocator = By.xpath("//a[contains(text(), 'Crear Nuevo Producto')]");
        
        // --- 1. NAVEGACIÓN A INVENTARIO ---
        System.out.println("DEBUG: Navegando a Inventario.");
        
        // Hacemos click forzado en 'Inventario General' del menú de navegación.
        WebElement linkInventario = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(), 'Inventario General')]")) 
        );
        js.executeScript("arguments[0].click();", linkInventario);
        
        // Esperamos que el botón 'Crear Nuevo Producto' sea visible, confirmando que la página de inventario cargó.
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnCrearLinkLocator));
        System.out.println("DEBUG: Página de Inventario cargada correctamente.");


        // --- 2. ACCEDER AL FORMULARIO ---
        System.out.println("DEBUG: Iniciando paso 2 - Acceder al formulario de CREAR Producto.");
        
        // Clic en el botón para ir al formulario de registro (Crear Nuevo Producto)
        WebElement btnCrear = driver.findElement(btnCrearLinkLocator); 
        btnCrear.click();
        
        // Esperar a que la URL cambie a la página del formulario
        wait.until(ExpectedConditions.urlContains("/productos/new"));
        
        
        // --- 3. LLENAR FORMULARIO Y GUARDAR ---
        System.out.println("DEBUG: Iniciando paso 3 - Llenar y guardar.");
        
        // Sincronización: esperamos que el campo 'name' esté presente.
        By nameLocator = By.id("name");
        wait.until(ExpectedConditions.presenceOfElementLocated(nameLocator));


        // Llenar campos del formulario
        driver.findElement(nameLocator).sendKeys(NOMBRE_PRODUCTO);
        driver.findElement(By.id("price")).sendKeys(PRECIO); 
        driver.findElement(By.id("quantity")).sendKeys(STOCK);
        
        // Clic en el botón Guardar/Crear Producto
        // El botón en el formulario se llama 'Crear Producto' (sin span, type='submit')
        WebElement btnGuardar = driver.findElement(By.xpath("//button[@type='submit']")); 
        btnGuardar.click();
        
        // Esperar redirección de vuelta al inventario (esperamos de nuevo el botón 'Crear Nuevo Producto')
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnCrearLinkLocator)); 
        System.out.println("DEBUG: Producto guardado y redirigido a inventario.");
        
        
        // --- 4. VERIFICAR (R) ---
        // Verificamos que el producto aparezca en la tabla
        String productoRowXPath = "//td[contains(text(), '" + NOMBRE_PRODUCTO + "')]";
        WebElement productoCreado = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.xpath(productoRowXPath))
        );
        assertTrue(productoCreado.isDisplayed(), "FALLÓ: El producto recién creado no se encontró en la lista/tabla.");
        
        System.out.println("✅ ÉXITO: El producto se creó y se verificó correctamente en el inventario.");
    }

    //-----------------------------------------------------------------------------------
    
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}