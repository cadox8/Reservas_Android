package es.ivan.espinardo.api.bookings;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class Bookings {

    private final Booking[] bookings;
}
