package br.com.wleydson.bancario.controller.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.jayway.jsonpath.JsonPath;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

public class PayloadExtractor implements ResultHandler {

    private MvcResult result;
    private final ObjectMapper jsonMapper;

    public PayloadExtractor(ObjectMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @Override
    public void handle(MvcResult result) throws Exception {
        this.result = result;
    }

    public <T> T as(Class<T> payloadClass) throws Exception {
        return jsonMapper.readValue(getContentAsString(), payloadClass);
    }

    public <T> List<T> asListOf(Class<T> payloadClass) throws UnsupportedEncodingException {
        return asListOf(payloadClass, false);
    }

    public <T> List<T> asListOf(Class<T> payloadClass, boolean isPaged) throws UnsupportedEncodingException {
        Object data = extractDataPayloadFromHttpBody(isPaged);

        CollectionType listType = jsonMapper.getTypeFactory().constructCollectionType(List.class, payloadClass);
        return jsonMapper.convertValue(data, listType);
    }

    private Object extractDataPayloadFromHttpBody(boolean isPaged) throws UnsupportedEncodingException {
        String body = getContentAsString();

        if (isPaged) {
            JsonPath jsonPath = JsonPath.compile("@.content");
            return jsonPath.read(body);
        }

        return JsonPath.parse(body).json();
    }

    public String getContentAsString() throws UnsupportedEncodingException {
        return result.getResponse().getContentAsString(Charset.forName("UTF-8"));
    }
}
