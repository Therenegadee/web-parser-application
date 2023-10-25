package ru.researchser.mappers.openapi;

import lombok.RequiredArgsConstructor;
import ru.researchser.DAO.interfaces.ParserResultDao;
import ru.researchser.exceptions.BadRequestException;
import ru.researchser.exceptions.NotFoundException;
import ru.researchser.models.ParserResult;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class ParserResultIdMapper {
    private final ParserResultDao parserResultDao;

    public ParserResult getParserResultById(Long id) {
        return parserResultDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Parser Result with id %d wasn't found", id)));
    }

    public Set<ParserResult> getParserResultByIdS(List<Long> ids){
        return parserResultDao.findAllByIds(ids);
    }

    public Long getId(ParserResult parserResult) {
        if(parserResult == null){
            throw new BadRequestException("Object is null!");
        }
        return parserResult.getId();
    }
}
