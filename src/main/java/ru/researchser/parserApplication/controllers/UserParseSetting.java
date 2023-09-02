package ru.researchser.parserApplication.controllers;


import jakarta.persistence.Entity;
import lombok.Data;
import ru.researchser.parserApplication.models.dataExporter.OutputFile;
import ru.researchser.parserApplication.models.dataExporter.OutputFileType;
import ru.researchser.parserApplication.models.elementLocator.ElementLocator;

import java.util.List;

@Data
@Entity
public class UserParseSetting {

    private String firstPageUrl;
    private int numOfPagesToParse;
    private String className; // класс, содержащий в себе ссылкий на страницы
    private String tagName; // тэг, уточняющий класс
    private String cssSelectorNextPage; // CSS Selector кнопки переключения страниц
    private List<String> header;
    private List<ElementLocator> parseSetting;
    private OutputFileType outputFileType;
    private String pathToOutput;
}
