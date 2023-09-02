package ru.researchser.parserApplication;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;
import ru.researchser.parserApplication.configs.ParserApplicationContextConfiguration;
import ru.researchser.parserApplication.controllers.UserParseSetting;
import ru.researchser.parserApplication.models.dataExporter.OutputFile;
import ru.researchser.parserApplication.models.elementLocator.ElementLocator;
import ru.researchser.parserApplication.models.elementLocator.ParseElement;
import ru.researchser.parserApplication.models.settingsForParsing.LinksExtractor;
import ru.researchser.parserApplication.services.ParserService;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@AllArgsConstructor
public class ParserApplication {

    private ParserApplicationContextConfiguration configuration;
    private WebDriver driver;
    private LinksExtractor linksExtractor;
    private ParserService parserService;

    private final List<ParseElement> parsingTypes = new ArrayList<>();
    private final List<List<String>> allPagesParseResult = new ArrayList<>();

    @PostConstruct
    private void initWebDriver() {
        if (driver == null) {
            driver = configuration.webDriver();
        }
    }

    public void runParser(UserParseSetting userParseSetting) {
        initWebDriver();

        String firstPageURL = userParseSetting.getFirstPageUrl(); // https://zhongchou.modian.com/all/top_comment/all/1
        driver.get(firstPageURL);

        for (ElementLocator e : userParseSetting.getParseSetting()) {
            parsingTypes.add(configuration.parseElement(e, driver));
        }

        List<String> linksToPagesForParse = linksExtractor.getPagesToParseLinks(driver, userParseSetting);

        int parsePageNumber = 1;
        for (String link : linksToPagesForParse) {
            System.out.printf("Парсим информацию со сыылки № %d", parsePageNumber);
            System.out.println();
            driver.get(link);
            List<String> pageParseResult = new ArrayList<>();
            for (ParseElement parseElement : parsingTypes) {
                pageParseResult.add(parseElement.parseByParameters(link));
            }
            allPagesParseResult.add(pageParseResult);
            parserService.saveParsingResult(pageParseResult, link);
            parsePageNumber++;
        }
        System.out.println("Парсинг закончен.");

        OutputFile outputFile = new OutputFile(userParseSetting.getOutputFileType());
        outputFile.exportData(userParseSetting.getHeader(), allPagesParseResult, userParseSetting.getPathToOutput());

        driver.quit();
    }
}
