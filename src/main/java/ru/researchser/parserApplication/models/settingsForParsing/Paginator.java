package ru.researchser.parserApplication.models.settingsForParsing;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;


@Component
public class Paginator {

    public void clickNextPageButton (WebDriver driver, String cssSelectorNextPage) {
        WebElement nextPageButton = driver.findElement(By.cssSelector(cssSelectorNextPage)); // "body > div > div.pro_field > div > div > a.next"
        nextPageButton.click();
    }

}
