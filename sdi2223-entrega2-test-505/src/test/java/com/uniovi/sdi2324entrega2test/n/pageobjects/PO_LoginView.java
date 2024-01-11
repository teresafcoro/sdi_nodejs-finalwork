package com.uniovi.sdi2324entrega2test.n.pageobjects;

import com.uniovi.sdi2324entrega2test.n.util.SeleniumUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_LoginView extends PO_NavView {

    static String LOGIN_BUTTON_TEXT = "Aceptar";

    static public void fillForm(WebDriver driver, String emailp, String passwordp) {
        WebElement email = driver.findElement(By.name("email"));
        email.click();
        email.clear();
        email.sendKeys(emailp);

        WebElement password = driver.findElement(By.name("password"));
        password.click();
        password.clear();
        password.sendKeys(passwordp);

        //Pulsar el boton de Alta.
        By boton = By.className("btn");
        driver.findElement(boton).click();
    }

    static public void fillLoginForm(WebDriver driver, String emailp, String passwordp) {
        WebElement email = driver.findElement(By.name("email"));
        email.click();
        email.clear();
        email.sendKeys(emailp);
        WebElement password = driver.findElement(By.name("password"));
        password.click();
        password.clear();
        password.sendKeys(passwordp);
        //Pulsar el boton de Alta.
        By boton = By.className("btn");
        driver.findElement(boton).click();
    }

    public static void fillLoginFormApi(WebDriver driver, String emailp, String passwordp) {
        WebElement email = driver.findElement(By.name("email"));
        email.click();
        email.clear();
        email.sendKeys(emailp);
        WebElement password = driver.findElement(By.name("password"));
        password.click();
        password.clear();
        password.sendKeys(passwordp);
        //Pulsar el boton de Alta.
        By boton = By.xpath("//*[@id=\"boton-login\"]");
        driver.findElement(boton).click();
    }

    static public void checkLoginPage(WebDriver driver) {
        //Esperamos a que se cargue el saludo de bienvenida en Español
        SeleniumUtils.waitLoadElementsBy(driver, "text", "Identificar usuario",
                getTimeout());
    }

    /**
     * Simula el proceso de cerrar sesión de un usuario.
     * targetText es el texto que debería ver después de hacer click en la opción escogida, en este caso logout
     *
     * @param driver
     */
    static public void logout(WebDriver driver) {
        PO_NavView.clickOption(driver, "logout", "text", "Cerrar Sesión");
    }

    /**
     * Acción de rellenar el formulario de login con credenciales válidas
     * y enviarlo.
     */
    static public void simulateLogin(WebDriver driver, String emailp, String passwordp) {
        fillForm(driver, emailp, passwordp);
//        PO_View.checkElementBy(driver, "text", LOGIN_BUTTON_TEXT);
    }
}
