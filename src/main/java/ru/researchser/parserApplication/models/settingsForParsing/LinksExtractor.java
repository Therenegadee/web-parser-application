package ru.researchser.parserApplication.models.settingsForParsing;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class LinksExtractor {

    private final Paginator paginator;
    private static final Scanner SCANNER = new Scanner(System.in);

    @Autowired
    public LinksExtractor(Paginator paginator) {
        this.paginator = paginator;
    }

    public List<String> getPagesToParseLinks(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10L));
        System.out.println("Введите кол-во страниц, которые необходимо спарсить: ");
        int numOfPagesToParse = SCANNER.nextInt();
        SCANNER.nextLine();
        List<String> linksToPagesForParse = extractPagesList(driver, wait, numOfPagesToParse);
        System.out.println(linksToPagesForParse);
        System.out.println("Собрали все ссылки.");
        return linksToPagesForParse;
    }

    private List<String> extractPagesList(WebDriver driver, WebDriverWait wait, int numOfPagesToParse) {
        List<String> linksToPagesForParse = new ArrayList<>();
        System.out.println("Необходимо ввести название класса и тэга, " +
                "по которому парсер будет получать ссылки на элементы страницы");
        System.out.println("Введите название класса: ");
        String className = SCANNER.nextLine(); // pc_ga_pro_index_17
        System.out.println("Введите название тега: ");
        String tagName = SCANNER.nextLine(); // a
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
            String cssSelectorNextPage = SCANNER.nextLine(); // body > div > div.pro_field > div > div > a.next
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
                paginator.clickNextPageButton(driver, cssSelectorNextPage);
            }
        }
        return linksToPagesForParse;
    }
}
