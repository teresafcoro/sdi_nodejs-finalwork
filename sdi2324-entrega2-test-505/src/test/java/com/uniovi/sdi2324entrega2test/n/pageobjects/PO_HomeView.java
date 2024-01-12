package com.uniovi.sdi2324entrega2test.n.pageobjects;

import com.uniovi.sdi2324entrega2test.n.util.SeleniumUtils;
import org.openqa.selenium.WebDriver;

import java.util.Objects;

public class PO_HomeView extends PO_NavView {

    /**
     * Comprueba que estamos en la página de bienvenida
     *
     * @param driver WebDriver
     * @param kind   String
     */
    static public void checkWelcomeToPage(WebDriver driver, String kind) {
        if (Objects.equals(kind, "standard"))
            SeleniumUtils.waitLoadElementsBy(driver, "text", "Mis ofertas",
                    getTimeout());
        else
            SeleniumUtils.waitLoadElementsBy(driver, "text", "Listado de Usuarios",
                    getTimeout());
    }

}