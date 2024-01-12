package com.uniovi.sdi2324entrega2test.n.pageobjects;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_SignUpView extends PO_NavView {

    /**
     * Rellena el formulario de registro de un nuevo usuario
     *
     * @param driver        WebDriver
     * @param emailp        String
     * @param namep         String
     * @param surnamep      String
     * @param datep         String
     * @param passwordp     String
     * @param passwordconfp String
     */
    static public void fillForm(WebDriver driver, String emailp, String namep, String surnamep,
                                String datep, String passwordp, String passwordconfp) {
        WebElement email = driver.findElement(By.name("email"));
        email.click();
        email.clear();
        email.sendKeys(emailp);
        WebElement name = driver.findElement(By.name("name"));
        name.click();
        name.clear();
        name.sendKeys(namep);
        WebElement surname = driver.findElement(By.name("surname"));
        surname.click();
        surname.clear();
        surname.sendKeys(surnamep);
        WebElement date = driver.findElement(By.name("dateOfBirth"));
        date.click();
        date.sendKeys(datep);
        WebElement password = driver.findElement(By.name("password"));
        password.click();
        password.clear();
        password.sendKeys(passwordp);
        WebElement passwordConfirm = driver.findElement(By.name("verifyPassword"));
        passwordConfirm.click();
        passwordConfirm.clear();
        passwordConfirm.sendKeys(passwordconfp);
        By boton = By.className("btn");
        driver.findElement(boton).click();
    }

    /**
     * Comprueba si nos encontramos en la página del registro de un usuario
     *
     * @param driver WebDriver
     */
    static public void checkSignUpPage(WebDriver driver) {
        if (driver.findElements(By.xpath("//*[contains(text(),'Registrar usuario')]")).size() > 0) {
            Assertions.assertTrue(true);
        } else {
            Assertions.fail();
        }
    }

}
