package es.ivan.espinardo.api.bookings;

import es.ivan.espinardo.api.AbstractAPI;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Data
public class Bookings extends AbstractAPI {

    private final Booking[] bookings;
}
