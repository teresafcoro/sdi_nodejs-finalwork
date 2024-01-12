package com.uniovi.sdi2324entrega2test.n.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_View {

    protected static PO_Properties p = new PO_Properties("messages");
    protected static int timeout = 2;

    public static int getTimeout() {
        return timeout;
    }

    /**
     * Realiza un click en un botón que contenga el texto text
     *
     * @param driver      WebDriver
     * @param buttonXPath Texto del botón
     */
    static public void clickOnButton(WebDriver driver, String buttonXPath) {
        WebElement buttonToClick = driver.findElement(By.xpath(buttonXPath));
        buttonToClick.click();
    }

    /**
     * Comprueba que se muestra un mensaje de error con el texto <code>message</code>
     *
     * @param driver  WebDriver
     * @param message Texto del mensaje de error
     */
    static public void checkErrorMessageIsShown(WebDriver driver, String message) {
        driver.findElement(By.xpath("//*[contains(text(),'" + message + "')]"));
    }

}
