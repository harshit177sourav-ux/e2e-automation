package com.harshitsourav.tests.api;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.harshitsourav.framework.api.TokenProvider;
import com.harshitsourav.framework.api.clients.BookingClient;
import com.harshitsourav.framework.api.data.BookingFactory;
import com.harshitsourav.framework.api.models.Booking;
import com.harshitsourav.framework.api.models.CreatedBooking;

public class BookingModelTest {

    private final BookingClient bookingClient = new BookingClient();

    @Test
    public void createdBookingCanBeReadBack() {

        Booking expected = BookingFactory.valid();

        CreatedBooking createdBooking = bookingClient.createBooking(expected)
                .then()
                .statusCode(200)
                .extract().as(CreatedBooking.class);

        int bookingId = createdBooking.getBookingid();

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

    private void assertBookingMatches(Booking actual, Booking expected, String context) {
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(actual.getFirstname(), expected.getFirstname(), context + ": firstname");
        soft.assertEquals(actual.getLastname(), expected.getLastname(), context + ": lastname");
        soft.assertEquals(actual.getTotalprice(), expected.getTotalprice(), context + ": totalprice");
        soft.assertEquals(actual.isDepositpaid(), expected.isDepositpaid(), context + ": depositpaid");
        soft.assertEquals(actual.getBookingdates(), expected.getBookingdates(), context + ": bookingdates");
        soft.assertEquals(actual.getAdditionalneeds(), expected.getAdditionalneeds(), context + ": additionalneeds");
        soft.assertEquals(actual, expected);
        soft.assertAll();
    }

}
