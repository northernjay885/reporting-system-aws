package com.antra.evaluation.reporting_system.pojo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;

public class CrudSNSRequest {
    @JsonProperty("Message")
    @JsonDeserialize(using = SNSCrudMessageDeserializer.class)
    CrudRequest crudRequest;

    public CrudRequest getCrudRequest() {
        return crudRequest;
    }

    public void setCrudRequest(CrudRequest crudRequest) {
        this.crudRequest = crudRequest;
    }

    @Override
    public String toString() {
        return "CrudSNSRequest{" +
                "crudRequest=" + crudRequest +
                '}';
    }
}

class SNSCrudMessageDeserializer extends JsonDeserializer<CrudRequest> {
    @Override
    public CrudRequest deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        String text = p.getText();
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        return mapper.readValue(text, CrudRequest.class);
    }
}
