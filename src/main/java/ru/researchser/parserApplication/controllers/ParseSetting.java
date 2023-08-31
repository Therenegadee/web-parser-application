package ru.researchser.parserApplication.controllers;


import lombok.Data;

@Data
public class ParseSetting {

    private String firstPageUrl;
    private int numOfPagesToParse;
    private String className; // класс, содержащий в себе ссылкий на страницы
    private String tagName; // тэг, уточняющий класс
    private String cssSelector; // CSS Selector кнопки переключения страниц
    private List<AbstractParseSetting> abstractParseSetting;
}
