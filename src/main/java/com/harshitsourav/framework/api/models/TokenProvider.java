package com.harshitsourav.framework.api.models;

import com.harshitsourav.framework.config.Config;

public class TokenProvider {
    private static String token;

    private TokenProvider() {
    }

    public static synchronized String token() {
        if (token == null) {
            AuthResponse response = new AuthClient()
                    .login(Config.secret("BOOKER_USERNAME"), Config.secret("BOOKER_PASSWORD"))
                    .then().statusCode(200)
                    .extract().as(AuthResponse.class);
            if (response.getToken() == null) {
                throw new IllegalStateException("Login failed" + response.getReason());
            }
            token = response.getToken();
        }
        return token;
    }
}
