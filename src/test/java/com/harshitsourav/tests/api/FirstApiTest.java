package com.harshitsourav.tests.api;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.harshitsourav.framework.api.ApiClient;
import com.harshitsourav.framework.config.Config;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class FirstApiTest {
    @Test
    public void listBookingReturnsData() {
        Response response = given()
                .baseUri(Config.apiBaseUrl())
                .accept(ContentType.JSON)
                .log().ifValidationFails()
                .when()
                .get("/booking");
        // System.out.println(response.statusCode());
        // System.out.println(response.jsonPath().getList("$").size());

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertTrue(response.jsonPath().getList("$").size() > 0, "Expected atlease 1 booking");
    }

    @Test
    public void useQueryParamAndCheckStatus() {
        Response response = given()
                .baseUri(Config.apiBaseUrl())
                .accept(ContentType.JSON)
                .queryParam("firstname", "Sally")
                .log().all()
                .when()
                .get("/booking");
        // System.out.println(response.statusCode());
    }

    @Test
    public void usePathParamAndCheckStatus() {
        int id = given(ApiClient.spec())
                .when()
                .get("/booking")
                .then().statusCode(200)
                .extract().path("[0].bookingid");

        Response response = given(ApiClient.spec())
                .pathParam("id", id)
                .when()
                .get("/booking/{id}")
                .then().statusCode(200)
                .extract().response();
        // System.out.println(response.jsonPath().getString("firstname"));
    }
}
