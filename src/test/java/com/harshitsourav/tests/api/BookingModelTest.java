package com.harshitsourav.tests.api;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.harshitsourav.framework.api.TokenProvider;
import com.harshitsourav.framework.api.data.BookingCleanupRegistry;
import com.harshitsourav.framework.api.data.BookingFactory;
import com.harshitsourav.framework.api.models.Booking;
import com.harshitsourav.framework.api.models.CreatedBooking;

public class BookingModelTest extends BaseApiTest {

    @Test
    public void createdBookingCanBeReadBack() {

        Booking expected = BookingFactory.valid();

        CreatedBooking createdBooking = bookingClient.createBooking(expected)
                .then()
                .statusCode(200)
                .extract().as(CreatedBooking.class);

        int bookingId = createdBooking.getBookingid();

        BookingCleanupRegistry.add(bookingId);

        Assert.assertTrue(bookingId > 0, "API should return a positive booking id");

        assertBookingMatches(createdBooking.getBooking(), expected, "Response from the New Created Booking");

        Booking actual = bookingClient
                .getBookingById(bookingId).then()
                .statusCode(200)
                .extract()
                .as(Booking.class);

        assertBookingMatches(actual, expected, "Response from the Stored Booking");

        Booking updated = bookingClient
                .updateBooking(bookingId, expected, TokenProvider.token())
                .then().statusCode(200)
                .extract().as(Booking.class);
    }

}
