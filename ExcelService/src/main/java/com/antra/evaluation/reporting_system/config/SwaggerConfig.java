package com.antra.evaluation.reporting_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.antra.evaluation.reporting_system"))
                .paths(PathSelectors.any())
                .build().apiInfo(metaInfo());

    }
    private ApiInfo metaInfo() {

        ApiInfo apiInfo=new ApiInfo("Excel Generation API",
                "API methods", "0.1",
                "Terms of Service",
                new Contact("Antra Inc","http://www.antra.com","dawei.zhuang@antra.com"),
                "MIT", "", Collections.EMPTY_LIST);

        return apiInfo;
    }
}
