package ru.researchser.parserApplication.models.htmlElementParser.DTOs;

public class TwoParseParameters implements AbstractParseParameter {

    private String parameter1;
    private String parameter2;

    public TwoParseParameters(String parameter1, String parameter2) {
        this.parameter1 = parameter1;
        this.parameter2 = parameter2;
    }

    public String getParameter1() {
        return parameter1;
    }

    public String getParameter2() {
        return parameter2;
    }

}
