package com.uniovi.sdi2324entrega2test.n.pageobjects;

import com.uniovi.sdi2324entrega2test.n.util.SeleniumUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PO_UsersListView extends PO_NavView {

    /**
     * Obtiene el elemento que contiene la lista de usuarios
     *
     * @param driver WebDriver
     * @return List<WebElement>
     */
    public static List<WebElement> getUsersList(WebDriver driver) {
        // Locate the table rows using the XPath expression
        return SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout());
    }

    /**
     * Hace click sobre el botón de eliminar usuarios
     *
     * @param driver WebDriver
     */
    public static void clickDeleteButton(WebDriver driver) {
        WebElement deleteBtn = driver.findElement(By.id("btnBorrar"));
        deleteBtn.click();
    }

    /**
     * Marca el checkbox del usuario
     *
     * @param driver WebDriver
     */
    public static void markCheckBoxUser(WebDriver driver) {
        WebElement firstUserChechbox = driver.findElement(By.name("userEmails"));
        firstUserChechbox.click();
    }

    /**
     * Indica si encuentra al usuario (email) en la lista
     *
     * @param driver WebDriver
     * @param email  String
     * @return boolean
     */
    public static boolean findUserInList(WebDriver driver, String email) {
        try {
            SeleniumUtils.waitLoadElementsBy(driver, "text", email,
                    getTimeout());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

}
