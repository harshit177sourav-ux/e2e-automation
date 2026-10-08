package com.harshitsourav.tests.api;

import org.testng.annotations.Test;

import com.harshitsourav.framework.api.TokenProvider;
import com.harshitsourav.framework.api.data.BookingFactory;
import com.harshitsourav.framework.api.models.Booking;

public class BookingUpdateTest extends BaseApiTest {

    @Test
    public void updateReplacesTheBooking() {
        Booking original = BookingFactory.valid();
        int bookingId = createBookingForTest(original);
        Booking replacement = BookingFactory.valid();

        Booking putResponse = bookingClient.updateBooking(bookingId, replacement, TokenProvider.token())
                .then()
                .statusCode(200)
                .extract().as(Booking.class);

        Booking getResponse = bookingClient.getBookingById(bookingId)
                .then()
                .statusCode(200)
                .extract().as(Booking.class);

        assertBookingMatches(putResponse, replacement, "PUT Response");
        assertBookingMatches(getResponse, replacement, "GET Response");
    }

    @Test
    public void updateWithoutTokenIsForbidden() {
        Booking original = BookingFactory.valid();
        int bookingId = createBookingForTest(original);
        Booking replacement = BookingFactory.valid();

        bookingClient.updateBookingWithoutToken(bookingId, replacement)
                .then()
                .statusCode(403);

        Booking getResponse = bookingClient.getBookingById(bookingId)
                .then()
                .statusCode(200)
                .extract().as(Booking.class);

        assertBookingMatches(getResponse, original, "After Rejected Update");
    }

    @Test
    public void updateWithInvalidTokenIsForbidden() {
        Booking original = BookingFactory.valid();
        int bookingId = createBookingForTest(original);
        Booking replacement = BookingFactory.valid();

        bookingClient.updateBooking(bookingId, replacement, "not-a-real-token")
                .then()
                .statusCode(403);

        Booking getResponse = bookingClient.getBookingById(bookingId)
                .then()
                .statusCode(200)
                .extract().as(Booking.class);

        assertBookingMatches(getResponse, original, "After Rejected Update");
    }

    @Test
    public void updateWithoutOptionalFieldClearsIt() {
        Booking original = BookingFactory.valid(); // has additionalneeds = "Breakfast"
        int bookingId = createBookingForTest(original);

        Booking replacement = BookingFactory.valid();
        replacement.setAdditionalneeds(null); // omitted when sent (NON_NULL)

        bookingClient.updateBooking(bookingId, replacement, TokenProvider.token())
                .then().statusCode(200);

        Booking stored = bookingClient.getBookingById(bookingId)
                .then().statusCode(200)
                .extract().as(Booking.class);

        assertBookingMatches(stored, replacement, "Stored after update without optional field");
    }

}
