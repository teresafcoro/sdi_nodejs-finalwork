package com.uniovi.sdi2324entrega2test.n.pageobjects;

import com.uniovi.sdi2324entrega2test.n.util.SeleniumUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PO_UserListView extends PO_NavView {

    public static List<WebElement> getUsersList(WebDriver driver) {
        // Locate the table rows using the XPath expression
        return SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout());
    }

    public static void compareOneByOneTwoUsersLists(WebDriver driver, List<WebElement> usersList//, List<User> usersSystem
    ) {
        for(int i=0;i< usersList.size(); i++){
            WebElement userWeb=usersList.get(i);
//            User user=usersSystem.get(i);
//            Assertions.assertEquals(user.getEmail(), userWeb.findElement(By.xpath("./td[1]")).getText());
//            Assertions.assertEquals(user.getName(), userWeb.findElement(By.xpath("./td[2]")).getText());
//            Assertions.assertEquals(user.getLastName(), userWeb.findElement(By.xpath("./td[3]")).getText());
        }
    }

    public static void clickDeleteButton(WebDriver driver) {
        WebElement deleteBtn=driver.findElement(By.id("btnBorrar"));
        deleteBtn.click();
    }

    public static void markCheckBoxUser(WebDriver driver) {
        WebElement firstUserChechbox = driver.findElement(By.name("userEmails"));
        firstUserChechbox.click();
    }

    public static String getTile(WebDriver driver) {
        return driver.findElement(By.name("title")).getText();
    }

    public static boolean findUserInList(WebDriver driver, String email){
        try {
            SeleniumUtils.waitLoadElementsBy(driver, "text", "admin@email.com",
                    getTimeout());
            return true;
        } catch (TimeoutException e){
            return false;
        }
    }

}
