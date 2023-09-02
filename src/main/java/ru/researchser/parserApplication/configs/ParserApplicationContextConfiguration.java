package ru.researchser.parserApplication.configs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import ru.researchser.parserApplication.models.elementLocator.*;
import ru.researchser.parserApplication.models.elementLocator.DTOs.OneParseParameter;
import ru.researchser.parserApplication.models.elementLocator.DTOs.ParseParameter;
import ru.researchser.parserApplication.models.elementLocator.DTOs.ElementType;
import ru.researchser.parserApplication.models.elementLocator.DTOs.TwoParseParameters;

import java.util.ArrayList;

import java.util.List;


@Configuration
@ConfigurationProperties("classpath:application.properties")
public class ParserApplicationContextConfiguration {
    @Value("${webdriver.http.factory}")
    private String webDriverHttpFactory;
    @Value("${webdriver.chrome.driver}")
    private String webDriverChromeDriver;

    @Bean
    public WebDriver webDriver() {
        System.setProperty("webdriver.http.factory", webDriverHttpFactory);
        System.setProperty("webdriver.chrome.driver", webDriverChromeDriver);
        return new ChromeDriver();
    }

    @Bean
    @Scope("prototype")
    public ParseElement parseElement(ElementLocator e, WebDriver driver) {
        List<ParseParameter> parameterList = new ArrayList<>();
        if (e instanceof XPathElement) {
            parameterList.add(new OneParseParameter(e.getPathToLocator()));
            return new ParseElement(driver, ElementType.XPATH, parameterList);
        }
        if (e instanceof CssSelectorElement) {
            parameterList.add(new OneParseParameter(e.getPathToLocator()));
            return new ParseElement(driver, ElementType.CSS, parameterList);
        }
        if (e instanceof TagAttrElement) {
            parameterList.add(
                    new TwoParseParameters(e.getPathToLocator(), ((TagAttrElement) e).getAttributeName()));
            return new ParseElement(driver, ElementType.TAG_ATTR, parameterList);
        }
        return null;
    }
}
