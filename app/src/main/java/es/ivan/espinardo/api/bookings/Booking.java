package es.ivan.espinardo.api.bookings;

import android.text.format.DateFormat;

import java.util.Arrays;
import java.util.Calendar;
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
    private final String date;

    private final String times;

    public Date getDate() {
        final String[] date = this.date.split("T")[0].replaceAll("T", "").split("-");
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]), Integer.parseInt(times.split(":")[0]), 0);
        return calendar.getTime();
    }
}
