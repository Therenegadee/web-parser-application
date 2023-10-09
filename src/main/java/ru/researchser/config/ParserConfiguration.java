package ru.researchser.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import ru.researchser.models.parser.ElementLocator;
import ru.researchser.services.parser.logic.element.*;
import ru.researchser.services.parser.logic.parameter.OneParseParameter;
import ru.researchser.services.parser.logic.parameter.ParseParameter;
import ru.researchser.services.parser.logic.parameter.TwoParseParameters;

import java.util.ArrayList;

import java.util.List;


@Configuration
@ConfigurationProperties("application.properties")
public class ParserConfiguration {
    @Value("${webdriver.http.factory}")
    private String webDriverHttpFactory;
    @Value("${webdriver.chrome.driver}")
    private String webDriverChromeDriver;

    @Bean
    public ChromeOptions chromeOptions() {
        System.setProperty("webdriver.http.factory", webDriverHttpFactory);
        System.setProperty("webdriver.chrome.driver", webDriverChromeDriver);
        return new ChromeOptions();
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
