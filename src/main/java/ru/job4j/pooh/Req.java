package ru.job4j.pooh;

public class Req {
    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        String[] contentByLine = content.split(System.lineSeparator());
        String[] firstLine = contentByLine[0].split(" ");
        String requestType = firstLine[0];
        String mode = firstLine[1].split("/")[1];
        String name = firstLine[1].split("/")[2];
        String parameter = "";
        if ("GET".equals(requestType) && "topic".equals(mode)) {
            parameter = firstLine[1].split("/")[3];
        }
        if ("POST".equals(requestType)) {
            parameter = contentByLine[contentByLine.length - 1];
        }
        return new Req(requestType, mode, name, parameter);
    }

    public String getHttpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}
