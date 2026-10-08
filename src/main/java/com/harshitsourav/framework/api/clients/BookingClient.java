package com.harshitsourav.framework.api.clients;

import static io.restassured.RestAssured.given;

import com.harshitsourav.framework.api.ApiClient;
import com.harshitsourav.framework.api.models.Booking;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BookingClient {

    private static final String TOKEN_COOKIE = "token";
    private static final String BOOKING = "/booking";
    private static final String BOOKING_ID = "/booking/{id}";

    public Response createBooking(Booking booking) {
        return given(ApiClient.spec())
                .body(booking)
                .when().post(BOOKING);
    }

    public Response getBookingById(int bookingId) {
        return given(ApiClient.spec())
                .pathParam("id", bookingId)
                .when().get(BOOKING_ID);
    }

    public Response getAllBookings() {
        return given(ApiClient.spec())
                .when().get(BOOKING);
    }

    public Response listByName(String firstName, String lastName) {
        return given(ApiClient.spec())
                .queryParam("firstname", firstName)
                .queryParam("lastname", lastName)
                .when().get(BOOKING);
    }

    public Response updateBooking(int bookingId, Booking booking, String token) {
        return sendUpdate(given(ApiClient.spec())
                .header("Cookie", TOKEN_COOKIE + "=" + token), bookingId, booking);
    }

    public Response updateBookingWithoutToken(int bookingId, Booking booking) {
        return sendUpdate(given(ApiClient.spec()), bookingId, booking);
    }

    private Response sendUpdate(RequestSpecification request, int bookingId, Booking booking) {
        return request
                .pathParam("id", bookingId)
                .body(booking)
                .when().put(BOOKING_ID);
    }

    public Response deleteBooking(int bookingId, String token) {
        return sendDelete(given(ApiClient.spec()).header("Cookie", TOKEN_COOKIE + "=" + token), bookingId);
    }

    public Response deleteBookingWithoutToken(int bookingId, String token) {
        return sendDelete(given(ApiClient.spec()), bookingId);
    }

    private Response sendDelete(RequestSpecification request, int bookingId) {
        return request
                .pathParam("id", bookingId)
                .when().delete(BOOKING_ID);
    }

}
