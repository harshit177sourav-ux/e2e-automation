package com.harshitsourav.framework.api;

import com.harshitsourav.framework.config.Config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ApiClient {
    private ApiClient() {

    }

    private static final RestAssuredConfig CONFIG = RestAssuredConfig.config().logConfig(
            LogConfig.logConfig()
                    .enableLoggingOfRequestAndResponseIfValidationFails()
                    .blacklistHeader("Authorization")
                    .blacklistHeader("Cookie"));

    public static RequestSpecification spec() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(Config.apiBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept("application/json")
                .setConfig(CONFIG);

        if (Config.logApiTraffic()) {
            builder.addFilter(new RequestLoggingFilter(LogDetail.ALL));
            builder.addFilter(new ResponseLoggingFilter(LogDetail.ALL));
        }
        return builder.build();
    }
}
