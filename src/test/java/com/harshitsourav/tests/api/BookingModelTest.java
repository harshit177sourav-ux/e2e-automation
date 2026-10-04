package com.harshitsourav.tests.api;

import static io.restassured.RestAssured.given;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.harshitsourav.framework.api.ApiClient;
import com.harshitsourav.framework.api.models.Booking;
import com.harshitsourav.framework.api.models.BookingDates;
import com.harshitsourav.framework.api.models.CreatedBooking;

public class BookingModelTest {

    @Test
    public void createdBookingCanBeReadBack() {
        Booking request = new Booking("Mike", "Sourav", 150, true, new BookingDates("2026-11-01", "2026-11-05"),
                "Breakfast");
        CreatedBooking createdBooking = given(ApiClient.spec()).body(request).when().post("/booking")
                .then().statusCode(200).extract().as(CreatedBooking.class);

        Assert.assertTrue(createdBooking.getBookingid() > 0);
    }

}
