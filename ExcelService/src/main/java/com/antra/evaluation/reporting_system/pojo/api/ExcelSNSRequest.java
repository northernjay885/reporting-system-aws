package com.antra.evaluation.reporting_system.pojo.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;

public class ExcelSNSRequest {
    @JsonProperty("Message")
    @JsonDeserialize(using = SNSMessageDeserializer.class)
    ExcelRequest excelRequest;

    public ExcelRequest getExcelRequest() {
        return excelRequest;
    }

    public void setExcelRequest(ExcelRequest excelRequest) {
        this.excelRequest = excelRequest;
    }

    @Override
    public String toString() {
        return "ExcelSNSRequest{" +
                "excelRequest=" + excelRequest +
                '}';
    }
}
class SNSMessageDeserializer extends JsonDeserializer<ExcelRequest> {
    @Override
    public ExcelRequest deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        String text = p.getText();
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        return mapper.readValue(text, ExcelRequest.class);
    }
}
