package es.ivan.espinardo.api.bookings;

import es.ivan.espinardo.api.AbstractAPI;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AvailableTimes extends AbstractAPI {

    private final String availableTimes;

    public CharSequence[] getAvailableTimes() {
        final String[] splitTimes = this.availableTimes.split("_");
        final CharSequence[] result = new CharSequence[splitTimes.length];
        System.arraycopy(splitTimes, 0, result, 0, splitTimes.length);
        return result;
    }
}
