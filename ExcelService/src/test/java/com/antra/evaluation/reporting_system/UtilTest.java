package com.antra.evaluation.reporting_system;

import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class UtilTest {
    private static final String DOWNLOAD_API_URI = "/excel/{id}/content";

    @Test
    public void testUri() {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http").host("localhost:8080").path(DOWNLOAD_API_URI)
                .buildAndExpand("junit-5");
        System.out.println(uriComponents);
    }
}
