package ru.researchser.parserApplication.configs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.context.annotation.*;
import ru.researchser.parserApplication.models.htmlElementParser.DTOs.AbstractParseParameter;
import ru.researchser.parserApplication.models.htmlElementParser.DTOs.ElementType;
import ru.researchser.parserApplication.models.htmlElementParser.ParseElement;

import java.util.Scanner;

import java.util.List;


@Configuration
@PropertySource("classpath:application.properties")
public class ParserApplicationContextConfiguration {

    @Bean
    @Scope("singleton")
    public WebDriver webDriver() {
        System.setProperty("webdriver.http.factory", "${webdriver.http.factory}");
        System.setProperty("webdriver.chrome.driver", "${webdriver.chrome.driver}");
        return new ChromeDriver();
    }

    @Bean
    @Scope("prototype")
    public ElementType elementType() {
        ElementType type = null;
        while (type == null) {
            System.out.println("Выберите элемент парсинга (введите число):\n" +
                    "1. xPath;\n" +
                    "2. CSS Selector;\n" +
                    "3. Tag + Attribute;");
            Scanner scanner = new Scanner(System.in);
            int numOfElement = scanner.nextInt();
            switch (numOfElement) {
                case 1 -> type = ElementType.XPATH;
                case 2 -> type = ElementType.CSS;
                case 3 -> type = ElementType.TAG_ATTR;
                default -> System.err.println("Неверный ввод числа. Пожалуйста, введите номер заново.");
            }
        }
        return type;
    }

    @Bean
    @Scope("prototype")
    public ParseElement parseElement(List<AbstractParseParameter> parameters) {
        return new ParseElement(webDriver(), elementType(), parameters);
    }
}
