package com.uniovi.sdi2324entrega2test.n.pageobjects;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_OfferView extends PO_NavView {

    static String BASE_PATH = "http://localhost:8081";

    /**
     * Simulate adding a new offer.
     *
     * @param driver       WebDriver
     * @param titlep       String
     * @param descriptionp String
     * @param pricep       String
     */
    public static void simulateAddNewOffer(WebDriver driver, String titlep, String descriptionp, String pricep) {
        // Pulsar en la opción de menu de agregar oferta:
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "addOffer");

        // Rellenar el formulario con datos válidos
        fillAddNewOfferForm(driver, titlep, descriptionp, pricep);
    }

    /**
     * Fill the add new offer form with values.
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
     * Simulate adding a new offer, featured value specified.
     *
     * @param driver       WebDriver
     * @param titlep       String
     * @param descriptionp String
     * @param pricep       String
     */
    public static void simulateAddNewOffer(WebDriver driver, String titlep, String descriptionp, String pricep, boolean featured) {
        // Pulsar en la opción de menu de agregar oferta:
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "addOffer");

        // Rellenar el formulario con datos válidos
        fillAddNewOfferForm(driver, titlep, descriptionp, pricep, featured);
    }

    /**
     * Fill the add new offer form with values, with a featured value passed.
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
     * Checks if the offer has been added correctly.
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
     * Checks if my offers list contains 'numberOfOffersExpected' offers.
     *
     * @param driver                 WebDriver
     * @param numberOfOffersExpected Integer
     */
    public static void checkMyOfferListingContainsOffers(WebDriver driver, int numberOfOffersExpected) {
        boolean isOffers = driver.findElements(By.xpath("//table/tbody/tr")).size() == numberOfOffersExpected;
        Assertions.assertTrue(isOffers);
    }

    /**
     * Checks if offers list contains 'numberOfOffersExpected' offers.
     *
     * @param driver                 WebDriver
     * @param numberOfOffersExpected Integer
     */
    public static void checkOfferListingContainsOffers(WebDriver driver, int numberOfOffersExpected) {
        boolean isOffers = driver.findElements(By.xpath("//table/tbody/tr")).size() == numberOfOffersExpected;
        Assertions.assertTrue(isOffers);
    }

    /**
     * Checks if featured offers list contains 'numberOfOffersExpected' offers.
     *
     * @param driver                 WebDriver
     * @param numberOfOffersExpected Integer
     */
    public static void checkPurchasesListingContainsOffers(WebDriver driver, int numberOfOffersExpected) {
        boolean isOffers = driver.findElements(By.xpath("//table/tbody/tr")).size() == numberOfOffersExpected;
        Assertions.assertTrue(isOffers);
    }

    /**
     * Delete an offer from user's offers list.
     * The one that is on 'row' row.
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
     * Eliminar la oferta de la fila <code>row</code> indicada.
     * Acceder al listado de ofertas disponibles. Que no pertenecen al usuario.
     */
    public static void deleteOfferFromAllAvailableOfferList(WebDriver driver, int row) {
        // Pulsar en la opción de menu de listar ofertas para comprar
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "listAllOffersMenu");
        // Dentro de la vista del listado de ofertas del usuario, pulsar
        // el botón de eliminar oferta
        PO_View.clickOnButton(driver, "/html/body/div/div[2]/table/tbody/tr[" + row + "]/td[5]/a");
    }

    /**
     * Comprueba que la oferta no aparece en la lista de ofertas del usuario.
     * Para obtener la oferta, se utiliza el título de la oferta.
     */
    public static void checkOfferNotAppearOnList(WebDriver driver, int row, String offerTitle) {
        WebElement element = driver.findElement(By.xpath("//tbody/tr[" + row + "]/td[1]"));
        Assertions.assertFalse(element.getText().contains(offerTitle));
    }

    /**
     * Comprueba que la oferta no aparece en la lista de ofertas del usuario.
     * Para obtener la oferta, se utiliza el título de la oferta.
     */
    public static boolean checkListIsEmpty(WebDriver driver) {
        WebElement element = driver.findElement(By.xpath("//div/div[3]/p"));
        return ("No existen ofertas").equals(element.getText());
    }

    /**
     * Comprueba que la oferta aparece en la lista de ofertas del usuario.
     * Para obtener la oferta, se utiliza el título de la oferta.
     */
    public static void checkOfferAppearOnList(WebDriver driver, int row, String offerTitle) {
        WebElement element = driver.findElement(By.xpath("//tbody/tr[" + row + "]/td[1]"));
        Assertions.assertTrue(element.getText().contains(offerTitle));
    }

    /**
     * Comprueba que la oferta aparece en la lista de ofertas disponibles.
     * Para obtener la oferta, se utiliza el título de la oferta.
     */
    public static void checkOfferAppearOnAllAvailableOfferList(WebDriver driver, int row, String offerTitle) {
        WebElement element = driver.findElement(By.xpath("//tbody/tr[" + row + "]/td[1]"));
        Assertions.assertTrue(element.getText().contains(offerTitle));
    }

    /**
     * Hace una búsqueda entre las ofertas de la lista de ofertas disponibles.
     * Para obtener la oferta, se utiliza el título de la oferta.
     */
    public static void searchOfferFromAllOfferList(WebDriver driver, String search) {
        WebElement element = driver.findElement(By.id("search"));
        element.click();
        element.sendKeys(search);
        driver.findElement(By.id("btnSubmit")).click();
    }

    /**
     * Comprar la oferta
     */
    public static void buyOfferFromAllAvailableOfferList(WebDriver driver, int row) {
        PO_View.clickOnButton(driver, "//div[3]/table/tbody/tr[" + row + "]/td[5]/a");
    }

    /**
     * Intento de comprar la oferta
     */
    public static void tryToBuyOfferFromAllAvailableOfferList(WebDriver driver, int row) {
        PO_View.clickOnButton(driver, "//div[3]/table/tbody/tr[" + row + "]/td[5]/a");
        driver.findElement(By.xpath("//*[contains(text(),'Saldo insuficiente para realizar la compra')]"));
    }

    /**
     * Comprueba que la oferta aparece en la lista de ofertas compradas.
     */
    public static void checkOfferAppearOnBoughtList(WebDriver driver, int row, String offerTitle) {
        WebElement element = driver.findElement(By.xpath("/html/body/div/div[2]/table/tbody/tr[" + row + "]/td[1]"));
        Assertions.assertTrue(element.getText().contains(offerTitle));
    }

    public static void checkOfferListingContainsOffersDest(WebDriver driver, int numberOfOffersExpected) {
        boolean isOffers = driver.findElements(By.xpath("/html/body/div/div[2]/table/tbody/tr")).size() == numberOfOffersExpected;
        Assertions.assertFalse(isOffers);
    }

    public static void checkWallet(WebDriver driver, String walletMssg) {
        String element = driver.findElement(By.id("wallet")).getText();
        Assertions.assertTrue(element.equals(walletMssg));
    }
}
