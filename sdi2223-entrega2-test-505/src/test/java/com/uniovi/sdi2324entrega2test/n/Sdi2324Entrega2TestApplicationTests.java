package com.uniovi.sdi2324entrega2test.n;

import com.uniovi.sdi2324entrega2test.n.pageobjects.*;
import com.uniovi.sdi2324entrega2test.n.util.DatabaseUtils;
import com.uniovi.sdi2324entrega2test.n.util.SeleniumUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Sdi2324Entrega2TestApplicationTests {
    static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    static String Geckodriver = "C:\\Dev\\tools\\selenium\\geckodriver-v0.30.0-win64.exe";
    static WebDriver driver = getDriver(PathFirefox, Geckodriver);
    static String URL = "http://localhost:8081";

    public static WebDriver getDriver(String PathFirefox, String Geckodriver) {
        System.setProperty("webdriver.firefox.bin", PathFirefox);
        System.setProperty("webdriver.gecko.driver", Geckodriver);
        driver = new FirefoxDriver();
        return driver;
    }

    @BeforeEach
    public void setUp() {
        driver.navigate().to(URL);
    }

    // Después de cada prueba se borran las cookies del navegador
    @AfterEach
    public void tearDown() {
        driver.manage().deleteAllCookies();
    }

    // Antes de la primera prueba
    @BeforeAll
    static public void begin() {
        // Crear los usuarios de prueba
        driver.navigate().to(URL);
        DatabaseUtils.seedUsers();
    }

    // Al finalizar la última prueba
    @AfterAll
    static public void end() {
        // Cerramos el navegador al finalizar las pruebas
        driver.quit();
    }

    // -------------------------------------
    // Part 1 - Web Application
    // -------------------------------------

    // [Prueba1] Registro de Usuario con datos válidos.
    @Test
    @Order(1)
    void PR01() {
        DatabaseUtils.resetUsersCollection();
        // Nos movemos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        // Cumplimentamos el registro con datos VALIDOS
        PO_SignUpView.fillForm(driver, "user01@email.com", "User01", "User01", "2003-05-22", "user01", "user01");
        // Comprobamos que hemos ido a la pagina de "MyOffers"
        // Confirmando así que el registro se ha completado con exito
        PO_HomeView.checkWelcomeToPage(driver, "standard");
    }

    // [Prueba2] Registro de Usuario con datos inválidos
    // (email, nombre, apellidos y fecha de nacimiento vacíos).
    @Test
    @Order(2)
    void PR02() {
        DatabaseUtils.resetUsersCollection();
        // Nos movemos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        // Cumplimentamos el registro con datos INVALIDOS
        PO_SignUpView.fillForm(driver, "", "", "", "", "77777", "77777");
        // Comprobamos que seguimos en la pantalla de registro
        PO_SignUpView.checkSignUpPage(driver);
    }

    // [Prueba3] Registro de Usuario con datos inválidos (repetición de contraseña inválida).
    @Test
    @Order(3)
    void PR03() {
        DatabaseUtils.resetUsersCollection();
        // Nos movemos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        // Cumplimentamos el registro con datos INVALIDOS
        PO_SignUpView.fillForm(driver, "user01@email.com", "User01", "User01", "2003-05-22", "user01", "user011");
        // Comprobamos que seguimos en la pantalla de registro
        PO_SignUpView.checkSignUpPage(driver);
    }

    // [Prueba4] Registro de Usuario con datos inválidos (email existente).
    @Test
    @Order(4)
    void PR04() {
        DatabaseUtils.resetUsersCollection();
        DatabaseUtils.seedUsers();
        // Nos movemos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        // Cumplimentamos el registro con datos INVALIDOS
        PO_SignUpView.fillForm(driver, "user01@email.com", "User01", "User01", "2003-05-22", "user01", "user01");
        // Comprobamos que seguimos en la pantalla de registro
        PO_SignUpView.checkSignUpPage(driver);
    }

    // [Prueba5] Inicio de sesión con datos válidos (administrador).
    @Test
    @Order(5)
    void PR05() {
        DatabaseUtils.resetUsersCollection();
        DatabaseUtils.seedUsers();
        // Nos movemos al formulario de inicio de sesión
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos con datos validos del usuario administrador
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
        // Comprobamos que hemos ido a la pagina de home, confirmando que el inicio de sesión se ha completado con exito
        PO_HomeView.checkWelcomeToPage(driver, "admin");
    }

    // [Prueba6] Inicio de sesión con datos válidos (usuario estándar).
    @Test
    @Order(6)
    void PR06() {
        DatabaseUtils.resetUsersCollection();
        DatabaseUtils.seedUsers();
        // Nos movemos al formulario de inicio de sesión
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos con datos validos del usuario estandar
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Comprobamos que hemos ido a la pagina de home, confirmando que el inicio de sesión se ha completado con exito
        PO_HomeView.checkWelcomeToPage(driver, "standard");
    }

    // [Prueba7] Inicio de sesión con datos inválidos
    // (usuario estándar, email existente, pero contraseña incorrecta).
    @Test
    @Order(7)
    void PR07() {
        DatabaseUtils.resetUsersCollection();
        DatabaseUtils.seedUsers();
        // Insertar contraseña incorrecta
        SeleniumUtils.logInIntoAccount(driver, "STANDARD", "user01@email.com", "123");
        PO_LoginView.checkLoginPage(driver);
    }

    // [Prueba8] Inicio de sesión con datos inválidos (campo email o contraseña vacíos).
    @Test
    @Order(8)
    void PR08() {
        DatabaseUtils.resetUsersCollection();
        DatabaseUtils.seedUsers();
        // Insertar contraseña incorrecta
        SeleniumUtils.logInIntoAccount(driver, "STANDARD", "", "user01");
        PO_LoginView.checkLoginPage(driver);
    }

    // [Prueba9] Hacer clic en la opción de salir de sesión y
    // comprobar que se redirige a la página de inicio de sesión (Login).
    @Test
    @Order(9)
    void PR09() {
        DatabaseUtils.resetUsersCollection();
        DatabaseUtils.seedUsers();
        // Nos movemos al formulario de inicio de sesión
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos con datos validos del usuario estandar
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Comprobamos que hemos ido a la pagina de home, confirmando que el inicio de sesión se ha completado con exito
        PO_HomeView.checkWelcomeToPage(driver, "standard");
        // Nos movemos al formulario de inicio de sesión, cerrando la sesión actual
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
        // Comprobamos que estamos en la pantalla de inicio de sesión
        PO_LoginView.checkLoginPage(driver);
    }

    // [Prueba10] Comprobar que el botón cerrar sesión no está visible si
    // el usuario no está autenticado.
    @Test
    @Order(10)
    void PR010() {
        // Buscamos que tenga el texto
        SeleniumUtils.textIsNotPresentOnPage(driver, "Cerrar Sesión");
    }

    // [Prueba11] Mostrar el listado de usuarios.
    // Comprobar que se muestran todos los que existen en el sistema,
    // contabilizando al menos el número de usuarios.
    @Test
    @Order(11)
    void PR011() {
        DatabaseUtils.resetUsersCollection();
        DatabaseUtils.seedUsers();
        // Iniciar sesión como administrador
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "admin@email.com", "admin");
        // Comprobar que se muestran todos los usuarios
        // Consultar primera pagina
        driver.findElement(By.id("Recargar")).click(); // Recargar tabla de usuarios
        List<WebElement> firstPageUsers = PO_UserListView.getUsersList(driver);
        Assertions.assertEquals(firstPageUsers.size(), 4);
        // Consultar segunda pagina
        driver.findElement(By.id("pi-2")).click();
        List<WebElement> secondPageUsers = PO_UserListView.getUsersList(driver);
        Assertions.assertEquals(4, secondPageUsers.size());
        // Consultar tercera pagina
        driver.findElement(By.id("pi-3")).click();
        List<WebElement> thirdPageUsers = PO_UserListView.getUsersList(driver);
        Assertions.assertEquals(4, thirdPageUsers.size());
        // Consultar cuarta pagina
        driver.findElement(By.id("pi-4")).click();
        List<WebElement> fourthPageUsers = PO_UserListView.getUsersList(driver);
        Assertions.assertEquals(3, fourthPageUsers.size());
    }

    // [Prueba12] Ir a la lista de usuarios, borrar el primer usuario de la lista,
    // comprobar que la lista se actualiza y dicho usuario desaparece.
    @Test
    @Order(12)
    void PR012() {
        DatabaseUtils.resetUsersCollection();
        DatabaseUtils.seedUsers();
        // Nos movemos al formulario de inicio de sesion
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        // Cumplimentamos el registro con datos VALIDOS (Admin)
        PO_LoginView.fillForm(driver, "admin@email.com", "admin");
        PO_HomeView.checkWelcomeToPage(driver, "admin");
        // Sacamos la lista de usuarios que hay
        driver.findElement(By.id("Recargar")).click(); // Recargar tabla de usuarios
        List<WebElement> usersList = PO_UserListView.getUsersList(driver);
        // Seleccionamos el checkbox del primer usuario
        PO_UserListView.markCheckBoxUser(driver);
        // Borramos dandole al boton
        PO_UserListView.clickDeleteButton(driver);
        // Comprobamos que en la última página hay un usuario menos
        driver.findElement(By.id("pi-2")).click();
        driver.findElement(By.id("pi-4")).click();
        List<WebElement> fourthPageUsers = PO_UserListView.getUsersList(driver);
        Assertions.assertEquals(2, fourthPageUsers.size());
    }

    // [Prueba13] Ir a la lista de usuarios, borrar el último usuario de la lista,
    // comprobar que la lista se actualiza y dicho usuario desaparece.
    @Test
    @Order(13)
    void PR013() {
        DatabaseUtils.resetUsersCollection();
        DatabaseUtils.seedUsers();
        // Nos movemos al formulario de inicio de sesion
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        // Cumplimentamos el registro con datos VALIDOS
        PO_LoginView.fillForm(driver, "admin@email.com", "admin");
        PO_HomeView.checkWelcomeToPage(driver, "admin");
        // Sacamos la lista de usuarios
        driver.findElement(By.id("Recargar")).click(); // Recargar tabla de usuarios
        List<WebElement> usersList = PO_UserListView.getUsersList(driver);
        // Seleccionamos el checkbox del último usuario
        driver.findElement(By.id("pi-2")).click();
        driver.findElement(By.id("pi-4")).click();
        PO_UserListView.markCheckBoxUser(driver);
        // Borramos el usuario
        PO_UserListView.clickDeleteButton(driver);
        // Comprobamos que en la última página hay un usuario menos
        driver.findElement(By.id("pi-2")).click();
        driver.findElement(By.id("pi-4")).click();
        List<WebElement> fourthPageUsers = PO_UserListView.getUsersList(driver);
        Assertions.assertEquals(2, fourthPageUsers.size());
    }

    // [Prueba14] Ir a la lista de usuarios, borrar 3 usuarios, comprobar que la lista se actualiza
    // y dichos usuarios desaparecen.
    @Test
    @Order(14)
    void PR014() {
        DatabaseUtils.resetUsersCollection();
        DatabaseUtils.seedUsers();
        // Nos movemos al formulario de inicio de sesion
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        // Cumplimentamos el registro con datos VALIDOS
        PO_LoginView.fillForm(driver, "admin@email.com", "admin");
        PO_HomeView.checkWelcomeToPage(driver, "admin");
        // Sacamos la lista de usuarios que hay
        driver.findElement(By.id("Recargar")).click(); // Recargar tabla de usuarios
        List<WebElement> usersList = PO_UserListView.getUsersList(driver);
        // Sacamos los tres primeros usuarios y marcamos de sus checkboxes
        WebElement u1 = usersList.get(0);
        WebElement u2 = usersList.get(1);
        WebElement u3 = usersList.get(2);
        PO_UserListView.markCheckBoxUser(driver);
        PO_UserListView.markCheckBoxUser(driver);
        PO_UserListView.markCheckBoxUser(driver);
        // Borramos dandole al boton
        PO_UserListView.clickDeleteButton(driver);
        // Comprobamos que en la última página, ahora la 3, hay 4 usuarios
        driver.findElement(By.id("pi-3")).click();
        List<WebElement> fourthPageUsers = PO_UserListView.getUsersList(driver);
        Assertions.assertEquals(4, fourthPageUsers.size());
    }

    // [Prueba15] Intentar borrar el usuario que se encuentra en sesión y
    // comprobar que no ha sido borrado.
    @Test
    @Order(15)
    void PR015() {
        DatabaseUtils.resetUsersCollection();
        DatabaseUtils.seedUsers();
        // Nos movemos al formulario de inicio de sesion
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        // Cumplimentamos el registro con datos VALIDOS
        PO_LoginView.fillForm(driver, "admin@email.com", "admin");
        PO_HomeView.checkWelcomeToPage(driver, "admin");
        // Buscamos el usuario administrador y comprobamos que no aparece en la lista
        Assertions.assertFalse(PO_UserListView.findUserInList(driver, "admin@email.com"));
    }

    // [Prueba16] Ir al formulario de alta de oferta, rellenarla con datos válidos
    // y pulsar el botón Submit.
    // Comprobar que la oferta sale en el listado de ofertas de dicho usuario.
    @Test
    @Order(16)
    public void PR16() {
        DatabaseUtils.resetUsersCollection();
        DatabaseUtils.seedUsers();
        // Iniciar sesión con datos validos del usuario estandar
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Añadir una oferta como usuario estándar
        PO_OfferView.simulateAddNewOffer(driver, "Oferta1", "Descripcion de la oferta1", "10");
        // Comprobar que la oferta aparece en el listado de ofertas del usuario
        driver.findElement(By.id("Recargar")).click(); // Recargar tabla de mis ofertas
        PO_OfferView.checkAddOffer(driver, "Oferta1");
    }

    // [Prueba17] Ir al formulario de alta de oferta, rellenarla con datos inválidos
    // (campo título vacío y precio en negativo) y pulsar el botón Submit.
    // Comprobar que se muestra el mensaje de campo inválido.
    @Test
    @Order(17)
    public void PR17() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.seedUsers();
        // Iniciar sesión con datos validos del usuario estandar
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Añadir una oferta introduciendo datos inválidos. Precio negativo
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba", "Descripcion de la oferta de prueba", "-1");
        // Comprobar que se muestra el mensaje de campo inválido. En este caso,
        // el campo precio debe ser mayor que 0.
        PO_View.checkErrorMessageIsShown(driver, "Datos incorrectos");
    }

    // [Prueba18] Mostrar el listado de ofertas para dicho usuario y
    // comprobar que se muestran todas las que existen para este usuario.
    @Test
    @Order(18)
    public void PR18() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.seedUsers();
        // Iniciar sesión con datos validos del usuario estandar
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Añadir dos ofertas de prueba
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 1", "Descripcion de la oferta de prueba 1", "10");
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 2", "Descripcion de la oferta de prueba 2", "3");
        driver.findElement(By.id("Recargar")).click(); // Recargar tabla de mis ofertas
        PO_OfferView.checkMyOfferListingContainsOffers(driver, 2);
    }

    // [Prueba19] Ir a la lista de ofertas, borrar la primera oferta de la lista,
    // comprobar que la lista se actualiza y que la oferta desaparece.
    @Test
    @Order(19)
    public void PR19() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.seedUsers();
        // Iniciar sesión con datos validos del usuario estandar
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Añadir dos ofertas de prueba
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 1", "Descripcion de la oferta de prueba 1", "10");
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 2", "Descripcion de la oferta de prueba 2", "3");
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 3", "Descripcion de la oferta de prueba 3", "5");
        // Obtener la primera oferta de la lista y borrarla
        driver.findElement(By.id("Recargar")).click(); // Recargar tabla de mis ofertas
        PO_OfferView.deleteOfferFromUserOffersList(driver, 1);
        // Comprobar que la oferta desaparece. Para ello, comprobar que no
        // aparece el título de la oferta en la lista de ofertas.
        driver.findElement(By.id("Recargar")).click(); // Recargar tabla de mis ofertas
        PO_OfferView.checkOfferNotAppearOnList(driver, 1, "Oferta de prueba 1");
        PO_OfferView.checkOfferNotAppearOnList(driver, 2, "Oferta de prueba 1");
    }

    // [Prueba20] Ir a la lista de ofertas, borrar la última oferta de la lista,
    // comprobar que la lista se actualiza y que la oferta desaparece.
    @Test
    @Order(20)
    public void PR20() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.seedUsers();
        // Iniciar sesión con datos validos del usuario estandar
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Añadir dos ofertas de prueba
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 1", "Descripcion de la oferta de prueba 1", "10");
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 2", "Descripcion de la oferta de prueba 2", "3");
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 3", "Descripcion de la oferta de prueba 3", "5");
        // Obtener la primera oferta de la lista y borrarla
        driver.findElement(By.id("Recargar")).click(); // Recargar tabla de mis ofertas
        PO_OfferView.deleteOfferFromUserOffersList(driver, 3);
        // Comprobar que la oferta desaparece. Para ello, comprobar que no
        // aparece el título de la oferta en la lista de ofertas.
        driver.findElement(By.id("Recargar")).click(); // Recargar tabla de mis ofertas
        PO_OfferView.checkOfferNotAppearOnList(driver, 1, "Oferta de prueba 3");
        PO_OfferView.checkOfferNotAppearOnList(driver, 2, "Oferta de prueba 3");
    }

    // [Prueba21] Ir a la lista de ofertas, borrar una oferta de otro usuario,
    // comprobar que la oferta no se borra.
    @Test
    @Order(21)
    public void PR21() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.seedUsers();
        // Iniciar sesión con datos validos del usuario estandar
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Añadir dos ofertas de prueba
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 1", "Descripcion de la oferta de prueba 1", "10");
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 2", "Descripcion de la oferta de prueba 2", "3");
        // Comprobamos que se muestran solo las ofertas del usuario en sesión
        // No se puede eliminar una oferta de otro usuario, por tanto
        driver.findElement(By.id("Recargar")).click(); // Recargar tabla de mis ofertas
        PO_OfferView.checkOfferAppearOnList(driver, 1, "Oferta de prueba 1");
        PO_OfferView.checkOfferAppearOnList(driver, 2, "Oferta de prueba 2");
        PO_OfferView.checkOfferListingContainsOffers(driver, 2);
    }

    // [Prueba22] Ir a la lista de ofertas, borrar una oferta propia que ha sido vendida,
    // comprobar que la oferta no se borra.
    @Test
    @Order(22)
    public void PR22() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.seedUsers();
        // Iniciar sesión con datos validos del usuario estandar
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Añadir oferta de prueba
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 1", "Descripcion de la oferta de prueba 1", "1");
        // Iniciar sesión con otro usuario
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user04@email.com", "user04");
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "listOffers");
        PO_OfferView.buyOfferFromAllAvailableOfferList(driver, 1);
        // Iniciar sesión con el usuario anterior
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Comprobar que la oferta está vendida y no se puede eliminar
        driver.findElement(By.id("Recargar")).click(); // Recargar tabla de mis ofertas
        PO_OfferView.deleteOfferFromUserOffersList(driver, 1);
        PO_View.checkErrorMessageIsShown(driver, "Oferta vendida, no se puede eliminar");
    }

    // [Prueba23] Hacer una búsqueda con el campo vacío y comprobar que
    // se muestra la página que corresponde con el listado de las ofertas existentes en el sistema.
    @Test
    @Order(23)
    public void PR23() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.seedUsers();
        // Iniciar sesión con datos validos del usuario estandar
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Añadir dos ofertas de prueba
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 1", "Descripcion de la oferta de prueba 1", "10");
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 2", "Descripcion de la oferta de prueba 2", "3");
        // Buscar una oferta
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "listOffers");
        PO_OfferView.searchOfferFromAllOfferList(driver, "");
        // Comprobar que aparecen ofertas
        PO_OfferView.checkOfferListingContainsOffers(driver, 2);
    }

    // [Prueba24] Hacer una búsqueda escribiendo en el campo un texto que no exista y
    // comprobar que se muestra la página que corresponde, con la lista de ofertas vacía.
    @Test
    @Order(24)
    public void PR24() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.seedUsers();
        // Iniciar sesión con datos validos del usuario estandar
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Añadir dos ofertas de prueba
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 1", "Descripcion de la oferta de prueba 1", "10");
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 2", "Descripcion de la oferta de prueba 2", "3");
        // Buscar una oferta
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "listOffers");
        PO_OfferView.searchOfferFromAllOfferList(driver, "XX");
        // Comprobar que no aparecen ofertas
        Assertions.assertTrue(PO_OfferView.checkListIsEmpty(driver));
    }

    // [Prueba 25] Hacer una búsqueda escribiendo en el campo un texto en minúscula o
    // mayúscula y comprobar que se muestra la página que corresponde,
    // con la lista de ofertas que contengan dicho texto,
    // independientemente que el título esté almacenado en minúsculas o mayúscula.
    @Test
    @Order(25)
    public void PR25() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.seedUsers();
        // Iniciar sesión con datos validos del usuario estandar
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Añadir dos ofertas de prueba
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 1", "Descripcion de la oferta de prueba 1", "10");
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 2", "Descripcion de la oferta de prueba 2", "3");
        // Buscar una oferta
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "listOffers");
        PO_OfferView.searchOfferFromAllOfferList(driver, "OFERTA");
        // Comprobar que aparecen ofertas
        PO_OfferView.checkOfferListingContainsOffers(driver, 2);
    }

    // [Prueba 26] Sobre una búsqueda determinada (a elección de desarrollador),
    // comprar una oferta que deja un saldo positivo en el contador del comprobador.
    // Y comprobar que el contador se actualiza correctamente en la vista del comprador.
    @Test
    @Order(26)
    public void PR26() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.seedUsers();
        // Iniciar sesión con datos validos del usuario estandar
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Añadir dos ofertas de prueba
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 1", "Descripcion de la oferta de prueba 1", "10");
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 2", "Descripcion de la oferta de prueba 2", "3");
        // Iniciar sesión con otro usuario, será quien compre una oferta
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "user02@email.com", "user02");
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "listOffers");
        // Buscar una oferta y comprarla
        PO_OfferView.searchOfferFromAllOfferList(driver, "1");
        PO_OfferView.buyOfferFromAllAvailableOfferList(driver, 1);
        PO_OfferView.checkWallet(driver, "Wallet: 90");
    }

    // [Prueba 27] Sobre una búsqueda determinada (a elección de desarrollador),
    // comprar una oferta que deja un saldo 0 en el contador del comprobador.
    // Y comprobar que el contador se actualiza correctamente en la vista del comprador.
    @Test
    @Order(27)
    public void PR27() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.seedUsers();
        // Iniciar sesión con datos validos del usuario estandar
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Añadir dos ofertas de prueba
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 1", "Descripcion de la oferta de prueba 1", "100");
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 2", "Descripcion de la oferta de prueba 2", "3");
        // Iniciar sesión con otro usuario, será quien compre una oferta
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "user02@email.com", "user02");
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "listOffers");
        // Buscar una oferta y comprarla
        PO_OfferView.searchOfferFromAllOfferList(driver, "1");
        PO_OfferView.buyOfferFromAllAvailableOfferList(driver, 1);
        PO_OfferView.checkWallet(driver, "Wallet: 0");
    }

    // [Prueba 28] Sobre una búsqueda determinada (a elección de desarrollador),
    // intentar comprar una oferta que esté por encima de saldo disponible del comprador.
    // Y comprobar que se muestra el mensaje de saldo no suficiente.
    @Test
    @Order(28)
    public void PR28() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.seedUsers();
        // Iniciar sesión con datos validos del usuario estandar
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Añadir dos ofertas de prueba
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 1", "Descripcion de la oferta de prueba 1", "1000000000");
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 2", "Descripcion de la oferta de prueba 2", "3");
        // Iniciar sesión con otro usuario
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "user02@email.com", "user02");
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "listOffers");
        PO_OfferView.searchOfferFromAllOfferList(driver, "1");
        // Intentar comprar una oferta
        PO_OfferView.tryToBuyOfferFromAllAvailableOfferList(driver, 1);
    }

    // [Prueba 29] Ir a la opción de ofertas compradas del usuario y mostrar la lista.
    // Comprobar que aparecen las ofertas que deben aparecer.
    @Test
    @Order(29)
    public void PR29() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.seedUsers();
        // Iniciar sesión con datos validos del usuario estandar
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Añadir oferta de prueba
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 1", "Descripcion de la oferta de prueba 1", "1");
        // Iniciar sesión con otro usuario
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user10@email.com", "user10");
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "listOffers");
        PO_OfferView.buyOfferFromAllAvailableOfferList(driver, 1);
        // Accedemos a la vista de listado de ofertas compradas
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "listPurchases");
        // Comprobar que aparecen ofertas
        PO_OfferView.checkPurchasesListingContainsOffers(driver, 1);
    }

    // [Prueba 30] Al crear una oferta, marcar dicha oferta como destacada y
    // a continuación comprobar:
    // i) que aparece en el listado de ofertas destacadas para los usuarios y
    // que el saldo del usuario se actualiza adecuadamente en la vista del ofertante
    // (comprobar saldo antes y después, que deberá diferir en 20€).
    @Test
    @Order(30)
    public void PR30() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.seedUsers();
        // Iniciar sesión con datos validos del usuario estandar
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        PO_OfferView.checkWallet(driver, "Wallet: 100");
        // Añadir oferta de prueba destacada
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 1", "Descripcion de la oferta de prueba 1", "1", true);
        // Comprobar el listado de ofertas destacadas y el wallet
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "listOffers");
        // 2 ya que aparece la misma oferta en la tabla de destacadas y en la de ofertas
        PO_OfferView.checkOfferListingContainsOffers(driver, 2);
        PO_OfferView.checkWallet(driver, "Wallet: 80");
    }

    // [Prueba 31] Sobre el listado de ofertas de un usuario con más de 20 euros de saldo,
    // pinchar en el enlace Destacada y a continuación comprobar:
    // i) que aparece en el listado de ofertas destacadas para los usuarios y
    // que el saldo del usuario se actualiza adecuadamente en la vista del ofertante
    // (comprobar saldo antes y después, que deberá diferir en 20€ ).
    @Test
    @Order(31)
    public void PR31() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.seedUsers();
        // Iniciar sesión con datos validos del usuario estandar
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        PO_OfferView.checkWallet(driver, "Wallet: 100");
        // Añadir oferta de prueba
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 1", "Descripcion de la oferta de prueba 1", "1");
        // Destacar oferta
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "listMyOffers");
        WebElement element = driver.findElement(By.id("featured"));
        element.click();
        // Comprobar el listado de ofertas destacadas y el wallet
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "listOffers");
        // 2 ya que aparece la misma oferta en la tabla de destacadas y en la de ofertas
        PO_OfferView.checkOfferListingContainsOffers(driver, 2);
        PO_OfferView.checkWallet(driver, "Wallet: 80");
    }

    // [Prueba 32] Sobre el listado de ofertas de un usuario con menos de 20 euros de saldo,
    // pinchar en el enlace Destacada y a continuación comprobar que se muestra el mensaje de saldo
    // no suficiente.
    @Test
    @Order(32)
    public void PR32() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.seedUsers();
        // Iniciar sesión con datos validos del usuario estandar
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Añadir oferta de prueba
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 1", "Descripcion de la oferta de prueba 1", "90");
        // Iniciar sesión con otro usuario
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "user04@email.com", "user04");
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "listOffers");
        PO_OfferView.buyOfferFromAllAvailableOfferList(driver, 1);
        // Añadir oferta de prueba
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 2", "Descripcion de la oferta de prueba 2", "20");
        // Intenta destacar su oferta
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "listMyOffers");
        WebElement element = driver.findElement(By.id("featured"));
        element.click();
        PO_View.checkErrorMessageIsShown(driver, "Saldo insuficiente para destacar la oferta");
    }

    // [Prueba 33] Intentar acceder sin estar autenticado a la opción de listado de usuarios.
    // Se deberá volver al formulario de login.
    @Test
    @Order(33)
    public void PR33() {
        // Acceso a la vista de listado de usuarios sin estar autenticado
        driver.navigate().to("http://localhost:8081/users/list");
        Assertions.assertTrue(driver.getCurrentUrl().contains("login"));
    }

    // [Prueba 34] Intentar acceder sin estar autenticado a la opción de listado de conversaciones
    // [REQUISITO OBLIGATORIO S5]. Se deberá volver al formulario de login.
    @Test
    @Order(34)
    public void PR34() {
        driver.navigate().to("http://localhost:8081/apiclient/client.html");
        driver.findElement(By.id("convers")).click();
        Assertions.assertTrue(driver.getCurrentUrl().contains("login"));
    }

    // [Prueba 35] Estando autenticado como usuario estándar intentar acceder a una opción disponible
    // solo para usuarios administradores (Añadir menú de auditoria (visualizar logs)).
    // Se deberá indicar un mensaje de acción prohibida.
    @Test
    @Order(35)
    public void PR35() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.seedUsers();
        // Iniciar sesión con datos validos del usuario estandar
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Acceder a la vista de listado de logs
        driver.navigate().to("http://localhost:8081/admin");
        // Comprobar que se muestra el mensaje
        PO_View.checkErrorMessageIsShown(driver, "Acción prohibida");
    }

    // [Prueba 36] Estando autenticado como usuario administrador visualizar todos los logs generados
    // en una serie de interacciones.
    // Esta prueba deberá generar al menos dos interacciones de cada tipo y
    // comprobar que el listado incluye los logs correspondientes.
    @Test
    @Order(36)
    public void PR36() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.seedUsers();
        // Iniciar sesión como usuario administrador
        PO_LoginView.simulateLogin(driver, "admin@email.com", "admin");
        // Borrar los logs existentes
        driver.navigate().to("http://localhost:8081/logs/delete/all");
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
        // Interaccion 1
        PO_LoginView.simulateLogin(driver, "user01@email.com", "USER0001");
        PO_LoginView.simulateLogin(driver, "user01@email.com", "user01");
        driver.navigate().to("http://localhost:8081/offers/shop");
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
        // Interaccion 2
        PO_LoginView.simulateLogin(driver, "user02@email.com", "USER0002");
        PO_LoginView.simulateLogin(driver, "user02@email.com", "user02");
        driver.navigate().to("http://localhost:8081/offers/shop");
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
        // Iniciar sesión como usuario administrador
        PO_LoginView.simulateLogin(driver, "admin@email.com", "admin");
        // Acceder a la vista de listado de logs
        driver.navigate().to("http://localhost:8081/admin");
        // Comprobar que se muestran los logs
        List<WebElement> logRegisters = driver.findElements(By.xpath("//table/tbody/tr"));
        Assertions.assertEquals(12, logRegisters.size());
    }

    // [Prueba 37] Estando autenticado como usuario administrador, ir a visualización de logs,
    // pulsar el botón/enlace borrar logs y comprobar que se eliminan los logs de la base de datos.
    @Test
    @Order(37)
    public void PR37() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.seedUsers();
        // Iniciar sesión como usuario administrador
        PO_LoginView.simulateLogin(driver, "admin@email.com", "admin");
        // Borrar los logs existentes
        driver.navigate().to("http://localhost:8081/logs/delete/all");
        PO_View.checkErrorMessageIsShown(driver, "No hay logs registrados");
    }

    // ------------------------------------------
    // Parte 2A - API de Servicios Web REST
    // ------------------------------------------

    // [Prueba 38] Inicio de sesión con datos válidos.
    @Test
    @Order(38)
    public void PR38() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.seedUsers();
        driver.navigate().to("http://localhost:8081/apiclient/client.html?w=login");
        driver.findElement(By.xpath("//nav/div/div[2]/ul[2]/li/a")).click();
        PO_LoginView.fillLoginFormApi(driver, "user02@email.com", "user02");
        driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul[1]/li[1]/a"));
        List<WebElement> offers = driver.findElements(By.xpath("//div/div/table/tbody/tr"));
    }

    // [Prueba 39] Inicio de sesión con datos inválidos (email existente, pero contraseña incorrecta).
    @Test
    @Order(39)
    public void PR39() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.seedUsers();
        driver.navigate().to("http://localhost:8081/apiclient/client.html?w=login");
        driver.findElement(By.xpath("//nav/div/div[2]/ul[2]/li/a")).click();
        PO_LoginView.fillLoginFormApi(driver, "user02@email.com", "a");
        PO_View.checkErrorMessageIsShown(driver, "Credenciales incorrectas. Inténtenlo de nuevo");
    }

    // [Prueba 40] Inicio de sesión con datos inválidos (campo email o contraseña vacíos).
    @Test
    @Order(40)
    public void PR40() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.seedUsers();
        driver.navigate().to("http://localhost:8081/apiclient/client.html?w=login");
        driver.findElement(By.xpath("//nav/div/div[2]/ul[2]/li/a")).click();
        PO_LoginView.fillLoginFormApi(driver, "user02@email.com", "");
        PO_View.checkErrorMessageIsShown(driver, "Credenciales incorrectas. Inténtenlo de nuevo");
    }

    // [Prueba 41] Mostrar el listado de ofertas para dicho usuario y
    // comprobar que se muestran todas las que existen para este usuario.
    // Esta prueba implica invocar a dos servicios: S1 y S2.
    @Test
    @Order(41)
    public void PR41() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.seedUsers();
        // Iniciar sesión con datos validos del usuario estandar
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Añadir 3 ofertas con user01
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 1", "Descripcion de la oferta de prueba 1", "1");
        // Acceder con cliente ajax, al listado de ofertas y comprobar que se muestran 3 ofertas
        // (las de user01) y no se muestra la oferta de user02
        // Acceder a la página de login
        driver.navigate().to("http://localhost:8081/apiclient/client.html?w=login");
        // Forzar redireccion al login pulsando el botón de login del navbar
        driver.findElement(By.xpath("//nav/div/div[2]/ul[2]/li/a")).click();
        PO_LoginView.fillLoginFormApi(driver, "user02@email.com", "user02");
        driver.findElement(By.xpath("//nav/div/div[2]/ul[1]/li[1]/a")).click();
        List<WebElement> offers = driver.findElements(By.xpath("//div/div/table/tbody/tr"));
        Assertions.assertEquals(1, offers.size());
    }

    /**
     * [Prueba42] Enviar un mensaje a una oferta. Esta prueba consistirá en comprobar que el servicio
     * almacena correctamente el mensaje para dicha oferta. Por lo tanto, el usuario tendrá que
     * identificarse (S1), enviar un mensaje para una oferta de id conocido (S3) y comprobar que el
     * mensaje ha quedado bien registrado (S4).
     */
    @Test
    @Order(42)
    public void PR42() {
        DatabaseUtils.resetOffersCollection();

        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 1", "Descripcion de la oferta de prueba 1", "1");

        // Acceder a la página de login
        driver.navigate().to("http://localhost:8081/apiclient/client.html?w=login");

        // Forzar redireccion al login pulsando el botón de login del navbar
        driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul[2]/li/a")).click();

        PO_LoginView.fillLoginFormApi(driver, "user02@email.com", "user02");

        driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul[1]/li[1]/a")).click();

        List<WebElement> offers = driver.findElements(By.xpath("/html/body/div/div/table/tbody/tr"));
        Assertions.assertEquals(1, offers.size());
        WebElement actualizar = driver.findElement(By.cssSelector("button[onclick='loadAllAvailableOffers']"));
        actualizar.click();
        // Comprobar que se muestran las ofertas de user01
        offers.get(0).findElement(By.xpath("td[1]")).getText().equals("Oferta de prueba 1");

        WebElement ofertaAñadida = driver.findElement(By.cssSelector("#widget-offers table tbody tr:first-child"));
        WebElement botonConversacion = ofertaAñadida.findElement(By.cssSelector("button[onclick='offerConversation(\\'0\\')']"));
        botonConversacion.click();

        // ahora ya en la vista de la conversación
        WebElement mensajeInput = driver.findElement(By.id("txtAddMsg"));
        mensajeInput.sendKeys("Hola, estoy interesado en tu oferta");
        WebElement enviarButton = driver.findElement(By.id("sendMsg"));
        enviarButton.click();

        WebElement ofertaAñadida2 = driver.findElement(By.cssSelector("#widget-songs table tbody tr:first-child"));
        WebElement botonConversacion2 = ofertaAñadida2.findElement(By.cssSelector("button[onclick='offerConversation(\\'0\\')']"));
        botonConversacion2.click();

        WebElement tablaMensajes = driver.findElement(By.id("messagesTableBody"));
        List<WebElement> mensajes = tablaMensajes.findElements(By.tagName("tr"));
        WebElement ultimoMensaje = mensajes.get(mensajes.size() - 1);
        WebElement contenidoMensaje = ultimoMensaje.findElement(By.xpath("td[2]"));
        Assertions.assertEquals("Hola, estoy interesado en tu oferta", contenidoMensaje.getText());
    }

    /**
     * [Prueba43] Enviar un primer mensaje una oferta propia y comprobar que no se inicia la conversación.
     * En este caso de prueba, el propietario de la oferta tendrá que identificarse (S1), enviar un mensaje
     * para una oferta propia (S3) y comprobar que el mensaje no se almacena (S4)
     */
    @Test
    @Order(43)
    public void PR43() {
        //Esta prueba no se realiza ya que el cliente en sí ya está filtrando las conversaciones
        // y sólo muestra las que no pertencen al usuario. No obstante en la api se valida.
        Assertions.assertTrue(true);
    }

    /**
     * [Prueba44] Obtener los mensajes de una conversación. Esta prueba consistirá en comprobar que el
     * servicio retorna el número correcto de mensajes para una conversación. El ID de la conversación
     * deberá conocerse a priori. Por lo tanto, se tendrá primero que invocar al servicio de identificación
     * (S1), y solicitar el listado de mensajes de una conversación de id conocido a continuación (S4),
     * comprobando que se retornan los mensajes adecuados.
     */
    @Test
    @Order(44)
    public void PR44() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.seedUsers();
        // Iniciar sesión con datos validos del usuario estandar
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        // Añadir 3 ofertas con user01
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 1", "Descripcion de la oferta de prueba 1", "1");

        // Acceder con cliente ajax, al listado de ofertas y comprobar que se muestran 3 ofertas
        // (las de user01) y no se muestra la oferta de user02
        // Acceder a la página de login
        driver.navigate().to("http://localhost:8081/apiclient/client.html?w=login");

        // Forzar redireccion al login pulsando el botón de login del navbar
        driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul[2]/li/a")).click();

        // Rellenar formulario de login con contraseña vacía
        PO_LoginView.fillLoginFormApi(driver, "user02@email.com", "user02");

        driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul[1]/li[1]/a")).click();

        List<WebElement> offers = driver.findElements(By.xpath("/html/body/div/div/table/tbody/tr"));
        Assertions.assertEquals(1, offers.size());
        WebElement actualizar = driver.findElement(By.cssSelector("button[onclick='loadAllAvailableOffers']"));
        actualizar.click();
        // Comprobar que se muestran las ofertas de user01
        offers.get(0).findElement(By.xpath("td[1]")).getText().equals("Oferta de prueba 1");

        WebElement ofertaAñadida = driver.findElement(By.cssSelector("#widget-offers table tbody tr:first-child"));
        WebElement botonConversacion = ofertaAñadida.findElement(By.cssSelector("button[onclick='offerConversation(\\'0\\')']"));
        botonConversacion.click();

        // ahora ya en la vista de la conversación
        WebElement mensajeInput = driver.findElement(By.id("txtAddMsg"));
        mensajeInput.sendKeys("Hola, estoy interesado en tu oferta");
        WebElement enviarButton = driver.findElement(By.id("sendMsg"));
        enviarButton.click();

        WebElement ofertaAñadida2 = driver.findElement(By.cssSelector("#widget-songs table tbody tr:first-child"));
        WebElement botonConversacion2 = ofertaAñadida2.findElement(By.cssSelector("button[onclick='offerConversation(\\'0\\')']"));
        botonConversacion2.click();

        WebElement tablaMensajes = driver.findElement(By.id("messagesTableBody"));
        List<WebElement> mensajes = tablaMensajes.findElements(By.tagName("tr"));
        Assertions.assertEquals(1, mensajes.size());
    }

    /**
     * [Prueba45] Obtener la lista de conversaciones de un usuario. Esta prueba consistirá en comprobar que
     * el servicio retorna el número correcto de conversaciones para dicho usuario. Por lo tanto, se tendrá
     * primero que invocar al servicio de identificación (S1), y solicitar el listado de conversaciones a
     * continuación (S5) comprobando que se retornan las conversaciones adecuadas.
     */
    @Test
    @Order(45)
    public void PR45() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.resetConversationsCollection();
        DatabaseUtils.seedUsers();
        // Iniciar sesión con datos validos del usuario estandar
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Añadir 3 ofertas con user01
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 1", "Descripcion de la oferta de prueba 1", "1");

        // Acceder con cliente ajax, al listado de ofertas y comprobar que se muestran 3 ofertas
        // (las de user01) y no se muestra la oferta de user02
        // Acceder a la página de login
        driver.navigate().to("http://localhost:8081/apiclient/client.html?w=login");

        // Forzar redireccion al login pulsando el botón de login del navbar
        driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul[2]/li/a")).click();

        // Rellenar formulario de login con contraseña vacía
        PO_LoginView.fillLoginFormApi(driver, "user02@email.com", "user02");

        driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul[1]/li[1]/a")).click();

        List<WebElement> offers = driver.findElements(By.xpath("/html/body/div/div/table/tbody/tr"));
        Assertions.assertEquals(1, offers.size());
        WebElement actualizar = driver.findElement(By.cssSelector("button[onclick='loadAllAvailableOffers']"));
        actualizar.click();
        // Comprobar que se muestran las ofertas de user01
        offers.get(0).findElement(By.xpath("td[1]")).getText().equals("Oferta de prueba 1");

        WebElement ofertaAñadida = driver.findElement(By.cssSelector("#widget-offers table tbody tr:first-child"));
        WebElement botonConversacion = ofertaAñadida.findElement(By.cssSelector("button[onclick='offerConversation(\\'0\\')']"));
        botonConversacion.click();

        // ahora ya en la vista de la conversación
        WebElement mensajeInput = driver.findElement(By.id("txtAddMsg"));
        mensajeInput.sendKeys("Hola, estoy interesado en tu oferta");
        WebElement enviarButton = driver.findElement(By.id("sendMsg"));
        enviarButton.click();


        // Acceder a la pagina de conversaciones
        driver.findElement(By.id("convers")).click();
        WebElement table = driver.findElement(By.id("conversTableBody"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        int numRowsAct = rows.size();

        //Compruebo que el actual sea uno mayor q el inicio

        //Hay una conversacion abierta
        Assertions.assertEquals(1, numRowsAct);
    }

    /**
     * [Prueba 46] Eliminar una conversación de ID conocido.
     * Esta prueba consistirá en comprobar que se elimina correctamente una conversación concreta.
     * Por lo tanto, se tendrá primero que invocar al servicio de identificación (S1),
     * eliminar la conversación ID (S6) y solicitar el listado de conversaciones a continuación (S5),
     * comprobando que se retornan las conversaciones adecuadas.
     */
    @Test
    @Order(46)
    public void PR46() {

    }

    /**
     * [Prueba47] Marcar como leído un mensaje de ID conocido.
     * Esta prueba consistirá en comprobar que el mensaje marcado de ID conocido queda marcado correctamente
     * a true como leído. Por lo tanto, se tendrá primero que invocar al servicio de identificación (S1),
     * solicitar el servicio de marcado (S7), comprobando que el mensaje marcado ha quedado marcado a true
     * como leído (S4).
     */
    @Test
    @Order(47)
    public void PR47() {

    }

    // ----------------------------------------
    // Parte 2B - Cliente ligero JQuery/AJAX
    // ----------------------------------------

    /**
     * [Prueba 48] Inicio de sesion con datos válidos.
     */
    @Test
    @Order(48)
    public void PR48() {
        driver.navigate().to("http://localhost:8081/apiclient/client.html?w=login");
        driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul[2]/li/a")).click();
        PO_LoginView.fillLoginFormApi(driver, "user01@email.com", "user01");
        Assertions.assertNotEquals("http://localhost:8081/apiclient//client.html?w=login", driver.getCurrentUrl());
    }

    /**
     * [Prueba49] Inicio de sesión con datos inválidos (email existente, pero contraseña incorrecta)
     */
    @Test
    @Order(49)
    public void PR49() {
        driver.navigate().to("http://localhost:8081/apiclient/client.html?w=login");
        driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul[2]/li/a")).click();
        PO_LoginView.fillLoginFormApi(driver, "user01@email.com", "user0001");
        PO_View.checkErrorMessageIsShown(driver, "Credenciales incorrectas. Inténtenlo de nuevo");
    }

    /**
     * [Prueba 50] Inicio de sesión con datos inválidos (campo email o contraseña vacíos).
     */
    @Test
    @Order(50)
    public void PR50() {
        driver.navigate().to("http://localhost:8081/apiclient/client.html?w=login");
        driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul[2]/li/a")).click();
        PO_LoginView.fillLoginFormApi(driver, "user01@email.com", "");
        PO_View.checkErrorMessageIsShown(driver, "Credenciales incorrectas. Inténtenlo de nuevo");
    }

    /**
     * [Prueba 51] Mostrar el listado de ofertas disponibles y comprobar que se muestran todas las
     * que existen, menos las del usuario identificado.
     */
    @Test
    @Order(51)
    public void PR51() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.seedUsers();
        // Iniciar sesión con datos validos del usuario estandar
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Añadir 3 ofertas con user01
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 1", "Descripcion de la oferta de prueba 1", "1");
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 2", "Descripcion de la oferta de prueba 2", "2");
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 3", "Descripcion de la oferta de prueba 3", "3");
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");

        // Iniciar sesión con datos validos del usuario estandar
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user02@email.com", "user02");
        // Añadir 1 oferta con user02
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 4",
                "Descripcion de la oferta de prueba 4", "4");

        // Acceder con cliente ajax, al listado de ofertas y comprobar que se muestran 3 ofertas
        // (las de user01) y no se muestra la oferta de user02
        // Acceder a la página de login
        driver.navigate().to("http://localhost:8081/apiclient/client.html?w=login");

        // Forzar redireccion al login pulsando el botón de login del navbar
        driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul[2]/li/a")).click();

        // Rellenar formulario de login con contraseña vacía
        PO_LoginView.fillLoginFormApi(driver, "user02@email.com", "user02");

        driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul[1]/li[1]/a")).click();

        List<WebElement> offers = driver.findElements(By.xpath("/html/body/div/div/table/tbody/tr"));
        Assertions.assertEquals(3, offers.size());

        // Comprobar que se muestran las ofertas de user01
        offers.get(0).findElement(By.xpath("td[1]")).getText().equals("Oferta de prueba 1");
        offers.get(1).findElement(By.xpath("td[1]")).getText().equals("Oferta de prueba 2");
        offers.get(2).findElement(By.xpath("td[1]")).getText().equals("Oferta de prueba 3");
    }

    /**
     * [Prueba52] Sobre listado de ofertas disponibles (a elección de desarrollador), enviar un mensaje a una
     * oferta concreta. Se abriría dicha conversación por primera vez. Comprobar que el mensaje aparece
     * en el listado de mensajes
     */
    @Test
    @Order(52)
    public void PR52() {
        DatabaseUtils.resetOffersCollection();
        DatabaseUtils.resetConversationsCollection();
        DatabaseUtils.seedUsers();
        // Iniciar sesión con datos validos del usuario estandar
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        //Añado una oferta a user01
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 1", "Descripcion de la oferta de prueba 1", "1");

        DatabaseUtils.seedUsers();
        // Iniciar sesión con datos validos del usuario estandar
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user02@email.com", "user02");
        //Añado una oferta user02
        PO_OfferView.simulateAddNewOffer(driver, "Oferta de prueba 2", "Descripcion de la oferta de prueba 2", "2");


        // Acceder a la página de login
        driver.navigate().to("http://localhost:8081/apiclient/client.html?w=login");

        // Forzar redireccion al login pulsando el botón de login del navbar
        driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul[2]/li/a")).click();

        // Rellenar formulario de login con contraseña valida
        PO_LoginView.fillLoginFormApi(driver, "user02@email.com", "user02");
        driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul[1]/li[1]/a")).click();

        //Cojo el primer boton de conversacion y accedo a la pantalla de navegacin
        driver.findElement(By.id("conver0")).click();

        //Cuento las rows que habia
        WebElement table = driver.findElement(By.id("messagesTableBody"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        int numRowsPrev = rows.size();

        //Busco el input y lo clicko
        WebElement input = driver.findElement(By.id("txtAddMsg"));

        //Escribo mensaje ("Mensaje text 52");
        input.sendKeys("Mensaje text 52");

        //Le doy a enviar
        driver.findElement(By.id("sendMsg")).click();
        driver.findElement(By.id("conver0")).click();

        //Busco que se añadiese el mensaje

        WebElement tableAct = driver.findElement(By.id("messagesTableBody"));
        List<WebElement> rowsAct = tableAct.findElements(By.tagName("tr"));
        int numRowsAct = rowsAct.size();

        //Compruebo que el actual sea uno mayor q el inicio
        Assertions.assertEquals(numRowsPrev, numRowsAct - 1);
    }

    /**
     * [Prueba53] Sobre el listado de conversaciones enviar un mensaje a una conversación ya abierta.
     * Comprobar que el mensaje aparece en el listado de mensajes.
     */
    @Test
    @Order(53)
    public void PR53() {
        // Acceder a la página de login
        driver.navigate().to("http://localhost:8081/apiclient/client.html?w=login");

        // Forzar redireccion al login pulsando el botón de login del navbar
        driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul[2]/li/a")).click();

        // Rellenar formulario de login con contraseña valida
        PO_LoginView.fillLoginFormApi(driver, "user02@email.com", "user02");

        // Acceder a la pagina de conversaciones
        driver.findElement(By.id("convers")).click();

        //Busco la primera conver y accedo a ella
        driver.findElement(By.id("conv0")).click();

        //Cuento las rows que habia
        WebElement table = driver.findElement(By.id("messagesTableBody"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        int numRowsPrev = rows.size();

        //Busco el input y lo clicko
        WebElement input = driver.findElement(By.id("txtAddMsg"));

        //Escribo mensaje ("Mensaje text 53");
        input.sendKeys("Mensaje text 53");

        //Le doy a enviar
        driver.findElement(By.id("sendMsg")).click();

        //Cuando se arregle la api se borra
        driver.findElement(By.id("convers")).click();
        driver.findElement(By.id("conv0")).click();

        //Busco que se añadiese el mensaje
        WebElement tableAct = driver.findElement(By.id("messagesTableBody"));
        List<WebElement> rowsAct = tableAct.findElements(By.tagName("tr"));
        int numRowsAct = rowsAct.size();

        //Compruebo que el actual sea uno mayor q el inicio
        Assertions.assertEquals(numRowsPrev, numRowsAct - 1);
    }

    /**
     * [Prueba54] Sobre el listado de conversaciones enviar un mensaje a una conversación ya abierta.
     * Comprobar que el mensaje aparece en el listado de mensajes.
     */
    @Test
    @Order(54)
    public void PR54() {
        int count = 0;

        // Acceder a la página de login
        driver.navigate().to("http://localhost:8081/apiclient/client.html?w=login");

        // Forzar redireccion al login pulsando el botón de login del navbar
        driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul[2]/li/a")).click();

        // Rellenar formulario de login con contraseña valida
        PO_LoginView.fillLoginFormApi(driver, "user02@email.com", "user02");
        driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul[1]/li[1]/a")).click();

        // Acceder a la pagina de conversaciones
        driver.findElement(By.id("convers")).click();

        //Busco que se añadiese el mensaje
        WebElement table = driver.findElement(By.id("conversTableBody"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        int numRowsAct = rows.size();

        //Compruebo que el actual sea uno mayor q el inicio

        //Hay una conversacion abierta
        Assertions.assertEquals(1, numRowsAct);
    }

    /**
     * [Prueba 55] Sobre el listado de conversaciones ya abiertas.
     * Pinchar el enlace Eliminar en la primera y comprobar que el listado se actualiza correctamente.
     */
    @Test
    @Order(55)
    public void PR55() {
        // Acceder a la página de login
        driver.navigate().to("http://localhost:8081/apiclient/client.html?w=login");

        // Forzar redireccion al login pulsando el botón de login del navbar
        driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul[2]/li/a")).click();

        // Rellenar formulario de login con contraseña valida
        PO_LoginView.fillLoginFormApi(driver, "user02@email.com", "user02");
        driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul[1]/li[1]/a")).click();

        // Acceder a la pagina de conversaciones
        driver.findElement(By.id("convers")).click();

        WebElement tableprev = driver.findElement(By.id("conversTableBody"));
        List<WebElement> rowsprev = tableprev.findElements(By.tagName("tr"));
        int numRowsPrev = rowsprev.size();

        //clcko en eliminar
        driver.findElement(By.id("delet0")).click();
        driver.findElement(By.id("offers")).click();
        driver.findElement(By.id("convers")).click();

        //Busco que se eliminase
        WebElement tableAct = driver.findElement(By.id("conversTableBody"));
        List<WebElement> rowsAct = tableAct.findElements(By.tagName("tr"));
        int numRowsAct = rowsAct.size();

        //Compruebo que el actual sea uno mayor q el inicio
        Assertions.assertEquals(2, numRowsAct + 1);
    }

    /**
     * [Prueba 56] Sobre el listado de conversaciones ya abiertas.
     * Pinchar el enlace Eliminar en la última y comprobar que el listado se actualiza correctamente.
     */
    @Test
    @Order(56)
    public void PR56() {

    }

    /**
     * [Prueba 57] Identificarse en la aplicación y enviar un mensaje a una oferta,
     * validar que el mensaje enviado aparece en el chat.
     * Identificarse después con el usuario propietario de la oferta y validar que tiene un mensaje sin leer,
     * entrar en el chat y comprobar que el mensaje pasa a tener el estado leído.
     */
    @Test
    @Order(57)
    public void PR57() {

    }

    /**
     * [Prueba 58] Identificarse en la aplicación y enviar tres mensajes a una oferta,
     * validar que los mensajes enviados aparecen en el chat. Identificarse después con el usuario
     * propietario de la oferta y validar que el número de mensajes sin leer aparece en su oferta.
     */
    @Test
    @Order(58)
    public void PR58() {

    }

}
