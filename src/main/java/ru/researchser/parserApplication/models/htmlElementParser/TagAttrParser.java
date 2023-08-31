package ru.researchser.parserApplication.models.htmlElementParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.researchser.parserApplication.models.htmlElementParser.DTOs.AbstractParseAlgorithm;
import ru.researchser.parserApplication.models.htmlElementParser.DTOs.AbstractParseParameter;
import ru.researchser.parserApplication.models.htmlElementParser.DTOs.TwoParseParameters;

import java.time.Duration;

@Component
public class TagAttrParser implements AbstractParseAlgorithm {

    private final WebDriver driver;

    @Autowired
    public TagAttrParser (WebDriver driver) {
        this.driver = driver;
    }


    @Override
    public String parseByParameters(AbstractParseParameter abstractParseParameter, String url) {
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
