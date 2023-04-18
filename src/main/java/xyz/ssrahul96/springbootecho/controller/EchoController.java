package xyz.ssrahul96.springbootecho.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.SystemProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.ssrahul96.springbootecho.models.LogData;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static xyz.ssrahul96.springbootecho.utils.DelayUtil.simulateDelay;


@Controller
@Log4j2
public class EchoController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ObjectMapper objectMapper;

    public static final String DELAY = "delay";

    @RequestMapping(value = "/**", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
    public ResponseEntity<LogData> echoBack(@RequestBody(required = false) byte[] rawBody, @RequestParam Map<String, String> requestParams) throws JsonProcessingException {

        final Map<String, String> headers = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(Function.identity(), request::getHeader));

        final LogData logdata = new LogData();

        logdata.setTimestamp(Instant.now().toEpochMilli());
        logdata.setMethod(request.getMethod());
        logdata.setQueryParams(requestParams);
        logdata.setUrl(request.getServletPath());
        logdata.setHeaders(headers);

        simulateRequestDelay(requestParams);

        if (rawBody != null) {
            String body = new String(rawBody, StandardCharsets.UTF_8);
            if (StringUtils.isNotBlank(body)) {
                logdata.setBody(body);
            }
        }

        String additionalContents = SystemProperties.get("ADDITIONAL_CONTENT");
        if (StringUtils.isNotBlank(additionalContents)) {
            logdata.setAdditionalContents(additionalContents);
        }

        String showHostName = SystemProperties.get("SHOW_HOSTNAME");

        if (Boolean.parseBoolean(showHostName)) {
            String hostName = null;
            try {
                hostName = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                log.error(e.getMessage(), e);
            }
            logdata.setHostName(hostName);
        }

        String transactionId = getTransactionId(headers);
        ThreadContext.put("transaction-id", transactionId);

        log.info(objectMapper.writeValueAsString(logdata));

        return new ResponseEntity<>(logdata, HttpStatus.OK);
    }

    private void simulateRequestDelay(Map<String, String> requestParams) {
        if (requestParams.containsKey(DELAY)) {
            simulateDelay(requestParams.get(DELAY));
        }
    }

    private String getTransactionId(Map<String, String> headers) {

        String[] expectedKeys = {"x-appgw-trace-id", "postman-token"};

        for (String expectedKey : expectedKeys) {
            if (headers.containsKey(expectedKey)) {
                return headers.get(expectedKey);
            }
        }

        return null;
    }
}
