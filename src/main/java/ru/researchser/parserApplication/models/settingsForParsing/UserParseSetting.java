package ru.researchser.parserApplication.models.settingsForParsing;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.researchser.parserApplication.models.elementLocator.ElementLocator;
import ru.researchser.parserApplication.models.outputFile.OutputFileType;
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
    @OneToOne(mappedBy = "userParseSetting")
    private User user;
}
