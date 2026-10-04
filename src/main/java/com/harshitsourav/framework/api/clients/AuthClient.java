package com.harshitsourav.framework.api.clients;

import static io.restassured.RestAssured.given;

import com.harshitsourav.framework.api.ApiClient;
import com.harshitsourav.framework.api.models.authmodels.AuthRequest;

import io.restassured.response.Response;

public class AuthClient {
    public Response login(String username, String password) {
        return given(ApiClient.spec())
                .body(new AuthRequest(username, password))
                .when().post("/auth");
    }
}
