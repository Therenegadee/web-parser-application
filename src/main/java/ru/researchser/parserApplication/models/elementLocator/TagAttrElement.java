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
import ru.researchser.parserApplication.models.elementLocator.DTOs.TwoParseParameters;

import java.time.Duration;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class TagAttrElement extends ElementLocator implements ParseAlgorithm {
    private final WebDriver driver;
    private String attributeName;

    @Override
    public String parseByParameters(ParseParameter abstractParseParameter, String url) {
        TwoParseParameters twoParseParameters = (TwoParseParameters) abstractParseParameter;
        String tagName = twoParseParameters.getParameter1();
        String attributeName = twoParseParameters.getParameter2();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10L));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(tagName)));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        WebElement tagElement = driver.findElement(By.tagName(tagName));
        String elementValue = tagElement.getAttribute(attributeName);
        return elementValue;
    }
}
