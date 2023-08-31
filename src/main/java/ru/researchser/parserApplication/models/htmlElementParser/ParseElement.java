package ru.researchser.parserApplication.models.htmlElementParser;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.researchser.parserApplication.configs.ParserApplicationContextConfiguration;
import ru.researchser.parserApplication.models.htmlElementParser.DTOs.*;

import java.util.*;

@Component
public class ParseElement {

    private final AbstractParseAlgorithm abstractParseAlgorithm;
    private ParserApplicationContextConfiguration contextConfiguration;
    private final ElementType type;
    private final List<AbstractParseParameter> parameters;
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final List<ParseElement> parsingMethodElements = new ArrayList<>();

    @Autowired
    public ParseElement(WebDriver driver, ElementType type, List<AbstractParseParameter> parameters) {
        this.type = type;
        this.parameters = parameters;
        this.abstractParseAlgorithm = switch (this.type) {
            case XPATH -> new XPathParser(driver);
            case CSS -> new CSSSelectorParser(driver);
            case TAG_ATTR -> new TagAttrParser(driver);
        };
    }

    public String parseByParameters(String url) {
        return parameters
                .stream()
                .map(parameter -> abstractParseAlgorithm.parseByParameters(parameter, url))
                .findFirst()
                .get();
    }

    public List<ParseElement> addElementsToParse(WebDriver driver, List<String> header) {
        String choice;
        do {
            System.out.println("Добавить метод парсинга? (да/нет)");
            choice = SCANNER.nextLine();
            if (choice.equalsIgnoreCase("да")) {
                ElementType type = contextConfiguration.elementType();
                if (type == ElementType.XPATH) {
                    List<AbstractParseParameter> parameters = new ArrayList<>();
                    System.out.println("Введите xPath к элементу: ");
                    String parameter = SCANNER.nextLine();
                    parameters.add(new OneParseParameter(parameter));
                    parsingMethodElements.add(new ParseElement(driver, type, parameters));
                    ParseElement.addHeader(header);
                } else if (type == ElementType.TAG_ATTR) {
                    List<AbstractParseParameter> parameters = new ArrayList<>();
                    System.out.println("Введите Tag элемента (без '<>'): ");
                    String parameter1 = SCANNER.nextLine();
                    System.out.println("Введите Attribute элемента: ");
                    String parameter2 = SCANNER.nextLine();
                    parameters.add(new TwoParseParameters(parameter1, parameter2));
                    parsingMethodElements.add(new ParseElement(driver, type, parameters));
                    ParseElement.addHeader(header);
                } else if (type == ElementType.CSS) {
                    List<AbstractParseParameter> parameters = new ArrayList<>();
                    System.out.println("Введите CSS Selector элемента: ");
                    String parameter = SCANNER.nextLine();
                    parameters.add(new OneParseParameter(parameter));
                    parsingMethodElements.add(new ParseElement(driver, type, parameters));
                    ParseElement.addHeader(header);
                } else {
                    System.out.println("Неверный ввод.");
                    continue;
                }
                System.out.println("Метод добавлен.");
            }
        } while (choice.equalsIgnoreCase("да") && parsingMethodElements.size() < 6);
        return parsingMethodElements;
    }

    private static void addHeader (List<String> header) {
        System.out.println("Введите Ваше название элемента: ");
        String nameForHeader = SCANNER.nextLine();
        header.add(nameForHeader);
    }
}
