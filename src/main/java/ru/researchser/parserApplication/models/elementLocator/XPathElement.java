package ru.researchser.parserApplication.models.elementLocator;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.researchser.parserApplication.models.elementLocator.DTOs.ParseAlgorithm;
import ru.researchser.parserApplication.models.elementLocator.DTOs.ParseParameter;
import ru.researchser.parserApplication.models.elementLocator.DTOs.OneParseParameter;

import java.time.Duration;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class XPathElement extends ElementLocator implements ParseAlgorithm {

    private final WebDriver driver;

    @Override
    public String parseByParameters(ParseParameter abstractParseParameter, String url) {
        String xPath = ((OneParseParameter)abstractParseParameter).getParameter();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10L));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        WebElement element = driver.findElement(By.xpath(xPath));
        String elementValue = element.getText();
        return elementValue;
    }

}
