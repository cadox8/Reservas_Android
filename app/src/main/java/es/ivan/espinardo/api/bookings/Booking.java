package es.ivan.espinardo.api.bookings;

import java.util.Date;

import es.ivan.espinardo.api.AbstractAPI;
import es.ivan.espinardo.api.installations.Installation;
import es.ivan.espinardo.api.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Data
public class Booking extends AbstractAPI {

    private final String token;
    private final User user;
    private final Installation installation;
    private final int date;

    private final String times;

    public Date getDate() {
        return new Date(this.date);
    }

    public CharSequence[] getTimes() {
        final String[] splitTimes = this.times.split("_");
        final CharSequence[] result = new CharSequence[splitTimes.length];
        System.arraycopy(splitTimes, 0, result, 0, splitTimes.length);
        return result;
    }
}
