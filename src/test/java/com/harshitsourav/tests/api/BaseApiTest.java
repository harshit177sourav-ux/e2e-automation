package com.harshitsourav.tests.api;

import org.testng.annotations.AfterMethod;
import org.testng.asserts.SoftAssert;

import com.harshitsourav.framework.api.TokenProvider;
import com.harshitsourav.framework.api.clients.BookingClient;
import com.harshitsourav.framework.api.data.BookingCleanupRegistry;
import com.harshitsourav.framework.api.models.Booking;
import com.harshitsourav.framework.api.models.CreatedBooking;

import io.restassured.response.Response;

public class BaseApiTest {
    protected final BookingClient bookingClient = new BookingClient();

    protected int createBookingForTest(Booking booking) {
        int bookingId = bookingClient.createBooking(booking)
                .then()
                .statusCode(200)
                .extract()
                .as(CreatedBooking.class)
                .getBookingid();
        BookingCleanupRegistry.add(bookingId);
        return bookingId;
    }

    protected void assertBookingMatches(Booking actual, Booking expected, String context) {
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(actual.getFirstname(), expected.getFirstname(), context + ": firstname");
        soft.assertEquals(actual.getLastname(), expected.getLastname(), context + ": lastname");
        soft.assertEquals(actual.getTotalprice(), expected.getTotalprice(), context + ": totalprice");
        soft.assertEquals(actual.isDepositpaid(), expected.isDepositpaid(), context + ": depositpaid");
        soft.assertEquals(actual.getBookingdates(), expected.getBookingdates(), context + ": bookingdates");
        soft.assertEquals(actual.getAdditionalneeds(), expected.getAdditionalneeds(), context + ": additionalneeds");
        soft.assertEquals(actual, expected, context + ": whole object");
        soft.assertAll();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        for (int id : BookingCleanupRegistry.drain()) {
            Response response = bookingClient.deleteBooking(id, TokenProvider.token());
            if (response.getStatusCode() != 201) {
                System.err.println("Warning: " + response.getStatusCode() + " is the status thrown");
            }
        }
    }
}
