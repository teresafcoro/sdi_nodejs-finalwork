package com.uniovi.sdi2324entrega2test.n.pageobjects;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_ShopView extends PO_NavView {

    /**
     * Comprueba si la lista de ofertas contiene 'numberOfOffersExpected' ofertas
     *
     * @param driver                 WebDriver
     * @param numberOfOffersExpected Integer
     */
    public static void checkOfferListingContainsOffers(WebDriver driver, int numberOfOffersExpected) {
        boolean isOffers = driver.findElements(By.xpath("//table/tbody/tr")).size() == numberOfOffersExpected;
        Assertions.assertTrue(isOffers);
    }

    /**
     * Intento de comprar la oferta
     *
     * @param driver WebDriver
     * @param row    int
     */
    public static void tryToBuyOfferFromAllAvailableOfferList(WebDriver driver, int row) {
        PO_View.clickOnButton(driver, "//div[3]/table/tbody/tr[" + row + "]/td[5]/a");
        driver.findElement(By.xpath("//*[contains(text(),'Saldo insuficiente para realizar la compra')]"));
    }

    /**
     * Hace una búsqueda entre las ofertas de la lista de ofertas disponibles.
     * Para obtener la oferta, se utiliza el título de la oferta.
     *
     * @param driver WebDriver
     * @param search String
     */
    public static void searchOfferFromAllOfferList(WebDriver driver, String search) {
        WebElement element = driver.findElement(By.id("search"));
        element.click();
        element.sendKeys(search);
        driver.findElement(By.id("btnSubmit")).click();
    }

    /**
     * Comprar la oferta
     *
     * @param driver WebDriver
     * @param row    int
     */
    public static void buyOfferFromAllAvailableOfferList(WebDriver driver, int row) {
        PO_View.clickOnButton(driver, "//div[3]/table/tbody/tr[" + row + "]/td[5]/a");
    }

}
