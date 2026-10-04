package com.harshitsourav.framework.api.data;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import com.harshitsourav.framework.api.models.Booking;
import com.harshitsourav.framework.api.models.BookingDates;

public final class BookingFactory {
    private BookingFactory() {
    }

    public static Booking valid() {
        String unique = UUID.randomUUID().toString().substring(0, 8);
        LocalDate checkin = LocalDate.now().plusDays(7);
        return new Booking(
                "Test-" + unique,
                "User-" + unique,
                ThreadLocalRandom.current().nextInt(50, 1000),
                true,
                new BookingDates(checkin.toString(), checkin.plusDays(3).toString()),
                "Breakfast");
    }
}
