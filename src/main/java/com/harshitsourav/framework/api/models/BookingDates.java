package com.harshitsourav.framework.api.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDates {

    private String checkin;
    private String checkout;

    // Required by Jackson for deserialization
    public BookingDates() {
    }

    public BookingDates(String checkin, String checkout) {
        this.checkin = checkin;
        this.checkout = checkout;
    }

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    // equals/hashCode are needed because Booking.equals compares its BookingDates
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookingDates)) {
            return false;
        }
        BookingDates other = (BookingDates) o;
        return Objects.equals(checkin, other.checkin) && Objects.equals(checkout, other.checkout);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checkin, checkout);
    }

    @Override
    public String toString() {
        return "BookingDates[checkin=" + checkin + ", checkout=" + checkout + "]";
    }
}
