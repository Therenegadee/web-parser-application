package ru.researchser.parserApplication.models.elementLocator;


import lombok.Data;
import org.springframework.stereotype.Component;
import ru.researchser.parserApplication.models.outputFile.OutputFileType;

import java.util.List;

@Data
@Component
public class UserParseSetting {

    private Long id;
    private String firstPageUrl;
    private int numOfPagesToParse;
    private String className; // класс, содержащий в себе ссылкий на страницы
    private String tagName; // тэг, уточняющий класс
    private String cssSelectorNextPage; // CSS Selector кнопки переключения страниц
    private List<String> header;
    private List<ElementLocator> parseSetting;
    private OutputFileType outputFileType;
}
