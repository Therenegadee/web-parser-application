package ru.researchser.parserApplication;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.researchser.parserApplication.configs.ParserApplicationConfiguration;
import ru.researchser.parserApplication.models.settingsForParsing.UserParseSetting;
import ru.researchser.parserApplication.models.outputFile.OutputFile;
import ru.researchser.parserApplication.models.elementLocator.ElementLocator;
import ru.researchser.parserApplication.models.elementLocator.ParseElement;
import ru.researchser.parserApplication.models.settingsForParsing.LinksExtractor;
import ru.researchser.parserApplication.services.ParserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ParserApplication {

    @Autowired
    private ParserApplicationConfiguration configuration;
    @Autowired
    private ChromeOptions chromeOptions;
    @Autowired
    private LinksExtractor linksExtractor;
    @Autowired
    private ParserService parserService;

    private final List<ParseElement> parsingTypes = new ArrayList<>();
    private final List<List<String>> allPagesParseResult = new ArrayList<>();

    public String runParser(UserParseSetting userParseSetting) {
        WebDriver driver = new ChromeDriver(configuration.chromeOptions());

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
        driver.quit();

        OutputFile outputFile = new OutputFile(userParseSetting.getOutputFileType());
        String outPutFilePath = UUID.randomUUID().toString().concat("file");
        outputFile.exportData(userParseSetting.getHeader(), allPagesParseResult, outPutFilePath);
        return outPutFilePath;
    }
}
