package ru.researchser.parser.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.researchser.parser.configs.ParserConfiguration;
import ru.researchser.parser.logic.element.ParseElement;
import ru.researchser.parser.logic.outputFile.OutputFile;
import ru.researchser.parser.logic.outputFile.OutputFileType;
import ru.researchser.parser.models.ElementLocator;
import ru.researchser.parser.models.UserParseSetting;

import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j
public class ParserService {
    @Autowired
    private final ParserConfiguration configuration;
    @Autowired
    private final ChromeOptions chromeOptions;
    private WebDriver driver;
    private final List<ParseElement> parsingTypes = new ArrayList<>();
    private final HashMap<String, List<String>> allPagesParseResult = new HashMap<>();

    public String runParser(UserParseSetting userParseSetting) {
        driver = new ChromeDriver(chromeOptions);
        String firstPageURL = userParseSetting.getFirstPageUrl(); // https://zhongchou.modian.com/all/top_comment/all/1
        driver.get(firstPageURL);

        for (ElementLocator e : userParseSetting.getParseSetting()) {
            parsingTypes.add(configuration.parseElement(e, driver));
        }

        List<String> linksToPagesForParse = getPagesToParseLinks(driver, userParseSetting);

        int parsePageNumber = 1;
        for (String link : linksToPagesForParse) {
            System.out.printf("Парсим информацию со сыылки № %d", parsePageNumber);
            System.out.println();
            driver.get(link);
            List<String> pageParseResult = new ArrayList<>();
            for (ParseElement parseElement : parsingTypes) {
                pageParseResult.add(parseElement.parseByParameters(link));
            }
            allPagesParseResult.put(link, pageParseResult);
            parsePageNumber++;
        }
        System.out.println("Парсинг закончен.");
        driver.quit();

        OutputFileType fileType = userParseSetting.getOutputFileType();
        StringBuilder fileNameBuilder = new StringBuilder(UUID.randomUUID().toString());
        fileNameBuilder.append("file");

        if(fileType == OutputFileType.XLSX) {
            fileNameBuilder.append(".xlsx");
        } else {
            fileNameBuilder.append(".csv");
        }

        String fileName = fileNameBuilder.toString();
        String outPutFilePath = "src/main/resources/savedFilesDirectory/" + fileName;
        OutputFile outputFile = new OutputFile(fileType);
        outputFile.exportData(userParseSetting.getHeader(), allPagesParseResult, outPutFilePath);
        return outPutFilePath;
    }
    public void clickNextPageButton (String cssSelectorNextPage) {
        WebElement nextPageButton = driver.findElement(By.cssSelector(cssSelectorNextPage)); // "body > div > div.pro_field > div > div > a.next"
        nextPageButton.click();
    }
    public List<String> getPagesToParseLinks(WebDriver driver, UserParseSetting userParseSetting) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10L));
        int numOfPagesToParse = userParseSetting.getNumOfPagesToParse();
        List<String> linksToPagesForParse = new ArrayList<>();
        String className = userParseSetting.getClassName(); // pc_ga_pro_index_17
        String tagName = userParseSetting.getTagName(); // a
        if (numOfPagesToParse <= 0) {
            System.err.println("Неверный ввод. Введите число в диапазоне от 1 до n");
        } else if (numOfPagesToParse == 1) {
            System.out.printf("Собираем ссылки со страницы %d...", 1);
            List<WebElement> webElementList = driver.findElements(By.className(className));
            for (WebElement element : webElementList) {
                try {
                    WebElement linkElement = element.findElement(By.tagName(tagName));
                    String href = linkElement.getAttribute("href");
                    if (href != null && !href.isEmpty()) {
                        linksToPagesForParse.add(href);
                    }
                } catch (StaleElementReferenceException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } else if (numOfPagesToParse > 1) {
            System.out.println("Введите CSS Selector путь кнопки переключения следующей страницы: ");
            String cssSelectorNextPage = userParseSetting.getCssSelectorNextPage(); // body > div > div.pro_field > div > div > a.next
            for (int i = 1; i <= numOfPagesToParse; i++) {
                System.out.printf("Собираем ссылки со страницы %d...", i);
                System.out.println();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(className)));
                List<WebElement> webElementList = driver.findElements(By.className(className));
                for (WebElement element : webElementList) {
                    try {
                        WebElement linkElement = element.findElement(By.tagName(tagName));
                        String href = linkElement.getAttribute("href");
                        if (href != null && !href.isEmpty()) {
                            linksToPagesForParse.add(href);
                        }
                    } catch (StaleElementReferenceException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                webElementList.clear();
                clickNextPageButton(cssSelectorNextPage);
            }
        }
        System.out.println("Собрали все ссылки.");
        System.out.println(linksToPagesForParse);
        return linksToPagesForParse;
    }
}
