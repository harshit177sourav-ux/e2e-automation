package com.harshitsourav.framework.api;

import com.harshitsourav.framework.config.Config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
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
        return new RequestSpecBuilder()
                .setBaseUri(Config.apiBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept("application/json")
                .setConfig(CONFIG)
                .build();
    }
}
