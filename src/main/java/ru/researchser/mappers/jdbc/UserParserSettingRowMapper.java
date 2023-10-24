package ru.researchser.mappers.jdbc;

import org.springframework.jdbc.core.RowMapper;
import ru.researchser.models.UserParserSetting;
import ru.researchser.services.parser.logic.outputFile.OutputFileType;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserParserSettingRowMapper implements RowMapper<UserParserSetting> {

    @Override
    public UserParserSetting mapRow(ResultSet rs, int rowNum) throws SQLException {
        Array array = rs.getArray("header");
        List<String> header = new ArrayList<>();
        if (array != null) {
            String[] stringArray = (String[]) array.getArray();
            header = Arrays.asList(stringArray);
        } else {
            throw new IllegalArgumentException("Header couldn't be empty!");
        }

        return UserParserSetting
                .builder()
                .id(rs.getLong("id"))
                .firstPageUrl(rs.getString("first_page_url"))
                .numOfPagesToParse(rs.getInt("num_of_pages_to_parse"))
                .className(rs.getString("class_name"))
                .tagName(rs.getString("tag_name"))
                .cssSelectorNextPage(rs.getString("css_selector_next_page"))
                .header(header)
                .outputFileType(OutputFileType.fromValue(rs.getString("output_file_type")))
                .build();
    }
}
