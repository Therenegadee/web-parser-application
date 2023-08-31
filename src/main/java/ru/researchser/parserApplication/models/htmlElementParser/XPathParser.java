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
import ru.researchser.parserApplication.models.htmlElementParser.DTOs.OneParseParameter;

import java.time.Duration;

@Component
public class XPathParser implements AbstractParseAlgorithm {

    private final WebDriver driver;

    @Autowired
    public XPathParser(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public String parseByParameters(AbstractParseParameter abstractParseParameter, String url) {
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
