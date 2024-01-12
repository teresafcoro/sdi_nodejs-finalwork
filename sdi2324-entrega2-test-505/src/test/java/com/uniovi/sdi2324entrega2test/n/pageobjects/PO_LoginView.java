package com.uniovi.sdi2324entrega2test.n.pageobjects;

import com.uniovi.sdi2324entrega2test.n.util.SeleniumUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_LoginView extends PO_NavView {

    /**
     * Rellena el formulario de inicio de sesión
     *
     * @param driver    WebDriver
     * @param emailp    String
     * @param passwordp String
     */
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

    /**
     * Rellena el formulario de inicio de sesión para la API
     *
     * @param driver    WebDriver
     * @param emailp    String
     * @param passwordp String
     */
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

    /**
     * Comprueba que nos encontramos en la página de inicio de sesión
     *
     * @param driver WebDriver
     */
    static public void checkLoginPage(WebDriver driver) {
        // Esperamos a que se cargue el saludo de bienvenida en Español
        SeleniumUtils.waitLoadElementsBy(driver, "text", "Identificar usuario",
                getTimeout());
    }

    /**
     * Inicio de sesión con los datos de un usuario
     *
     * @param driver    WebDriver
     * @param emailp    String
     * @param passwordp String
     */
    static public void login(WebDriver driver, String emailp, String passwordp) {
        fillLoginForm(driver, emailp, passwordp);
    }

}
