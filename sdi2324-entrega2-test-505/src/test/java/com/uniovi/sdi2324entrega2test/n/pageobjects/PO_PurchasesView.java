package com.uniovi.sdi2324entrega2test.n.pageobjects;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PO_PurchasesView extends PO_NavView {

    /**
     * Comprueba si la lista de ofertas compradas contiene 'numberOfOffersExpected' ofertas
     *
     * @param driver                 WebDriver
     * @param numberOfOffersExpected Integer
     */
    public static void checkPurchasesListingContainsOffers(WebDriver driver, int numberOfOffersExpected) {
        boolean isOffers = driver.findElements(By.xpath("//table/tbody/tr")).size() == numberOfOffersExpected;
        Assertions.assertTrue(isOffers);
    }

}
