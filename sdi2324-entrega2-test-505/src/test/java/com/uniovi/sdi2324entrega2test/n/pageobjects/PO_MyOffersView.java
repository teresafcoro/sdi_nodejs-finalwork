package com.uniovi.sdi2324entrega2test.n.pageobjects;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_MyOffersView extends PO_NavView {

    /**
     * Añade una nueva oferta
     *
     * @param driver       WebDriver
     * @param titlep       String
     * @param descriptionp String
     * @param pricep       String
     */
    public static void addNewOffer(WebDriver driver, String titlep, String descriptionp, String pricep) {
        // Pulsar en la opción de menu de agregar oferta:
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "addOffer");

        // Rellenar el formulario con datos válidos
        fillAddNewOfferForm(driver, titlep, descriptionp, pricep);
    }

    /**
     * Rellena el formulario de nueva oferta
     *
     * @param driver       WebDriver
     * @param titlep       String
     * @param descriptionp String
     * @param pricep       String
     */
    public static void fillAddNewOfferForm(WebDriver driver, String titlep, String descriptionp, String pricep) {
        // Rellenamos el campo de título
        WebElement offerTitle = driver.findElement(By.name("title"));
        offerTitle.click();
        offerTitle.clear();
        offerTitle.sendKeys(titlep);

        // Rellenamos el campo de descripción
        WebElement offerDescription = driver.findElement(By.name("detail"));
        offerDescription.click();
        offerDescription.clear();
        offerDescription.sendKeys(descriptionp);

        // Rellenamos el campo de precio
        WebElement offerPrice = driver.findElement(By.name("price"));
        offerPrice.click();
        offerPrice.clear();
        offerPrice.sendKeys(pricep);

        // Pulsar el boton de Alta.
        By addOfferButton = By.xpath("//div/form/div[5]/div/button");
        driver.findElement(addOfferButton).click();
    }

    /**
     * Añade una nueva oferta, con el parámetro featured (destacada)
     *
     * @param driver       WebDriver
     * @param titlep       String
     * @param descriptionp String
     * @param pricep       String
     */
    public static void addNewOffer(WebDriver driver, String titlep, String descriptionp, String pricep, boolean featured) {
        // Pulsar en la opción de menu de agregar oferta:
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "addOffer");

        // Rellenar el formulario con datos válidos
        fillAddNewOfferForm(driver, titlep, descriptionp, pricep, featured);
    }

    /**
     * Rellena el formulario de nueva oferta, con el parámetro featured (destacada)
     *
     * @param driver       WebDriver
     * @param titlep       String
     * @param descriptionp String
     * @param pricep       String
     * @param featured     String
     */
    public static void fillAddNewOfferForm(WebDriver driver, String titlep, String descriptionp, String pricep, boolean featured) {
        // Rellenamos el campo de título
        WebElement offerTitle = driver.findElement(By.name("title"));
        offerTitle.click();
        offerTitle.clear();
        offerTitle.sendKeys(titlep);

        // Rellenamos el campo de descripción
        WebElement offerDescription = driver.findElement(By.name("detail"));
        offerDescription.click();
        offerDescription.clear();
        offerDescription.sendKeys(descriptionp);

        // Rellenamos el campo de precio
        WebElement offerPrice = driver.findElement(By.name("price"));
        offerPrice.click();
        offerPrice.clear();
        offerPrice.sendKeys(pricep);

        // Destacar nueva oferta
        if (featured)
            driver.findElement(By.name("featured")).click();

        // Pulsar el boton de Alta.
        By addOfferButton = By.xpath("//div/form/div[5]/div/button");
        driver.findElement(addOfferButton).click();
    }

    /**
     * Comprueba si la oferta se añadió correctamente
     *
     * @param driver WebDriver
     * @param titlep String
     */
    public static void checkAddOffer(WebDriver driver, String titlep) {
        // Esperamos a que se cargue el saludo de bienvenida en Español
        WebElement element = driver.findElement(By.xpath("//*[contains(text(),'" + titlep + "')]"));
        Assertions.assertNotNull(element);
    }

    /**
     * Comprueba si mi lista de ofertas contiene 'numberOfOffersExpected' ofertas
     *
     * @param driver                 WebDriver
     * @param numberOfOffersExpected Integer
     */
    public static void checkMyOfferListingContainsOffers(WebDriver driver, int numberOfOffersExpected) {
        boolean isOffers = driver.findElements(By.xpath("//table/tbody/tr")).size() == numberOfOffersExpected;
        Assertions.assertTrue(isOffers);
    }

    /**
     * Elimina una oferta de mi lista de ofertas
     * Se especifica la fila en la que se encuentra la oferta
     *
     * @param driver WebDriver
     * @param row    Integer
     */
    public static void deleteOfferFromUserOffersList(WebDriver driver, int row) {
        // Pulsar en la opción de menu de listar ofertas propias
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "listMyOffers");

        // Dentro de la vista del listado de ofertas del usuario, pulsar
        // el botón de eliminar oferta
        PO_View.clickOnButton(driver, "//tbody/tr[" + row + "]/td[5]/a");
    }

    /**
     * Comprueba que la oferta no aparece en la lista de ofertas del usuario
     * Para obtener la oferta, se utiliza el título de la oferta
     *
     * @param driver     WebDriver
     * @param row        Integer
     * @param offerTitle String
     */
    public static void checkOfferNotAppearOnList(WebDriver driver, int row, String offerTitle) {
        WebElement element = driver.findElement(By.xpath("//tbody/tr[" + row + "]/td[1]"));
        Assertions.assertFalse(element.getText().contains(offerTitle));
    }

    /**
     * Comprueba que la oferta no aparece en la lista de ofertas del usuario
     * Para obtener la oferta, se utiliza el título de la oferta
     *
     * @param driver WebDriver
     */
    public static boolean checkListIsEmpty(WebDriver driver) {
        WebElement element = driver.findElement(By.xpath("//div/div[3]/p"));
        return ("No existen ofertas").equals(element.getText());
    }

    /**
     * Comprueba que la oferta aparece en la lista de ofertas del usuario
     * Para obtener la oferta, se utiliza el título de la oferta
     *
     * @param driver     WebDriver
     * @param row        Integer
     * @param offerTitle String
     */
    public static void checkOfferAppearOnList(WebDriver driver, int row, String offerTitle) {
        WebElement element = driver.findElement(By.xpath("//tbody/tr[" + row + "]/td[1]"));
        Assertions.assertTrue(element.getText().contains(offerTitle));
    }

}
