package es.ivan.espinardo.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Data
public class Booking extends AbstractAPI {

    private final User user;
    private final Installation installation;
}
