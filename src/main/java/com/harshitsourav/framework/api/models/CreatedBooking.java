package com.harshitsourav.framework.api.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class CreatedBooking {

    private int bookingid;
    private Booking booking;

    // Required by Jackson for deserialization
    public CreatedBooking() {
    }

    public CreatedBooking(int bookingid, Booking booking) {
        this.bookingid = bookingid;
        this.booking = booking;
    }

    public int getBookingid() {
        return bookingid;
    }

    public void setBookingid(int bookingid) {
        this.bookingid = bookingid;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CreatedBooking)) {
            return false;
        }
        CreatedBooking other = (CreatedBooking) o;
        return bookingid == other.bookingid && Objects.equals(booking, other.booking);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingid, booking);
    }

    @Override
    public String toString() {
        return "CreatedBooking[bookingid=" + bookingid + ", booking=" + booking + "]";
    }
}
