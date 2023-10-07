package ru.researchser.parser.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.researchser.parser.logic.outputFile.OutputFileType;
import ru.researchser.user.models.User;

import java.util.List;

@Data
@Entity
@Table(name = "user_parse_settings")
@AllArgsConstructor
@NoArgsConstructor
public class UserParseSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstPageUrl;
    private int numOfPagesToParse;
    private String className; // класс, содержащий в себе ссылкий на страницы
    private String tagName; // тэг, уточняющий класс
    private String cssSelectorNextPage; // CSS Selector кнопки переключения страниц
    private List<String> header;
    @OneToMany(mappedBy = "userParseSetting")
    private List<ElementLocator> parseSetting;
    @Enumerated(EnumType.STRING)
    private OutputFileType outputFileType;
}
