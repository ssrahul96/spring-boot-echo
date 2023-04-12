package xyz.ssrahul96.springbootecho.models;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@ToString
public class LogData {

    private long timestamp;
    private String method;
    private String url;
    private Map<String, String> headers;
    private String body;
    private String additionalContents;
    private Map<String, String> queryParams;
}